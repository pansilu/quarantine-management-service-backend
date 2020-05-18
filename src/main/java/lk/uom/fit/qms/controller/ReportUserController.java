package lk.uom.fit.qms.controller;

import io.swagger.annotations.*;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Regiment;
import lk.uom.fit.qms.service.ReportUserService;

import lk.uom.fit.qms.util.enums.Rank;
import lk.uom.fit.qms.util.enums.RoleType;
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

    @ApiOperation(value = "Create or update privileged user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createUser(
            @Valid @RequestBody PrivilegedUserRequestDto privilegedUserRequestDto, BindingResult bindingResult, HttpServletRequest request) throws QmsException {

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
            throw new QmsException(QmsExceptionCode.RUC00X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        Long userId = getUserIdFromRequest(request);
        reportUserService.createUser(privilegedUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Admin Users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 204, message = "All validations pass", response = PrivilegedUserListResponse.class)
    })
    public ResponseEntity<PrivilegedUserListResponse> getAdminUsers(@PageableDefault Pageable pageable, HttpServletRequest request) throws QmsException {

        PrivilegedUserListResponse privilegedUserListResponse = reportUserService.getUsers(pageable);

        return new ResponseEntity<>(privilegedUserListResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Privileged User")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrivilegedUserResponseDto> getAdminUser(@PathVariable("id") Long userId, HttpServletRequest request) throws QmsException {

        PrivilegedUserResponseDto privilegedUserResponseDto = reportUserService.getUser(userId);

        return new ResponseEntity<>(privilegedUserResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Deactivate user")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deactivateAdminUser(@PathVariable("id") Long userId, HttpServletRequest request) throws QmsException {

        reportUserService.deActivateUser(userId);
        return HttpStatus.OK;
    }
}
