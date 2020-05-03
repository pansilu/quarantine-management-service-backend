package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.PositiveCovidDetailService;
import lk.uom.fit.qms.service.QuarantineUserService;

import lk.uom.fit.qms.util.enums.QuarantineUserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.util.Map;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.controller.
 */
@RestController
//@RequestMapping("/api/user/quarantine")
@Api(value = "quarantineUser", tags = {"Quarantine User Management"})
public class QuarantineUserController extends BaseController {

    @Autowired
    private QuarantineUserService quarantineUserService;

    @Autowired
    private PositiveCovidDetailService positiveCovidDetailService;

    @ApiOperation(value = "Create/Edit a quatantine user")
    @PostMapping(value = "/api/user/quarantine", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createUser(
            @Valid @RequestBody QuarantineUserRequestDto quarantineUserRequestDto, BindingResult bindingResult, HttpServletRequest request) throws QmsException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Quarantine user create/edit validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new QmsException(QmsExceptionCode.QUC00X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        Long userId = getUserIdFromRequest(request);
        quarantineUserService.createUser(quarantineUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Authenticate", notes = "Authenticate quarantine user by secret")
    @PostMapping(value = "/api/user/quarantine/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> authenticate(@Valid @RequestBody SecretDto secret, BindingResult bindingResult) throws QmsException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Quarantine user auth validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new QmsException(QmsExceptionCode.QUC00X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        if (isDebugEnable) {
            logger.debug("Request authenticate, username : {} ", secret);
        }

        UserLoginResponseDto userLoginResponseDto = quarantineUserService.authenticateUser(secret.getSecret());

        if (isDebugEnable) {
            logger.debug("Response authenticate, secret : {}, returning : {}", secret, userLoginResponseDto);
        }
        return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Points", notes = "Update quarantine user points")
    @PutMapping (value = "/api/user/quarantine/point", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> updatePoints(@RequestBody Map<String, Boolean> pointValueMap, HttpServletRequest request) throws QmsException {

        /*Long userId = getUserIdFromRequest(request);

        if (isDebugEnable) {
            logger.debug("Points update, for user: {}", userId);
        }

        quarantineUserService.updatePointValue(pointValueMap, userId);*/

        return new ResponseEntity<>(new SuccessResponse("Updated Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Quarantine Users")
    @GetMapping(value = "/api/user/quarantine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuarantineUserMultiPageResDto> getQuarantineUsers(
            @RequestParam(required = false) String search, @RequestParam(required = false) QuarantineUserStatus status, @PageableDefault(sort = {"status"}) Pageable pageable, HttpServletRequest request) throws QmsException {

        Long adminId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);
        QuarantineUserMultiPageResDto reportUserRequestDtos = quarantineUserService.getQuarantineUsers(pageable, adminId, userRoles, search, status);

        return new ResponseEntity<>(reportUserRequestDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Quarantine User's point value details")
    @GetMapping(value = "/api/user/quarantine/point/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuarantineUserPointValueDto> getQuarantineUserPointValues(@PathVariable("id") Long userId, HttpServletRequest request) throws QmsException, ServiceUnavailableException {

        /*Long adminId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);
        QuarantineUserPointValueDto quarantineUserPointValueDto = quarantineUserService.getUserPointValues(userId, adminId, userRoles);

        return new ResponseEntity<>(quarantineUserPointValueDto, HttpStatus.OK);*/
        throw new ServiceUnavailableException();
    }

    @ApiOperation(value = "Get Quarantine User")
    @GetMapping(value = "/api/user/quarantine/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuarantineUserResDto> getQuarantineUser(@PathVariable("id") Long userId, HttpServletRequest request) throws QmsException {

        Long adminId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);
        QuarantineUserResDto quarantineUserResDto = quarantineUserService.getUser(userId, adminId, userRoles);

        return new ResponseEntity<>(quarantineUserResDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Welfare", notes = "Update quarantine user resources")
    @PutMapping (value = {"/api/user/quarantine/submitWelfareReport", "/submitWelfareReport"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> updateResources(@RequestBody Map<String, Boolean> pointValueMap, HttpServletRequest request) throws QmsException {

        /*Long userId = getUserIdFromRequest(request);

        if (isDebugEnable) {
            logger.debug("resource update, for user: {}", userId);
        }*/

        return new ResponseEntity<>(new SuccessResponse("Updated Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Positive Covid case details")
    @GetMapping(value = "/api/user/quarantine/pc/case", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PositiveCovidCaseDetail>> getPositiveCovidCaseDetails(@RequestParam(required = false) String search) throws QmsException {

        List<PositiveCovidCaseDetail> positiveCovidCaseDetails = positiveCovidDetailService.getCaseDetails(search);

        return new ResponseEntity<>(positiveCovidCaseDetails, HttpStatus.OK);
    }
}
