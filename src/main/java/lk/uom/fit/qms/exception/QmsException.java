package lk.uom.fit.qms.exception;

import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import org.springframework.http.HttpStatus;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/10/2020
 * @Package lk.uom.fit.qms.exception
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class QmsException extends Exception {

    private static final long serialVersionUID = -1871659178537590831L;

    private final QmsExceptionCode exceptionCode;
    private final HttpStatus httpStatusCode;

    public QmsException(QmsExceptionCode exceptionCode, HttpStatus httpStatusCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
        this.httpStatusCode = httpStatusCode;
    }

    public QmsExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
