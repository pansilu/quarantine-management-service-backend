package lk.uom.fit.qms.config;

import lk.uom.fit.qms.util.Constant;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

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
public class HttpRequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String logIdentifier = request.getHeader(Constant.LOG_IDENTIFIER_KEY);
        if (logIdentifier != null) {
            MDC.put(Constant.LOG_IDENTIFIER_KEY, logIdentifier);
        } else {
            MDC.put(Constant.LOG_IDENTIFIER_KEY, UUID.randomUUID().toString());
        }
        return true;
    }
}
