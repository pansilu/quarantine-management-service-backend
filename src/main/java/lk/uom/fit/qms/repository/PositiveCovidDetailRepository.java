package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.PositiveCovidDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/26/2020
 * @Package lk.uom.fit.qms.repository
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Repository
public interface PositiveCovidDetailRepository extends JpaRepository<PositiveCovidDetail, Long> {

    PositiveCovidDetail findPositiveCovidDetailByCaseNum(String caseNum);

    @Query("SELECT COUNT(pc) > 0 FROM PositiveCovidDetail pc WHERE pc.caseNum = :caseNum")
    boolean checkGivenCaseNumExits(@Param("caseNum") String caseNum);

    @Query("SELECT COUNT(pc) > 0 FROM PositiveCovidDetail pc WHERE pc.caseNum = :caseNum AND pc.id <> :id")
    boolean checkGivenCaseNumExits(@Param("id") Long id, @Param("caseNum") String caseNum);

    @Query("SELECT pc FROM PositiveCovidDetail pc WHERE LOWER(pc.user.name) LIKE LOWER(:pattern) OR LOWER(pc.caseNum) LIKE LOWER(:pattern) ORDER BY pc.caseNum")
    List<PositiveCovidDetail> filterBySearch(@Param("pattern") String pattern);

    @Query("SELECT pc FROM PositiveCovidDetail pc ORDER BY pc.caseNum")
    List<PositiveCovidDetail> getOrderedDetails();

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.identifiedDate = :givenDate")
    Long getNewCasesPerDate(@Param("givenDate") LocalDate date);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.identifiedDate = :givenDate AND pc.user.address.gnDivision.id = :id")
    Long getNewCasesPerDateAndGnd(@Param("givenDate") LocalDate date, @Param("id") Long gndId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.identifiedDate = :givenDate AND pc.user.address.gnDivision.division.id = :id")
    Long getNewCasesPerDateAndDivision(@Param("givenDate") LocalDate date, @Param("id") Long divisionId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.identifiedDate = :givenDate AND pc.user.address.gnDivision.division.district.id = :id")
    Long getNewCasesPerDateAndDistrict(@Param("givenDate") LocalDate date, @Param("id") Long districtId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.identifiedDate = :givenDate AND pc.user.address.gnDivision.division.district.province.id = :id")
    Long getNewCasesPerDateAndProvision(@Param("givenDate") LocalDate date, @Param("id") Long provinceId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.dischargeDate = :givenDate AND" +
            " (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = :givenDate AND dd.isDeleted = false) = 0")
    Long getNewRecoveredCasesPerDate(@Param("givenDate") LocalDate date);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.dischargeDate = :givenDate AND" +
            " (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = :givenDate AND dd.isDeleted = false) = 0 AND pc.user.address.gnDivision.id = :id")
    Long getNewRecoveredCasesPerDateAndGnd(@Param("givenDate") LocalDate date, @Param("id") Long gndId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.dischargeDate = :givenDate AND" +
            " (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = :givenDate AND dd.isDeleted = false) = 0 AND pc.user.address.gnDivision.division.id = :id")
    Long getNewRecoveredCasesPerDateAndDivision(@Param("givenDate") LocalDate date, @Param("id") Long divisionId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.dischargeDate = :givenDate AND" +
            " (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = :givenDate AND dd.isDeleted = false) = 0 AND pc.user.address.gnDivision.division.district.id = :id")
    Long getNewRecoveredCasesPerDateAndDistrict(@Param("givenDate") LocalDate date, @Param("id") Long districtId);

    @Query("SELECT COUNT(pc) FROM PositiveCovidDetail pc WHERE pc.dischargeDate = :givenDate AND" +
            " (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = :givenDate AND dd.isDeleted = false) = 0 AND pc.user.address.gnDivision.division.district.province.id = :id")
    Long getNewRecoveredCasesPerDateAndProvision(@Param("givenDate") LocalDate date, @Param("id") Long provinceId);
}
