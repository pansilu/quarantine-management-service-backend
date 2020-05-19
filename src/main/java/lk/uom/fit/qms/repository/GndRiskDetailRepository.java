package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.GndRiskDetail;
import lk.uom.fit.qms.util.enums.RiskType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
public interface GndRiskDetailRepository extends JpaRepository<GndRiskDetail, Long> {

    GndRiskDetail findGndRiskDetailByGnDivisionId(Long gndId);

    @Query("SELECT grd FROM GndRiskDetail grd WHERE grd.gnDivision.id IN :ids")
    List<GndRiskDetail> findGndRiskDetailsForGivenGndIds(@Param("ids") Collection<Long> gndIds);

    GndRiskDetail findTopByGnDivisionIdIsInAndRiskTypeOrderByStartDateDesc(Collection<Long> gndIds, RiskType riskType);

    List<GndRiskDetail> findGndRiskDetailsByParentRiskDetailId(Long parentId);

    @Query("SELECT grd FROM GndRiskDetail grd WHERE grd.gnDivision.division.district.id = :id")
    List<GndRiskDetail> findGndRiskDetailsByDistrict(@Param("id") Long districtId);
}
