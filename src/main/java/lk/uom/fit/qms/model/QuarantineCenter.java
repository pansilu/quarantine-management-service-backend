package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/26/2020
 * @Package lk.uom.fit.qms.model
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Entity
@Where(clause = "is_deleted = 0")
public class QuarantineCenter extends AbstractEntity {

    private static final long serialVersionUID = -3215998249098208046L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(150)", unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(20)")
    private String lat;
    @Column(columnDefinition = "varchar(20)")
    private String lon;

    @JsonBackReference
    @OneToMany(mappedBy = "quarantineCenter")
    private List<RemoteQuarantineDetail> rqDetails = new ArrayList<>();

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public List<RemoteQuarantineDetail> getRqDetails() {
        return rqDetails;
    }

    public void setRqDetails(List<RemoteQuarantineDetail> rqDetails) {
        this.rqDetails = rqDetails;
    }
}
