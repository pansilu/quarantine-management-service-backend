package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.dto.QuarantineUserRequestDto;
import lk.uom.fit.qms.dto.SuccessResponse;
import lk.uom.fit.qms.exception.UserAuthenticationException;
import lk.uom.fit.qms.service.QuarantineUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static lk.uom.fit.qms.util.Constant.AUTHORIZATION_HEADER;

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
            @RequestBody QuarantineUserRequestDto quarantineUserRequestDto, HttpServletRequest request) throws UserAuthenticationException {

        Long userId = getUserIdFromRequest(request);
        quarantineUserService.createUser(quarantineUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }
}
