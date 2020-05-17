package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.SlaUserDto;
import lk.uom.fit.qms.dto.SlaUserResponseDto;
import lk.uom.fit.qms.exception.QmsException;

import java.util.List;

public interface SlaService {
    void createUser(SlaUserDto slaUserDto, Long userId, String userRole) throws QmsException;

    List<SlaUserResponseDto> getAllUsers(Long userId, boolean isRoot) throws QmsException;

    List<SlaUserResponseDto> getFilteredUsers(String filter,Long userId, boolean isRoot) throws QmsException;
}
