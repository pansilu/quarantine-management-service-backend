package lk.uom.fit.qms.service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/2/2020
 * @Package lk.uom.fit.qms.service
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public interface DeceasedDetailService {

    long getNewDeceasedCasesPerDate(LocalDate date);

    long getNewDeceasedCasesPerDateAndGnd(LocalDate date, Long gndId);

    long getNewDeceasedCasesPerDateAndDivision(LocalDate date, Long divisionId);

    long getNewDeceasedCasesPerDateAndDistrict(LocalDate date, Long districtId);

    long getNewDeceasedCasesPerDateAndProvince(LocalDate date, Long provinceId);

    List<Long[]> getDeceasedCaseCountAgainstAgeGroup();

    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndGnd(Long gndId);

    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndDivision(Long divisionId);

    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndDistrict(Long districtId);

    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndProvince(Long provinceId);

    List<Object []> getNewDeceasedCasesPerDateForGivenDistricts(List<Long> districtIds, LocalDate date);
}
