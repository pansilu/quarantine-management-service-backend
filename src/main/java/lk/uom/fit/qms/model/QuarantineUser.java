package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lk.uom.fit.qms.util.enums.QuarantineUserStatus;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDate;
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
    private LocalDate arrivalDate;

    @JsonManagedReference
    @ManyToOne
    private Country arrivedCountry;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HomeQuarantineDetail> hqDetails = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RemoteQuarantineDetail> rqDetails = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SuspectCovidDetail> scDetails = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PositiveCovidDetail> pcDetails = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<DeceasedDetail> deceasedDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private QuarantineUserStatus status;

    @ColumnDefault("false")
    private boolean isExternallyAdded;

    @Column(columnDefinition = "varchar(10)", unique = true)
    private String externalKey;


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

    public List<HomeQuarantineDetail> getHqDetails() {
        return hqDetails;
    }

    public void setHqDetails(List<HomeQuarantineDetail> hqDetails) {
        this.hqDetails = hqDetails;
    }

    public List<RemoteQuarantineDetail> getRqDetails() {
        return rqDetails;
    }

    public void setRqDetails(List<RemoteQuarantineDetail> rqDetails) {
        this.rqDetails = rqDetails;
    }

    public List<SuspectCovidDetail> getScDetails() {
        return scDetails;
    }

    public void setScDetails(List<SuspectCovidDetail> scDetails) {
        this.scDetails = scDetails;
    }

    public List<PositiveCovidDetail> getPcDetails() {
        return pcDetails;
    }

    public void setPcDetails(List<PositiveCovidDetail> pcDetails) {
        this.pcDetails = pcDetails;
    }

    public List<DeceasedDetail> getDeceasedDetails() {
        return deceasedDetails;
    }

    public void setDeceasedDetails(List<DeceasedDetail> deceasedDetails) {
        this.deceasedDetails = deceasedDetails;
    }

    public QuarantineUserStatus getStatus() {
        return status;
    }

    public void setStatus(QuarantineUserStatus status) {
        this.status = status;
    }

    public boolean isExternallyAdded() {
        return isExternallyAdded;
    }

    public void setExternallyAdded(boolean externallyAdded) {
        isExternallyAdded = externallyAdded;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }
}
