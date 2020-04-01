package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
@Where(clause = "is_deleted = 0")
public class PatientDetails extends AbstractEntity{

    private static final long serialVersionUID = -6909741094210798979L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonBackReference
    @OneToOne
    private QuarantineUser patient;
    @ManyToOne
    private Hospital admitHospital;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate admittedDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dischargedDate;
    @ManyToOne
    private Hospital confirmedHospital;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate confirmedDate;
    @ColumnDefault("false")
    private boolean isInfected;
    @ColumnDefault("false")
    private boolean isDischarged;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuarantineUser getPatient() {
        return patient;
    }

    public void setPatient(QuarantineUser patient) {
        this.patient = patient;
    }

    public Hospital getAdmitHospital() {
        return admitHospital;
    }

    public void setAdmitHospital(Hospital admitHospital) {
        this.admitHospital = admitHospital;
    }

    public LocalDate getAdmittedDate() {
        return admittedDate;
    }

    public void setAdmittedDate(LocalDate admittedDate) {
        this.admittedDate = admittedDate;
    }

    public LocalDate getDischargedDate() {
        return dischargedDate;
    }

    public void setDischargedDate(LocalDate dischargedDate) {
        this.dischargedDate = dischargedDate;
    }

    public Hospital getConfirmedHospital() {
        return confirmedHospital;
    }

    public void setConfirmedHospital(Hospital confirmedHospital) {
        this.confirmedHospital = confirmedHospital;
    }

    public LocalDate getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(LocalDate confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public boolean isDischarged() {
        return isDischarged;
    }

    public void setDischarged(boolean discharged) {
        isDischarged = discharged;
    }
}
