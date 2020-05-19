package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
 * @created on 5/7/2020
 * @Package lk.uom.fit.qms.model
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Entity
public class GndCoordinateDetail extends AbstractEntity {

    private static final long serialVersionUID = -3529633730854574999L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String nearestGndIds;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String coordinates;
    @Column(columnDefinition = "varchar(20)")
    private String lat;
    @Column(columnDefinition = "varchar(20)")
    private String lon;

    @JsonBackReference
    @OneToOne
    private GramaNiladariDivision gnDivision;

    @JsonManagedReference
    @OneToMany(mappedBy = "gndCoordinateDetail", cascade = CascadeType.ALL)
    private List<NearestGndDetail> nearestGndDetails = new ArrayList<>();

    public GndCoordinateDetail(String coordinates, GramaNiladariDivision gnDivision) {
        this.coordinates = coordinates;
        this.gnDivision = gnDivision;
    }

    public GndCoordinateDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNearestGndIds() {
        return nearestGndIds;
    }

    public void setNearestGndIds(String nearestGndIds) {
        this.nearestGndIds = nearestGndIds;
    }

    public GramaNiladariDivision getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GramaNiladariDivision gnDivision) {
        this.gnDivision = gnDivision;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public List<NearestGndDetail> getNearestGndDetails() {
        return nearestGndDetails;
    }

    public void setNearestGndDetails(List<NearestGndDetail> nearestGndDetails) {
        this.nearestGndDetails = nearestGndDetails;
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
}
