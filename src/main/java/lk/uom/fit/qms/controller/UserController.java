package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.UserService;

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
@RequestMapping("/api/user")
@Api(value = "user", tags = {"User Management"})
public class UserController extends BaseController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Authenticate", notes = "Authenticate user by username and password")
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> authenticate(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, BindingResult bindingResult) throws QmsException {

        if(bindingResult.hasFieldErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> fieldsErrorListDesc = new ArrayList<>();

            for(FieldError fieldError: fieldErrors){

                String errorList = Arrays.toString(fieldError.getArguments());
                logger.warn("Admin user auth validation ERROR: ------ FieldErrorExists: errorCode: {}, fieldName: {}," +
                                " rejectedValue: {}, , arguments: {}, defaultMessage: {}", fieldError.getCode(),
                        fieldError.getField(), fieldError.getRejectedValue(), errorList, fieldError.getDefaultMessage());
                fieldsErrorListDesc.add(fieldError.getDefaultMessage());
            }
            throw new QmsException(QmsExceptionCode.UC000X, HttpStatus.BAD_REQUEST, String.join(",", fieldsErrorListDesc));
        }

        if (isDebugEnable) {
            logger.debug("Request authenticate, username : {} ", userLoginRequestDto.getUsername());
        }

        UserLoginResponseDto userLoginResponseDto = userService.authenticateUser(userLoginRequestDto);

        if (isDebugEnable) {
            logger.debug("Response authenticate, username : {}, returning : {}", userLoginRequestDto.getUsername(), userLoginResponseDto);
        }
        return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Check mobile num exists")
    @GetMapping(value = "/mobile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MobileNumExistsResDto> getAdminUser(@RequestParam(value = "mobile") String mobile, @RequestParam(value = "id", required = false) Long userId, HttpServletRequest request) throws QmsException {

        MobileNumExistsResDto mobileNumExistsResDto = userService.getMobileNumExistsResponse(mobile, userId);

        return new ResponseEntity<>(mobileNumExistsResDto, HttpStatus.OK);
    }
}
