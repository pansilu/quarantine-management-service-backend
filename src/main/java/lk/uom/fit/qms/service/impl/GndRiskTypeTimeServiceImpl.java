package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.model.GndRiskTypeTime;
import lk.uom.fit.qms.repository.GndRiskTypeTimeRepository;
import lk.uom.fit.qms.service.GndRiskTypeTimeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @created on 5/9/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GndRiskTypeTimeServiceImpl implements GndRiskTypeTimeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GndRiskTypeTimeRepository gndRiskTypeTimeRepository;

    @Override
    public GndRiskTypeTime findGndRiskTypeTimeForGnd(Long gndId) {
        return gndRiskTypeTimeRepository.findGndRiskTypeTimeByGnDivisionId(gndId);
    }
}
