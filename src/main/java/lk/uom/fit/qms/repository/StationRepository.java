package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Station;
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
 * @created on 4/2/2020
 * @Package lk.uom.fit.qms.repository.
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("SELECT s FROM Station s WHERE s.id IN :ids")
    List<Station> findStationsByGivenIdList(@Param("ids") List<Long> idList);
}
