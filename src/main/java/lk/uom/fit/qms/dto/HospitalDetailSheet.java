package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/28/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HospitalDetailSheet {

    private HospitalOverallData data;

    public HospitalOverallData getData() {
        return data;
    }

    public void setData(HospitalOverallData data) {
        this.data = data;
    }
}
