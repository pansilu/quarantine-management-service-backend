package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/9/2020
 * @Package lk.uom.fit.qms.model
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Entity
@Table(indexes = {
                @Index(columnList = "gnd_coordinate_detail_id, grama_niladari_division_id", name = "idx_gnd_coordinate_detail_id_and_grama_niladari_division_id", unique = true)
        })
public class NearestGndDetail extends AbstractEntity {

    private static final long serialVersionUID = -683578644787211357L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "gnd_coordinate_detail_id")
    private GndCoordinateDetail gndCoordinateDetail;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "grama_niladari_division_id")
    private GramaNiladariDivision gramaNiladariDivision;

    public NearestGndDetail(GndCoordinateDetail gndCoordinateDetail, GramaNiladariDivision gramaNiladariDivision) {
        this.gndCoordinateDetail = gndCoordinateDetail;
        this.gramaNiladariDivision = gramaNiladariDivision;
    }

    public NearestGndDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GndCoordinateDetail getGndCoordinateDetail() {
        return gndCoordinateDetail;
    }

    public void setGndCoordinateDetail(GndCoordinateDetail gndCoordinateDetail) {
        this.gndCoordinateDetail = gndCoordinateDetail;
    }

    public GramaNiladariDivision getGramaNiladariDivision() {
        return gramaNiladariDivision;
    }

    public void setGramaNiladariDivision(GramaNiladariDivision gramaNiladariDivision) {
        this.gramaNiladariDivision = gramaNiladariDivision;
    }
}
