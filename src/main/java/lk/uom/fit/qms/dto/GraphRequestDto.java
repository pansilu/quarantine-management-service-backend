package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lk.uom.fit.qms.config.LocalDateDeserializer;
import lk.uom.fit.qms.util.enums.GraphType;
import lk.uom.fit.qms.util.enums.QuserType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
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

public class GraphRequestDto implements Serializable {

    private static final long serialVersionUID = -6874786041720095511L;

    private List<Long> stationIds;
    private List<Long> divisionIds;
    @NotNull(message = "Select graph type to proceed")
    private GraphType graphType;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
    private QuserType quserType = QuserType.BOTH;

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

    public QuserType getQuserType() {
        return quserType;
    }

    public void setQuserType(QuserType quserType) {
        this.quserType = quserType;
    }

    @Override
    public String toString() {
        return "GraphRequestDto{" +
                "stationIds=" + stationIds +
                ", divisionIds=" + divisionIds +
                ", graphType=" + graphType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", quserType=" + quserType +
                '}';
    }
}
