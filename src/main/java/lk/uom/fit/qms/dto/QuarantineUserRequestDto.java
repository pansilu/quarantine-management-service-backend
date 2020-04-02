package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lk.uom.fit.qms.config.LocalDateDeserializer;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class QuarantineUserRequestDto extends UserRequestDto {

    boolean isAppEnable;
    String secret;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    LocalDate arrivalDate;
    Long countryId;
    Long gramaSewaDivisionId;
    List<Long> inspectorIds;
    private GuardianDto guardianDetails;
    private LocalDate reportedDate;
    private LocalDate informedDate;
    private String fileNo;

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocalDate getInformedDate() {
        return informedDate;
    }

    public void setInformedDate(LocalDate informedDate) {
        this.informedDate = informedDate;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public boolean isAppEnable() {
        return isAppEnable;
    }

    public void setAppEnable(boolean appEnable) {
        isAppEnable = appEnable;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public List<Long> getInspectorIds() {
        return inspectorIds;
    }

    public void setInspectorIds(List<Long> inspectorIds) {
        this.inspectorIds = inspectorIds;
    }

    public Long getGramaSewaDivisionId() {
        return gramaSewaDivisionId;
    }

    public void setGramaSewaDivisionId(Long gramaSewaDivisionId) {
        this.gramaSewaDivisionId = gramaSewaDivisionId;
    }

    public GuardianDto getGuardianDetails() {
        return guardianDetails;
    }

    public void setGuardianDetails(GuardianDto guardianDetails) {
        this.guardianDetails = guardianDetails;
    }
}
