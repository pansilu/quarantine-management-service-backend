package lk.uom.fit.qms.dto;

import java.util.List;

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

public class QuarantineUserPointValueDto {

    private Long userId;
    private String name;
    private List<PointValueDto> dailyUpdates;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PointValueDto> getDailyUpdates() {
        return dailyUpdates;
    }

    public void setDailyUpdates(List<PointValueDto> dailyUpdates) {
        this.dailyUpdates = dailyUpdates;
    }
}
