package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.QuarantineUserStatus;

import java.time.LocalDate;

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
public class QuarantineMultiUserResDto extends UserResponseDto {

    private AddressDto address;
    private LocalDate arrivalDate;
    private CountryDto arrivedCountry;
    private QuarantineUserStatus status;
    private Short remainingDays;

    public String getReportDate() {
        return arrivalDate == null ? null : arrivalDate.toString();
    }

    public void setReportDate(LocalDate reportDate) {
        this.arrivalDate = reportDate;
    }

    public QuarantineUserStatus getStatus() {
        return status;
    }

    public void setStatus(QuarantineUserStatus status) {
        this.status = status;
    }

    public CountryDto getArrivedCountry() {
        return arrivedCountry;
    }

    public void setArrivedCountry(CountryDto arrivedCountry) {
        this.arrivedCountry = arrivedCountry;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public Short getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Short remainingDays) {
        this.remainingDays = remainingDays;
    }
}
