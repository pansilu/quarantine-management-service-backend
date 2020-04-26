package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lk.uom.fit.qms.config.LocalDateDeserializer;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/26/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class QuarantineUserStatusDetail {

    private Long id;
    @NotNull(message = "User status type need to be entered")
    private QuarantineUserStatus type;
    @Size(max = 50, message = "Case Number should need to be characters less than 50")
    private String caseNum;
    private String parentCaseNum;
    @Size(max = 2000, message = "Description should need to be characters less than 2000")
    private String description;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;
    private Long hospitalId;
    private Long qCenterId;
    private boolean isDelete = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuarantineUserStatus getType() {
        return type;
    }

    public void setType(QuarantineUserStatus type) {
        this.type = type;
    }

    public String getCaseNum() {
        return caseNum;
    }

    public void setCaseNum(String caseNum) {
        this.caseNum = caseNum;
    }

    public String getParentCaseNum() {
        return parentCaseNum;
    }

    public void setParentCaseNum(String parentCaseNum) {
        this.parentCaseNum = parentCaseNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Long getqCenterId() {
        return qCenterId;
    }

    public void setqCenterId(Long qCenterId) {
        this.qCenterId = qCenterId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "QuarantineUserStatusDetail{" +
                "id=" + id +
                ", type=" + type +
                ", caseNum='" + caseNum + '\'' +
                ", parentCaseNum='" + parentCaseNum + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", hospitalId=" + hospitalId +
                ", qCenterId=" + qCenterId +
                ", isDelete=" + isDelete +
                '}';
    }
}
