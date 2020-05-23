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
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
@Where(clause = "is_deleted = 0")
public class Hospital extends AbstractEntity {

    private static final long serialVersionUID = -3635434212099785556L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(150)", unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(20)")
    private String lat;
    @Column(columnDefinition = "varchar(20)")
    private String lon;
    private Long mappingId;

    @JsonBackReference
    @OneToMany(mappedBy = "hospital")
    private List<SuspectCovidDetail> scDetails = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "hospital")
    private List<PositiveCovidDetail> pcDetails = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "hospital")
    private List<HospitalMappingName> hospitalMappingNames = new ArrayList<>();

    public Hospital(Long mappingId, String name) {
        this.mappingId = mappingId;
        this.name = name;
    }

    public Hospital() {
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

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public List<HospitalMappingName> getHospitalMappingNames() {
        return hospitalMappingNames;
    }

    public void setHospitalMappingNames(List<HospitalMappingName> hospitalMappingNames) {
        this.hospitalMappingNames = hospitalMappingNames;
    }
}
