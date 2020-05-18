package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lk.uom.fit.qms.config.LocalDateDeserializer;
import lk.uom.fit.qms.model.Regiment;
import lk.uom.fit.qms.model.Unit;
import lk.uom.fit.qms.util.enums.BloodGroup;
import lk.uom.fit.qms.util.enums.Gender;
import lk.uom.fit.qms.util.enums.Rank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SlaUserDto extends UserRequestDto {
    private String serviceNo;
    private Rank rank;
    private String unit;
    private String regiment;
    @NotNull(message = "report date should need to be entered")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dob;
    private String province;
    private String district;
    private String divisional_secretarial;
    private String gnd;
    private String bloodGroup;
    private Gender gender;

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRegiment() {
        return regiment;
    }

    public void setRegiment(String regiment) {
        this.regiment = regiment;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivisional_secretarial() {
        return divisional_secretarial;
    }

    public void setDivisional_secretarial(String divisional_secretarial) {
        this.divisional_secretarial = divisional_secretarial;
    }

    public String getGnd() {
        return gnd;
    }

    public void setGnd(String gnd) {
        this.gnd = gnd;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }
}
