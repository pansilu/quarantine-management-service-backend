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
 * @created on 5/3/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictFeatureDetail {

    private List<DistrictFeature> features;

    public List<DistrictFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<DistrictFeature> features) {
        this.features = features;
    }
}
