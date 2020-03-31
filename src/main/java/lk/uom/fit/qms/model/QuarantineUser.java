package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Where(clause = "is_deleted = 0")
public class QuarantineUser extends User{

    private static final long serialVersionUID = 630753049059032510L;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate reportDate;
    @ColumnDefault("false")
    private boolean isVisitCountry;
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

    @OneToMany(mappedBy = "quarantineUser")
    private List<QuarantineUserInspectDetails> quarantineUserInspectDetails;

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
}
