package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.UserAuthenticationException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.QuarantineUserService;
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
@RequestMapping("/api/user/quarantine")
@Api(value = "quarantineUser", tags = {"Quarantine User Management"})
public class QuarantineUserController extends BaseController {

    @Autowired
    private QuarantineUserService quarantineUserService;

    @ApiOperation(value = "Create a new user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createUser(
            @RequestBody QuarantineUserRequestDto quarantineUserRequestDto, HttpServletRequest request) throws UserAuthenticationException, BadRequestException {

        Long userId = getUserIdFromRequest(request);
        quarantineUserService.createUser(quarantineUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Authenticate", notes = "Authenticate quarantine user by secret")
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> authenticate(@Valid @RequestBody SecretDto secret, BindingResult bindingResult) throws BadRequestException {

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
            throw new BadRequestException(QmsExceptionCode.QUC00X, String.join(",", fieldsErrorListDesc));
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
    @PutMapping (value = "/point", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> updatePoints(@RequestBody Map<String, Boolean> pointValueMap, HttpServletRequest request) throws BadRequestException, UserAuthenticationException {

        Long userId = getUserIdFromRequest(request);

        if (isDebugEnable) {
            logger.debug("Points update, for user: {}", userId);
        }

        quarantineUserService.updatePointValue(pointValueMap, userId);

        return new ResponseEntity<>(new SuccessResponse("Updated Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Quarantine Users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuarantineMultiUserPageResDto> getQuarantineUsers(@PageableDefault(value = Integer.MAX_VALUE, sort = {"totalPoints"}, direction = Sort.Direction.DESC) Pageable pageable) {

        QuarantineMultiUserPageResDto reportUserRequestDtos = quarantineUserService.getQuarantineUsers(pageable);

        return new ResponseEntity<>(reportUserRequestDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Quarantine Users")
    @GetMapping(value = "/point/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuarantineUserPointValueDto> getQuarantineUserPointValues(@PathVariable("id") Long userId) {

        QuarantineUserPointValueDto quarantineUserPointValueDto = quarantineUserService.getUserPointValues(userId);

        return new ResponseEntity<>(quarantineUserPointValueDto, HttpStatus.OK);
    }
}
