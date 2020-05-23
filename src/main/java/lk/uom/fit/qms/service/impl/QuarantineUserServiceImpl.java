package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.config.security.CustomJwtTokenCreator;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.*;
import lk.uom.fit.qms.util.enums.LocationState;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;
import lk.uom.fit.qms.util.enums.RoleType;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private HospitalService hospitalService;

    @Autowired
    private GramaNiladariDivisionService gramaNiladariDivisionService;

    @Autowired
    private QuarantineCenterService quarantineCenterService;

    @Autowired
    private PositiveCovidDetailService positiveCovidDetailService;

    @Autowired
    private GndRiskDetailService gndRiskDetailService;

    /*@PostConstruct
    private void init() {
        logger.info("start init remaining days cal method");
        //calUserRemainingDays();
        initUsers();
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
        } else {
            quarantineUser.setArrivalDate(null);
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

        QuarantineUser savedUser = quarantineUserRepository.save(quarantineUser);

        gndRiskDetailService.updateGndRiskDetail(savedUser);
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
    public QuarantineUserMultiPageResDto getQuarantineUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles, String search, QuarantineUserStatus status) throws QmsException {

        /*boolean isRoot = userService.checkUserIsRoot(userRoles);*/

        Page<QuarantineUser> users = getPageableQuarantineUsers(pageable, search, status);

        QuarantineUserMultiPageResDto quarantineUserMultiPageResDto = new QuarantineUserMultiPageResDto();
        List<QuarantineMultiUserResDto> userResDtoList = new ArrayList<>();

        users.forEach(user -> {
            QuarantineMultiUserResDto userResDto = modelMapper.map(user, QuarantineMultiUserResDto.class);
            userResDto.getAddress().setGndId(user.getAddress().getGnDivision().getId());

            if(user.getStatus() == QuarantineUserStatus.HOUSE_QUARANTINE) {
                quarantineUserRepository.getUserHomeQuarantineDetails(user.getId()).stream().filter(HomeQuarantineDetail::isActive)
                        .forEach(homeQuarantineDetail -> userResDto.setRemainingDays(homeQuarantineDetail.getRemainingDays()));
            }

            if(user.getStatus() == QuarantineUserStatus.REMOTE_QUARANTINE) {
                quarantineUserRepository.getUserRemoteQuarantineDetails(user.getId()).stream().filter(RemoteQuarantineDetail::isActive)
                        .forEach(remoteQuarantineDetail -> userResDto.setRemainingDays(remoteQuarantineDetail.getRemainingDays()));
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

        /*if(!userService.checkUserIsRoot(userRoles) && !quarantineUserRepository.checkQuarantineUserExistForGivenIdInSelectedStations(userId, reportUserService.getAdminUserStations(adminId))) {
            logger.warn("No q_user: {} exists for admin: {}", userId, adminId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Selected Quarantine User view not allowed");
        }*/

        QuarantineUserResDto quarantineUserResDto = modelMapper.map(user, QuarantineUserResDto.class);

        Type type = new TypeToken<List<QuarantineUserStatusDetail>>() {}.getType();
        quarantineUserResDto.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserHomeQuarantineDetails(userId), type));
        quarantineUserResDto.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserRemoteQuarantineDetails(userId), type));
        quarantineUserResDto.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserSuspectCovidDetails(userId), type));
        quarantineUserResDto.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserPositiveCovidDetails(userId), type));
        quarantineUserResDto.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserDeceasedDetails(userId), type));

        return quarantineUserResDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void calUserRemainingDays() {

        logger.info("cal user remaining days");

        List<QuarantineUser> hqActiveUsers = quarantineUserRepository.findQuarantineUsersWithHqStatus();

        hqActiveUsers.forEach(quarantineUser -> quarantineUser.getHqDetails().stream().filter(HomeQuarantineDetail::isActive)
                .forEach(homeQuarantineDetail -> homeQuarantineDetail.setRemainingDays(getRemainingDays(homeQuarantineDetail.getStartDate(), null))));

        List<QuarantineUser> rqActiveUsers = quarantineUserRepository.findQuarantineUsersWithRqStatus();

        rqActiveUsers.forEach(quarantineUser -> quarantineUser.getRqDetails().stream().filter(RemoteQuarantineDetail::isActive)
                .forEach(remoteQuarantineDetail -> remoteQuarantineDetail.setRemainingDays(getRemainingDays(remoteQuarantineDetail.getStartDate(), null))));

        logger.info("cal user remaining days completed");
    }

    // not used....
    @Override
    public boolean isAppEnable(Long userId) {
        return true;
    }

    @Override
    public QuarantineUserMapResponse getQuarantineUserMapResponse(Long userId) throws QmsException {

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(userId);

        if(user == null) {
            logger.warn("User not exists for id: {}", userId);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Quarantine User Not Found");
        }

        QuarantineUserMapResponse quarantineUserMapResponse = modelMapper.map(user, QuarantineUserMapResponse.class);

        Type type = new TypeToken<List<QuarantineUserStatusDetailResponse>>() {}.getType();
        quarantineUserMapResponse.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserHomeQuarantineDetails(userId), type));
        quarantineUserMapResponse.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserRemoteQuarantineDetails(userId), type));
        quarantineUserMapResponse.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserSuspectCovidDetails(userId), type));
        quarantineUserMapResponse.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserPositiveCovidDetails(userId), type));
        quarantineUserMapResponse.getUserStatusDetails().addAll(modelMapper.map(quarantineUserRepository.getUserDeceasedDetails(userId), type));

        return quarantineUserMapResponse;
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

    private Page<QuarantineUser> getPageableQuarantineUsers(Pageable pageable, String search, QuarantineUserStatus status) {

        Page<QuarantineUser> users;

        if (!StringUtils.isEmpty(search) && status != null) {

            String pattern = "%" + search + "%";
            users = quarantineUserRepository.findQuarantineUsersByPatternAndStatus(pattern, pageable, status);

        } else if (!StringUtils.isEmpty(search)) {
            String pattern = "%" + search + "%";
            users = quarantineUserRepository.findQuarantineUsersByPattern(pattern, pageable);

        } else if (status != null) {

            users = quarantineUserRepository.findQuarantineUsersByStatus(status, pageable);

        } else {
            users = quarantineUserRepository.findAll(pageable);
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

    private short getRemainingDays(LocalDate startDate, LocalDate endDate) {

        short remainingDays = 0;

        if(startDate == null || endDate != null) {
            return remainingDays;
        }

        LocalDate currentDate = LocalDate.now(zoneId);
        short diff = (short) DAYS.between(startDate, currentDate);

        if(diff == 0) {
            return quarantinePeriod;
        }
        else if(diff > quarantinePeriod) {
            return remainingDays;
        } else {
            return (short)(quarantinePeriod - (diff - 1));
        }
    }

    private void setUserStatus(QuarantineUser quarantineUser, List<QuarantineUserStatusDetail> userStatusDetails) throws QmsException {

        QuarantineUserStatus status = QuarantineUserStatus.COMPLETE;

        if(quarantineUser.getId() != null) {
            QuarantineUser persistUser = quarantineUserRepository.findQuarantineUserById(quarantineUser.getId());
            Long userId = persistUser.getId();

            quarantineUser.setAddedBy(persistUser.getAddedBy());

            quarantineUser.setHqDetails(quarantineUserRepository.getUserHomeQuarantineDetails(userId));
            quarantineUser.setRqDetails(quarantineUserRepository.getUserRemoteQuarantineDetails(userId));
            quarantineUser.setScDetails(quarantineUserRepository.getUserSuspectCovidDetails(userId));
            quarantineUser.setPcDetails(quarantineUserRepository.getUserPositiveCovidDetails(userId));
            quarantineUser.setDeceasedDetails(quarantineUserRepository.getUserDeceasedDetails(userId));
        }

        boolean isDeceased = false;
        int numOfStatusWithoutEndDate = 0;
        int numOfDeceaseUserStatusRecords = 0;

        for(QuarantineUserStatusDetail quarantineUserStatusDetail : userStatusDetails) {

            if(quarantineUserStatusDetail.getType() != QuarantineUserStatus.DECEASED && !isDeceased && quarantineUserStatusDetail.getEndDateClass() == null && !quarantineUserStatusDetail.isDelete()) {
                numOfStatusWithoutEndDate += 1;
                status = quarantineUserStatusDetail.getType();
            }

            if(quarantineUserStatusDetail.getType() == QuarantineUserStatus.DECEASED && !quarantineUserStatusDetail.isDelete()) {
                isDeceased = true;
                status = QuarantineUserStatus.DECEASED;
                numOfDeceaseUserStatusRecords += 1;
            }

            if(numOfStatusWithoutEndDate > 1) {
                logger.warn("More than one records without end date found in user status details: {}", userStatusDetails);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Found more than one, not complete user status");
            }

            if(numOfDeceaseUserStatusRecords > 1) {
                logger.warn("More than one records entered for user deceased status: {}", userStatusDetails);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Found more than one, entries for user deceased status");
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

    private void addHouseQuarantineStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        validateDateValues(quarantineUserStatusDetail);
        short remainingDays = getRemainingDays(quarantineUserStatusDetail.getStartDateClass(), quarantineUserStatusDetail.getEndDateClass());

        if(quarantineUserStatusDetail.getId() != null) {
            boolean isExists = false;
            for(HomeQuarantineDetail homeQuarantineDetail : user.getHqDetails()) {

                if(Objects.equals(homeQuarantineDetail.getId(), quarantineUserStatusDetail.getId())) {
                    isExists = true;
                    homeQuarantineDetail.setStartDate(quarantineUserStatusDetail.getStartDateClass());
                    homeQuarantineDetail.setEndDate(quarantineUserStatusDetail.getEndDateClass());
                    homeQuarantineDetail.setDescription(quarantineUserStatusDetail.getDescription());
                    homeQuarantineDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
                    homeQuarantineDetail.setRemainingDays(remainingDays);
                    homeQuarantineDetail.setUser(user);
                    homeQuarantineDetail.setDeleted(quarantineUserStatusDetail.isDelete());
                    break;
                }
            }

            if(!isExists) {
                logger.warn("No hq user status detail found for id: {}", quarantineUserStatusDetail.getId());
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "House quarantine user status not found");
            }
        } else {
            HomeQuarantineDetail homeQuarantineDetail = modelMapper.map(quarantineUserStatusDetail, HomeQuarantineDetail.class);
            homeQuarantineDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
            homeQuarantineDetail.setUser(user);
            homeQuarantineDetail.setRemainingDays(remainingDays);
            user.getHqDetails().add(homeQuarantineDetail);
        }
    }

    private void addRemoteQuarantineStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        if(quarantineUserStatusDetail.getqCenterId() == null) {
            logger.warn("Quarantine center not select in remote quarantine user status: {}", quarantineUserStatusDetail);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to select Quarantine Center");
        }

        QuarantineCenter quarantineCenter = quarantineCenterService.getQuarantineCenterForGivenId(quarantineUserStatusDetail.getqCenterId());

        validateDateValues(quarantineUserStatusDetail);

        short remainingDays = getRemainingDays(quarantineUserStatusDetail.getStartDateClass(), quarantineUserStatusDetail.getEndDateClass());

        if(quarantineUserStatusDetail.getId() != null) {
            boolean isExists = false;
            for(RemoteQuarantineDetail remoteQuarantineDetail : user.getRqDetails()) {

                if(Objects.equals(remoteQuarantineDetail.getId(), quarantineUserStatusDetail.getId())) {
                    isExists = true;
                    remoteQuarantineDetail.setStartDate(quarantineUserStatusDetail.getStartDateClass());
                    remoteQuarantineDetail.setEndDate(quarantineUserStatusDetail.getEndDateClass());
                    remoteQuarantineDetail.setDescription(quarantineUserStatusDetail.getDescription());
                    remoteQuarantineDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
                    remoteQuarantineDetail.setRemainingDays(remainingDays);
                    remoteQuarantineDetail.setUser(user);
                    remoteQuarantineDetail.setQuarantineCenter(quarantineCenter);
                    remoteQuarantineDetail.setDeleted(quarantineUserStatusDetail.isDelete());
                    break;
                }
            }

            if(!isExists) {
                logger.warn("No rq user status detail found for id: {}", quarantineUserStatusDetail.getId());
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Remote quarantine user status not found");
            }
        } else {
            RemoteQuarantineDetail remoteQuarantineDetail = modelMapper.map(quarantineUserStatusDetail, RemoteQuarantineDetail.class);
            remoteQuarantineDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
            remoteQuarantineDetail.setUser(user);
            remoteQuarantineDetail.setRemainingDays(remainingDays);
            remoteQuarantineDetail.setQuarantineCenter(quarantineCenter);
            user.getRqDetails().add(remoteQuarantineDetail);
        }
    }

    private void addSuspectCovidStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        if(quarantineUserStatusDetail.getHospitalId() == null) {
            logger.warn("Hospital not select in suspect covid user status: {}", quarantineUserStatusDetail);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to select Hospital");
        }

        Hospital hospital = hospitalService.findHospitalForGivenId(quarantineUserStatusDetail.getHospitalId());

        validateDateValues(quarantineUserStatusDetail);

        if(quarantineUserStatusDetail.getId() != null) {
            boolean isExists = false;
            for(SuspectCovidDetail suspectCovidDetail : user.getScDetails()) {

                if(Objects.equals(suspectCovidDetail.getId(), quarantineUserStatusDetail.getId())) {
                    isExists = true;
                    suspectCovidDetail.setAdmitDate(quarantineUserStatusDetail.getStartDateClass());
                    suspectCovidDetail.setDischargeDate(quarantineUserStatusDetail.getEndDateClass());
                    suspectCovidDetail.setDescription(quarantineUserStatusDetail.getDescription());
                    suspectCovidDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
                    suspectCovidDetail.setUser(user);
                    suspectCovidDetail.setHospital(hospital);
                    suspectCovidDetail.setDeleted(quarantineUserStatusDetail.isDelete());
                    break;
                }
            }

            if(!isExists) {
                logger.warn("No sc user status detail found for id: {}", quarantineUserStatusDetail.getId());
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Suspect covid user status not found");
            }
        } else {
            SuspectCovidDetail suspectCovidDetail = modelMapper.map(quarantineUserStatusDetail, SuspectCovidDetail.class);
            suspectCovidDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
            suspectCovidDetail.setUser(user);
            suspectCovidDetail.setHospital(hospital);

            user.getScDetails().add(suspectCovidDetail);
        }
    }

    private void addPositiveCovidStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        if(quarantineUserStatusDetail.getHospitalId() == null) {
            logger.warn("Hospital not select in positive covid user status: {}", quarantineUserStatusDetail);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to select Hospital");
        }

        if(StringUtils.isEmpty(quarantineUserStatusDetail.getCaseNum())) {
            logger.warn("Case number not eneterd in positive covid user status: {}", quarantineUserStatusDetail);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to add case number");
        }

        positiveCovidDetailService.checkCaseNumAlreadyExists(quarantineUserStatusDetail.getId(), quarantineUserStatusDetail.getCaseNum());
        PositiveCovidDetail parentCase = positiveCovidDetailService.findPositiveCovidDetailByCaseNum(quarantineUserStatusDetail.getParentCaseNum());

        Hospital hospital = hospitalService.findHospitalForGivenId(quarantineUserStatusDetail.getHospitalId());

        validateDateValues(quarantineUserStatusDetail);

        if(quarantineUserStatusDetail.getId() != null) {
            boolean isExists = false;
            for(PositiveCovidDetail positiveCovidDetail : user.getPcDetails()) {

                if(Objects.equals(positiveCovidDetail.getId(), quarantineUserStatusDetail.getId())) {
                    isExists = true;
                    positiveCovidDetail.setIdentifiedDate(quarantineUserStatusDetail.getStartDateClass());
                    positiveCovidDetail.setDischargeDate(quarantineUserStatusDetail.getEndDateClass());
                    positiveCovidDetail.setDescription(quarantineUserStatusDetail.getDescription());
                    positiveCovidDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
                    positiveCovidDetail.setUser(user);
                    positiveCovidDetail.setHospital(hospital);
                    positiveCovidDetail.setParentCase(parentCase);
                    positiveCovidDetail.setDeleted(quarantineUserStatusDetail.isDelete());
                    break;
                }
            }

            if(!isExists) {
                logger.warn("No pc user status detail found for id: {}", quarantineUserStatusDetail.getId());
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Positive covid user status not found");
            }
        } else {
            PositiveCovidDetail positiveCovidDetail = modelMapper.map(quarantineUserStatusDetail, PositiveCovidDetail.class);
            positiveCovidDetail.setActive(quarantineUserStatusDetail.getEndDateClass() == null);
            positiveCovidDetail.setUser(user);
            positiveCovidDetail.setHospital(hospital);
            positiveCovidDetail.setParentCase(parentCase);

            user.getPcDetails().add(positiveCovidDetail);
        }
    }

    private void addDeceasedStatusDetail(QuarantineUser user, QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        logger.info("Decease status record: {}", quarantineUserStatusDetail);

        if(!quarantineUserStatusDetail.isDelete()) {
            if(quarantineUserStatusDetail.getEndDateClass() == null) {
                logger.warn("Deceased date not entered in user status: {}", quarantineUserStatusDetail);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to enter deceased date for deceased user status");
            }
            if(quarantineUserStatusDetail.getEndDateClass().isAfter(LocalDate.now(zoneId))) {
                logger.warn("Future date select for Deceased date in user status: {}", quarantineUserStatusDetail);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Future date select for deceased date");
            }
        }

        boolean isActiveStatusExists = false;
        Long activeStatusId = null;

        for(DeceasedDetail deceasedDetail : user.getDeceasedDetails()) {
            if(!deceasedDetail.isDeleted()) {
                isActiveStatusExists = true;
                activeStatusId = deceasedDetail.getId();
                break;
            }
        }

        if(isActiveStatusExists && (quarantineUserStatusDetail.getId() == null || !Objects.equals(quarantineUserStatusDetail.getId(), activeStatusId))) {
            logger.warn("Deceased user status: {} already exists for user: {}, request: {}", activeStatusId, user.getId(), quarantineUserStatusDetail);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Deceased user status exists for given user");
        }

        if(quarantineUserStatusDetail.getId() == null) {

            DeceasedDetail deceasedDetail = modelMapper.map(quarantineUserStatusDetail, DeceasedDetail.class);
            deceasedDetail.setUser(user);
            user.getDeceasedDetails().add(deceasedDetail);
        } else {

            for(DeceasedDetail deceasedDetail : user.getDeceasedDetails()) {
                if(Objects.equals(deceasedDetail.getId(), quarantineUserStatusDetail.getId())) {
                    deceasedDetail.setDeceasedDate(quarantineUserStatusDetail.getEndDateClass());
                    deceasedDetail.setUser(user);
                    deceasedDetail.setDeleted(quarantineUserStatusDetail.isDelete());
                    break;
                }
            }

        }
    }

    private void validateDateValues(QuarantineUserStatusDetail quarantineUserStatusDetail) throws QmsException {

        if(!quarantineUserStatusDetail.isDelete()) {
            if (quarantineUserStatusDetail.getStartDateClass() == null) {
                logger.warn("Start date not entered in user status: {}", quarantineUserStatusDetail);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to select start date in user status");
            }

            LocalDate currentDate = LocalDate.now(zoneId);

            if (quarantineUserStatusDetail.getStartDateClass().isAfter(currentDate)) {
                logger.warn("Future date select for start date in user status: {}", quarantineUserStatusDetail);
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Future date select in user status");
            }

            if (quarantineUserStatusDetail.getEndDateClass() != null) {
                if (quarantineUserStatusDetail.getEndDateClass().isAfter(currentDate)) {
                    logger.warn("Future date select for end date in user status: {}", quarantineUserStatusDetail);
                    throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Future date select in user status");
                }
                if (quarantineUserStatusDetail.getEndDateClass().isBefore(quarantineUserStatusDetail.getStartDateClass())) {
                    logger.warn("Invalid start and end date select in user status: {}", quarantineUserStatusDetail);
                    throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Invalid start and end date select in user status");
                }
            }
        }
    }

    private void initUsers() {

        try(FileInputStream excelFile = new FileInputStream(new File("src/main/resources/Total Patients First 350 V3.xlsx"))) {

            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            int totalComplete = 0;
            int notComplete = 0;

            for(Row row : sheet) {

                //logger.info("row: {}", row.getRowNum());

                if (row.getRowNum() == 0 || row.getRowNum() == 1 || row.getRowNum() == 343 || row.getRowNum() >= 353) {
                    continue;
                }

                int sn = (int) row.getCell(0).getNumericCellValue();

                String province = row.getCell(1).getStringCellValue().trim();
                String district = row.getCell(2).getStringCellValue().trim();
                String ds = row.getCell(3).getStringCellValue().trim();
                String gnd = row.getCell(4).getStringCellValue().trim();

                Long gndId = gramaNiladariDivisionService.findUserGndId(province, district, ds, gnd);
                if(gndId == null) {
                    logger.warn("No gnd found for record: {}", sn);
                    notComplete += 1;
                    continue;
                }

                QuarantineUserRequestDto quarantineUserRequestDto = new QuarantineUserRequestDto();

                String state = checkStringEmptyOrNot(row.getCell(5).getStringCellValue());
                LocationState locationState = LocationState.OK;
                if(state != null) {
                    locationState = LocationState.valueOf(state);
                }

                String name = row.getCell(7).getStringCellValue().trim();
                quarantineUserRequestDto.setName(name);

                String line = checkStringEmptyOrNot(row.getCell(11).getStringCellValue());

                Address address = addressRepository.findExistingAddress(line, gndId, locationState);
                AddressDto addressDto = new AddressDto();
                if(address != null) {
                    addressDto = modelMapper.map(address, AddressDto.class);
                } else {
                    addressDto.setGndId(gndId);
                    addressDto.setLine(line);
                    addressDto.setLocationState(locationState);

                    String[] coordinates = gramaNiladariDivisionService.getGndCenterCoordinates(gndId);
                    addressDto.setLon(coordinates[0]);
                    addressDto.setLat(coordinates[1]);
                }
                quarantineUserRequestDto.setAddress(addressDto);

                String caseNum = String.valueOf((int) row.getCell(20).getNumericCellValue());

                String externalKey = 'P' + caseNum;
                quarantineUserRequestDto.setExternalKey(externalKey);
                quarantineUserRequestDto.setExternallyAdded(true);

                QuarantineUser existingUser = quarantineUserRepository.findQuarantineUserByExternalKey(externalKey);

                String admitDateString = checkStringEmptyOrNot(row.getCell(21).getStringCellValue());
                String hospital = checkStringEmptyOrNot(row.getCell(22).getStringCellValue());
                String dischargeDateString = checkStringEmptyOrNot(row.getCell(23).getStringCellValue());

                Long hospitalId = hospitalService.getHospitalIdFromHospitalMappingName(hospital);
                if(hospitalId == null) {
                    logger.warn("No hospital id found, for record: {}", sn);
                    notComplete += 1;
                    continue;
                }

                LocalDate admittedDate = getDateFromString(admitDateString);
                LocalDate dischargedDate = dischargeDateString != null ? getDateFromString(dischargeDateString) : null;

                List<QuarantineUserStatusDetail> userStatusDetails = new ArrayList<>();

                QuarantineUserStatusDetail pcDetail = new QuarantineUserStatusDetail();
                pcDetail.setType(QuarantineUserStatus.POSITIVE_COVID);
                pcDetail.setStartDate(admittedDate);
                pcDetail.setEndDate(dischargedDate);
                pcDetail.setCaseNum(caseNum);
                pcDetail.setHospitalId(hospitalId);

                QuarantineUserStatusDetail deceasedDetail = new QuarantineUserStatusDetail();
                deceasedDetail.setType(QuarantineUserStatus.DECEASED);
                String deceasedDateString = checkStringEmptyOrNot(row.getCell(24).getStringCellValue());

                if(existingUser != null) {
                    quarantineUserRequestDto.setId(existingUser.getId());

                    quarantineUserRepository.getUserPositiveCovidDetails(existingUser.getId()).stream()
                            .filter(positiveCovidDetail -> positiveCovidDetail.getCaseNum().equals(caseNum))
                            .forEach(positiveCovidDetail -> pcDetail.setId(positiveCovidDetail.getId()));

                    quarantineUserRepository.getUserDeceasedDetails(existingUser.getId()).forEach(deceasedDetail1 -> {
                        if(deceasedDateString == null) {
                            deceasedDetail.setId(deceasedDetail.getId());
                            deceasedDetail.setEndDate(deceasedDetail1.getDeceasedDate());
                            deceasedDetail.setDelete(true);
                            userStatusDetails.add(deceasedDetail);
                        } else {
                            deceasedDetail.setId(deceasedDetail1.getId());
                        }
                    });
                }

                if(deceasedDateString != null) {
                    LocalDate deceasedDate = getDateFromString(deceasedDateString);
                    deceasedDetail.setEndDate(deceasedDate);
                    userStatusDetails.add(deceasedDetail);
                }

                userStatusDetails.add(pcDetail);

                quarantineUserRequestDto.setUserStatusDetails(userStatusDetails);

                //logger.info("test");

                try {
                    createUser(quarantineUserRequestDto, 1L);
                    totalComplete += 1;

                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (QmsException e) {
                    logger.warn("User not saved, for record: {}", sn);
                    notComplete += 1;
                    logger.error("error", e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            logger.info("total complte: {}, not: {}", totalComplete, notComplete);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String checkStringEmptyOrNot(String value) {

        if(StringUtils.isEmpty(value)) {
            return null;
        } else {
            return value.trim();
        }
    }

    private LocalDate getDateFromString(String date) {

        if(date == null) {
            date = "27.01.2020";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(date, formatter);
    }
}
