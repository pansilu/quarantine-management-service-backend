package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Address;
import lk.uom.fit.qms.util.enums.LocationState;
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
            "AND LOWER(a.policeArea) LIKE LOWER(:police) ORDER BY LOWER(a.line)")
    List<Address> filterByLineAndPoliceArea(@Param("id") Long gndId, @Param("line") String line, @Param("police") String police);

    @Query("SELECT a FROM Address a WHERE a.gnDivision.id = :id AND LOWER(a.line) LIKE LOWER(:line) ORDER BY LOWER(a.line)")
    List<Address> filterByLine(@Param("id") Long gndId, @Param("line") String line);

    @Query("SELECT a FROM Address a WHERE a.gnDivision.id = :id AND LOWER(a.policeArea) LIKE LOWER(:police) ORDER BY LOWER(a.line)")
    List<Address> filterByPoliceArea(@Param("id") Long gndId, @Param("police") String police);

    @Query("SELECT a FROM Address a WHERE a.gnDivision.id = :id ORDER BY LOWER(a.line)")
    List<Address> filterByGnd(@Param("id") Long gndId);

    List<Address> findAddressesByGnDivisionId(Long id);

    @Query("SELECT a FROM Address a WHERE (SELECT COUNT(u) FROM User u WHERE u.address.id = a.id) = 0")
    List<Address> findIsolateAddresses();

    @Query("SELECT a FROM Address a WHERE LOWER(a.line) = LOWER(:line) AND a.gnDivision.id = :id AND a.locationState = :state")
    Address findExistingAddress(@Param("line") String line, @Param("id") Long gndId, @Param("state") LocationState state);
}
