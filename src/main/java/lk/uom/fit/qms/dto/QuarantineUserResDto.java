package lk.uom.fit.qms.dto;

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

public class QuarantineUserResDto extends UserResponseDto {

    private CountryDto arrivedCountry;
    private LocalDate arrivalDate;

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
}
