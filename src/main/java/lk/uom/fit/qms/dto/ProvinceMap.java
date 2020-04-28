package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.model.Province;

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

public class ProvinceMap {

    private Province province;
    private Map<String, DistrictMap> districtMaps;

    public ProvinceMap(Province province, Map<String, DistrictMap> districtMaps) {
        this.province = province;
        this.districtMaps = districtMaps;
    }

    public ProvinceMap() {
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Map<String, DistrictMap> getDistrictMaps() {
        return districtMaps;
    }

    public void setDistrictMaps(Map<String, DistrictMap> districtMaps) {
        this.districtMaps = districtMaps;
    }
}
