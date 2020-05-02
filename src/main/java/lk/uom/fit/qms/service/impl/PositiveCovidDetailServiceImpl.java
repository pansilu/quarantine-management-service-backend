package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.PositiveCovidCaseDetail;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.PositiveCovidDetail;
import lk.uom.fit.qms.repository.PositiveCovidDetailRepository;
import lk.uom.fit.qms.service.PositiveCovidDetailService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
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
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PositiveCovidDetailServiceImpl implements PositiveCovidDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PositiveCovidDetailRepository positiveCovidDetailRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public PositiveCovidDetailServiceImpl(PositiveCovidDetailRepository positiveCovidDetailRepository, ModelMapper modelMapper) {
        this.positiveCovidDetailRepository = positiveCovidDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PositiveCovidDetail findPositiveCovidDetailByCaseNum(String caseNum) throws QmsException {

        if(StringUtils.isEmpty(caseNum)) {
            return null;
        }

        PositiveCovidDetail positiveCovidDetail = positiveCovidDetailRepository.findPositiveCovidDetailByCaseNum(caseNum);
        if(positiveCovidDetail == null) {
            logger.warn("Positive covid user status didn't exist for caseNum: {}", caseNum);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Positive Covid User status not found for given case number");
        }

        return positiveCovidDetail;
    }

    @Override
    public void checkCaseNumAlreadyExists(Long id, String caseNum) throws QmsException {

        boolean isCaseNumExists;

        if(id != null) {
            isCaseNumExists = positiveCovidDetailRepository.checkGivenCaseNumExits(id, caseNum);
        } else {
            isCaseNumExists = positiveCovidDetailRepository.checkGivenCaseNumExits(caseNum);
        }

        if(isCaseNumExists) {
            logger.warn("Given caseNum: {}, already exists for positive covid status", caseNum);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Given case num already exists!");
        }
    }

    @Override
    public List<PositiveCovidCaseDetail> getCaseDetails(String search) {

        List<PositiveCovidDetail> positiveCovidDetails;

        if(StringUtils.isEmpty(search)) {
            positiveCovidDetails = positiveCovidDetailRepository.getOrderedDetails();
        } else {
            String pattern = "%" + search + "%";
            positiveCovidDetails = positiveCovidDetailRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<PositiveCovidCaseDetail>>() {}.getType();
        return modelMapper.map(positiveCovidDetails, type);
    }

    @Override
    public long getNewCasesPerDate(LocalDate date) {
        return positiveCovidDetailRepository.getNewCasesPerDate(date);
    }

    @Override
    public long getNewCasesPerDateAndGnd(LocalDate date, Long gndId) {
        return positiveCovidDetailRepository.getNewCasesPerDateAndGnd(date, gndId);
    }

    @Override
    public long getNewCasesPerDateAndDivision(LocalDate date, Long divisionId) {
        return positiveCovidDetailRepository.getNewCasesPerDateAndDivision(date, divisionId);
    }

    @Override
    public long getNewCasesPerDateAndDistrict(LocalDate date, Long districtId) {
        return positiveCovidDetailRepository.getNewCasesPerDateAndDistrict(date, districtId);
    }

    @Override
    public long getNewCasesPerDateAndProvince(LocalDate date, Long provinceId) {
        return positiveCovidDetailRepository.getNewCasesPerDateAndProvision(date, provinceId);
    }

    @Override
    public long getNewRecoveredCasesPerDate(LocalDate date) {
        return positiveCovidDetailRepository.getNewRecoveredCasesPerDate(date);
    }

    @Override
    public long getNewRecoveredCasesPerDateAndGnd(LocalDate date, Long gndId) {
        return positiveCovidDetailRepository.getNewRecoveredCasesPerDateAndGnd(date, gndId);
    }

    @Override
    public long getNewRecoveredCasesPerDateAndDivision(LocalDate date, Long divisionId) {
        return positiveCovidDetailRepository.getNewRecoveredCasesPerDateAndDivision(date, divisionId);
    }

    @Override
    public long getNewRecoveredCasesPerDateAndDistrict(LocalDate date, Long districtId) {
        return positiveCovidDetailRepository.getNewRecoveredCasesPerDateAndDistrict(date, districtId);
    }

    @Override
    public long getNewRecoveredCasesPerDateAndProvince(LocalDate date, Long provinceId) {
        return positiveCovidDetailRepository.getNewRecoveredCasesPerDateAndProvision(date, provinceId);
    }

    @Override
    public List<Long[]> getAllPositiveCaseCountAgainstAgeGroup() {
        return positiveCovidDetailRepository.getAllPositiveCaseCountAgainstAgeGroup();
    }

    @Override
    public List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndGnd(Long gndId) {
        return positiveCovidDetailRepository.getAllPositiveCaseCountAgainstAgeGroupAndGnd(gndId);
    }

    @Override
    public List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndDivision(Long divisionId) {
        return positiveCovidDetailRepository.getAllPositiveCaseCountAgainstAgeGroupAndDivision(divisionId);
    }

    @Override
    public List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndDistrict(Long districtId) {
        return positiveCovidDetailRepository.getAllPositiveCaseCountAgainstAgeGroupAndDistrict(districtId);
    }

    @Override
    public List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndProvince(Long provinceId) {
        return positiveCovidDetailRepository.getAllPositiveCaseCountAgainstAgeGroupAndProvince(provinceId);
    }

    @Override
    public List<Long[]> getActiveCaseCountAgainstAgeGroup() {
        return positiveCovidDetailRepository.getActiveCaseCountAgainstAgeGroup();
    }

    @Override
    public List<Long[]> getActiveCaseCountAgainstAgeGroupAndGnd(Long gndId) {
        return positiveCovidDetailRepository.getActiveCaseCountAgainstAgeGroupAndGnd(gndId);
    }

    @Override
    public List<Long[]> getActiveCaseCountAgainstAgeGroupAndDivision(Long divisionId) {
        return positiveCovidDetailRepository.getActiveCaseCountAgainstAgeGroupAndDivision(divisionId);
    }

    @Override
    public List<Long[]> getActiveCaseCountAgainstAgeGroupAndDistrict(Long districtId) {
        return positiveCovidDetailRepository.getActiveCaseCountAgainstAgeGroupAndDistrict(districtId);
    }

    @Override
    public List<Long[]> getActiveCaseCountAgainstAgeGroupAndProvince(Long provinceId) {
        return positiveCovidDetailRepository.getActiveCaseCountAgainstAgeGroupAndProvince(provinceId);
    }

    @Override
    public List<Long[]> getRecoveredCaseCountAgainstAgeGroup() {
        return positiveCovidDetailRepository.getRecoveredCaseCountAgainstAgeGroup();
    }

    @Override
    public List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndGnd(Long gndId) {
        return positiveCovidDetailRepository.getRecoveredCaseCountAgainstAgeGroupAndGnd(gndId);
    }

    @Override
    public List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndDivision(Long divisionId) {
        return positiveCovidDetailRepository.getRecoveredCaseCountAgainstAgeGroupAndDivision(divisionId);
    }

    @Override
    public List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndDistrict(Long districtId) {
        return positiveCovidDetailRepository.getRecoveredCaseCountAgainstAgeGroupAndDistrict(districtId);
    }

    @Override
    public List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndProvince(Long provinceId) {
        return positiveCovidDetailRepository.getRecoveredCaseCountAgainstAgeGroupAndProvince(provinceId);
    }
}
