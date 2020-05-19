package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.QuarantineUserStatus;

import java.time.LocalDate;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/5/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class QuarantineUserStatusDetailResponse {

    private Long id;
    private QuarantineUserStatus type;
    private String caseNum;
    private String parentCaseNum;
    private String description;
    private String startDate;
    private String endDate;
    private HospitalDto hospital;
    private QuarantineCenterDto quarantineCenter;

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate != null ? startDate.toString() : null;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate != null ? endDate.toString() : null;
    }

    public HospitalDto getHospital() {
        return hospital;
    }

    public void setHospital(HospitalDto hospital) {
        this.hospital = hospital;
    }

    public QuarantineCenterDto getQuarantineCenter() {
        return quarantineCenter;
    }

    public void setQuarantineCenter(QuarantineCenterDto quarantineCenter) {
        this.quarantineCenter = quarantineCenter;
    }
}
