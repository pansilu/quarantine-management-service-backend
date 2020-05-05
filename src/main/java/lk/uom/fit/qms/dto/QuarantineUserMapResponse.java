package lk.uom.fit.qms.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/5/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class QuarantineUserMapResponse extends UserResponseDto {

    private CountryDto arrivedCountry;
    private String arrivalDate;

    private AddressResponse address;

    private List<QuarantineUserStatusDetailResponse> userStatusDetails = new ArrayList<>();

    public CountryDto getArrivedCountry() {
        return arrivedCountry;
    }

    public void setArrivedCountry(CountryDto arrivedCountry) {
        this.arrivedCountry = arrivedCountry;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate != null ? arrivalDate.toString() : null;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }

    public List<QuarantineUserStatusDetailResponse> getUserStatusDetails() {
        return userStatusDetails;
    }

    public void setUserStatusDetails(List<QuarantineUserStatusDetailResponse> userStatusDetails) {
        this.userStatusDetails = userStatusDetails;
    }
}
