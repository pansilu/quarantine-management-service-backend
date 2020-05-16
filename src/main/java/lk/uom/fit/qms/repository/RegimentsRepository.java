package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Regiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegimentsRepository extends JpaRepository<Regiment, Long> {
    Regiment findRegimentByCode(String code);
    Regiment findRegimentByName(String name);
}
