package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.config.security.CustomJwtTokenCreator;
import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.CountryService;
import lk.uom.fit.qms.service.QuarantineUserService;
import lk.uom.fit.qms.service.UserService;
import lk.uom.fit.qms.util.enums.RoleType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private GramaSewaDevisionRepository gramaSewaDevisionRepository;

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
    public void createUser(QuarantineUserRequestDto quarantineUserRequestDto, Long addedUserId) throws BadRequestException {

        logger.debug("addedUserId: {}", addedUserId);

        //Todo: validate if quarantineUser id exist
        //Tod: need to implemet rUser ---> quser
        // check mobile num exists
        userService.checkUserWithMobileNumExists(quarantineUserRequestDto.getMobile(), quarantineUserRequestDto.getId());

        QuarantineUser quarantineUser = modelMapper.map(quarantineUserRequestDto, QuarantineUser.class);

        checkSecretExistForAnotherUser(quarantineUserRequestDto, quarantineUser);

        quarantineUser.setArrivedCountry(countryService.findOne(quarantineUserRequestDto.getCountryId()));

        List<QuarantineUserInspectDetails> quarantineUserInspectDetailList = new ArrayList<>();
        if(quarantineUserRequestDto.getInspectorIds() != null) {
            quarantineUserRequestDto.getInspectorIds().forEach(inspectorId -> {

                QuarantineUserInspectDetails quarantineUserInspectDetails = new QuarantineUserInspectDetails();
                quarantineUserInspectDetails.setReportUser(reportUserRepository.findReportUserById(inspectorId));
                quarantineUserInspectDetails.setQuarantineUser(quarantineUser);

                quarantineUserInspectDetailList.add(quarantineUserInspectDetails);
            });
        }

        if(quarantineUserRequestDto.getInformedDate() != null) {
            quarantineUser.setInformedAuthority(true);
        }

        quarantineUser.setQuarantineUserInspectDetails(quarantineUserInspectDetailList);

        if(quarantineUserRequestDto.getGuardianDetails() != null) {
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
        }

        if(quarantineUserRequestDto.getAdmitHosId() != null || quarantineUserRequestDto.getConfirmedHosId() != null) {
            quarantineUser.setPatient(true);
            PatientDetails patientDetails = new PatientDetails();

            quarantineUser.setPatientDetails(patientDetails);

            if(quarantineUserRequestDto.getAdmitHosId() != null){
                patientDetails.setAdmitHospital(hospitalRepository.findHospitalById(quarantineUserRequestDto.getAdmitHosId()));
            }

            if(quarantineUserRequestDto.getDischargedDate() != null) {
                quarantineUser.getPatientDetails().setDischarged(true);
            }

            if(quarantineUserRequestDto.getConfirmedHosId() != null) {
                quarantineUser.getPatientDetails().setConfirmedHospital(hospitalRepository.findHospitalById(quarantineUserRequestDto.getConfirmedHosId()));
                quarantineUser.getPatientDetails().setInfected(true);
            }
            quarantineUser.getPatientDetails().setPatient(quarantineUser);
        }

        UserRole userRole = new UserRole();
        userRole.setRole(roleRepository.findRoleByName(RoleType.Q_USER));
        userRole.setUser(quarantineUser);

        quarantineUser.getUserRoles().add(userRole);
        quarantineUser.setAddedBy(userService.findUserById(addedUserId));

        Address address = quarantineUser.getAddress();
        address.setGramaSewaDivision(gramaSewaDevisionRepository.findGramaSewaDivisionById(quarantineUserRequestDto.getGramaSewaDivisionId()));

        quarantineUser.setAddress(addressRepository.save(address));
        quarantineUserRepository.save(quarantineUser);
    }

    @Override
    public UserLoginResponseDto authenticateUser(String secret) throws BadRequestException {

        if (isDebugEnable) {
            logger.debug("Login request for secret : {}", secret);
        }

        QuarantineUser user = quarantineUserRepository.findQuarantineUserBySecret(secret);

        if (user == null) {
            logger.warn("No user found by given secret : {}", secret);
            throw new BadRequestException(QmsExceptionCode.USR00X, "No user found by given secret");
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
    public void updatePointValue(Map<String, Boolean> pointValueMap, Long qUserId) throws BadRequestException {

        LocalDate localDate = LocalDate.now(zoneId);

        if(userDailyPointDetailsRepository.isUserUpdateForCurrentDate(qUserId, localDate)) {
            logger.warn("User: {}, already update point table", qUserId);
            throw new BadRequestException(QmsExceptionCode.USR00X, "Create entry found for today!!");
        }

        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(qUserId);
        List<Point> regularPoints = pointRepository.getRegularPointNames();
        short totalPoints = 0;

        LocalDate reportDate = user.getReportDate();
        Period period = Period.between(localDate, reportDate);
        short diff = (short) period.getDays();
        short remainingDays = (short)(quarantinePeriod - diff);

        List<UserDailyPointDetails> userDailyPointDetailsList = new ArrayList<>();

        for(Point point : regularPoints) {
            if(pointValueMap.containsKey(point.getCode())) {
                UserDailyPointDetails userDailyPointDetails = new UserDailyPointDetails();
                userDailyPointDetails.setPoint(point);
                userDailyPointDetails.setUser(user);
                userDailyPointDetails.setRecordDate(localDate);
                userDailyPointDetails.setValue(pointValueMap.get(point.getCode()));
                userDailyPointDetailsList.add(userDailyPointDetails);

                totalPoints = (short)(totalPoints + point.getValue());
            }
        }

        user.setTotalPoints(totalPoints);
        user.setLastValueUpdateDate(LocalDateTime.now(zoneId));
        user.setRemainingDays(remainingDays);
        quarantineUserRepository.save(user);

        userDailyPointDetailsRepository.saveAll(userDailyPointDetailsList);
    }

    @Override
    public QuarantineMultiUserPageResDto getQuarantineUsers(Pageable pageable) {

        Page<QuarantineUser> users = quarantineUserRepository.findAll(pageable);

        QuarantineMultiUserPageResDto quarantineMultiUserPageResDto = new QuarantineMultiUserPageResDto();
        List<QuarantineMultiUserResDto> userResDtoList = new ArrayList<>();

        LocalDateTime currentDateTime = LocalDateTime.now(zoneId);

        users.forEach(user -> {
            QuarantineMultiUserResDto userResDto = modelMapper.map(user, QuarantineMultiUserResDto.class);

            if(ChronoUnit.HOURS.between(currentDateTime, user.getLastValueUpdateDate()) > maxRemindPeriod) {
                userResDto.setNeedToRemind(true);
            }
            userResDtoList.add(userResDto);
        });

        quarantineMultiUserPageResDto.setUsers(userResDtoList);
        quarantineMultiUserPageResDto.setTotalPages(users.getTotalPages());

        return quarantineMultiUserPageResDto;
    }

    @Override
    public QuarantineUserPointValueDto getUserPointValues(Long userId) {

        List<UserDailyPointDetails> pointDetailsList = userDailyPointDetailsRepository.findAllPointDetailsByUserId(userId);
        QuarantineUser user = quarantineUserRepository.findQuarantineUserById(userId);

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

        quarantineUserPointValueDto.setPointValues(pointValueMap);

        return quarantineUserPointValueDto;
    }

    void checkSecretExistForAnotherUser(QuarantineUserRequestDto quarantineUserRequestDto, QuarantineUser quarantineUser) throws BadRequestException {

        if(quarantineUserRequestDto.getMobile() != null && quarantineUserRequestDto.isAppEnable()) {
            if (quarantineUserRequestDto.getId() != null) {
                if(quarantineUserRepository.isSecretExistForAnotherUser(quarantineUserRequestDto.getMobile(), quarantineUserRequestDto.getId())) {
                    logger.warn("Mobile app secret : {} already exists for another user", quarantineUserRequestDto.getMobile());
                    throw new BadRequestException(QmsExceptionCode.USR00X, "Mobile app secret exists for another user!");
                }
            } else {
                if(quarantineUserRepository.isSecretExistForAnotherUser(quarantineUserRequestDto.getMobile())) {
                    logger.warn("Mobile app secret : {} already exists for another user", quarantineUserRequestDto.getMobile());
                    throw new BadRequestException(QmsExceptionCode.USR00X, "Mobile app secret exists for another user!");
                }
            }
            quarantineUser.setSecret(quarantineUserRequestDto.getMobile());
            quarantineUser.setUsername(quarantineUserRequestDto.getMobile());
        }
    }
}
