package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.Rank;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/3/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class ReportUserResponseDto extends UserResponseDto{

    private String officeId;
    private Rank rank;
    private List<StationDto> stations;
    private boolean canCreateUser;
    private String showingName;

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

    public List<StationDto> getStations() {
        return stations;
    }

    public void setStations(List<StationDto> stations) {
        this.stations = stations;
    }

    public boolean isCanCreateUser() {
        return canCreateUser;
    }

    public void setCanCreateUser(boolean canCreateUser) {
        this.canCreateUser = canCreateUser;
    }

    public String getShowingName() {
        return showingName;
    }

    public void setShowingName(String showingName) {
        this.showingName = showingName;
    }
}
