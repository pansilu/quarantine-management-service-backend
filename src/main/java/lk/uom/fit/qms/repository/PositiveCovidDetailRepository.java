package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.GramaNiladariDivision;
import lk.uom.fit.qms.model.PositiveCovidDetail;

import lk.uom.fit.qms.model.QuarantineUser;
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

    // Age filter All.................
    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc")
    List<Long[]> getAllPositiveCaseCountAgainstAgeGroup();

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.user.address.gnDivision.id = :id")
    List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndGnd(@Param("id") Long gndId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.user.address.gnDivision.division.id = :id")
    List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndDivision(@Param("id") Long divisionId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.user.address.gnDivision.division.district.id = :id")
    List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndDistrict(@Param("id") Long districtId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.user.address.gnDivision.division.district.province.id = :id")
    List<Long[]> getAllPositiveCaseCountAgainstAgeGroupAndProvince(@Param("id") Long province);

    // Age filter Active.................
    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = true")
    List<Long[]> getActiveCaseCountAgainstAgeGroup();

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = true" +
            " AND pc.user.address.gnDivision.id = :id")
    List<Long[]> getActiveCaseCountAgainstAgeGroupAndGnd(@Param("id") Long gndId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = true" +
            " AND pc.user.address.gnDivision.division.id = :id")
    List<Long[]> getActiveCaseCountAgainstAgeGroupAndDivision(@Param("id") Long divisionId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = true" +
            " AND pc.user.address.gnDivision.division.district.id = :id")
    List<Long[]> getActiveCaseCountAgainstAgeGroupAndDistrict(@Param("id") Long districtId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = true" +
            " AND pc.user.address.gnDivision.division.district.province.id = :id")
    List<Long[]> getActiveCaseCountAgainstAgeGroupAndProvince(@Param("id") Long province);

    // Age filter Recovered.................
    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = false" +
            " AND (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = pc.dischargeDate AND dd.isDeleted = false) = 0")
    List<Long[]> getRecoveredCaseCountAgainstAgeGroup();

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = false" +
            " AND (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = pc.dischargeDate AND dd.isDeleted = false) = 0" +
            " AND pc.user.address.gnDivision.id = :id")
    List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndGnd(@Param("id") Long gndId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = false" +
            " AND (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = pc.dischargeDate AND dd.isDeleted = false) = 0" +
            " AND pc.user.address.gnDivision.division.id = :id")
    List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndDivision(@Param("id") Long divisionId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = false" +
            " AND (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = pc.dischargeDate AND dd.isDeleted = false) = 0" +
            " AND pc.user.address.gnDivision.division.district.id = :id")
    List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndDistrict(@Param("id") Long districtId);

    @Query("SELECT SUM(CASE WHEN pc.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN pc.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN pc.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN pc.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN pc.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN pc.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN pc.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM PositiveCovidDetail pc WHERE pc.isActive = false" +
            " AND (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = pc.dischargeDate AND dd.isDeleted = false) = 0" +
            " AND pc.user.address.gnDivision.division.district.province.id = :id")
    List<Long[]> getRecoveredCaseCountAgainstAgeGroupAndProvince(@Param("id") Long province);

    @Query("SELECT pc.user.address.gnDivision.division.district.name AS name, COUNT(pc) AS toatal FROM PositiveCovidDetail pc" +
            " WHERE pc.identifiedDate = :givenDate AND pc.user.address.gnDivision.division.district.id IN :ids GROUP BY pc.user.address.gnDivision.division.district.name")
    List<Object[]> getNewCasesPerDateForGivenDistricts(@Param("ids") List<Long> districtIds, @Param("givenDate") LocalDate date);

    @Query("SELECT pc.user.address.gnDivision.division.district.name AS name, COUNT(pc) AS toatal FROM PositiveCovidDetail pc" +
            " WHERE pc.identifiedDate = :givenDate AND pc.user.address.gnDivision.division.district.id IN :ids AND" +
            " (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = :givenDate AND dd.isDeleted = false) = 0" +
            " GROUP BY pc.user.address.gnDivision.division.district.name")
    List<Object[]> getNewRecoveredCasesPerDateForGivenDistricts(@Param("ids") List<Long> districtIds, @Param("givenDate") LocalDate date);

    @Query("SELECT pc.user FROM PositiveCovidDetail pc WHERE pc.isActive = true AND pc.user.address.gnDivision.division.district.id IN :ids")
    List<QuarantineUser> getAllActiveCovidUserDetails(@Param("ids") List<Long> districtIds);

    @Query("SELECT pc.user FROM PositiveCovidDetail pc WHERE pc.isActive = false AND pc.user.address.gnDivision.division.district.id IN :ids" +
            " AND (SELECT COUNT(dd) FROM DeceasedDetail dd WHERE dd.user.id = pc.user.id AND dd.deceasedDate = pc.dischargeDate AND dd.isDeleted = false) = 0")
    List<QuarantineUser> getAllRecoveredCovidUserDetails(@Param("ids") List<Long> districtIds);

    PositiveCovidDetail findTopByUserIdOrderByIdentifiedDateDesc(Long userId);

    @Query("SELECT DISTINCT pc.user.address.gnDivision FROM PositiveCovidDetail pc")
    List<GramaNiladariDivision> getGndSpreadOfPositiveCovidDetail();

    PositiveCovidDetail findTopByUserAddressGnDivisionIdOrderByIdentifiedDateDesc(Long gndId);
}
