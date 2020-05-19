package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/27/2020
 * @Package lk.uom.fit.qms.repository
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query("SELECT p FROM Province p WHERE LOWER(p.name) LIKE LOWER(:pattern) OR LOWER(p.code) LIKE LOWER(:pattern) ORDER BY p.name")
    List<Province> filterBySearch(@Param("pattern") String pattern);

    @Query("SELECT p FROM Province p ORDER BY p.name")
    List<Province> findOrderProvinces();

    @Query("SELECT p FROM Province p LEFT JOIN p.districts d LEFT JOIN d.divisions ds LEFT JOIN ds.gnDivisions")
    List<Province> findAllProvinceDetails();
}
