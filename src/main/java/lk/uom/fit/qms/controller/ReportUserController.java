package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.UserAuthenticationException;
import lk.uom.fit.qms.service.ReportUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
            @Valid @RequestBody ReportUserRequestDto reportUserRequestDto, HttpServletRequest request) throws UserAuthenticationException, BadRequestException {

        Long userId = getUserIdFromRequest(request);
        reportUserService.createUser(reportUserRequestDto, userId);
        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get User Locations")
    @GetMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DivisionDto>> getLocation(HttpServletRequest request) throws UserAuthenticationException, BadRequestException {

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoles = getUserRoles(request);

        List<DivisionDto> divisionDtos = reportUserService.getLocationDetails(userId, userRoles);

        return new ResponseEntity<>(divisionDtos, HttpStatus.OK);
    }

}
