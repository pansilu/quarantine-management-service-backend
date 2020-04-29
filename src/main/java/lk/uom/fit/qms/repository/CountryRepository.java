package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Country;
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
public interface CountryRepository extends JpaRepository<Country, Long> {

    Country findCountryById(Long id);

    @Query("SELECT c FROM Country c WHERE LOWER(c.name) LIKE LOWER(:pattern) ORDER BY c.name")
    List<Country> filterBySearch(@Param("pattern") String pattern);

    @Query("SELECT c FROM Country c ORDER BY c.name")
    List<Country> getOrderedCountryList();
}
