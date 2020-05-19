package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/4/2020
 * @Package lk.uom.fit.qms.dto.
 */

public class QuarantineUserResDto extends UserResponseDto {

    private AddressDto address;

    @JsonIgnore
    private CountryDto arrivedCountry;
    private LocalDate arrivalDate;
    private Long countryId;

    private Long provinceId;
    private Long districtId;
    private Long divisionId;
    private Long gndId;

    private List<QuarantineUserStatusDetail> userStatusDetails = new ArrayList<>();

    private QuarantineUserStatus status;

    public CountryDto getArrivedCountry() {
        return arrivedCountry;
    }

    public void setArrivedCountry(CountryDto arrivedCountry) {
        this.arrivedCountry = arrivedCountry;
    }

    public String getArrivalDate() {
        return arrivalDate == null ? null : arrivalDate.toString();
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(Long divisionId) {
        this.divisionId = divisionId;
    }

    public Long getGndId() {
        return gndId;
    }

    public void setGndId(Long gndId) {
        this.gndId = gndId;
    }

    public List<QuarantineUserStatusDetail> getUserStatusDetails() {
        return userStatusDetails;
    }

    public void setUserStatusDetails(List<QuarantineUserStatusDetail> userStatusDetails) {
        this.userStatusDetails = userStatusDetails;
    }

    public QuarantineUserStatus getStatus() {
        return status;
    }

    public void setStatus(QuarantineUserStatus status) {
        this.status = status;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
