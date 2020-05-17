package lk.uom.fit.qms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.service.SlaService;
import lk.uom.fit.qms.util.enums.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            HttpServletRequest request) throws QmsException {

        Long userId = getUserIdFromRequest(request);
        List<UserRoleDto> userRoleDtos = getUserRoles(request);

        if(userRoleDtos.isEmpty()){
            throw new QmsException(QmsExceptionCode.USR00X,HttpStatus.UNAUTHORIZED,"No Roles Found");
        }

        boolean isRoot = userRoleDtos.get(0).getRole().trim().equals(RoleType.ROOT.name());

        SlaUserMultiPageResDto slaUserMultiPageResDto = new SlaUserMultiPageResDto();
        if(filter.isEmpty()){
            List<SlaUserResponseDto> slaUserDtos = slaService.getAllUsers(userId, isRoot);
            slaUserMultiPageResDto.setData(slaUserDtos);
            slaUserMultiPageResDto.setTotalPages(slaUserDtos.size());
        }else{
            List<SlaUserResponseDto> slaUserDtos = slaService.getFilteredUsers(filter, userId, isRoot);
            slaUserMultiPageResDto.setData(slaUserDtos);
            slaUserMultiPageResDto.setTotalPages(slaUserDtos.size());
        }

        return new ResponseEntity<>(slaUserMultiPageResDto,HttpStatus.OK);
    }

}
