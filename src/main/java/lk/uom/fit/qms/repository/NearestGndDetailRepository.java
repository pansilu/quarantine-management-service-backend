package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.GramaNiladariDivision;
import lk.uom.fit.qms.model.NearestGndDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/10/2020
 * @Package lk.uom.fit.qms.repository
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Repository
public interface NearestGndDetailRepository extends JpaRepository<NearestGndDetail, Long> {

    @Query("SELECT ngd.gramaNiladariDivision FROM NearestGndDetail ngd WHERE ngd.gndCoordinateDetail.gnDivision.id = :id")
    List<GramaNiladariDivision> getNearestGndDetailsForGivenGnd(@Param("id") Long gndId);
}
