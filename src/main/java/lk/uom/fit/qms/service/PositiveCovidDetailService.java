package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.PositiveCovidCaseDetail;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.PositiveCovidDetail;

import java.time.LocalDate;
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

public interface PositiveCovidDetailService {

    PositiveCovidDetail findPositiveCovidDetailByCaseNum(String caseNum) throws QmsException;

    void checkCaseNumAlreadyExists(Long id, String caseNum) throws QmsException;

    List<PositiveCovidCaseDetail> getCaseDetails(String search);

    long getNewCasesPerDate(LocalDate date);

    long getNewCasesPerDateAndGnd(LocalDate date, Long gndId);

    long getNewCasesPerDateAndDivision(LocalDate date, Long divisionId);

    long getNewCasesPerDateAndDistrict(LocalDate date, Long districtId);

    long getNewCasesPerDateAndProvince(LocalDate date, Long provinceId);

    long getNewRecoveredCasesPerDate(LocalDate date);

    long getNewRecoveredCasesPerDateAndGnd(LocalDate date, Long gndId);

    long getNewRecoveredCasesPerDateAndDivision(LocalDate date, Long divisionId);

    long getNewRecoveredCasesPerDateAndDistrict(LocalDate date, Long districtId);

    long getNewRecoveredCasesPerDateAndProvince(LocalDate date, Long provinceId);
}
