package lk.uom.fit.qms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.UserJwtTokenDto;
import lk.uom.fit.qms.dto.UserRoleDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static lk.uom.fit.qms.util.Constant.AUTHORIZATION_HEADER;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.controller.
 */
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    protected boolean isDebugEnable = logger.isDebugEnabled();

    @Autowired
    private ObjectMapper objectMapper;

    public String getTokenFromRequest(String jwtToken) {
        jwtToken = jwtToken.replace("Bearer ", "");
        return jwtToken;
    }

    public Long getUserIdFromRequest(HttpServletRequest request) throws QmsException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        UserJwtTokenDto userJwtTokenDto = getUserJwtTokenDtoFromToken(authorization);
        return userJwtTokenDto.getUserId();
    }

    public List<UserRoleDto> getUserRoles(HttpServletRequest request) throws QmsException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        UserJwtTokenDto userJwtTokenDto = getUserJwtTokenDtoFromToken(authorization);
        return userJwtTokenDto.getRoles();
    }

    public UserJwtTokenDto getUserJwtTokenDtoFromToken(String request) throws QmsException {
        String jwtToken = getTokenFromRequest(request);
        if (jwtToken == null) {
            logger.info("Null jwtToken for login");
            throw new QmsException(QmsExceptionCode.JWT00X, HttpStatus.UNAUTHORIZED, "Null jwtToken for login");
        }
        Jwt jwt = JwtHelper.decode(jwtToken);
        String jwtString = jwt.getClaims();
        if (jwtString == null) {
            logger.info("Null jwtString after decode for login");
            throw new QmsException(QmsExceptionCode.JWT00X, HttpStatus.UNAUTHORIZED, "Null jwtString after decode for login");
        }

        UserJwtTokenDto userJwtTokenDto;
        try {
            userJwtTokenDto = objectMapper.readValue(jwtString, UserJwtTokenDto.class);
        } catch (IOException e) {
            throw new QmsException(QmsExceptionCode.JWT00X, HttpStatus.UNAUTHORIZED, "Mapping exception, " + e.getMessage());
        }
        return userJwtTokenDto;
    }
}
