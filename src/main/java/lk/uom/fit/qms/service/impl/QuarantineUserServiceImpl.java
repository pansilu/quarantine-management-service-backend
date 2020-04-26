package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.config.security.CustomJwtTokenCreator;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.*;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;
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

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    @Value("${current.year}")
    int currentYear;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QuarantineUserRepository quarantineUserRepository;

    @Autowired
    private ReportUserService reportUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomJwtTokenCreator customJwtTokenCreator;

    @Autowired
    private ZoneId zoneId;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private GramaNiladariDivisionService gramaNiladariDivisionService;

    /*@PostConstruct
    private void init() {
        logger.info("start init method");
        calUserRemainingDays();
        initQuarantineUserAge();
    }*/

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(QuarantineUserRequestDto quarantineUserRequestDto, Long addedUserId) throws QmsException {

        logger.debug("addedUserId: {}", addedUserId);

        //Future Impl: need to implemet rUser ---> quser
        userService.checkUserExists(quarantineUserRequestDto.getId());
        userService.checkUserWithMobileNumExists(quarantineUserRequestDto.getMobile(), quarantineUserRequestDto.getId()); // check mobile num exists

        if(quarantineUserRequestDto.getAddress() == null) {
            logger.warn("Address object not set in request");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Address need to be set");
        }

        QuarantineUser quarantineUser = modelMapper.map(quarantineUserRequestDto, QuarantineUser.class);
        quarantineUser.setNic(userService.validateNic(quarantineUserRequestDto.getNic(), quarantineUserRequestDto.getId()));
        quarantineUser.setPassportNo(userService.validatePassport(quarantineUserRequestDto.getPassportNo(), quarantineUserRequestDto.getId()));
        getAge(quarantineUser);

        if(quarantineUserRequestDto.getCountryId() != null) {
            quarantineUser.setArrivedCountry(countryService.findOne(quarantineUserRequestDto.getCountryId()));
        }

        if(quarantineUserRequestDto.getId() == null) {
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findRoleByName(RoleType.Q_USER));
            userRole.setUser(quarantineUser);

            quarantineUser.getUserRoles().add(userRole);
            quarantineUser.setAddedBy(userService.findUserById(addedUserId));
        }

        Address address = quarantineUser.getAddress();
        address.setGnDivision(gramaNiladariDivisionService.getGramaNiladariDivision(quarantineUserRequestDto.getAddress().getGndId()));

        quarantineUser.setAddress(addressRepository.save(address));

        setUserStatus(quarantineUser, quarantineUserRequestDto.getUserStatusDetails());

        quarantineUserRepository.save(quarantineUser);
    }

    @Override
    public UserLoginResponseDto authenticateUser(String secret) throws QmsException {

        if (isDebugEnable) {
            logger.debug("Login request for secret : {}", secret);
        }

        QuarantineUser user = quarantineUserRepository.findAppEnableQuarantineUserBySecret(secret);

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

    // not used....
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePointValue(Map<String, Boolean> pointValueMap, Long qUserId) throws QmsException {

        /*if(quarantineUserRepository.checkUserQuarantinePeriodOver(qUserId)) {
            logger.warn("User: {}, quarantine period over", qUserId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Your Quarantine period was over!!");
        }

        LocalDate localDate = LocalDate.now(zoneId);

        if(userDailyPointDetailsRepository.isUserUpdateForCurrentDate(qUserId, localDate)) {
            logger.warn("User: {}, already update point table", qUserId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Create entry found for today!!");
        }

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(qUserId);
        List<Point> regularPoints = pointRepository.getRegularPointNames();
        short totalPoints = 0;

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
        quarantineUserRepository.save(user);

        userDailyPointDetailsRepository.saveAll(userDailyPointDetailsList);*/
    }

    @Override
    public QuarantineUserMultiPageResDto getQuarantineUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles, String search) throws QmsException {

        boolean isRoot = userService.checkUserIsRoot(userRoles);

        Page<QuarantineUser> users = getPageableQuarantineUsers(pageable, search, isRoot, adminId);

        QuarantineUserMultiPageResDto quarantineUserMultiPageResDto = new QuarantineUserMultiPageResDto();
        List<QuarantineMultiUserResDto> userResDtoList = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now();

        users.forEach(user -> {
            QuarantineMultiUserResDto userResDto = modelMapper.map(user, QuarantineMultiUserResDto.class);
            userResDto.setLastUpdateDate(convertUtcTimeToLocalDateTime(user.getLastValueUpdateDate()).toLocalDate());
            StationResDto stationResDto = modelMapper.map(user.getAddress().getStation(), StationResDto.class);
            userResDto.setStationResDto(stationResDto);

            if(user.isAppEnable() && !user.isCompleted() && (ChronoUnit.HOURS.between(user.getLastValueUpdateDate(), currentDateTime) > maxRemindPeriod)) {
                userResDto.setNeedToRemind(true);
            }
            userResDtoList.add(userResDto);
        });

        quarantineUserMultiPageResDto.setData(userResDtoList);
        quarantineUserMultiPageResDto.setTotalPages(users.getTotalPages());

        return quarantineUserMultiPageResDto;
    }

    // not used....
    @Override
    public QuarantineUserPointValueDto getUserPointValues(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException {

        /*QuarantineUser user = quarantineUserRepository.findQuarantineUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Quarantine User Not Found");
        }

        if(!userService.checkUserIsRoot(userRoles) && !quarantineUserRepository.checkQuarantineUserExistForGivenIdInSelectedStations(userId, reportUserService.getAdminUserStations(adminId))) {
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

        return quarantineUserPointValueDto;*/

        return null;
    }

    @Override
    public QuarantineUserResDto getUser(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException {

        logger.debug("request user details for user: {}", userId);

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Quarantine User Not Found");
        }

        if(!userService.checkUserIsRoot(userRoles) && !quarantineUserRepository.checkQuarantineUserExistForGivenIdInSelectedStations(userId, reportUserService.getAdminUserStations(adminId))) {
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void calUserRemainingDays() {

        logger.info("cal user remaining days");

        List<QuarantineUser> users = quarantineUserRepository.findAll();

        users.forEach(quarantineUser -> {
            setRemainingDays(quarantineUser);
            quarantineUserRepository.save(quarantineUser);
        });

        logger.info("cal user remaining days completed");
    }

    // not used....
    @Override
    public boolean isAppEnable(Long userId) {
        return true;
    }

    /*void checkSecretExistForAnotherUser(QuarantineUserRequestDto quarantineUserRequestDto, QuarantineUser quarantineUser) throws QmsException {

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
    }*/

    private Page<QuarantineUser> getPageableQuarantineUsers(Pageable pageable, String search, boolean isRoot, Long adminId) throws QmsException {

        Page<QuarantineUser> users;

        if(!StringUtils.isEmpty(search)) {
            String pattern = "%" + search + "%";
            if(isRoot) {
                users = quarantineUserRepository.findQuarantineUsersForRoot(pattern, pageable);
            } else {
                users = quarantineUserRepository.findQuarantineUsersInStations(reportUserService.getAdminUserStations(adminId), pattern, pageable);
            }
        } else {
            if (isRoot) {
                users = quarantineUserRepository.findAll(pageable);
            } else {
                users = quarantineUserRepository.findQuarantineUsersInStations(reportUserService.getAdminUserStations(adminId), pageable);
            }
        }

        return users;
    }

    private void getAge(QuarantineUser user) {

        if(user.getNic() != null && user.getAge() == null) {

            String birthYear;
            int age;

            if(user.getNic().matches("^[0-9]{9}[vVxX]$")) {

                birthYear = user.getNic().substring(0, 2);
                birthYear = "19" + birthYear;
                age = currentYear - Integer.parseInt(birthYear);
                user.setAge(age);
            } else if(user.getNic().matches("^[0-9]{12}$")) {

                birthYear = user.getNic().substring(0, 4);
                age = currentYear - Integer.parseInt(birthYear);
                user.setAge(age);
            } else {
                user.setNic(null);
            }
        }
    }

    private void setRemainingDays(QuarantineUser user) {

        LocalDate localDate = LocalDate.now(zoneId);
        LocalDate reportDate = user.getReportDate();
        short diff = (short) DAYS.between(reportDate, localDate);
        short remainingDays = 0;

        if(diff == 0) {
            remainingDays = quarantinePeriod;
        }
        else if(diff > quarantinePeriod) {
            user.setCompleted(true);
            user.setCompletedDate(reportDate.plusDays((short)(quarantinePeriod + 1)));
        } else {
            remainingDays = (short)(quarantinePeriod - (diff - 1));
        }

        user.setRemainingDays(remainingDays);
    }

    private void initQuarantineUserAge() {

        logger.info("cal user age");

        List<QuarantineUser> users = quarantineUserRepository.findAll();
        users.forEach(quarantineUser -> {

            if(StringUtils.isEmpty(quarantineUser.getNic())) {
                quarantineUser.setNic(null);
            } else {
                String nic = quarantineUser.getNic().replaceAll("\\s+","");
                quarantineUser.setNic(nic);
                getAge(quarantineUser);
            }

            if(StringUtils.isEmpty(quarantineUser.getPassportNo())) {
                quarantineUser.setPassportNo(null);
            } else {
                String passport = quarantineUser.getPassportNo().replaceAll("\\s+","");
                quarantineUser.setPassportNo(passport);
            }

            quarantineUserRepository.save(quarantineUser);
        });

        logger.info("cal user age completed");

    }

    private LocalDateTime convertUtcTimeToLocalDateTime(LocalDateTime utcTime) {

        ZonedDateTime utcZoned  = utcTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime ldtZoned = utcZoned.withZoneSameInstant(zoneId);
        return ldtZoned.toLocalDateTime();
    }

    private void setUserStatus(QuarantineUser quarantineUser, List<QuarantineUserStatusDetail> userStatusDetails) throws QmsException {

        QuarantineUserStatus status = QuarantineUserStatus.COMPLETE;

        if(quarantineUser.getId() != null) {
            QuarantineUser persistUser = quarantineUserRepository.findQuarantineUserById(quarantineUser.getId());
            quarantineUser.setAddedBy(persistUser.getAddedBy());

            quarantineUser.setHqDetails(persistUser.getHqDetails());
            quarantineUser.setRqDetails(persistUser.getRqDetails());
            quarantineUser.setScDetails(persistUser.getScDetails());
            quarantineUser.setPcDetails(persistUser.getPcDetails());
            quarantineUser.setDeceasedDetails(persistUser.getDeceasedDetails());

            status = persistUser.getStatus();
        }

        boolean isDeceased = false;
        int numOfStatusWithoutEndDate = 0;

        for(QuarantineUserStatusDetail quarantineUserStatusDetail : userStatusDetails) {

            if(quarantineUserStatusDetail.getType() != QuarantineUserStatus.DECEASED && !isDeceased && quarantineUserStatusDetail.getEndDate() == null && !quarantineUserStatusDetail.isDelete()) {
                numOfStatusWithoutEndDate += 1;
                status = quarantineUserStatusDetail.getType();
            }

            if(quarantineUserStatusDetail.getType() == QuarantineUserStatus.DECEASED && !quarantineUserStatusDetail.isDelete()) {
                isDeceased = true;
                status = QuarantineUserStatus.DECEASED;
            }

            if(numOfStatusWithoutEndDate > 1) {
                logger.warn("More than one records without end date found in user status details: {}", userStatusDetails);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Found more than one, not complete user status");
            }

            setUserSingleStatus(quarantineUser, quarantineUserStatusDetail);
        }

        quarantineUser.setStatus(status);
    }

    private void setUserSingleStatus(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        switch(quarantineUserStatusDetail.getType()) {

            case HOUSE_QUARANTINE:
                addHouseQuarantineStatusDetail(user, quarantineUserStatusDetail);
                break;
            case REMOTE_QUARANTINE:
                addRemoteQuarantineStatusDetail(user, quarantineUserStatusDetail);
                break;
            case SUSPECT_COVID:
                addSuspectCovidStatusDetail(user, quarantineUserStatusDetail);
                break;
            case POSITIVE_COVID:
                addPositiveCovidStatusDetail(user, quarantineUserStatusDetail);
                break;
            case DECEASED:
                addDeceasedStatusDetail(user, quarantineUserStatusDetail);
                break;
            default:
                logger.warn("Not a valid status type: {}", quarantineUserStatusDetail.getType());
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Invalid user status type");
        }
    }

    private void addHouseQuarantineStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) {

    }

    private void addRemoteQuarantineStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) {

    }

    private void addSuspectCovidStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) {

    }

    private void addPositiveCovidStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) {

    }

    private void addDeceasedStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) {

    }
}
