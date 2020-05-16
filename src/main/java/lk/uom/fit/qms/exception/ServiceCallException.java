package lk.uom.fit.qms.exception;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.exception.
 */
public class ServiceCallException extends RuntimeException {

    private static final long serialVersionUID = 7096771126113591477L;

    public ServiceCallException(String message) {
        super(message);
    }
}
