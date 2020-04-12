package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.MobileNumExistsResDto;
import lk.uom.fit.qms.dto.UserLoginRequestDto;
import lk.uom.fit.qms.dto.UserLoginResponseDto;
import lk.uom.fit.qms.dto.UserRoleDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.User;

import java.util.List;

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

    UserLoginResponseDto authenticateUser(UserLoginRequestDto userLoginRequestDto) throws QmsException;

    User findOne(Long userId);

    void checkUserWithMobileNumExists(String mobileNum, Long userId) throws QmsException;

    User findUserById(Long id);

    User saveGuardian(User user);

    void checkUserExists(Long id) throws QmsException;

    boolean checkUserIsRoot(List<UserRoleDto> userRoles);

    MobileNumExistsResDto getMobileNumExistsResponse(String mobileNum, Long userId);

    String validateNic(String nic, Long userId) throws QmsException;

    String validatePassport(String passport, Long userId) throws QmsException;
}
