package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.ReportUserService;

import lk.uom.fit.qms.util.enums.Rank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.naming.ServiceUnavailableException;
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

    private final ReportUserService reportUserService;

    @Autowired
    public ReportUserController(ReportUserService reportUserService) {
        this.reportUserService = reportUserService;
    }

    @ApiOperation(value = "Create/Edit an admin user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createUser(
            @Valid @RequestBody ReportUserRequestDto reportUserRequestDto, BindingResult bindingResult, HttpServletRequest request) throws QmsException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Admin user create/edit validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new QmsException(QmsExceptionCode.RUC00X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        Long userId = getUserIdFromRequest(request);
        reportUserService.createUser(reportUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get User Locations")
    @GetMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DivisionDto>> getLocation(HttpServletRequest request) throws QmsException, ServiceUnavailableException {

        /*Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);

        List<DivisionDto> divisionDtos = reportUserService.getLocationDetails(userId, userRoles);

        return new ResponseEntity<>(divisionDtos, HttpStatus.OK);*/

        throw new ServiceUnavailableException();
    }

    @ApiOperation(value = "Get Admin Users from filter by rank or assign station ids")
    @PostMapping(value = "/filter",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReportUserResponseDto>> getAdminUsers(@RequestBody AdminFilterReqDto adminFilterReqDto, @RequestParam(required = false) String search) throws ServiceUnavailableException {

        /*List<ReportUserResponseDto> reportUserRequestDtos = reportUserService.getReportUsers(adminFilterReqDto,search);

        return new ResponseEntity<>(reportUserRequestDtos, HttpStatus.OK);*/

        throw new ServiceUnavailableException();
    }

    @ApiOperation(value = "Get Admin Users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportUserMultiPageResDto> getAdminUsers(@PageableDefault Pageable pageable, @RequestParam(required = false) String search, HttpServletRequest request) throws QmsException {

        Long adminId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);

        ReportUserMultiPageResDto reportUserMultiPageResDto = reportUserService.getUsers(pageable, adminId, userRoles, search);

        return new ResponseEntity<>(reportUserMultiPageResDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Admin User")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReportUserResponseDto> getAdminUser(@PathVariable("id") Long userId, HttpServletRequest request) throws QmsException {

        Long adminId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);

        ReportUserResponseDto reportUserResponseDto = reportUserService.getUser(userId, adminId, userRoles);

        return new ResponseEntity<>(reportUserResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Ranks")
    @GetMapping(value = "/rank", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Rank>> getAllCountries() throws ServiceUnavailableException {
        /*return new ResponseEntity<>(Arrays.asList(Rank.values()), HttpStatus.OK);*/
        throw new ServiceUnavailableException();
    }
}
