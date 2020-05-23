package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.repository.
 */
@Repository
public interface QuarantineUserRepository extends JpaRepository<QuarantineUser, Long> {

    QuarantineUser findQuarantineUserByUsername(String username);

    @Query("SELECT u FROM QuarantineUser u WHERE u.mobile = :secret")
    QuarantineUser findAppEnableQuarantineUserBySecret(@Param("secret") String secret);

    QuarantineUser findQuarantineUserById(Long id);

    /*@Query("SELECT COUNT(q) > 0 FROM QuarantineUser q WHERE q.secret = :secret AND q.id <> :id")
    boolean isSecretExistForAnotherUser(@Param("secret") String secret, @Param("id") Long userId);

    @Query("SELECT COUNT(q) > 0 FROM QuarantineUser q WHERE q.secret = :secret")
    boolean isSecretExistForAnotherUser(@Param("secret") String secret);*/

    /*@Query("SELECT DISTINCT u FROM QuarantineUser u WHERE u.address.station.id IN :ids")
    Page<QuarantineUser> findQuarantineUsersInStations(@Param("ids") List<Long> stationIds, Pageable pageable);*/

    /*@Query("SELECT COUNT(u) > 0 FROM QuarantineUser u WHERE u.id = :id AND u.address.station.id IN :ids")
    boolean checkQuarantineUserExistForGivenIdInSelectedStations(@Param("id") Long userId, @Param("ids") List<Long> stationIds);*/

    /*@Query("SELECT DISTINCT u FROM QuarantineUser u WHERE u.address.station.id IN :ids AND (LOWER(u.name) LIKE LOWER(:pattern)" +
            " OR LOWER(u.address.line) LIKE LOWER(:pattern) OR LOWER(u.address.station.name) LIKE LOWER(:pattern)" +
            " OR LOWER(u.nic) LIKE LOWER(:pattern) OR LOWER(u.passportNo) LIKE LOWER(:pattern))")
    Page<QuarantineUser> findQuarantineUsersInStations(@Param("ids") List<Long> stationIds, @Param("pattern") String pattern, Pageable pageable);*/

    @Query("SELECT DISTINCT u FROM QuarantineUser u WHERE LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.address.line) LIKE LOWER(:pattern)" +
            " OR LOWER(u.address.gnDivision.name) LIKE LOWER(:pattern) OR LOWER(u.nic) LIKE LOWER(:pattern) OR LOWER(u.passportNo) LIKE LOWER(:pattern)")
    Page<QuarantineUser> findQuarantineUsersByPattern(@Param("pattern") String pattern, Pageable pageable);

    /*@Query("SELECT COUNT(u) > 0 FROM QuarantineUser u WHERE u.id = :id AND u.isCompleted = true")
    boolean checkUserQuarantinePeriodOver(@Param("id") Long userId);*/

    /*@Query("SELECT u FROM QuarantineUser u WHERE u.isCompleted = false")
    List<QuarantineUser> findQuarantinePeriodNotCompletedUsers();

    @Query("SELECT u.isAppEnable FROM QuarantineUser u WHERE u.id = :id")
    boolean isMobileAppEnable(@Param("id") Long userId);*/


    // ***************************************************************************************************************

    /*@Query("SELECT SUM(CASE WHEN u.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN u.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN u.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN u.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN u.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN u.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN u.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM QuarantineUser u WHERE u.address.station.id IN :ids")
    List<Long[]> getQuserCountAgainstAgeGroup(@Param("ids") List<Long> stationIds);

    @Query("SELECT SUM(CASE WHEN u.age < 18 THEN 1 ELSE 0 END) AS grp1," +
            "SUM(CASE WHEN u.age BETWEEN 18 AND 24 THEN 1 ELSE 0 END) AS grp2," +
            "SUM(CASE WHEN u.age BETWEEN 25 AND 34 THEN 1 ELSE 0 END) AS grp3," +
            "SUM(CASE WHEN u.age BETWEEN 35 AND 50 THEN 1 ELSE 0 END) AS grp4," +
            "SUM(CASE WHEN u.age BETWEEN 51 AND 65 THEN 1 ELSE 0 END) AS grp5," +
            "SUM(CASE WHEN u.age > 65 THEN 1 ELSE 0 END) AS grp6," +
            "SUM(CASE WHEN u.age IS NULL THEN 1 ELSE 0 END) AS grp7 FROM QuarantineUser u WHERE u.address.station.id IN :ids AND u.isCompleted = :isCompleted")
    List<Long[]> getQuserCountAgainstAgeGroup(@Param("isCompleted") boolean isCompleted, @Param("ids") List<Long> stationIds);

    @Query("SELECT COUNT(u) FROM QuarantineUser u WHERE u.address.station.id IN :ids AND u.reportDate = :reportDate")
    Long getQuserCountAgainstReportedDate(@Param("ids") List<Long> stationIds, @Param("reportDate") LocalDate reportDate);

    @Query("SELECT COUNT(u) FROM QuarantineUser u WHERE u.address.station.id IN :ids AND u.completedDate = :completeDate")
    Long getQuserCountAgainstCompletedDate(@Param("ids") List<Long> stationIds, @Param("completeDate") LocalDate completeDate);*/

    @Query("SELECT DISTINCT hq FROM QuarantineUser u LEFT JOIN u.hqDetails hq WHERE u.id = :id AND hq.isDeleted = false")
    List<HomeQuarantineDetail> getUserHomeQuarantineDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT rq FROM QuarantineUser u LEFT JOIN u.rqDetails rq WHERE u.id = :id AND rq.isDeleted = false")
    List<RemoteQuarantineDetail> getUserRemoteQuarantineDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT sc FROM QuarantineUser u LEFT JOIN u.scDetails sc WHERE u.id = :id AND sc.isDeleted = false")
    List<SuspectCovidDetail> getUserSuspectCovidDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT pc FROM QuarantineUser u LEFT JOIN u.pcDetails pc WHERE u.id = :id AND pc.isDeleted = false")
    List<PositiveCovidDetail> getUserPositiveCovidDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT d FROM QuarantineUser u LEFT JOIN u.deceasedDetails d WHERE u.id = :id AND d.isDeleted = false")
    List<DeceasedDetail> getUserDeceasedDetails(@Param("id") Long id);

    @Query("SELECT u FROM QuarantineUser u LEFT JOIN FETCH u.hqDetails WHERE u.status = 'HOUSE_QUARANTINE'")
    List<QuarantineUser> findQuarantineUsersWithHqStatus();

    @Query("SELECT u FROM QuarantineUser u LEFT JOIN FETCH u.rqDetails WHERE u.status = 'REMOTE_QUARANTINE'")
    List<QuarantineUser> findQuarantineUsersWithRqStatus();

    @Query("SELECT DISTINCT u FROM QuarantineUser u WHERE u.status = :status AND (LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.address.line) LIKE LOWER(:pattern)" +
            " OR LOWER(u.address.gnDivision.name) LIKE LOWER(:pattern) OR LOWER(u.nic) LIKE LOWER(:pattern) OR LOWER(u.passportNo) LIKE LOWER(:pattern))")
    Page<QuarantineUser> findQuarantineUsersByPatternAndStatus(@Param("pattern") String pattern, Pageable pageable, @Param("status") QuarantineUserStatus status);

    Page<QuarantineUser> findQuarantineUsersByStatus(QuarantineUserStatus status, Pageable pageable);

    QuarantineUser findQuarantineUserByExternalKey(String externalKey);
}
