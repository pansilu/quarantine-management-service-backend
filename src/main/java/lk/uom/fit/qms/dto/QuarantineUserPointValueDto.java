package lk.uom.fit.qms.dto;

import java.time.LocalDate;
import java.util.Map;

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
    private Map<LocalDate, Map<String, Boolean>> pointValues;

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

    public Map<LocalDate, Map<String, Boolean>> getPointValues() {
        return pointValues;
    }

    public void setPointValues(Map<LocalDate, Map<String, Boolean>> pointValues) {
        this.pointValues = pointValues;
    }
}
