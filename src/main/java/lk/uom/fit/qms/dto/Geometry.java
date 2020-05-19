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
 * @created on 5/7/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {

    private List<CoordinatesSet> geometries;

    public List<CoordinatesSet> getGeometries() {
        return geometries;
    }

    public void setGeometries(List<CoordinatesSet> geometries) {
        this.geometries = geometries;
    }
}
