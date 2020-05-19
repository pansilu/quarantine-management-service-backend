package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.GramaNiladariDivision;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
public interface GramaNiladariDivisionRepository extends JpaRepository<GramaNiladariDivision, Long> {

    GramaNiladariDivision findGramaNiladariDivisionById(Long id);

    @Query("SELECT gn FROM GramaNiladariDivision gn WHERE LOWER(gn.name) LIKE LOWER(:pattern) OR LOWER(gn.code) LIKE LOWER(:pattern) OR LOWER(gn.gndNo) LIKE LOWER(:pattern) ORDER BY gn.name")
    List<GramaNiladariDivision> filterBySearch(@Param("pattern") String pattern);

    GramaNiladariDivision findGramaNiladariDivisionByObjectId(String objectId);

    @Query("SELECT gn FROM GramaNiladariDivision gn WHERE gn.division.id = :id AND (LOWER(gn.name) LIKE LOWER(:pattern) OR LOWER(gn.code) LIKE LOWER(:pattern) OR LOWER(gn.gndNo) LIKE LOWER(:pattern)) ORDER BY gn.name")
    List<GramaNiladariDivision> filterBySearch(@Param("id") Long divisionId, @Param("pattern") String pattern);

    List<GramaNiladariDivision> findGramaNiladariDivisionsByDivisionIdOrderByName(Long id);

    List<GramaNiladariDivision> findGramaNiladariDivisionsByIdIn(Collection<Long> gndIds);
}
