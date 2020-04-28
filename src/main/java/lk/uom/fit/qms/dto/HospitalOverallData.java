package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

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
public class HospitalOverallData {

    private List<HospitalStat> hospital_data;

    public List<HospitalStat> getHospital_data() {
        return hospital_data;
    }

    public void setHospital_data(List<HospitalStat> hospital_data) {
        this.hospital_data = hospital_data;
    }
}
