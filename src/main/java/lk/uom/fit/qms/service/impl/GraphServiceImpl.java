package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.GraphRequestDto;
import lk.uom.fit.qms.service.GraphService;
import lk.uom.fit.qms.service.QuarantineUserService;
import lk.uom.fit.qms.service.ReportUserService;
import lk.uom.fit.qms.service.UserService;
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
 * @created on 4/11/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GraphServiceImpl implements GraphService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final QuarantineUserService quarantineUserService;

    private final ReportUserService reportUserService;

    @Autowired
    public GraphServiceImpl(UserService userService, QuarantineUserService quarantineUserService, ReportUserService reportUserService) {
        this.userService = userService;
        this.quarantineUserService = quarantineUserService;
        this.reportUserService = reportUserService;
    }

    @Override
    public Object getGraphDetails(GraphRequestDto graphRequestDto, Long userId) {

        switch (graphRequestDto.getGraphType()) {

            case AGE:
                return getAgeGraphDetails(graphRequestDto, userId);
            case DATE:
                return getDateGraphDetails(graphRequestDto, userId);
            case GROWTH:
                return getGrowthGraphDetails(graphRequestDto, userId);
            case STATION:
                return getStationGraphDetails(graphRequestDto, userId);
            default:
                return getDivisionGraphDetails(graphRequestDto, userId);
        }
    }

    private Object getAgeGraphDetails(GraphRequestDto graphRequestDto, Long userId) {
        return null;
    }

    private Object getStationGraphDetails(GraphRequestDto graphRequestDto, Long userId) {
        return null;
    }

    private Object getDivisionGraphDetails(GraphRequestDto graphRequestDto, Long userId) {
        return null;
    }

    private Object getDateGraphDetails(GraphRequestDto graphRequestDto, Long userId) {
        return null;
    }

    private Object getGrowthGraphDetails(GraphRequestDto graphRequestDto, Long userId) {
        return null;
    }
}
