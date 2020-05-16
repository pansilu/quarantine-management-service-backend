package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.PrivilegedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegedUserRepository extends JpaRepository<PrivilegedUser, Long> {
    PrivilegedUser findPrivilegedUserById(Long id);
}
