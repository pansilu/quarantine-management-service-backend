package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.repository.DeceasedDetailRepository;
import lk.uom.fit.qms.service.DeceasedDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeceasedDetailServiceImpl implements DeceasedDetailService {

    private final DeceasedDetailRepository deceasedDetailRepository;

    @Autowired
    public DeceasedDetailServiceImpl(DeceasedDetailRepository deceasedDetailRepository) {
        this.deceasedDetailRepository = deceasedDetailRepository;
    }

    @Override
    public long getNewDeceasedCasesPerDate(LocalDate date) {
        return deceasedDetailRepository.getNewDeceasedCasesPerDate(date);
    }

    @Override
    public long getNewDeceasedCasesPerDateAndGnd(LocalDate date, Long gndId) {
        return deceasedDetailRepository.getNewDeceasedCasesPerDateAndGnd(date, gndId);
    }

    @Override
    public long getNewDeceasedCasesPerDateAndDivision(LocalDate date, Long divisionId) {
        return deceasedDetailRepository.getNewDeceasedCasesPerDateAndDivision(date, divisionId);
    }

    @Override
    public long getNewDeceasedCasesPerDateAndDistrict(LocalDate date, Long districtId) {
        return deceasedDetailRepository.getNewDeceasedCasesPerDateAndDistrict(date, districtId);
    }

    @Override
    public long getNewDeceasedCasesPerDateAndProvince(LocalDate date, Long provinceId) {
        return deceasedDetailRepository.getNewDeceasedCasesPerDateAndProvince(date, provinceId);
    }

    @Override
    public List<Long[]> getDeceasedCaseCountAgainstAgeGroup() {
        return deceasedDetailRepository.getDeceasedCaseCountAgainstAgeGroup();
    }

    @Override
    public List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndGnd(Long gndId) {
        return deceasedDetailRepository.getDeceasedCaseCountAgainstAgeGroupAndGnd(gndId);
    }

    @Override
    public List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndDivision(Long divisionId) {
        return deceasedDetailRepository.getDeceasedCaseCountAgainstAgeGroupAndDivision(divisionId);
    }

    @Override
    public List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndDistrict(Long districtId) {
        return deceasedDetailRepository.getDeceasedCaseCountAgainstAgeGroupAndDistrict(districtId);
    }

    @Override
    public List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndProvince(Long provinceId) {
        return deceasedDetailRepository.getDeceasedCaseCountAgainstAgeGroupAndProvince(provinceId);
    }

    @Override
    public List<Object[]> getNewDeceasedCasesPerDateForGivenDistricts(List<Long> districtIds, LocalDate date) {
        return deceasedDetailRepository.getNewDeceasedCasesPerDateForGivenDistricts(districtIds, date);
    }
}
