package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
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
import org.springframework.util.StringUtils;

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
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
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
    private DivisionRepository divisionRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(ReportUserRequestDto reportUserRequestDto, Long addedUserId) throws QmsException {

        logger.debug("addedUserId: {}", addedUserId);

        checkOfficeIdExists(reportUserRequestDto.getOfficeId(), reportUserRequestDto.getId());

        userService.checkUserExists(reportUserRequestDto.getId());
        if(reportUserRequestDto.getMobile() == null) {
            logger.warn("Empty mobile num!");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Mobile num can't be null");
        }

        userService.checkUserWithMobileNumExists(reportUserRequestDto.getMobile(), reportUserRequestDto.getId());

        ReportUser reportUser = modelMapper.map(reportUserRequestDto, ReportUser.class);
        reportUser.setNic(userService.validateNic(reportUserRequestDto.getNic(), reportUser.getId()));
        reportUser.setPassportNo(userService.validatePassport(reportUserRequestDto.getPassportNo(), reportUserRequestDto.getId()));

        reportUser.setUsername(reportUserRequestDto.getMobile());
        reportUser.setPassword(passwordEncoder.encode(reportUser.getOfficeId()));

        setRole(reportUserRequestDto, reportUser, addedUserId);

        if(reportUserRequestDto.getName() != null && reportUserRequestDto.getOfficeId()!= null) {
            reportUser.setShowingName(reportUserRequestDto.getName() + " " + reportUserRequestDto.getOfficeId());
        }

        reportUserRepository.save(reportUser);
    }

    // not used..........
    @Override
    public List<DivisionDto> getLocationDetails(Long userId, List<UserRoleDto> userRoles) {

        /*if(userRoles != null) {
            for(UserRoleDto userRoleDto : userRoles) {
                if(Objects.equals(userRoleDto.getRole(), RoleType.ROOT.name())){
                    List<Division> divisions = divisionRepository.getAllUserDivisions();
                    Type type = new TypeToken<List<DivisionDto>>() {}.getType();
                    return modelMapper.map(divisions, type);
                }
            }
        }*/

        /*Map<Long, DivisionDto> divisionDtoMap = new HashMap<>();

        List<Station> stations = stationRepository.findStationsByUserId(userId);
        for(Station station : stations) {

            StationDto stationDto = modelMapper.map(station, StationDto.class);

            if(divisionDtoMap.containsKey(station.getDivision().getId())) {
                DivisionDto divisionDto = divisionDtoMap.get(station.getDivision().getId());
                divisionDto.getStations().add(stationDto);
                divisionDtoMap.put(station.getDivision().getId(), divisionDto);
            }
            else {
                DivisionDto divisionDto = modelMapper.map(station.getDivision(), DivisionDto.class);
                List<StationDto> stationDtos = new ArrayList<>();
                stationDtos.add(stationDto);
                divisionDto.setStations(stationDtos);
                divisionDtoMap.put(station.getDivision().getId(), divisionDto);
            }
        }*/

        return new ArrayList<>();
    }

    // not used..........
    @Override
    public List<ReportUserResponseDto> getReportUsers(AdminFilterReqDto adminFilterReqDto, String search) {

        /*ist<ReportUser> reportUsers;

        if(!StringUtils.isEmpty(search)) {

            String pattern = "%" + search + "%";
            if (adminFilterReqDto.getRanks() != null && adminFilterReqDto.getStationIds() != null) {

                reportUsers = reportUserRepository.findReportUsersByRanksAndStations(pattern, adminFilterReqDto.getRanks(), adminFilterReqDto.getStationIds());
            } else if (adminFilterReqDto.getRanks() != null) {

                reportUsers = reportUserRepository.findReportUsersByGivenRanks(pattern, adminFilterReqDto.getRanks());
            } else if (adminFilterReqDto.getStationIds() != null) {

                reportUsers = reportUserRepository.findReportUsersByGivenStations(pattern, adminFilterReqDto.getStationIds());
            } else {

                reportUsers = reportUserRepository.findReportUsersWithStations(pattern);
            }
        } else {

            if (adminFilterReqDto.getRanks() != null && adminFilterReqDto.getStationIds() != null) {

                reportUsers = reportUserRepository.findReportUsersByRanksAndStations(adminFilterReqDto.getRanks(), adminFilterReqDto.getStationIds());
            } else if (adminFilterReqDto.getRanks() != null) {

                reportUsers = reportUserRepository.findReportUsersByGivenRanks(adminFilterReqDto.getRanks());
            } else if (adminFilterReqDto.getStationIds() != null) {

                reportUsers = reportUserRepository.findReportUsersByGivenStations(adminFilterReqDto.getStationIds());
            } else {

                reportUsers = reportUserRepository.findReportUsersWithStations();
            }
        }

        Type type = new TypeToken<List<ReportUserResponseDto>>() {}.getType();
        return modelMapper.map(reportUsers, type);*/

        return new ArrayList<>();
    }

    @Override
    public ReportUserMultiPageResDto getUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles, String search) {

        //boolean isRoot = userService.checkUserIsRoot(userRoles);

        Page<ReportUser> users = getPageableReportUsers(pageable, search);

        ReportUserMultiPageResDto reportUserMultiPageResDto = new ReportUserMultiPageResDto();

        List<ReportUserResponseDto> reportUserResponseDtoList = new ArrayList<>();

        users.forEach(reportUser -> {
            ReportUserResponseDto reportUserResponseDto = modelMapper.map(reportUser, ReportUserResponseDto.class);
            reportUserResponseDtoList.add(reportUserResponseDto);
        });

        reportUserMultiPageResDto.setData(reportUserResponseDtoList);
        reportUserMultiPageResDto.setTotalPages(users.getTotalPages());

        return reportUserMultiPageResDto;
    }

    @Override
    public ReportUserResponseDto getUser(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException {

        ReportUser user = reportUserRepository.findReportUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Admin User Not Found");
        }

        if(!userService.checkUserIsRoot(userRoles) && !reportUserRepository.checkReportUserExistForGivenIdAndAddedUser(userId, adminId)) {
            logger.warn("No a_user: {} exists for admin: {}", userId, adminId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Selected Admin User view not allowed");
        }

        ReportUserResponseDto reportUserResponseDto = modelMapper.map(user, ReportUserResponseDto.class);

        user.getUserRoles().stream().filter(userRole -> userRole.getRole().getName() == RoleType.ADMIN)
                .forEach(userRole -> reportUserResponseDto.setCanCreateUser(userRole.isCreateUser()));

        return reportUserResponseDto;
    }

    @Override
    public ReportUser findReportUserById(Long userId) throws QmsException {

        ReportUser user = reportUserRepository.findReportUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Admin User Not Found");
        }
        return user;
    }

    @Override
    public List<Long> getAdminUserStations(Long adminId) throws QmsException {

        /*ReportUser reportUser = findReportUserById(adminId);
        List<Long> stationIdList = new ArrayList<>();

        reportUser.getStations().forEach(station -> stationIdList.add(station.getId()));*/

        return new ArrayList<>();
    }

    private void setRole(ReportUserRequestDto reportUserRequestDto, ReportUser reportUser, Long addedUserId) {

        if(reportUserRequestDto.getId() == null) {
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findRoleByName(RoleType.ADMIN));
            userRole.setUser(reportUser);
            userRole.setCreateUser(reportUserRequestDto.isCanCreateUser());

            reportUser.getUserRoles().add(userRole);
            reportUser.setAddedBy(userService.findUserById(addedUserId));
        } else {
            ReportUser persistUser = reportUserRepository.findReportUserById(reportUserRequestDto.getId());
            List<UserRole> userRoles = persistUser.getUserRoles();

            userRoles.stream().filter(userRole -> userRole.getRole().getName() == RoleType.ADMIN)
                    .forEach(userRole -> userRole.setCreateUser(reportUserRequestDto.isCanCreateUser()));

            reportUser.setUserRoles(userRoles);
            reportUser.setAddedBy(persistUser.getAddedBy());
        }
    }

    private void checkOfficeIdExists(String officeId, Long userId) throws QmsException {

        boolean isUserExists = false;
        if(userId != null) {
            isUserExists = reportUserRepository.checkReportUserByOfficeId(officeId, userId);
        } else {
            isUserExists = reportUserRepository.checkReportUserByOfficeId(officeId);
        }

        if(isUserExists) {
            logger.warn("User with same officeId {} already exists", officeId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Given OfficeId was already existed. Please enter valid new OfficeId or edit existing user with given officeId");
        }
    }

    private Page<ReportUser> getPageableReportUsers(Pageable pageable, String search) {

        Page<ReportUser> users;

        if(!StringUtils.isEmpty(search)) {
            String pattern = "%" + search + "%";
            users = reportUserRepository.findReportUsersWithStations(pattern, pageable);

        } else {
            users = reportUserRepository.findAll(pageable);
        }

        return users;
    }
}
