package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.DeceasedDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/2/2020
 * @Package lk.uom.fit.qms.repository
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Repository
public interface DeceasedDetailRepository extends JpaRepository<DeceasedDetail, Long> {

    @Query("SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.deceasedDate = :givenDate")
    Long getNewDeceasedCasesPerDate(@Param("givenDate") LocalDate date);

    @Query("SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.deceasedDate = :givenDate AND dd.user.address.gnDivision.id = :id")
    Long getNewDeceasedCasesPerDateAndGnd(@Param("givenDate") LocalDate date, @Param("id") Long gndId);

    @Query("SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.deceasedDate = :givenDate AND dd.user.address.gnDivision.division.id = :id")
    Long getNewDeceasedCasesPerDateAndDivision(@Param("givenDate") LocalDate date, @Param("id") Long divisionId);

    @Query("SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.deceasedDate = :givenDate AND dd.user.address.gnDivision.division.district.id = :id")
    Long getNewDeceasedCasesPerDateAndDistrict(@Param("givenDate") LocalDate date, @Param("id") Long districtId);

    @Query("SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.deceasedDate = :givenDate AND dd.user.address.gnDivision.division.district.province.id = :id")
    Long getNewDeceasedCasesPerDateAndProvince(@Param("givenDate") LocalDate date, @Param("id") Long provinceId);
}
