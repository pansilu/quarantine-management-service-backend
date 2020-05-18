package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Regiment;
import lk.uom.fit.qms.model.SlaUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlaRepository extends JpaRepository<SlaUser, Long> {

    SlaUser findSlaUserById(Long id);

    Page<SlaUser> findSlaUsersByRegiment(Regiment regiment, Pageable pageable);

    Page<SlaUser> findSlaUsersByNameStartsWithOrNicStartingWith(String name,String nic, Pageable pageable);

    Page<SlaUser> findSlaUsersByNameStartsWithOrNicStartsWithAndRegiment(String name, String nic, Regiment regiment, Pageable pageable);

    Page<SlaUser> findSlaUsersByNameStartsWithAndNicStartsWith(String name, String nic, Pageable pageable);

    Page<SlaUser> findSlaUsersByNameStartsWithAndNicStartsWithAndRegiment(String name, String nic, Regiment regiment, Pageable pageable);
}
