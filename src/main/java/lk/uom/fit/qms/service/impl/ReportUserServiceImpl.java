package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.model.PrivilegedUser;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.ReportUserService;
import lk.uom.fit.qms.service.UserService;
import lk.uom.fit.qms.util.enums.RoleType;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.service.impl.
 */
@Service
@Transactional
public class ReportUserServiceImpl implements ReportUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReportUserRepository reportUserRepository;

    @Autowired
    private RegimentsRepository regimentsRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private lk.uom.fit.qms.repository.PrivilegedUserRepository privilegedUserRepository;

    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private UserService userService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(PrivilegedUserRequestDto privilegedUserRequestDto, Long addedUserId) throws QmsException {

        logger.debug("addedUserId: {}", addedUserId);

        userService.checkUserExists(privilegedUserRequestDto.getId());

        PrivilegedUser privilegedUser = modelMapper.map(privilegedUserRequestDto, PrivilegedUser.class);

        // default username and password is NIC
        privilegedUser.setUsername(privilegedUserRequestDto.getNic());
        privilegedUser.setPassword(passwordEncoder.encode(privilegedUserRequestDto.getNic()));
        privilegedUser.setRegiment(regimentsRepository.findRegimentByCode(privilegedUserRequestDto.getRegiment()));

        UserRole ur = new UserRole();
        ur.setRole(roleRepository.findRoleByName(privilegedUser.getRole()));

        List<UserRole> roles = new ArrayList<>();
        roles.add(ur);

        privilegedUser.setUserRoles(roles);

        // find by nic
        if(privilegedUserRequestDto.getId() == null){
            PrivilegedUser existingPrivilegedUser = privilegedUserRepository.findPrivilegedUserByNic(privilegedUser.getNic());
            if(existingPrivilegedUser!=null){
                privilegedUser.setId(existingPrivilegedUser.getId());
                privilegedUserRequestDto.setId(existingPrivilegedUser.getId());
            }
        }
        setRole(privilegedUserRequestDto, privilegedUser, addedUserId);
        privilegedUserRepository.save(privilegedUser);

    }

    @Override
    public PrivilegedUserListResponse getUsers(Pageable pageable) {

        Page<PrivilegedUser> users = privilegedUserRepository.findAll(pageable);

        PrivilegedUserListResponse privilegedUserListResponse = new PrivilegedUserListResponse();

        List<PrivilegedUserResponseDto> privilegedUserResponseDtoArrayList = new ArrayList<>();

        users.forEach(privilegedUser -> {
            PrivilegedUserResponseDto reportUserResponseDto = modelMapper.map(privilegedUser, PrivilegedUserResponseDto.class);
            reportUserResponseDto.setRegiment(modelMapper.map(privilegedUser.getRegiment(), RegimentResponseDto.class));
            privilegedUserResponseDtoArrayList.add(reportUserResponseDto);
        });

        privilegedUserListResponse.setData(privilegedUserResponseDtoArrayList);
        privilegedUserListResponse.setTotalPages(users.getTotalPages());

        return privilegedUserListResponse;
    }

    @Override
    public List<RegimentResponseDto> getRegiments(){

        List<Regiment> regiments = regimentsRepository.findAll();
        List<RegimentResponseDto> regimentResponseDtoArrayList = new ArrayList<>();

        regiments.forEach(regiment -> {
            regimentResponseDtoArrayList.add(modelMapper.map(regiment, RegimentResponseDto.class));
        });

        return regimentResponseDtoArrayList;
    }

    public List<UnitDto> getUnits(){
        List<Unit> unitList = unitRepository.findAll();
        List<UnitDto> unitDtos = new ArrayList<>();

        unitList.forEach(unit -> {
            unitDtos.add(modelMapper.map(unit, UnitDto.class));
        });

        return unitDtos;
    }

    @Override
    public void deActivateUser(Long userId) throws QmsException {
        privilegedUserRepository.deActivatePrivilegedUser(userId);
    }

    @Override
    public PrivilegedUserResponseDto getUser(Long userId) throws QmsException {

        PrivilegedUser user = privilegedUserRepository.findPrivilegedUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Admin User Not Found");
        }

        PrivilegedUserResponseDto privilegedUserResponseDto = modelMapper.map(user, PrivilegedUserResponseDto.class);
        privilegedUserResponseDto.setRegiment(modelMapper.map(user.getRegiment(),RegimentResponseDto.class));
        return privilegedUserResponseDto;
    }

    private void setRole(PrivilegedUserRequestDto privilegedUserRequestDto, PrivilegedUser privilegedUser, Long addedUserId) {

        if(privilegedUserRequestDto.getId() == null) {
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findRoleByName(privilegedUserRequestDto.getRole()));
            userRole.setUser(privilegedUser);
            if(privilegedUser.getRole() == RoleType.ADMIN){
                userRole.setCreateUser(true);
            }else{
                userRole.setCreateUser(false);
            }

            privilegedUser.getUserRoles().add(userRole);
            privilegedUser.setAddedBy(userService.findUserById(addedUserId));
        } else {
            PrivilegedUser persistUser = privilegedUserRepository.findPrivilegedUserById(privilegedUserRequestDto.getId());
            List<UserRole> userRoles = persistUser.getUserRoles();

            privilegedUser.setUserRoles(userRoles);
        }
    }
}
