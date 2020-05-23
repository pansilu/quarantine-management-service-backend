package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lk.uom.fit.qms.config.LocalDateDeserializer;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class QuarantineUserRequestDto extends UserRequestDto {

    @NotNull(message = "Need to entered address details for quarantine person/patient")
    @Valid
    private AddressDto address;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate arrivalDate;
    private Long countryId;

    @JsonIgnore
    private String externalKey;
    @JsonIgnore
    private boolean isExternallyAdded = false;

    @NotNull(message = "Need to add at least current/previous status of the user")
    @NotEmpty(message = "Need to add at least current/previous status of the user")
    @Valid
    private List<QuarantineUserStatusDetail> userStatusDetails;

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public List<QuarantineUserStatusDetail> getUserStatusDetails() {
        return userStatusDetails;
    }

    public void setUserStatusDetails(List<QuarantineUserStatusDetail> userStatusDetails) {
        this.userStatusDetails = userStatusDetails;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public boolean isExternallyAdded() {
        return isExternallyAdded;
    }

    public void setExternallyAdded(boolean externallyAdded) {
        isExternallyAdded = externallyAdded;
    }
}
