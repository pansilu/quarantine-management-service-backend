package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
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
@Where(clause = "is_deleted = 0")
public class Country extends AbstractEntity{

    private static final long serialVersionUID = 4178348203179055721L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(150)", unique = true, nullable = false)
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "arrivedCountry")
    private List<QuarantineUser> quarantineUsers = new ArrayList<>();

    public Country(String name) {
        this.name = name;
    }

    public Country() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<QuarantineUser> getQuarantineUsers() {
        return quarantineUsers;
    }

    public void setQuarantineUsers(List<QuarantineUser> quarantineUsers) {
        this.quarantineUsers = quarantineUsers;
    }
}
