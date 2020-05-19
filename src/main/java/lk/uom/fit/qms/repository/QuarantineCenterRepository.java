package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.QuarantineCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
public interface QuarantineCenterRepository extends JpaRepository<QuarantineCenter, Long> {

    QuarantineCenter findQuarantineCenterById(Long id);

    @Query("SELECT qc FROM QuarantineCenter qc WHERE LOWER(qc.name) LIKE LOWER(:pattern)")
    List<QuarantineCenter> filterBySearch(@Param("pattern") String pattern);
}
