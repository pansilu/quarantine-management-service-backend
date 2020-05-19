package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lk.uom.fit.qms.util.enums.RiskType;

import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
@Where(clause = "is_deleted = 0")
public class GndRiskDetail extends AbstractEntity {

    private static final long serialVersionUID = -1541130427221284165L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference
    @ManyToOne
    private GramaNiladariDivision gnDivision;

    @JsonManagedReference
    @OneToOne
    private PositiveCovidDetail latestPcDetail;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private RiskType riskType;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate endDate;

    private Integer remainingPeriod;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "parentId")
    private GndRiskDetail parentRiskDetail;
    @JsonManagedReference
    @OneToMany(mappedBy = "parentRiskDetail", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<GndRiskDetail> childrenRiskDetail = new ArrayList<>();

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

    public PositiveCovidDetail getLatestPcDetail() {
        return latestPcDetail;
    }

    public void setLatestPcDetail(PositiveCovidDetail latestCovidCase) {
        this.latestPcDetail = latestCovidCase;
    }

    public RiskType getRiskType() {
        return riskType;
    }

    public void setRiskType(RiskType riskType) {
        this.riskType = riskType;
    }

    public Integer getRemainingPeriod() {
        return remainingPeriod;
    }

    public void setRemainingPeriod(Integer remainingPeriod) {
        this.remainingPeriod = remainingPeriod;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate riskRemovingDate) {
        this.endDate = riskRemovingDate;
    }

    public GndRiskDetail getParentRiskDetail() {
        return parentRiskDetail;
    }

    public void setParentRiskDetail(GndRiskDetail parentRiskDetail) {
        this.parentRiskDetail = parentRiskDetail;
    }

    public List<GndRiskDetail> getChildrenRiskDetail() {
        return childrenRiskDetail;
    }

    public void setChildrenRiskDetail(List<GndRiskDetail> childrenRiskDetail) {
        this.childrenRiskDetail = childrenRiskDetail;
    }
}
