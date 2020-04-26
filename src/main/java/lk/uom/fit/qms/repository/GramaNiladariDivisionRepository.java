package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.GramaNiladariDivision;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface GramaNiladariDivisionRepository extends JpaRepository<GramaNiladariDivision, Long> {

    GramaNiladariDivision findGramaNiladariDivisionById(Long id);
}
