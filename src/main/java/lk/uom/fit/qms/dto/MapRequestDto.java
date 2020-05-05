package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.UserState;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

public class MapRequestDto {

    @NotNull(message = "Need to select at least one district")
    @NotEmpty(message = "Need to select at least one district")
    List<Long> districtIds;
    @NotNull(message = "Select user status")
    private UserState userState;

    public List<Long> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<Long> districtIds) {
        this.districtIds = districtIds;
    }

    public UserState getCovidCaseType() {
        return userState;
    }

    public void setCovidCaseType(UserState userState) {
        this.userState = userState;
    }
}
