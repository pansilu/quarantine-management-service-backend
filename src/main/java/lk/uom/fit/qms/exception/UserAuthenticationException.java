package lk.uom.fit.qms.exception;

import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.exception.
 */
public class UserAuthenticationException extends Exception {

    private static final long serialVersionUID = 114089342369281949L;

    private final QmsExceptionCode errorCode;
    private final String errorMessage;

    public UserAuthenticationException(QmsExceptionCode errorCode, String errorMessage){

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public QmsExceptionCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
