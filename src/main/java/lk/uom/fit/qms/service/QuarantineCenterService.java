package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.QuarantineCenterDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.QuarantineCenter;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/26/2020
 * @Package lk.uom.fit.qms.service
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public interface QuarantineCenterService {

    QuarantineCenter getQuarantineCenterForGivenId(Long id) throws QmsException;

    void createOrEditQuarantineCenter(QuarantineCenterDto quarantineCenterDto) throws QmsException;

    List<QuarantineCenterDto> findQuarantineCenters(String search);

    QuarantineCenterDto getQuarantineCenterDetails(Long id) throws QmsException;
}
