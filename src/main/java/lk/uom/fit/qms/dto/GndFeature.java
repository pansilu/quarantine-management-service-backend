package lk.uom.fit.qms.dto;

import org.json.simple.JSONObject;

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

public class GndFeature {

    private String type;
    private String id;
    private JSONObject geometry;
    private GndProperties properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JSONObject getGeometry() {
        return geometry;
    }

    public void setGeometry(JSONObject geometry) {
        this.geometry = geometry;
    }

    public GndProperties getProperties() {
        return properties;
    }

    public void setProperties(GndProperties properties) {
        this.properties = properties;
    }
}
