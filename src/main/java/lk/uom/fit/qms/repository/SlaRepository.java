package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Regiment;
import lk.uom.fit.qms.model.SlaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlaRepository extends JpaRepository<SlaUser, Long> {
    List<SlaUser> findSlaUsersByRegiment(Regiment regiment);

    List<SlaUser> findSlaUsersByNameStartsWithOrNicStartingWith(String name,String nic);

    List<SlaUser> findSlaUsersByNameStartsWithOrNicStartsWithAndRegiment(String name, String nic, Regiment regiment);

    List<SlaUser> findSlaUsersByNameStartsWithAndNicStartsWith(String name, String nic);

    List<SlaUser> findSlaUsersByNameStartsWithAndNicStartsWithAndRegiment(String name, String nic, Regiment regiment);
}
