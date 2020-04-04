package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.NotFoundException;
import lk.uom.fit.qms.exception.UserAuthenticationException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.ReportUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.controller.
 */
@RestController
@RequestMapping("/api/user/admin")
@Api(value = "reportUser", tags = {"Report User Management"})
public class ReportUserController extends BaseController {

    @Autowired
    private ReportUserService reportUserService;

    @ApiOperation(value = "Create a new user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createUser(
            @Valid @RequestBody ReportUserRequestDto reportUserRequestDto, HttpServletRequest request, BindingResult bindingResult) throws UserAuthenticationException, BadRequestException, NotFoundException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Admin user create auth validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new BadRequestException(QmsExceptionCode.RUC00X, String.join(",", fieldsErrorListDesc));
        }

        Long userId = getUserIdFromRequest(request);
        reportUserService.createUser(reportUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get User Locations")
    @GetMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DivisionDto>> getLocation(HttpServletRequest request) throws UserAuthenticationException {

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);

        List<DivisionDto> divisionDtos = reportUserService.getLocationDetails(userId, userRoles);

        return new ResponseEntity<>(divisionDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Admin Users from filter by rank or assign station ids")
    @PostMapping(value = "/filter",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportUserResponseDto>> getAdminUsers(@RequestBody AdminFilterReqDto adminFilterReqDto) {

        List<ReportUserResponseDto> reportUserRequestDtos = reportUserService.getReportUsers(adminFilterReqDto);

        return new ResponseEntity<>(reportUserRequestDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Admin Users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportUserMultiPageResDto> getAdminUsers(@PageableDefault Pageable pageable) {

        ReportUserMultiPageResDto reportUserMultiPageResDto = reportUserService.getUsers(pageable);

        return new ResponseEntity<>(reportUserMultiPageResDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Admin User")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportUserResponseDto> getAdminUser(@PathVariable("id") Long userId) {

        ReportUserResponseDto reportUserResponseDto = reportUserService.getUser(userId);

        return new ResponseEntity<>(reportUserResponseDto, HttpStatus.OK);
    }
}
