package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.model.District;

import java.util.Map;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/27/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class DistrictMap {

    private District district;
    private Map<String, DivisionMap> divisionMaps;

    public DistrictMap(District district, Map<String, DivisionMap> divisionMaps) {
        this.district = district;
        this.divisionMaps = divisionMaps;
    }

    public DistrictMap() {
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Map<String, DivisionMap> getDivisionMaps() {
        return divisionMaps;
    }

    public void setDivisionMaps(Map<String, DivisionMap> divisionMaps) {
        this.divisionMaps = divisionMaps;
    }
}
