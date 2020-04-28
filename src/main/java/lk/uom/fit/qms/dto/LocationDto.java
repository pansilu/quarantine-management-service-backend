package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDto {

    private String gnd_n;
    private String gnd_no;
    private String gnd_c;
    private String province_n;
    private String province_c;
    private String district_n;
    private String district_c;
    private String dsd_n;
    private String dsd_c;
    private String objectid_1;

    public String getGnd_n() {
        return gnd_n;
    }

    public void setGnd_n(String gnd_n) {
        this.gnd_n = gnd_n;
    }

    public String getGnd_no() {
        return gnd_no;
    }

    public void setGnd_no(String gnd_no) {
        this.gnd_no = gnd_no;
    }

    public String getGnd_c() {
        return gnd_c;
    }

    public void setGnd_c(String gnd_c) {
        this.gnd_c = gnd_c;
    }

    public String getProvince_n() {
        return province_n;
    }

    public void setProvince_n(String province_n) {
        this.province_n = province_n;
    }

    public String getProvince_c() {
        return province_c;
    }

    public void setProvince_c(String province_c) {
        this.province_c = province_c;
    }

    public String getDistrict_n() {
        return district_n;
    }

    public void setDistrict_n(String district_n) {
        this.district_n = district_n;
    }

    public String getDistrict_c() {
        return district_c;
    }

    public void setDistrict_c(String district_c) {
        this.district_c = district_c;
    }

    public String getDsd_n() {
        return dsd_n;
    }

    public void setDsd_n(String dsd_n) {
        this.dsd_n = dsd_n;
    }

    public String getDsd_c() {
        return dsd_c;
    }

    public void setDsd_c(String dsd_c) {
        this.dsd_c = dsd_c;
    }

    public String getObjectid_1() {
        return objectid_1;
    }

    public void setObjectid_1(String objectid_1) {
        this.objectid_1 = objectid_1;
    }
}
