package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.config.security.CustomJwtTokenCreator;
import lk.uom.fit.qms.dto.UserLoginRequestDto;
import lk.uom.fit.qms.dto.UserLoginResponseDto;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.QuarantineUser;
import lk.uom.fit.qms.model.User;
import lk.uom.fit.qms.repository.QuarantineUserRepository;
import lk.uom.fit.qms.service.ReportUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private boolean isDebugEnable = logger.isDebugEnabled();

    @Value("${security.jwt.expiretime.days}")
    private Integer jwtExpireTimeInDays;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QuarantineUserRepository quarantineUserRepository;

    @Autowired
    private CustomJwtTokenCreator customJwtTokenCreator;

    @Override
    public UserLoginResponseDto authenticateUser(UserLoginRequestDto userLoginRequestDto) throws BadRequestException {

        if (isDebugEnable) {
            logger.debug("Login request user_name : {}", userLoginRequestDto.getUsername());
        }

        QuarantineUser user = quarantineUserRepository.findQuarantineUserByUsername(userLoginRequestDto.getUsername());

        if (user == null) {
            logger.info("No user found by given username : {}", userLoginRequestDto.getUsername());
            throw new BadRequestException(QmsExceptionCode.USR00X, "No user found by given username");
        }

        String persistPassword = user.getPassword();
        if (persistPassword == null) {
            logger.info("Null persistPassword for user by given username : {}", userLoginRequestDto.getUsername());
            throw new BadRequestException(QmsExceptionCode.USR00X, "No persistPassword set");
        }

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), persistPassword)) {
            logger.info("Password not matched for user name : {}", userLoginRequestDto.getUsername());
            throw new BadRequestException(QmsExceptionCode.USR00X, "Password not matched");
        }

        String token = customJwtTokenCreator.generateJwtToken(user, jwtExpireTimeInDays);
        logger.info("Web User authentication enable response with token : {}", token);

        if (isDebugEnable) {
            logger.debug("Login response token : {}, for user_name : {}", token, userLoginRequestDto.getUsername());
        }
        return new UserLoginResponseDto(token);
    }
}
