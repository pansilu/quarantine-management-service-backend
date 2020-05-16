package lk.uom.fit.qms.exception.pojo;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.exception.pojo.
 */
public class ErrorResponse {

    private QmsExceptionCode errorCode;
    private String errorDesc;

    public ErrorResponse(QmsExceptionCode errorCode, String errorDesc) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }

    public ErrorResponse() {
    }

    public QmsExceptionCode getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(QmsExceptionCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }
    public void setErrorDesc( String errorDesc ) {
        this.errorDesc = errorDesc;
    }
}
