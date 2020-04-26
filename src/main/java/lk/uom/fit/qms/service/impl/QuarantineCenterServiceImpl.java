package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.QuarantineCenter;
import lk.uom.fit.qms.repository.QuarantineCenterRepository;
import lk.uom.fit.qms.service.QuarantineCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class QuarantineCenterServiceImpl implements QuarantineCenterService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuarantineCenterRepository quarantineCenterRepository;

    @Override
    public QuarantineCenter getQuarantineCenterForGivenId(Long id) throws QmsException {

        QuarantineCenter quarantineCenter = quarantineCenterRepository.findQuarantineCenterById(id);

        if(quarantineCenter == null) {
            logger.warn("Quarantine center didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Quarantine Center Not Found!!!");
        }

        return quarantineCenter;
    }
}
