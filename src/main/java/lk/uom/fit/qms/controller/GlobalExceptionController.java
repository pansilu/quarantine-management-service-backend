package lk.uom.fit.qms.controller;

import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.ErrorResponse;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.ServiceUnavailableException;

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

@ControllerAdvice
public class GlobalExceptionController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = QmsException.class)
    public ResponseEntity<ErrorResponse> handleQmsException(QmsException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getExceptionCode(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getHttpStatusCode());
    }

    @ExceptionHandler(value = ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(ServiceUnavailableException e) {
        ErrorResponse errorResponse = new ErrorResponse(QmsExceptionCode.SVR00X, "Service is not available");
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("Unexpected exception occurs : ", e);
        ErrorResponse errorResponse = new ErrorResponse(QmsExceptionCode.SVR00X, "Unexpected operation failure, please try again");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
