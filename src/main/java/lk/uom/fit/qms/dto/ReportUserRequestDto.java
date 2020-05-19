package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.Rank;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/2/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class ReportUserRequestDto extends UserRequestDto {

    @NotEmpty(message = "Please enter officeId")
    @Size(max = 50, message = "Office Id should need to be characters less than 50")
    private String officeId;

    private boolean canCreateUser;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public boolean isCanCreateUser() {
        return canCreateUser;
    }

    public void setCanCreateUser(boolean canCreateUser) {
        this.canCreateUser = canCreateUser;
    }
}
