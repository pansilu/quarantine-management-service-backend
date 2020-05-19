package lk.uom.fit.qms.dto;

import org.json.simple.JSONObject;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/3/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class DistrictFeature {

    private String type;
    private JSONObject geometry;
    private DistrictProperties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getGeometry() {
        return geometry;
    }

    public void setGeometry(JSONObject geometry) {
        this.geometry = geometry;
    }

    public DistrictProperties getProperties() {
        return properties;
    }

    public void setProperties(DistrictProperties properties) {
        this.properties = properties;
    }
}
