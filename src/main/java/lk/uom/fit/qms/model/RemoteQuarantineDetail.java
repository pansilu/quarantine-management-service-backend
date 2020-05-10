package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

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
public class RemoteQuarantineDetail extends AbstractEntity {

    private static final long serialVersionUID = -5597123034889713351L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate startDate;

    @Column(columnDefinition = "varchar(2000)")
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate endDate;

    @ColumnDefault("true")
    private boolean isActive = true;

    @JsonBackReference
    @ManyToOne
    private QuarantineUser user;

    @JsonManagedReference
    @ManyToOne
    private QuarantineCenter quarantineCenter;

    @Column(columnDefinition = "SMALLINT(6) default 0")
    private short remainingDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public QuarantineCenter getQuarantineCenter() {
        return quarantineCenter;
    }

    public void setQuarantineCenter(QuarantineCenter quarantineCenter) {
        this.quarantineCenter = quarantineCenter;
    }

    public short getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(short remainingDays) {
        this.remainingDays = remainingDays;
    }
}
