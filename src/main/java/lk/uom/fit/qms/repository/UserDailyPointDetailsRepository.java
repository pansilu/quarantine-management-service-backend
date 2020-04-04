package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.UserDailyPointDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/2/2020
 * @Package lk.uom.fit.qms.repository.
 */
@Repository
public interface UserDailyPointDetailsRepository extends JpaRepository<UserDailyPointDetails, Long> {

    @Query("SELECT COUNT(pd) > 0 FROM UserDailyPointDetails pd WHERE pd.recordDate =:currentDate AND pd.user.id = :id")
    boolean isUserUpdateForCurrentDate(@Param("id") Long userId, @Param("currentDate")LocalDate currentDate);
}
