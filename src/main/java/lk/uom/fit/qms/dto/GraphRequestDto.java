package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.GraphType;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/11/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GraphRequestDto {

    private List<Long> stationIds;
    private List<Long> divisionIds;
    @NotEmpty(message = "Select graph type to proceed")
    private GraphType graphType;
    private LocalDate startDate;
    private LocalDate endDate;

    public List<Long> getStationIds() {
        return stationIds;
    }

    public void setStationIds(List<Long> stationIds) {
        this.stationIds = stationIds;
    }

    public List<Long> getDivisionIds() {
        return divisionIds;
    }

    public void setDivisionIds(List<Long> divisionIds) {
        this.divisionIds = divisionIds;
    }

    public GraphType getGraphType() {
        return graphType;
    }

    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
