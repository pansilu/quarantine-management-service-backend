package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.NotFoundException;
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
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReportUserServiceImpl implements ReportUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StationRepository stationRepository;

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
    public void createUser(ReportUserRequestDto reportUserRequestDto, Long addedUserId) throws BadRequestException, NotFoundException {

        logger.debug("addedUserId: {}", addedUserId);

        userService.checkUserExists(reportUserRequestDto.getId());
        if(reportUserRequestDto.getMobile() == null) {
            logger.warn("Empty mobile num!");
            throw new BadRequestException(QmsExceptionCode.USR00X, "Mobile num can't be null");
        }

        userService.checkUserWithMobileNumExists(reportUserRequestDto.getMobile(), reportUserRequestDto.getId());

        ReportUser reportUser = modelMapper.map(reportUserRequestDto, ReportUser.class);

        reportUser.setUsername(reportUserRequestDto.getMobile());
        reportUser.setPassword(passwordEncoder.encode(reportUser.getOfficeId()));

        List<Station> grantLocations = stationRepository.findStationsByGivenIdList(reportUserRequestDto.getStationIdList());
        reportUser.setStations(grantLocations);

        setRole(reportUserRequestDto, reportUser, addedUserId);

        if(reportUserRequestDto.getName() != null && reportUserRequestDto.getOfficeId()!= null) {
            reportUser.setShowingName(reportUserRequestDto.getName() + " " + reportUserRequestDto.getOfficeId());
        }

        reportUserRepository.save(reportUser);
    }

    @Override
    public List<DivisionDto> getLocationDetails(Long userId, List<UserRoleDto> userRoles) {

        if(userRoles != null) {
            for(UserRoleDto userRoleDto : userRoles) {
                if(Objects.equals(userRoleDto.getRole(), RoleType.ROOT.name())){
                    List<Division> divisions = divisionRepository.getAllUserDivisions();
                    Type type = new TypeToken<List<DivisionDto>>() {}.getType();
                    return modelMapper.map(divisions, type);
                }
            }
        }

        List<Station> stations = stationRepository.findStationsByUserId(userId);
        Map<Long, DivisionDto> divisionDtoMap = new HashMap<>();

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
        }

        return new ArrayList<>(divisionDtoMap.values());
    }

    @Override
    public List<ReportUserResponseDto> getReportUsers(AdminFilterReqDto adminFilterReqDto) {

        List<ReportUser> reportUsers;

        if (adminFilterReqDto.getRanks() != null && adminFilterReqDto.getStationIds() != null) {

            reportUsers = reportUserRepository.findReportUsersByRanksAndStations(adminFilterReqDto.getRanks(), adminFilterReqDto.getStationIds());
            Type type = new TypeToken<List<ReportUserResponseDto>>() {}.getType();
            return modelMapper.map(reportUsers, type);

        } else if (adminFilterReqDto.getRanks() != null) {

            reportUsers = reportUserRepository.findReportUsersByGivenRanks(adminFilterReqDto.getRanks());
            Type type = new TypeToken<List<ReportUserResponseDto>>() {}.getType();
            return modelMapper.map(reportUsers, type);

        } else if (adminFilterReqDto.getStationIds() != null) {

            reportUsers = reportUserRepository.findReportUsersByGivenStations(adminFilterReqDto.getStationIds());
            Type type = new TypeToken<List<ReportUserResponseDto>>() {}.getType();
            return modelMapper.map(reportUsers, type);

        } else {

            reportUsers = reportUserRepository.findReportUsersWithStations();
            Type type = new TypeToken<List<ReportUserResponseDto>>() {}.getType();
            return modelMapper.map(reportUsers, type);

        }
    }

    @Override
    public ReportUserMultiPageResDto getUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles) {

        boolean isRoot = userService.checkUserIsRoot(userRoles);

        Page<ReportUser> users;
        if(isRoot) {
            users = reportUserRepository.findReportUsersWithStations(pageable);
        } else {
            users = reportUserRepository.findAddedReportUsersWithStations(adminId, pageable);
        }

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
    public ReportUserResponseDto getUser(Long userId, Long adminId, List<UserRoleDto> userRoles) throws NotFoundException, BadRequestException {

        ReportUser user = reportUserRepository.findReportUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new NotFoundException(QmsExceptionCode.USR00X, "Admin User Not Found");
        }

        if(!userService.checkUserIsRoot(userRoles) && !reportUserRepository.checkReportUserExistForGivenIdAndAddedUser(userId, adminId)) {
            logger.warn("No a_user: {} exists for admin: {}", userId, adminId);
            throw new BadRequestException(QmsExceptionCode.USR00X, "Selected Admin User view not allowed");
        }

        ReportUserResponseDto reportUserResponseDto = modelMapper.map(user, ReportUserResponseDto.class);

        user.getUserRoles().stream().filter(userRole -> userRole.getRole().getName() == RoleType.ADMIN)
                .forEach(userRole -> reportUserResponseDto.setCanCreateUser(userRole.isCreateUser()));

        return reportUserResponseDto;
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
        }
    }
}
