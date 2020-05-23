package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Hospital findHospitalById(Long id);

    @Query("SELECT h FROM Hospital h WHERE LOWER(h.name) LIKE LOWER (:pattern)")
    List<Hospital> filterBySearch(@Param("pattern") String pattern);

    @Query("SELECT h.id FROM Hospital h JOIN h.hospitalMappingNames hmn WHERE hmn.name = :name")
    Long findHospitalForMappingName(@Param("name") String mappingName);
}
