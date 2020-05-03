package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.DistrictResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.District;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/27/2020
 * @Package lk.uom.fit.qms.service
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public interface DistrictService {

    List<DistrictResDto> getAllDistrictsInProvince(Long provinceId, String search) throws QmsException;

    void checkDistrictExists(Long id) throws QmsException;

    District findDistrictById(Long id) throws QmsException;
}
