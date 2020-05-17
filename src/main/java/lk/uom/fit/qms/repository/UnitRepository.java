package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Unit findByName(String name);
}
