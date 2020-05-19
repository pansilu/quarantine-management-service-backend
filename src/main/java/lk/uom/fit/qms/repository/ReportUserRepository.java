package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.ReportUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.repository.
 */
@Repository
public interface ReportUserRepository extends JpaRepository<ReportUser, Long> {

    ReportUser findReportUserById(Long id);

    /*@Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE u.rank IN :ranks AND s.id IN :ids")
    List<ReportUser> findReportUsersByRanksAndStations(@Param("ranks") List<Rank> ranks, @Param("ids") List<Long> stationIds);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE s.id IN :ids")
    List<ReportUser> findReportUsersByGivenStations(@Param("ids") List<Long> stationIds);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE u.rank IN :ranks")
    List<ReportUser> findReportUsersByGivenRanks(@Param("ranks") List<Rank> ranks);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s")
    List<ReportUser> findReportUsersWithStations();

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s")
    Page<ReportUser> findReportUsersWithStations(Pageable pageable);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE u.addedBy.id = :id")
    Page<ReportUser> findAddedReportUsersWithStations(@Param("id") Long id, Pageable pageable);*/

    @Query("SELECT COUNT(u) > 0 FROM ReportUser u WHERE u.id = :id AND u.addedBy.id = :addedUserId")
    boolean checkReportUserExistForGivenIdAndAddedUser(@Param("id") Long userId, @Param("addedUserId") Long addedUserId);

    @Query("SELECT COUNT(u) > 0 FROM ReportUser u WHERE u.id <> :id AND LOWER(u.officeId) = LOWER(:officeId)")
    boolean checkReportUserByOfficeId(@Param("officeId") String officeId, @Param("id") Long userId);

    @Query("SELECT COUNT(u) > 0 FROM ReportUser u WHERE LOWER(u.officeId) = LOWER(:officeId)")
    boolean checkReportUserByOfficeId(@Param("officeId") String officeId);

    @Query("SELECT DISTINCT u FROM ReportUser u WHERE LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.officeId) LIKE LOWER(:pattern)")
    Page<ReportUser> findReportUsersWithStations(@Param("pattern") String pattern, Pageable pageable);

    /*@Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE u.addedBy.id = :id AND " +
            "(LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.officeId) LIKE LOWER(:pattern) OR LOWER(s.name) LIKE LOWER(:pattern)) OR LOWER(u.rank) LIKE LOWER(:pattern)")
    Page<ReportUser> findAddedReportUsersWithStations(@Param("pattern") String pattern, @Param("id") Long id, Pageable pageable);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE u.rank IN :ranks AND s.id IN :ids " +
            "AND (LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.officeId) LIKE LOWER(:pattern))")
    List<ReportUser> findReportUsersByRanksAndStations(@Param("pattern") String pattern, @Param("ranks") List<Rank> ranks, @Param("ids") List<Long> stationIds);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE s.id IN :ids" +
            " AND (LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.officeId) LIKE LOWER(:pattern))")
    List<ReportUser> findReportUsersByGivenStations(@Param("pattern") String pattern, @Param("ids") List<Long> stationIds);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE u.rank IN :ranks" +
            " AND (LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.officeId) LIKE LOWER(:pattern))")
    List<ReportUser> findReportUsersByGivenRanks(@Param("pattern") String pattern, @Param("ranks") List<Rank> ranks);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s WHERE LOWER(u.name) LIKE LOWER(:pattern) OR LOWER(u.officeId) LIKE LOWER(:pattern)")
    List<ReportUser> findReportUsersWithStations(@Param("pattern") String pattern);*/
}
