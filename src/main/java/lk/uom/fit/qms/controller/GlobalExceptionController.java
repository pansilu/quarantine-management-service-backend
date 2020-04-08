package lk.uom.fit.qms.controller;

import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.NotFoundException;
import lk.uom.fit.qms.exception.UserAuthenticationException;
import lk.uom.fit.qms.exception.pojo.ErrorResponse;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

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
    protected boolean isDebugEnable = logger.isDebugEnabled();

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getErrorCode());
        error.setErrorDesc(ex.getErrorMessage());

        logger.warn("Not Found Ex: ", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(UserAuthenticationException ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getErrorCode());
        error.setErrorDesc(ex.getErrorMessage());

        logger.warn("UserAuthentication Ex: ", ex);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex){
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(ex.getErrorCode());
        error.setErrorDesc(ex.getErrorMessage());

        logger.warn("Bad Request Ex: ", ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServiceException(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(QmsExceptionCode.EXR00X);
        errorResponse.setErrorDesc(ex.getMessage());

        logger.error("Server Error Ex: ", ex);
        return new ResponseEntity<>( errorResponse, HttpStatus.INTERNAL_SERVER_ERROR );
    }
}
