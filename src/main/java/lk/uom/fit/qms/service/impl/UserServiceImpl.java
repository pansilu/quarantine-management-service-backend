package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.config.security.CustomJwtTokenCreator;
import lk.uom.fit.qms.dto.MobileNumExistsResDto;
import lk.uom.fit.qms.dto.UserLoginRequestDto;
import lk.uom.fit.qms.dto.UserLoginResponseDto;
import lk.uom.fit.qms.dto.UserRoleDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.User;
import lk.uom.fit.qms.model.UserRole;
import lk.uom.fit.qms.repository.UserRepository;
import lk.uom.fit.qms.service.UserService;
import lk.uom.fit.qms.util.enums.RoleType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isDebugEnable = logger.isDebugEnabled();

    @Value("${security.jwt.expiretime.days}")
    private Integer jwtExpireTimeInDays;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomJwtTokenCreator customJwtTokenCreator;

    @Override
    public UserLoginResponseDto authenticateUser(UserLoginRequestDto userLoginRequestDto) throws QmsException {

        if (isDebugEnable) {
            logger.debug("Login request user_name : {}", userLoginRequestDto.getUsername());
        }

        User user = userRepository.findUserByUsername(userLoginRequestDto.getUsername());

        if (user == null) {
            logger.warn("No user found by given username : {}", userLoginRequestDto.getUsername());
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "No user found by given username");
        }

        boolean isAdminOrRoot = false;

        List<UserRole> userRoles = user.getUserRoles();
        for(UserRole userRole : userRoles) {
            String roleName = userRole.getRole().getName().name();
            if(Objects.equals(roleName, RoleType.ROOT.name()) || Objects.equals(roleName, RoleType.ADMIN.name())) {
                isAdminOrRoot = true;
                break;
            }
        }

        if(!isAdminOrRoot) {
            logger.info("No ADMIN or ROOT user role for given username : {}", userLoginRequestDto.getUsername());
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "No pAdmin or ROOT acc. exists for given username");
        }

        String persistPassword = user.getPassword();
        if (persistPassword == null) {
            logger.info("Null persistPassword for user by given username : {}", userLoginRequestDto.getUsername());
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "No persistPassword set");
        }

        System.out.println(passwordEncoder.encode("test"));

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), persistPassword)) {
            logger.info("Password not matched for user name : {}", userLoginRequestDto.getUsername());
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Password not matched");
        }

        String token = customJwtTokenCreator.generateJwtToken(user, jwtExpireTimeInDays);
        logger.info("Web User authentication enable response with token : {}", token);

        if (isDebugEnable) {
            logger.debug("Login response token : {}, for user_name : {}", token, userLoginRequestDto.getUsername());
        }
        return new UserLoginResponseDto(token);
    }

    @Override
    public User findOne(Long userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public void checkUserWithMobileNumExists(String mobileNum, Long userId) throws QmsException {

        if(mobileNum == null) {
            return;
        }

        boolean isUserExists;

        if(userId == null) {
            isUserExists = userRepository.isUserExistsWithMobileNum(mobileNum);
        }
        else {
            isUserExists = userRepository.isUserExistsWithMobileNum(mobileNum, userId);
        }

        if (isUserExists) {
            logger.warn("User with same mobile num {} already exists", mobileNum);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "User with same mobile num already exists");
        }
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public User saveGuardian(User user) {
        return userRepository.save(user);
    }

    @Override
    public void checkUserExists(Long id) throws QmsException {

        if(id != null && userRepository.findUserById(id) == null) {
            logger.warn("User didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "User Not Found!!!");
        }
    }

    @Override
    public boolean checkUserIsRoot(List<UserRoleDto> userRoles) {

        boolean isRoot = false;

        if(userRoles != null) {
            for(UserRoleDto userRoleDto : userRoles) {
                if(Objects.equals(userRoleDto.getRole(), RoleType.ROOT.name())){
                    isRoot = true;
                    break;
                }
            }
        }
        return isRoot;
    }

    @Override
    public MobileNumExistsResDto getMobileNumExistsResponse(String mobileNum, Long userId) {

        MobileNumExistsResDto mobileNumExistsResDto = new MobileNumExistsResDto();
        if(mobileNum == null) {
            return mobileNumExistsResDto;
        }

        boolean isUserExists;

        if(userId == null) {
            isUserExists = userRepository.isUserExistsWithMobileNum(mobileNum);
        }
        else {
            isUserExists = userRepository.isUserExistsWithMobileNum(mobileNum, userId);
        }

        mobileNumExistsResDto.setExist(isUserExists);

        return mobileNumExistsResDto;
    }
}
