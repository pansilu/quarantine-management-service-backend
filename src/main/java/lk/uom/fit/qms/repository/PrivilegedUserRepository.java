package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.PrivilegedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegedUserRepository extends JpaRepository<PrivilegedUser, Long> {

    PrivilegedUser findPrivilegedUserByUsername(String username);

    PrivilegedUser findPrivilegedUserById(Long id);

    PrivilegedUser findPrivilegedUserByNic(String nic);

    @Modifying
    @Query(value="UPDATE PrivilegedUser c SET c.isDeleted = true WHERE c.id = :id")
    void deActivatePrivilegedUser(@Param("id") Long userId);
}
