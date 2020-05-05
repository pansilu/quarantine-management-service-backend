package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.service.
 */
public interface QuarantineUserService {

    void createUser(QuarantineUserRequestDto quarantineUserRequestDto, Long addedUserId) throws QmsException;

    UserLoginResponseDto authenticateUser(String secret) throws QmsException;

    void updatePointValue(Map<String, Boolean> pointValueMap, Long qUserId) throws QmsException;

    QuarantineUserMultiPageResDto getQuarantineUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles, String search, QuarantineUserStatus status) throws QmsException;

    QuarantineUserPointValueDto getUserPointValues(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException;

    QuarantineUserResDto getUser(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException;

    void calUserRemainingDays();

    boolean isAppEnable(Long userId);

    QuarantineUserMapResponse getQuarantineUserMapResponse(Long userId) throws QmsException;
}
