package lk.uom.fit.qms.repository;

import lk.uom.fit.qms.model.District;
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
public interface DistrictRepository extends JpaRepository<District, Long> {

    @Query("SELECT d FROM District d WHERE LOWER(d.name) LIKE LOWER(:pattern) OR LOWER(d.code) LIKE LOWER(:pattern) ORDER BY d.name")
    List<District> filterBySearch(@Param("pattern") String pattern);

    @Query("SELECT d FROM District d WHERE d.province.id = :id AND (LOWER(d.name) LIKE LOWER(:pattern) OR LOWER(d.code) LIKE LOWER(:pattern)) ORDER BY d.name")
    List<District> filterBySearch(@Param("id") Long provinceId, @Param("pattern") String pattern);

    List<District> findDistrictsByProvinceIdOrderByName(Long id);

    District findDistrictById(Long id);

    District findDistrictByName(String name);

    @Query("SELECT d FROM District d ORDER BY d.name")
    List<District> getOrderedDistrictList();
}
