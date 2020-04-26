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
public class DeceasedDetail extends AbstractEntity {

    private static final long serialVersionUID = -1616248201019911266L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate deceasedDate;

    @Column(columnDefinition = "varchar(2000)")
    private String description;

    @JsonBackReference
    @ManyToOne
    private QuarantineUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDeceasedDate() {
        return deceasedDate;
    }

    public void setDeceasedDate(LocalDate deceasedDate) {
        this.deceasedDate = deceasedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QuarantineUser getUser() {
        return user;
    }

    public void setUser(QuarantineUser user) {
        this.user = user;
    }
}
