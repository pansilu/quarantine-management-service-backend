package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
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
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a FROM Address a WHERE LOWER(a.line) LIKE LOWER(:pattern)")
    List<Address> filterBySearch(@Param("pattern") String pattern);

    @Query("SELECT a FROM Address a WHERE a.gnDivision.id = :id AND LOWER(a.line) LIKE LOWER(:line) " +
            "AND LOWER(a.policeArea) LIKE LOWER(:police) AND LOWER(a.town) LIKE LOWER(:town) AND LOWER(a.village) LIKE LOWER(:village) ORDER BY LOWER(a.line)")
    List<Address> filterBySearch(@Param("id") Long gndId, @Param("line") String line, @Param("police") String police, @Param("town") String town, @Param("village") String village);

    List<Address> findAddressesByGnDivisionId(Long id);
}
