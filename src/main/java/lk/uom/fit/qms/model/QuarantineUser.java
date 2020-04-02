package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
public class QuarantineUser extends User{

    private static final long serialVersionUID = 630753049059032510L;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate reportDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate arrivalDate;
    @ManyToOne
    @JsonManagedReference
    private Country arrivedCountry;

    @ColumnDefault("false")
    private boolean isInformedAuthority;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate informedDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate noticeAttachDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "quarantineUser", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<QuarantineUserInspectDetails> quarantineUserInspectDetails;

    @ManyToOne
    @JoinColumn(name = "guardianId")
    private User guardian;

    @ManyToOne
    @JoinColumn(name = "addedBy")
    private User addedBy;

    @Column(columnDefinition = "varchar(2000)")
    private String otherFacts;

    @ColumnDefault("false")
    private boolean isPatient;

    @JsonManagedReference
    @OneToOne(mappedBy = "patient", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private PatientDetails patientDetails;

    @ColumnDefault("false")
    private boolean isCompleted;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<UserDailyPointDetails> userDailyPointDetailsList;

    private String secret;

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Country getArrivedCountry() {
        return arrivedCountry;
    }

    public void setArrivedCountry(Country arrivedCountry) {
        this.arrivedCountry = arrivedCountry;
    }

    public boolean isInformedAuthority() {
        return isInformedAuthority;
    }

    public void setInformedAuthority(boolean informedAuthority) {
        isInformedAuthority = informedAuthority;
    }

    public LocalDate getInformedDate() {
        return informedDate;
    }

    public void setInformedDate(LocalDate informedDate) {
        this.informedDate = informedDate;
    }

    public LocalDate getNoticeAttachDate() {
        return noticeAttachDate;
    }

    public void setNoticeAttachDate(LocalDate noticeAttachDate) {
        this.noticeAttachDate = noticeAttachDate;
    }

    public List<QuarantineUserInspectDetails> getQuarantineUserInspectDetails() {
        return quarantineUserInspectDetails;
    }

    public void setQuarantineUserInspectDetails(List<QuarantineUserInspectDetails> quarantineUserInspectDetails) {
        this.quarantineUserInspectDetails = quarantineUserInspectDetails;
    }

    public User getGuardian() {
        return guardian;
    }

    public void setGuardian(User guardian) {
        this.guardian = guardian;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public String getOtherFacts() {
        return otherFacts;
    }

    public void setOtherFacts(String otherFacts) {
        this.otherFacts = otherFacts;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setPatient(boolean patient) {
        isPatient = patient;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public List<UserDailyPointDetails> getUserDailyPointDetailsList() {
        return userDailyPointDetailsList;
    }

    public void setUserDailyPointDetailsList(List<UserDailyPointDetails> userDailyPointDetailsList) {
        this.userDailyPointDetailsList = userDailyPointDetailsList;
    }
}
