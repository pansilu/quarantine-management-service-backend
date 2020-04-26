package lk.uom.fit.qms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.JwtTokenDto;
import lk.uom.fit.qms.dto.UserRoleDto;
import lk.uom.fit.qms.util.Constant;

import lk.uom.fit.qms.util.enums.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

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
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isDebugEnable = logger.isDebugEnabled();

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomUserAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {

        if (isDebugEnable) {
            logger.debug("Token_map : {}", map);
        }

        JwtTokenDto jwtTokenDto;
        try {
            jwtTokenDto = objectMapper.convertValue(map, JwtTokenDto.class);
        } catch (IllegalArgumentException e) {
            logger.error("Jwt token : {}, mapping exception : ", map, e);
            throw new InvalidTokenException("Invalid token format");
        }

        if (jwtTokenDto == null) {
            logger.info("Jwt token : {}, mapped to null", map);
            throw new InvalidTokenException("Invalid token, mapped to null");
        }

        if (jwtTokenDto.getUsername() == null) {
            logger.error("No user_name found in, jwt token : {}", map);
            throw new InvalidTokenException("No user_name found");
        }

        return new UsernamePasswordAuthenticationToken(jwtTokenDto.getUsername(), "N/A", getAuthoritiesCollection(jwtTokenDto, map));
    }

    private Collection<SimpleGrantedAuthority> getAuthoritiesCollection(JwtTokenDto jwtTokenDto, Map<String, ?> map) {

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        List<UserRoleDto> roles = jwtTokenDto.getRoles();

        roles.forEach(role -> {
            if(Objects.equals(role.getRole(), RoleType.Q_USER.name())) {
                logger.error("Mobile app not enable for user, jwt token : {}", map);
                throw new InvalidTokenException("Mobile App not enable");
            }
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
            if(role.isCreateUser()){
                authorities.add(new SimpleGrantedAuthority(Constant.USER_CREATE_PERMISSION));
            }
        });

        if (isDebugEnable) {
            logger.debug("Authorities list : {}, token_map : {}", authorities, map);
        }

        if (authorities.isEmpty()) {
            logger.warn("No roles found for userId : {}, in received token : {}", jwtTokenDto.getUserId(), map);
        }

        return authorities;
    }
}
