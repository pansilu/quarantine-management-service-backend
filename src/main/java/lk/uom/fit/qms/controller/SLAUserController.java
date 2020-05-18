package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.SlaService;
import lk.uom.fit.qms.util.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/sla/user")
@Api(value = "SLA users")
public class SLAUserController extends BaseController {

    @Autowired
    private SlaService slaService;

    @ApiOperation(value = "Create a new SLA user")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessResponse> createUser(
            @Valid @RequestBody SlaUserDto slaUserDto, BindingResult bindingResult, HttpServletRequest request) throws QmsException {

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoleDtos = getUserRoles(request);

        if(userRoleDtos.isEmpty()){
            throw new QmsException(QmsExceptionCode.USR00X,HttpStatus.UNAUTHORIZED,"No Roles Found");
        }

        slaService.createUser(slaUserDto, userId, userRoleDtos.get(0).getRole().trim());

        return new ResponseEntity<>(new SuccessResponse("Added Successfully"), HttpStatus.OK);
    }

    @ApiOperation(value = "Get SLA User")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SlaUserMultiPageResDto> getUser(
            @ApiParam(value = "size - page size | default 50, page=0")
            @PageableDefault(value = 50, page = 0, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
            @ApiParam(value = "filter should be comma separated. Ex: LK Suraweera, 9080998V or 9080998V or LK Suraweera", example = "LK Suraweera, 9080998V")
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            HttpServletRequest request) throws QmsException {

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoleDtos = getUserRoles(request);

        if(userRoleDtos.isEmpty()){
            throw new QmsException(QmsExceptionCode.USR00X,HttpStatus.UNAUTHORIZED,"No Roles Found");
        }

        boolean isRoot = userRoleDtos.get(0).getRole().trim().equals(RoleType.ROOT.name());

        SlaUserMultiPageResDto slaUserMultiPageResDto;
        if(filter.isEmpty()){
            slaUserMultiPageResDto = slaService.getAllUsers(pageable,userId, isRoot);
        }else{
            slaUserMultiPageResDto = slaService.getFilteredUsers(pageable,filter, userId, isRoot);
        }

        return new ResponseEntity<>(slaUserMultiPageResDto,HttpStatus.OK);
    }

    @ApiOperation(value = "Get SLA User by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SlaUserResponseDto> getUserById(
            @ApiParam(value = "Id number", example = "10")
            @PathVariable("id") Long id,
            HttpServletRequest request) throws QmsException {

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoleDtos = getUserRoles(request);

        if(userRoleDtos.isEmpty()){
            throw new QmsException(QmsExceptionCode.USR00X,HttpStatus.UNAUTHORIZED,"No Roles Found");
        }

        boolean isRoot = userRoleDtos.get(0).getRole().trim().equals(RoleType.ROOT.name());

        SlaUserResponseDto slaUserResponseDto = slaService.getUserFromId(id,userId,isRoot);
        return new ResponseEntity<>(slaUserResponseDto,HttpStatus.OK);
    }

}
