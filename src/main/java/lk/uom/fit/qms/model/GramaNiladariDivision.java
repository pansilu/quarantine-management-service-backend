package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
public class GramaNiladariDivision extends AbstractEntity {

    private static final long serialVersionUID = -7783010923472250816L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "varchar(100)")
    private String name;
    @Column(columnDefinition = "varchar(20)")
    private String code;
    @Column(columnDefinition = "varchar(20)")
    private String gndNo;
    @Column(columnDefinition = "varchar(20)", unique = true)
    private String objectId;
    @Column(columnDefinition = "LONGTEXT")
    private String feature;

    @JsonBackReference
    @ManyToOne
    private Division division;

    @JsonBackReference
    @OneToMany(mappedBy = "gnDivision")
    private List<Address> addressList = new ArrayList<>();

    public GramaNiladariDivision(String name, String code, String gndNo, String objectId) {
        this.name = name;
        this.code = code;
        this.gndNo = gndNo;
        this.objectId = objectId;
    }

    public GramaNiladariDivision() {
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

    public String getGndNo() {
        return gndNo;
    }

    public void setGndNo(String gndNo) {
        this.gndNo = gndNo;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
