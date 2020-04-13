package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.ReportUser;
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

    void createUser(ReportUserRequestDto reportUserRequestDto, Long addedUserId) throws QmsException;

    List<DivisionDto> getLocationDetails(Long userId, List<UserRoleDto> userRoles);

    List<ReportUserResponseDto> getReportUsers(AdminFilterReqDto adminFilterReqDto, String search);

    ReportUserMultiPageResDto getUsers(Pageable pageable, Long adminId, List<UserRoleDto> userRoles, String search);

    ReportUserResponseDto getUser(Long userId, Long adminId, List<UserRoleDto> userRoles) throws QmsException;

    ReportUser findReportUserById(Long userId) throws QmsException;

    List<Long> getAdminUserStations(Long adminId) throws QmsException;
}
