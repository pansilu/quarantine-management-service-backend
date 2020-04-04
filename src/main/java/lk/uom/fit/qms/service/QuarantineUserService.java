package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.NotFoundException;
import org.springframework.data.domain.Pageable;

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

    void createUser(QuarantineUserRequestDto quarantineUserRequestDto, Long addedUserId) throws BadRequestException, NotFoundException;

    UserLoginResponseDto authenticateUser(String secret) throws BadRequestException;

    void updatePointValue(Map<String, Boolean> pointValueMap, Long qUserId) throws BadRequestException;

    QuarantineUserMultiPageResDto getQuarantineUsers(Pageable pageable);

    QuarantineUserPointValueDto getUserPointValues(Long userId);

    QuarantineUserResDto getUser(Long userId) throws NotFoundException;
}
