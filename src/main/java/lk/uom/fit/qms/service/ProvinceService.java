package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.ProvinceResDto;
import lk.uom.fit.qms.exception.QmsException;

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

public interface ProvinceService {

    List<ProvinceResDto> findAllProvinces(String search);

    void checkProvinceExits(Long id) throws QmsException;
}
