package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.UserLoginRequestDto;
import lk.uom.fit.qms.dto.UserLoginResponseDto;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.model.User;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.service.
 */
public interface UserService {

    UserLoginResponseDto authenticateUser(UserLoginRequestDto userLoginRequestDto) throws BadRequestException;

    User findOne(Long userId);

    void checkUserWithMobileNumExists(String mobileNum, Long userId) throws BadRequestException;
}
