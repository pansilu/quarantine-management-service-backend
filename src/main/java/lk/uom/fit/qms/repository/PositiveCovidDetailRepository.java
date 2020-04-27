package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.PositiveCovidDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
