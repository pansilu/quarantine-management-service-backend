package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.Regiment;
import org.springframework.data.domain.Pageable;

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
public interface ReportUserService {

    void createUser(PrivilegedUserRequestDto privilegedUserRequestDto, Long addedUserId) throws QmsException;

    PrivilegedUserListResponse getUsers(Pageable pageable);

    List<RegimentResponseDto> getRegiments();

    List<DivisionDto> getLocationDetails(Long userId, List<UserRoleDto> userRoles);

    List<ReportUserResponseDto> getReportUsers(AdminFilterReqDto adminFilterReqDto);

    PrivilegedUserResponseDto getUser(Long userId) throws QmsException;

    void deActivateUser(Long userId) throws QmsException;

    List<UnitDto> getUnits();
}
