package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.dto.UserLoginRequestDto;
import lk.uom.fit.qms.dto.UserLoginResponseDto;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @ApiOperation(value = "Authenticate", notes = "Authenticate user by username and password")
    @PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserLoginResponseDto> authenticate(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) throws BadRequestException {

        if (isDebugEnable) {
            logger.debug("Request authenticate, username : {} ", userLoginRequestDto.getUsername());
        }

        UserLoginResponseDto userLoginResponseDto = userService.authenticateUser(userLoginRequestDto);

        if (isDebugEnable) {
            logger.debug("Response authenticate, username : {}, returning : {}", userLoginRequestDto.getUsername(), userLoginRequestDto);
        }
        return new ResponseEntity<>(userLoginResponseDto, HttpStatus.OK);
    }

    @GetMapping(value = "/token/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEncodePwd(@PathVariable String name) {
        return new ResponseEntity<>(passwordEncoder.encode(name), HttpStatus.OK);
    }
}
