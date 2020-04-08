package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.QuarantineUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.repository.
 */
@Repository
public interface QuarantineUserRepository extends JpaRepository<QuarantineUser, Long> {

    QuarantineUser findQuarantineUserByUsername(String username);

    QuarantineUser findQuarantineUserBySecret(String secret);

    QuarantineUser findQuarantineUserById(Long id);

    @Query("SELECT COUNT(q) > 0 FROM QuarantineUser q WHERE q.secret = :secret AND q.id <> :id")
    boolean isSecretExistForAnotherUser(@Param("secret") String secret, @Param("id") Long userId);

    @Query("SELECT COUNT(q) > 0 FROM QuarantineUser q WHERE q.secret = :secret")
    boolean isSecretExistForAnotherUser(@Param("secret") String secret);

    @Query("SELECT DISTINCT u FROM QuarantineUser u WHERE u.address.station.id IN :ids")
    Page<QuarantineUser> findQuarantineUsersInStations(@Param("ids") List<Long> gramSewaIds, Pageable pageable);

    @Query("SELECT COUNT(u) > 0 FROM QuarantineUser u WHERE u.id = :id AND u.address.station.id IN :ids")
    boolean checkQuarantineUserExistForGivenIdInSelectedStations(@Param("id") Long userId, @Param("ids") List<Long> gramSewaIds);
}
