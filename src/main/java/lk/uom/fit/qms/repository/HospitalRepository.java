package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Hospital findHospitalById(Long id);
}
