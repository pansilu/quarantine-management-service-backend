package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.exception.QmsException;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/12/2020
 * @Package lk.uom.fit.qms.service
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public interface AddressService {

    List<AddressDto> getAllAddress(Long gndId, String police, String line) throws QmsException;
}
