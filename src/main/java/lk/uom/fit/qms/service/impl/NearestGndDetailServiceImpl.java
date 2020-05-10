package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.model.GramaNiladariDivision;
import lk.uom.fit.qms.repository.NearestGndDetailRepository;
import lk.uom.fit.qms.service.NearestGndDetailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/10/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NearestGndDetailServiceImpl implements NearestGndDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NearestGndDetailRepository nearestGndDetailRepository;

    @Override
    public List<GramaNiladariDivision> getNearestGnDivisionDetailsForGivenGnd(Long gndId) {

        logger.debug("get nearest gnds for gnd: {}", gndId);
        return nearestGndDetailRepository.getNearestGndDetailsForGivenGnd(gndId);
    }
}
