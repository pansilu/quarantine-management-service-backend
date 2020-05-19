package lk.uom.fit.qms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.config.security.
 */
public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        Map<String, Object> data = new HashMap<>();
        data.put("errorCode", "Invalid Token");
        data.put("errorDesc", e.getMessage());

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(data));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();

        logger.warn("Invalid Token Detected. Request URI: {}, Request Method: {}, Error Description: {}," +
                        " ErrorStackTrace: {}", httpServletRequest.getRequestURI(), httpServletRequest.getMethod(),
                e.getMessage(), e);
    }
}
