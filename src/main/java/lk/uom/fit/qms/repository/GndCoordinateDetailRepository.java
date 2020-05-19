package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.GndCoordinateDetail;
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
 * @created on 5/7/2020
 * @Package lk.uom.fit.qms.repository
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Repository
public interface GndCoordinateDetailRepository extends JpaRepository<GndCoordinateDetail, Long> {

    @Query("SELECT gcd FROM GndCoordinateDetail gcd WHERE gcd.gnDivision.id in :ids")
    List<GndCoordinateDetail> findGndCoordinateDetailsForGivenGnIds(@Param("ids") List<Long> gndIds);

    @Query("SELECT gcd FROM GndCoordinateDetail gcd WHERE gcd.gnDivision.division.district.id = :id")
    List<GndCoordinateDetail> findGndCoordinateDetailsForDistrictId(@Param("id") Long districtId);

    @Query("SELECT gcd FROM GndCoordinateDetail gcd WHERE gcd.gnDivision.division.id = :id")
    List<GndCoordinateDetail> findGndCoordinateDetailsForDivisionId(@Param("id") Long divisionId);
}
