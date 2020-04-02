package lk.uom.fit.qms.config.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lk.uom.fit.qms.dto.InspectUserJwtDto;
import lk.uom.fit.qms.dto.UserRoleDto;
import lk.uom.fit.qms.model.User;
import lk.uom.fit.qms.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.config.
 */
@Component
public class CustomJwtTokenCreator {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isDebugEnable = logger.isDebugEnabled();

    @Value("${security.jwt.sign.key}")
    private String jwtSignKey;

    @Autowired
    private ZoneId zoneId;

    public String generateJwtToken(User user, int jwtExpireTimeInDays) {

        Date currentDateTime = getCurrentDateTime();
        long expireDateTime = currentDateTime.getTime() + (jwtExpireTimeInDays * 24 * 60 * 60 * 1000);

        if (isDebugEnable) {
            logger.debug("Token expire time : {}, create time : {}", expireDateTime, currentDateTime.getTime());
        }

        return Jwts.builder()
                .setIssuedAt(currentDateTime)
                .setExpiration(new Date(expireDateTime))
                .addClaims(getUserDetailMap(user))
                .setHeaderParam(Constant.JWT_HEADER_TYPE_KEY, Constant.JWT_HEADER_TYPE_VALUE)
                .signWith(
                        SignatureAlgorithm.HS256,
                        jwtSignKey.getBytes()
                )
                .compact();
    }

    public String generateMobileJwtToken(User user, int jwtExpireTimeInDays, List<InspectUserJwtDto> inspectUserDetails) {

        Date currentDateTime = getCurrentDateTime();
        long expireDateTime = currentDateTime.getTime() + (jwtExpireTimeInDays * 24 * 60 * 60 * 1000);

        if (isDebugEnable) {
            logger.debug("Token expire time : {}, create time : {}", expireDateTime, currentDateTime.getTime());
        }

        Map<String, Object> userDetailMap = getUserDetailMap(user);
        userDetailMap.put(Constant.USER_INSPECT_DETAIL_KEY, inspectUserDetails);

        return Jwts.builder()
                .setIssuedAt(currentDateTime)
                .setExpiration(new Date(expireDateTime))
                .addClaims(userDetailMap)
                .setHeaderParam(Constant.JWT_HEADER_TYPE_KEY, Constant.JWT_HEADER_TYPE_VALUE)
                .signWith(
                        SignatureAlgorithm.HS256,
                        jwtSignKey.getBytes()
                )
                .compact();
    }

    private Date getCurrentDateTime() {
        return Date.from(ZonedDateTime.now(zoneId).toInstant());
    }

    private Map<String, Object> getUserDetailMap(User user) {
        Map<String, Object> userDetailMap = new HashMap<>();
        userDetailMap.put(Constant.USER_ID_KEY, user.getId());
        userDetailMap.put(Constant.USER_DEFAULT_NAME_KEY, user.getName());
        userDetailMap.put(Constant.USER_PHONE_KEY, user.getPhone());
        userDetailMap.put(Constant.USER_MOBILE_KEY, user.getMobile());
        userDetailMap.put(Constant.USER_NAME_KEY, user.getUsername());
        userDetailMap.put(Constant.USER_ROLE_KEY, getUserRoles(user));
        return userDetailMap;
    }

    private List<UserRoleDto> getUserRoles(User user) {

        List<UserRoleDto> userRoles = new ArrayList<>();
        user.getUserRoles().forEach(userRole -> {
            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setRole(userRole.getRole().getName().name());
            userRoleDto.setCreateUser(userRole.isCreateUser());
            userRoles.add(userRoleDto);
        });

        return userRoles;
    }
}
