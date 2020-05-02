package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.repository.DeceasedDetailRepository;
import lk.uom.fit.qms.service.DeceasedDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
}
