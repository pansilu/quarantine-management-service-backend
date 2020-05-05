package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.DeceasedDetail;

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


    @Query("SELECT SUM(CASE WHEN dd.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN dd.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN dd.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN dd.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN dd.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN dd.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN dd.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM DeceasedDetail dd")
    List<Long[]> getDeceasedCaseCountAgainstAgeGroup();

    @Query("SELECT SUM(CASE WHEN dd.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN dd.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN dd.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN dd.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN dd.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN dd.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN dd.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM DeceasedDetail dd WHERE dd.user.address.gnDivision.id = :id")
    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndGnd(@Param("id") Long gndId);

    @Query("SELECT SUM(CASE WHEN dd.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN dd.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN dd.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN dd.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN dd.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN dd.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN dd.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM DeceasedDetail dd WHERE dd.user.address.gnDivision.division.id = :id")
    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndDivision(@Param("id") Long divisionId);

    @Query("SELECT SUM(CASE WHEN dd.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN dd.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN dd.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN dd.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN dd.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN dd.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN dd.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM DeceasedDetail dd WHERE dd.user.address.gnDivision.division.district.id = :id")
    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndDistrict(@Param("id") Long districtId);

    @Query("SELECT SUM(CASE WHEN dd.user.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN dd.user.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN dd.user.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN dd.user.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN dd.user.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN dd.user.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN dd.user.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM DeceasedDetail dd WHERE dd.user.address.gnDivision.division.district.province.id = :id")
    List<Long[]> getDeceasedCaseCountAgainstAgeGroupAndProvince(@Param("id") Long provinceId);

    @Query("SELECT dd.user.address.gnDivision.division.district.name AS name, COUNT(dd) AS toatal FROM DeceasedDetail dd" +
            " WHERE dd.deceasedDate = :givenDate AND dd.user.address.gnDivision.division.district.id IN :ids GROUP BY dd.user.address.gnDivision.division.district.name")
    List<Object[]> getNewDeceasedCasesPerDateForGivenDistricts(@Param("ids") List<Long> districtIds, @Param("givenDate") LocalDate date);

    @Query("SELECT dd.user FROM DeceasedDetail dd WHERE dd.user.address.gnDivision.division.district.id IN :ids")
    List<QuarantineUser> getAllDeceasedUserDetails(@Param("ids") List<Long> districtIds);
}
