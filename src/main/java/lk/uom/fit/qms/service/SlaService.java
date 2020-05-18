package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.SlaUserDto;
import lk.uom.fit.qms.dto.SlaUserMultiPageResDto;
import lk.uom.fit.qms.dto.SlaUserResponseDto;
import lk.uom.fit.qms.exception.QmsException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SlaService {
    void createUser(SlaUserDto slaUserDto, Long userId, String userRole) throws QmsException;

    SlaUserMultiPageResDto getAllUsers(Pageable pageable, Long userId, boolean isRoot) throws QmsException;

    SlaUserMultiPageResDto getFilteredUsers(Pageable pageable,String filter,Long userId, boolean isRoot) throws QmsException;
}
