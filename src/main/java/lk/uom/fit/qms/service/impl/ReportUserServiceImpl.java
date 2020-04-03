package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.ReportUserService;
import lk.uom.fit.qms.util.enums.RoleType;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(ReportUserRequestDto reportUserRequestDto, Long addedUserId) throws BadRequestException {

        logger.debug("addedUserId: {}", addedUserId);

        if(reportUserRequestDto.getMobile() == null) {
            logger.warn("Empty mobile num!");
            throw new BadRequestException(QmsExceptionCode.USR00X, "Mobile num can't be null");
        }

        ReportUser reportUser = modelMapper.map(reportUserRequestDto, ReportUser.class);

        if(reportUserRequestDto.getId() == null) {
            reportUser.setUsername(reportUserRequestDto.getMobile());
            reportUser.setPassword(passwordEncoder.encode(reportUser.getOfficeId()));
        }

        List<Station> grantLocations = stationRepository.findStationsByGivenIdList(reportUserRequestDto.getStationIdList());
        reportUser.setStations(grantLocations);

        UserRole userRole = new UserRole();
        userRole.setRole(roleRepository.findRoleByName(RoleType.Q_USER));
        userRole.setUser(reportUser);

        reportUser.getUserRoles().add(userRole);

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
}
