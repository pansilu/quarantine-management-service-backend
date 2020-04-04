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
public class AdminFilterReqDto {

    List<Rank> ranks;
    List<Long> stationIds;

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public void setStationIds(List<Long> stationIds) {
        this.stationIds = stationIds;
    }
}
