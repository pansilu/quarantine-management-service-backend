package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.DivisionResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.Division;

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

public interface DivisionService {

    List<DivisionResDto> getAllDivisionsInDistrict(Long districtId, String search) throws QmsException;

    void checkDivisionExists(Long id) throws QmsException;

    Division findDivisionByName(String name);

    Division createNewDivision(Division division);
}
