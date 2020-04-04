package lk.uom.fit.qms.dto;

import java.time.LocalDate;

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
public class QuarantineMultiUserResDto extends UserResponseDto {

    private LocalDate reportDate;
    private boolean isInformedAuthority;
    private LocalDate informedDate;
    private LocalDate noticeAttachDate;
    private boolean isPatient;
    private boolean isCompleted;
    private String fileNo;
    private Short remainingDays;
    private Short totalPoints;
    private LocalDate lastUpdateDate;
    private boolean needToRemind = false;

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public boolean isInformedAuthority() {
        return isInformedAuthority;
    }

    public void setInformedAuthority(boolean informedAuthority) {
        isInformedAuthority = informedAuthority;
    }

    public LocalDate getInformedDate() {
        return informedDate;
    }

    public void setInformedDate(LocalDate informedDate) {
        this.informedDate = informedDate;
    }

    public LocalDate getNoticeAttachDate() {
        return noticeAttachDate;
    }

    public void setNoticeAttachDate(LocalDate noticeAttachDate) {
        this.noticeAttachDate = noticeAttachDate;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public void setPatient(boolean patient) {
        isPatient = patient;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public Short getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Short remainingDays) {
        this.remainingDays = remainingDays;
    }

    public Short getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Short totalPoints) {
        this.totalPoints = totalPoints;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public boolean isNeedToRemind() {
        return needToRemind;
    }

    public void setNeedToRemind(boolean needToRemind) {
        this.needToRemind = needToRemind;
    }
}
