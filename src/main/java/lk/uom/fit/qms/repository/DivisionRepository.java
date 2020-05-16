package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("SELECT DISTINCT d FROM Division d JOIN d.stations")
    List<Division> getAllUserDivisions();
}
