package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.GndRiskDetailResponse;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.QuarantineUser;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/9/2020
 * @Package lk.uom.fit.qms.service
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public interface GndRiskDetailService {

    void updateGndRiskDetail(QuarantineUser quarantineUser);

    void updateGndRiskDetails();

    List<GndRiskDetailResponse> getGndRiskDetailsForDistrict(Long districtId) throws QmsException;
}
