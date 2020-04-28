package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Division extends AbstractEntity{

    private static final long serialVersionUID = -7674375563253715470L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100)", unique = true, nullable = false)
    private String name;
    @Column(columnDefinition = "varchar(20)")
    private String code;

    @JsonManagedReference
    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    private List<GramaNiladariDivision> gnDivisions = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    private District district;

    public Division(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public Division() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<GramaNiladariDivision> getGnDivisions() {
        return gnDivisions;
    }

    public void setGnDivisions(List<GramaNiladariDivision> gnDivisions) {
        this.gnDivisions = gnDivisions;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
