package lk.uom.fit.qms.model;

import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;

@Entity
public class HospitalMappingName extends AbstractEntity {

    private static final long serialVersionUID = -1139948845145007089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100)", unique = true, nullable = false)
    private String name;
    @JsonManagedReference
    @ManyToOne
    private Hospital hospital;

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

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
