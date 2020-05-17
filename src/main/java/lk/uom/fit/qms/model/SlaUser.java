package lk.uom.fit.qms.model;

import lk.uom.fit.qms.util.enums.BloodGroup;
import lk.uom.fit.qms.util.enums.Gender;
import lk.uom.fit.qms.util.enums.Rank;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class SlaUser extends User {

    private static final long serialVersionUID = -6862191390484786975L;

    private String serviceNo;
    private Rank rank;
    @ManyToOne
    private Unit unit;
    @ManyToOne
    private Regiment regiment;
    private LocalDate dob;
    private String province;
    private String district;
    private String divisional_secretarial;
    private String gnd;
    private BloodGroup bloodGroup;
    private Gender gender;

    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Regiment getRegiment() {
        return regiment;
    }

    public void setRegiment(Regiment regiment) {
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

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
