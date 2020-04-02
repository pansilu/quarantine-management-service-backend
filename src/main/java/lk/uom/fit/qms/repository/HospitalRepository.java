package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Address;
import lk.uom.fit.qms.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
