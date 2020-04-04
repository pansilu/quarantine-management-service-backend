package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.ReportUser;
import lk.uom.fit.qms.util.enums.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s LEFT JOIN s.gramaSewaDivisions WHERE u.rank IN :ranks AND s.id IN :ids")
    List<ReportUser> findReportUsersByRanksAndStations(@Param("ranks") List<Rank> ranks, @Param("ids") List<Long> stationIds);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s LEFT JOIN s.gramaSewaDivisions WHERE s.id IN :ids")
    List<ReportUser> findReportUsersByGivenStations(@Param("ids") List<Long> stationIds);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s LEFT JOIN s.gramaSewaDivisions WHERE u.rank IN :ranks")
    List<ReportUser> findReportUsersByGivenRanks(@Param("ranks") List<Rank> ranks);

    @Query("SELECT DISTINCT u FROM ReportUser u LEFT JOIN u.stations s LEFT JOIN s.gramaSewaDivisions")
    List<ReportUser> findReportUsersWithStations();
}
