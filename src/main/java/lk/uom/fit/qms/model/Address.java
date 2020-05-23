package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lk.uom.fit.qms.util.enums.LocationState;
import org.hibernate.annotations.ColumnDefault;
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
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
@Where(clause = "is_deleted = 0")
public class Address extends AbstractEntity {

    private static final long serialVersionUID = 4434089240900061938L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100)")
    private String policeArea;
    @Column(columnDefinition = "varchar(300)")
    private String line;
    @Column(columnDefinition = "varchar(20)")
    private String lat;
    @Column(columnDefinition = "varchar(20)")
    private String lon;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private LocationState locationState = LocationState.OK;

    @JsonManagedReference
    @ManyToOne
    private GramaNiladariDivision gnDivision;

    @JsonManagedReference
    @OneToMany(mappedBy = "address")
    private List<User> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getPoliceArea() {
        return policeArea;
    }

    public void setPoliceArea(String policeArea) {
        this.policeArea = policeArea;
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

    public GramaNiladariDivision getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GramaNiladariDivision gnDivision) {
        this.gnDivision = gnDivision;
    }

    public LocationState getLocationState() {
        return locationState;
    }

    public void setLocationState(LocationState locationState) {
        this.locationState = locationState;
    }
}
