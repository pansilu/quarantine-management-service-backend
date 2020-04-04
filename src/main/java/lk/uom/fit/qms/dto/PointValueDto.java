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
public class PointValueDto {

    private LocalDate date;
    private Map<String, Boolean> pointValues;

    public PointValueDto (LocalDate date, Map<String, Boolean> pointValues) {
        this.date = date;
        this.pointValues = pointValues;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Map<String, Boolean> getPointValues() {
        return pointValues;
    }

    public void setPointValues(Map<String, Boolean> pointValues) {
        this.pointValues = pointValues;
    }
}
