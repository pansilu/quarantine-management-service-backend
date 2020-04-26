package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lk.uom.fit.qms.config.LocalDateDeserializer;

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

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate arrivalDate;
    private Long countryId;

    @NotNull(message = "Need to add at least current/previous status of the user")
    @NotEmpty(message = "Need to add at least current/previous status of the user")
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
}
