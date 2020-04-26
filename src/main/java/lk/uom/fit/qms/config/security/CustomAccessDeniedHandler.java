package lk.uom.fit.qms.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

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
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {

        Authentication auth = SecurityContextHolder.getContext()
                .getAuthentication();
        if (auth != null) {
            logger.warn("User: {}, attempted to access the protected URL: {}",auth.getName(), httpServletRequest.getRequestURI());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("errorCode", "Invalid Token");
        data.put("errorDesc", "You don't have access to view this resource. If you think this is an error, please contact higher administration");

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(data));
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }
}
