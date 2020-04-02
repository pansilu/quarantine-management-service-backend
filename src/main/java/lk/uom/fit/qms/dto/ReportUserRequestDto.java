package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.Rank;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    private String officeId;
    private Rank rank;
    @NotNull(message = "Need to select at least one station")
    @NotEmpty(message = "Need to select at least one station")
    private List<Long> stationIdList;
    private boolean canCreateUser;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public List<Long> getStationIdList() {
        return stationIdList;
    }

    public void setStationIdList(List<Long> stationIdList) {
        this.stationIdList = stationIdList;
    }

    public boolean isCanCreateUser() {
        return canCreateUser;
    }

    public void setCanCreateUser(boolean canCreateUser) {
        this.canCreateUser = canCreateUser;
    }
}
