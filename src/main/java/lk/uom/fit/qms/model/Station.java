package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
@Where(clause = "is_deleted = 0")
public class Station extends AbstractEntity{

    private static final long serialVersionUID = -4904031595348091280L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JsonBackReference
    @ManyToOne
    private Division division;

    @JsonBackReference
    @ManyToMany(mappedBy = "stations")
    private List<ReportUser> reportUsers;

    @JsonManagedReference
    @OneToMany(mappedBy = "station")
    private List<Address> addressList;

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

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public List<ReportUser> getReportUsers() {
        return reportUsers;
    }

    public void setReportUsers(List<ReportUser> reportUsers) {
        this.reportUsers = reportUsers;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
