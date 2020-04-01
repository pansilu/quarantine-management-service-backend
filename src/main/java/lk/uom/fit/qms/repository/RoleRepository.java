package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Role;
import lk.uom.fit.qms.util.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(RoleType type);
}
