package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsername(String username);

    User findUserById(Long id);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.mobile = :mobile AND u.id <> :id")
    boolean isUserExistsWithMobileNum(@Param("mobile") String mobile, @Param("id") Long currentUserId);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.mobile = :mobile")
    boolean isUserExistsWithMobileNum(@Param("mobile") String mobile);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.nic) = LOWER(:nic) AND u.id <> :id")
    boolean isUserExistsWithNic(@Param("nic") String nic, @Param("id") Long currentUserId);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.nic) = LOWER(:nic)")
    boolean isUserExistsWithNic(@Param("nic") String nic);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.passportNo) = LOWER(:passport) AND u.id <> :id")
    boolean isUserExistsWithPassport(@Param("passport") String passport, @Param("id") Long currentUserId);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.passportNo) = LOWER(:passport)")
    boolean isUserExistsWithPassport(@Param("passport") String passport);
}
