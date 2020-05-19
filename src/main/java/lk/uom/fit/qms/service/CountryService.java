package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.Country;

import java.util.List;

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
public interface CountryService {

    Country findOne(Long id) throws QmsException;

    List<CountryDto> findAll(String search);
}
