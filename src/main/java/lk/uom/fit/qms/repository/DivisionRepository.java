package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Division;
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
public interface DivisionRepository extends JpaRepository<Division, Long> {

    /*@Query("SELECT DISTINCT d FROM Division d JOIN d.stations")
    List<Division> getAllUserDivisions();*/

    /*@Query("SELECT s.id FROM Division d JOIN d.stations s WHERE d.id IN :ids")
    List<Long> getStationIdsForGivenDivisions(@Param("ids") List<Long> divisionIdList);*/

    /*@Query("SELECT d.name AS name, d.id AS key, COUNT(q) AS total FROM Division d, QuarantineUser q LEFT JOIN d.stations s LEFT JOIN s.addressList a LEFT JOIN a.users u WHERE d.id IN :dIds AND s.id IN :ids AND q.id = u.id GROUP BY d.name, d.id ORDER BY d.name ASC")
    List<Object []> getQuserAgainstDivision(@Param("ids") List<Long> stationIds, @Param("dIds") List<Long> divisionIds);*/

    /*@Query("SELECT d.name AS name, d.id AS key, COUNT(q) AS total FROM Division d, QuarantineUser q LEFT JOIN d.stations s LEFT JOIN s.addressList a LEFT JOIN a.users u WHERE d.id IN :dIds AND s.id IN :ids AND q.id = u.id AND q.isCompleted = :isCompleted GROUP BY d.name, d.id ORDER BY d.name ASC")
    List<Object []> getQuserAgainstDivision(@Param("isCompleted") boolean isCompleted, @Param("ids") List<Long> stationIds, @Param("dIds") List<Long> divisionIds);*/

    List<Division> findDivisionsByIdIn(List<Long> divisionIdList);

    @Query("SELECT ds FROM Division ds WHERE LOWER(ds.name) LIKE LOWER(:pattern) OR LOWER(ds.code) LIKE LOWER(:pattern)")
    List<Division> filterBySearch(@Param("pattern") String pattern);
}
