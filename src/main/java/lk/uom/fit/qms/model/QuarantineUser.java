package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class QuarantineUser extends User {

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
    private List<QuarantineUserInspectDetails> quarantineUserInspectDetails = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "guardianId")
    private User guardian;

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

    @Column(columnDefinition = "varchar(25)")
    private String fileNo;

    @Column(columnDefinition = "SMALLINT(6) default 0")
    private short remainingDays;
    @Column(columnDefinition = "SMALLINT(6) default 0")
    private short totalPoints;
    @CreationTimestamp
    @Column(name = "last_value_update_date", nullable = false, columnDefinition = "DATETIME(0)")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime lastValueUpdateDate;

    @ColumnDefault("false")
    private boolean isAppEnable;

    public String getFileNo() { return fileNo; }

    public void setFileNo(String fileNo) { this.fileNo = fileNo; }

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

    public short getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(short remainingDays) {
        this.remainingDays = remainingDays;
    }

    public short getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(short totalPoints) {
        this.totalPoints = totalPoints;
    }

    public LocalDateTime getLastValueUpdateDate() {
        return lastValueUpdateDate;
    }

    public void setLastValueUpdateDate(LocalDateTime lastValueUpdateDate) {
        this.lastValueUpdateDate = lastValueUpdateDate;
    }

    public boolean isAppEnable() {
        return isAppEnable;
    }

    public void setAppEnable(boolean appEnable) {
        isAppEnable = appEnable;
    }
}
