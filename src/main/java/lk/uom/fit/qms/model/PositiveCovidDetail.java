package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.ColumnDefault;
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
 * @created on 4/25/2020
 * @Package lk.uom.fit.qms.model
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Entity
@Where(clause = "is_deleted = 0")
public class PositiveCovidDetail extends AbstractEntity {

    private static final long serialVersionUID = 7974298209034773404L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate identifiedDate;

    @Column(columnDefinition = "varchar(50)")
    private String caseNum;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "relatedCaseId")
    private PositiveCovidDetail parentCase;
    @JsonManagedReference
    @OneToMany(mappedBy = "parentCase", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<PositiveCovidDetail> subCases = new ArrayList<>();

    @Column(columnDefinition = "varchar(2000)")
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dischargeDate;

    @ColumnDefault("true")
    private boolean isActive = true;

    @JsonBackReference
    @ManyToOne
    private QuarantineUser user;

    @JsonManagedReference
    @ManyToOne
    private Hospital hospital;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getIdentifiedDate() {
        return identifiedDate;
    }

    public void setIdentifiedDate(LocalDate identifiedDate) {
        this.identifiedDate = identifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public QuarantineUser getUser() {
        return user;
    }

    public void setUser(QuarantineUser user) {
        this.user = user;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public PositiveCovidDetail getParentCase() {
        return parentCase;
    }

    public void setParentCase(PositiveCovidDetail parentCase) {
        this.parentCase = parentCase;
    }

    public List<PositiveCovidDetail> getSubCases() {
        return subCases;
    }

    public void setSubCases(List<PositiveCovidDetail> subCases) {
        this.subCases = subCases;
    }
}
