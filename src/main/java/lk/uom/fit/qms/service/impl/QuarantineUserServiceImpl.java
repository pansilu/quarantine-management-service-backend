package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.config.security.CustomJwtTokenCreator;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.CountryService;
import lk.uom.fit.qms.service.QuarantineUserService;
import lk.uom.fit.qms.service.UserService;
import lk.uom.fit.qms.util.enums.RoleType;

import org.modelmapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.service.impl.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class QuarantineUserServiceImpl implements QuarantineUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isDebugEnable = logger.isDebugEnabled();

    @Value("${security.jwt.max.expiretime.days}")
    private Integer jwtExpireTimeInDays;

    @Value("${quarantine.period}")
    private short quarantinePeriod;

    @Value("${max.remind.period}")
    long maxRemindPeriod;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QuarantineUserRepository quarantineUserRepository;

    @Autowired
    private ReportUserRepository reportUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomJwtTokenCreator customJwtTokenCreator;

    @Autowired
    private ZoneId zoneId;

    @Autowired
    private UserDailyPointDetailsRepository userDailyPointDetailsRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(QuarantineUserRequestDto quarantineUserRequestDto, Long addedUserId) throws QmsException {

        logger.debug("addedUserId: {}", addedUserId);

        //Future Impl: need to implemet rUser ---> quser
        // check mobile num exists
        userService.checkUserExists(quarantineUserRequestDto.getId());
        userService.checkUserWithMobileNumExists(quarantineUserRequestDto.getMobile(), quarantineUserRequestDto.getId());

        QuarantineUser quarantineUser = modelMapper.map(quarantineUserRequestDto, QuarantineUser.class);
        setQuserMandetoryFieldIfUserExits(quarantineUserRequestDto.getId(), quarantineUser);

        checkSecretExistForAnotherUser(quarantineUserRequestDto, quarantineUser);

        quarantineUser.setArrivedCountry(countryService.findOne(quarantineUserRequestDto.getCountryId()));

        if(quarantineUserRequestDto.getInformedDate() != null) {
            quarantineUser.setInformedAuthority(true);
        }

        quarantineUser.setQuarantineUserInspectDetails(getInspectorDetails(quarantineUser, quarantineUserRequestDto));

        /*if(quarantineUserRequestDto.getGuardianDetails() != null) {
            GuardianDto guardianDto = quarantineUserRequestDto.getGuardianDetails();

            User guardian;
            if(guardianDto.getId() != null) {
               guardian = userService.findUserById(guardianDto.getId());
               guardian.setNic(guardianDto.getNic());
               guardian.setMobile(guardianDto.getMobile());
               guardian.setPassportNo(guardianDto.getPassportNo());
            }
            else {
                guardian = modelMapper.map(guardianDto, User.class);
            }
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findRoleByName(RoleType.GUARDIAN));
            userRole.setUser(guardian);

            guardian.getUserRoles().add(userRole);
            quarantineUser.setGuardian(userService.saveGuardian(guardian));
        }*/

        setPatientDetails(quarantineUserRequestDto, quarantineUser);

        if(quarantineUserRequestDto.getId() == null) {
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findRoleByName(RoleType.Q_USER));
            userRole.setUser(quarantineUser);

            quarantineUser.getUserRoles().add(userRole);
            quarantineUser.setAddedBy(userService.findUserById(addedUserId));
        }

        Address address = quarantineUser.getAddress();
        address.setStation(stationRepository.findStationsById(quarantineUserRequestDto.getStationId()));

        quarantineUser.setAddress(addressRepository.save(address));
        quarantineUserRepository.save(quarantineUser);
    }

    @Override
    public UserLoginResponseDto authenticateUser(String secret) throws QmsException {

        if (isDebugEnable) {
            logger.debug("Login request for secret : {}", secret);
        }

        QuarantineUser user = quarantineUserRepository.findQuarantineUserBySecret(secret);

        if (user == null) {
            logger.warn("No user found by given secret : {}", secret);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "No user found by given secret");
        }

        List<InspectUserJwtDto> inspectUserDetails = new ArrayList<>();

        if(user.getQuarantineUserInspectDetails() != null) {
            user.getQuarantineUserInspectDetails().forEach(quarantineUserInspectDetails -> {
                InspectUserJwtDto inspectUserJwtDto = modelMapper.map(quarantineUserInspectDetails.getReportUser(), InspectUserJwtDto.class);
                inspectUserDetails.add(inspectUserJwtDto);
            });
        }

        String token = customJwtTokenCreator.generateMobileJwtToken(user, jwtExpireTimeInDays, inspectUserDetails);
        logger.info("Mobile User authentication enable response with token : {}", token);

        if (isDebugEnable) {
            logger.debug("Login response token : {}, for secret : {}", token, secret);
        }
        return new UserLoginResponseDto(token);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePointValue(Map<String, Boolean> pointValueMap, Long qUserId) throws QmsException {

        LocalDate localDate = LocalDate.now(zoneId);

        if(userDailyPointDetailsRepository.isUserUpdateForCurrentDate(qUserId, localDate)) {
            logger.warn("User: {}, already update point table", qUserId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Create entry found for today!!");
        }

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(qUserId);
        List<Point> regularPoints = pointRepository.getRegularPointNames();
        short totalPoints = 0;

        LocalDate reportDate = user.getReportDate();
        short diff = (short) DAYS.between(reportDate, localDate);
        short remainingDays;
        if(diff > quarantinePeriod) {
            remainingDays = 0;
        } else {
            remainingDays = (short)(quarantinePeriod - diff);
        }

        List<UserDailyPointDetails> userDailyPointDetailsList = new ArrayList<>();

        for(Point point : regularPoints) {
            if(pointValueMap.containsKey(point.getCode())) {
                UserDailyPointDetails userDailyPointDetails = new UserDailyPointDetails();
                userDailyPointDetails.setPoint(point);
                userDailyPointDetails.setUser(user);
                userDailyPointDetails.setRecordDate(localDate);
                userDailyPointDetails.setValue(pointValueMap.get(point.getCode()));
                userDailyPointDetailsList.add(userDailyPointDetails);

                if(Boolean.TRUE.equals(pointValueMap.get(point.getCode()))) {
                    totalPoints = (short) (totalPoints + point.getValue());
                }
            }
        }

        user.setTotalPoints(totalPoints);
        user.setLastValueUpdateDate(LocalDateTime.now());
        user.setRemainingDays(remainingDays);
        quarantineUserRepository.save(user);

        userDailyPointDetailsRepository.saveAll(userDailyPointDetailsList);
    }

    @Override
    public QuarantineUserMultiPageResDto getQuarantineUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles) {

        boolean isRoot = userService.checkUserIsRoot(userRoles);

        Page<QuarantineUser> users;
        if(isRoot) {
            users = quarantineUserRepository.findAll(pageable);
        } else {
            users = quarantineUserRepository.findQuarantineUsersInStations(getAdminUserStations(adminId), pageable);
        }

        QuarantineUserMultiPageResDto quarantineUserMultiPageResDto = new QuarantineUserMultiPageResDto();
        List<QuarantineMultiUserResDto> userResDtoList = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();

        users.forEach(user -> {
            QuarantineMultiUserResDto userResDto = modelMapper.map(user, QuarantineMultiUserResDto.class);
            userResDto.setLastUpdateDate(user.getLastValueUpdateDate().toLocalDate());

            if(ChronoUnit.HOURS.between(user.getLastValueUpdateDate(), currentDateTime) > maxRemindPeriod) {
                userResDto.setNeedToRemind(true);
            }
            userResDtoList.add(userResDto);
        });

        quarantineUserMultiPageResDto.setData(userResDtoList);
        quarantineUserMultiPageResDto.setTotalPages(users.getTotalPages());

        return quarantineUserMultiPageResDto;
    }

    @Override
    public QuarantineUserPointValueDto getUserPointValues(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException {

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Quarantine User Not Found");
        }

        if(!userService.checkUserIsRoot(userRoles) && !quarantineUserRepository.checkQuarantineUserExistForGivenIdInSelectedStations(userId, getAdminUserStations(adminId))) {
            logger.warn("No q_user: {} exists for admin: {}", userId, adminId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Selected Quarantine User view not allowed");
        }

        List<UserDailyPointDetails> pointDetailsList = userDailyPointDetailsRepository.findAllPointDetailsByUserId(userId);

        QuarantineUserPointValueDto quarantineUserPointValueDto = new QuarantineUserPointValueDto();
        quarantineUserPointValueDto.setUserId(user.getId());
        quarantineUserPointValueDto.setName(user.getName());

        Map<LocalDate, Map<String, Boolean>> pointValueMap = new HashMap<>();

        pointDetailsList.forEach(userDailyPointDetails -> {

            Point point = userDailyPointDetails.getPoint();

            if(pointValueMap.containsKey(userDailyPointDetails.getRecordDate())) {
                pointValueMap.get(userDailyPointDetails.getRecordDate()).put(point.getCode(), userDailyPointDetails.isValue());
            } else {

                Map<String, Boolean> pointValue = new HashMap<>();
                pointValue.put(point.getCode(), userDailyPointDetails.isValue());
                pointValueMap.put(userDailyPointDetails.getRecordDate(), pointValue);
            }
        });

        List<PointValueDto> pointValueDtos = new ArrayList<>();

        pointValueMap.forEach((localDate, stringBooleanMap) -> pointValueDtos.add(new PointValueDto(localDate, stringBooleanMap)));

        quarantineUserPointValueDto.setDailyUpdates(pointValueDtos);

        return quarantineUserPointValueDto;
    }

    @Override
    public QuarantineUserResDto getUser(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException {

        logger.debug("request user details for user: {}", userId);

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Quarantine User Not Found");
        }

        if(!userService.checkUserIsRoot(userRoles) && !quarantineUserRepository.checkQuarantineUserExistForGivenIdInSelectedStations(userId, getAdminUserStations(adminId))) {
            logger.warn("No q_user: {} exists for admin: {}", userId, adminId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Selected Quarantine User view not allowed");
        }

        QuarantineUserResDto quarantineUserResDto = modelMapper.map(user, QuarantineUserResDto.class);

        StationResDto stationResDto = modelMapper.map(user.getAddress().getStation(), StationResDto.class);
        quarantineUserResDto.setStationResDto(stationResDto);

        if(user.getGuardian() != null) {
            quarantineUserResDto.setGuardianDetails(modelMapper.map(user.getGuardian(), GuardianDto.class));
        }

        List<ReportUserResponseDto> inspectorDetails = new ArrayList<>();
        user.getQuarantineUserInspectDetails().forEach(quarantineUserInspectDetails -> {
            ReportUserResponseDto reportUserResponseDto = modelMapper.map(quarantineUserInspectDetails.getReportUser(), ReportUserResponseDto.class);
            inspectorDetails.add(reportUserResponseDto);
        });
        quarantineUserResDto.setInspectorDetails(inspectorDetails);

        if(user.isPatient()) {
            PatientDetails patientDetails = user.getPatientDetails();

            if(patientDetails.getAdmitHospital() != null) {
                quarantineUserResDto.setAdmitHos(modelMapper.map(patientDetails.getAdmitHospital(), HospitalDto.class));
                quarantineUserResDto.setAdmittedDate(patientDetails.getAdmittedDate());
            }

            if(patientDetails.getConfirmedHospital() != null) {
                quarantineUserResDto.setConfirmedHos(modelMapper.map(patientDetails.getConfirmedHospital(), HospitalDto.class));
                quarantineUserResDto.setConfirmedDate(patientDetails.getConfirmedDate());
            }

            quarantineUserResDto.setDischargedDate(patientDetails.getDischargedDate());
        }

        return quarantineUserResDto;
    }

    void checkSecretExistForAnotherUser(QuarantineUserRequestDto quarantineUserRequestDto, QuarantineUser quarantineUser) throws QmsException {

        if(quarantineUserRequestDto.getMobile() != null && quarantineUserRequestDto.isAppEnable()) {
            if (quarantineUserRequestDto.getId() != null) {
                if(quarantineUserRepository.isSecretExistForAnotherUser(quarantineUserRequestDto.getMobile(), quarantineUserRequestDto.getId())) {
                    logger.warn("Mobile app secret : {} already exists for another user", quarantineUserRequestDto.getMobile());
                    throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Mobile app secret exists for another user!");
                }
            } else {
                if(quarantineUserRepository.isSecretExistForAnotherUser(quarantineUserRequestDto.getMobile())) {
                    logger.warn("Mobile app secret : {} already exists for another user", quarantineUserRequestDto.getMobile());
                    throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Mobile app secret exists for another user!");
                }
            }
            quarantineUser.setSecret(quarantineUserRequestDto.getMobile());
            quarantineUser.setUsername(quarantineUserRequestDto.getMobile());
            quarantineUser.setAppEnable(true);
        }
    }

    private List<QuarantineUserInspectDetails> getInspectorDetails(
            QuarantineUser quarantineUser, QuarantineUserRequestDto quarantineUserRequestDto) {

        List<QuarantineUserInspectDetails> quarantineUserUpdateInspectDetailList = new ArrayList<>();
        List<QuarantineUserInspectDetails> quarantineUserPersistInspectDetailList = new ArrayList<>();

        List<Long> persistInspectIdsCopy1 = new ArrayList<>();

        if(quarantineUserRequestDto.getId() != null) {
            QuarantineUser persistUser = quarantineUserRepository.findQuarantineUserById(quarantineUserRequestDto.getId());
            persistUser.getQuarantineUserInspectDetails().forEach(quarantineUserInspectDetails -> {
                persistInspectIdsCopy1.add(quarantineUserInspectDetails.getReportUser().getId());
                quarantineUserPersistInspectDetailList.add(quarantineUserInspectDetails);
            });
        }

        if(quarantineUserRequestDto.getInspectorIds() != null) {

            List<Long> persistInspectIdsCopy2 = new ArrayList<>(persistInspectIdsCopy1);
            List<Long> updatedInspectIdsCopy1 = quarantineUserRequestDto.getInspectorIds().stream().distinct().collect(Collectors.toList());
            List<Long> updatedInspectIdsCopy2 = new ArrayList<>(updatedInspectIdsCopy1);

            // add new inspect details....
            updatedInspectIdsCopy1.removeAll(persistInspectIdsCopy1);

            updatedInspectIdsCopy1.forEach(inspectorId -> {

                ReportUser reportUser = reportUserRepository.findReportUserById(inspectorId);
                if(reportUser != null) {
                    QuarantineUserInspectDetails quarantineUserInspectDetails = new QuarantineUserInspectDetails();
                    quarantineUserInspectDetails.setReportUser(reportUserRepository.findReportUserById(inspectorId));
                    quarantineUserInspectDetails.setQuarantineUser(quarantineUser);

                    quarantineUserUpdateInspectDetailList.add(quarantineUserInspectDetails);
                }
            });

            // remove change inspect details....
            persistInspectIdsCopy2.removeAll(updatedInspectIdsCopy2);

            persistInspectIdsCopy2.forEach(removeInspectId -> quarantineUserPersistInspectDetailList.stream().filter(quarantineUserInspectDetails -> quarantineUserInspectDetails.getReportUser().getId().equals(removeInspectId))
                    .forEach(quarantineUserInspectDetails -> quarantineUserInspectDetails.setDeleted(true)));
        }

        quarantineUserUpdateInspectDetailList.addAll(quarantineUserPersistInspectDetailList);

        return quarantineUserUpdateInspectDetailList;
    }

    private void setPatientDetails(QuarantineUserRequestDto quarantineUserRequestDto, QuarantineUser quarantineUser) throws QmsException {

        if(quarantineUserRequestDto.getAdmittedDate() != null || quarantineUserRequestDto.getConfirmedDate() != null) {
            logger.info("setting patien details... admitDate: {}, confirmedDate: {}", quarantineUserRequestDto.getAdmittedDate(), quarantineUserRequestDto.getConfirmedDate());
            quarantineUser.setPatient(true);
            PatientDetails patientDetails = new PatientDetails();

            if(quarantineUserRequestDto.getAdmittedDate() != null && quarantineUserRequestDto.getAdmitHos() != null){

                if(quarantineUserRequestDto.getAdmitHos().getId() != null && quarantineUserRequestDto.getAdmitHos().getId() != 0) {
                    patientDetails.setAdmitHospital(hospitalRepository.findHospitalById(quarantineUserRequestDto.getAdmitHos().getId()));
                } else {
                    if (StringUtils.isEmpty(quarantineUserRequestDto.getAdmitHos().getName())) {
                        logger.warn("Admit Hospital name can't be empty");
                        throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Admit Hospital name can't be empty");
                    } else {
                        Hospital hospital = new Hospital();
                        hospital.setName(quarantineUserRequestDto.getAdmitHos().getName());
                        patientDetails.setAdmitHospital(hospitalRepository.save(hospital));
                    }
                }
                patientDetails.setAdmittedDate(quarantineUserRequestDto.getAdmittedDate());
            }

            if(quarantineUserRequestDto.getDischargedDate() != null) {
                patientDetails.setDischarged(true);
                patientDetails.setDischargedDate(quarantineUserRequestDto.getDischargedDate());
            }

            if(quarantineUserRequestDto.getConfirmedDate() != null && quarantineUserRequestDto.getConfirmedHos() != null) {

                if(quarantineUserRequestDto.getConfirmedHos().getId() != null && quarantineUserRequestDto.getConfirmedHos().getId() != 0) {
                    patientDetails.setConfirmedHospital(hospitalRepository.findHospitalById(quarantineUserRequestDto.getConfirmedHos().getId()));
                } else {
                    if (StringUtils.isEmpty(quarantineUserRequestDto.getConfirmedHos().getName())) {
                        logger.warn("Confirmed Hospital name can't be empty");
                        throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Confirmed Hospital name can't be empty");
                    } else {
                        Hospital hospital = new Hospital();
                        hospital.setName(quarantineUserRequestDto.getConfirmedHos().getName());
                        patientDetails.setConfirmedHospital(hospitalRepository.save(hospital));
                    }
                }

                patientDetails.setInfected(true);
                patientDetails.setConfirmedDate(quarantineUserRequestDto.getConfirmedDate());
            }

            quarantineUser.setPatientDetails(patientDetails);
            quarantineUser.getPatientDetails().setPatient(quarantineUser);
        }
    }

    private List<Long> getAdminUserStations(Long adminId) {

        ReportUser reportUser = reportUserRepository.findReportUserById(adminId);
        List<Long> stationIdList = new ArrayList<>();

        reportUser.getStations().forEach(station -> stationIdList.add(station.getId()));

        return stationIdList;
    }

    private void setQuserMandetoryFieldIfUserExits(Long id, QuarantineUser quarantineUser) {

        if(id != null) {
            QuarantineUser persistUser = quarantineUserRepository.findQuarantineUserById(id);
            quarantineUser.setRemainingDays(persistUser.getRemainingDays());
            quarantineUser.setLastValueUpdateDate(persistUser.getLastValueUpdateDate());
            quarantineUser.setTotalPoints(persistUser.getTotalPoints());
        }
    }
}
