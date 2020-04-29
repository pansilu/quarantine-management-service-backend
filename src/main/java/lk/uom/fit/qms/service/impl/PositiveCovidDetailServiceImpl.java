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
}
