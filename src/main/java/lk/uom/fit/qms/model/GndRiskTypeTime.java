package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/9/2020
 * @Package lk.uom.fit.qms.model
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Entity
public class GndRiskTypeTime extends AbstractEntity {

    private static final long serialVersionUID = 1111186989560398780L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @OneToOne
    private GramaNiladariDivision gnDivision;

    private Integer highRiskPeriod;

    private Integer moderateRiskPeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GramaNiladariDivision getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GramaNiladariDivision gnDivision) {
        this.gnDivision = gnDivision;
    }

    public Integer getHighRiskPeriod() {
        return highRiskPeriod;
    }

    public void setHighRiskPeriod(Integer highRiskDuration) {
        this.highRiskPeriod = highRiskDuration;
    }

    public Integer getModerateRiskPeriod() {
        return moderateRiskPeriod;
    }

    public void setModerateRiskPeriod(Integer moderateRiskDuration) {
        this.moderateRiskPeriod = moderateRiskDuration;
    }
}
