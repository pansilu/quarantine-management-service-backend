package lk.uom.fit.qms.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/2/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
@Where(clause = "is_deleted = 0")
public class Point extends AbstractEntity {

    private static final long serialVersionUID = 5601811892605916603L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String code;
    private short value;
    @ColumnDefault("false")
    private boolean isFixed;
    @OneToMany(mappedBy = "point")
    private List<UserDailyPointDetails> userDailyPointDetailsList;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    public List<UserDailyPointDetails> getUserDailyPointDetailsList() {
        return userDailyPointDetailsList;
    }

    public void setUserDailyPointDetailsList(List<UserDailyPointDetails> userDailyPointDetailsList) {
        this.userDailyPointDetailsList = userDailyPointDetailsList;
    }
}
