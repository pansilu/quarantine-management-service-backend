package lk.uom.fit.qms.dto;

import java.time.LocalDate;
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

public class QuarantineUserResDto extends UserResponseDto {

    private boolean isAppEnable;
    private String secret;
    private CountryDto arrivedCountry;
    private LocalDate arrivalDate;
    private boolean isInformedAuthority;
    private LocalDate informedDate;
    private LocalDate reportDate;
    private GramaSewaDivisionDto gramaSewaDivision;
    private AddressDto address;
    private GuardianDto guardianDetails;
    private List<ReportUserResponseDto> inspectorDetails;
    private LocalDate noticeAttachDate;
    private String fileNo;
    private LocalDate dischargedDate;
    private LocalDate admittedDate;
    private LocalDate confirmedDate;
    private String otherFacts;
    private HospitalDto admitHospital;
    private HospitalDto confirmedHospital;

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

    public CountryDto getArrivedCountry() {
        return arrivedCountry;
    }

    public void setArrivedCountry(CountryDto arrivedCountry) {
        this.arrivedCountry = arrivedCountry;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public GramaSewaDivisionDto getGramaSewaDivision() {
        return gramaSewaDivision;
    }

    public void setGramaSewaDivision(GramaSewaDivisionDto gramaSewaDivision) {
        this.gramaSewaDivision = gramaSewaDivision;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public GuardianDto getGuardianDetails() {
        return guardianDetails;
    }

    public void setGuardianDetails(GuardianDto guardianDetails) {
        this.guardianDetails = guardianDetails;
    }

    public List<ReportUserResponseDto> getInspectorDetails() {
        return inspectorDetails;
    }

    public void setInspectorDetails(List<ReportUserResponseDto> inspectorDetails) {
        this.inspectorDetails = inspectorDetails;
    }

    public LocalDate getNoticeAttachDate() {
        return noticeAttachDate;
    }

    public void setNoticeAttachDate(LocalDate noticeAttachDate) {
        this.noticeAttachDate = noticeAttachDate;
    }

    public String getFileNo() {
        return fileNo;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public LocalDate getDischargedDate() {
        return dischargedDate;
    }

    public void setDischargedDate(LocalDate dischargedDate) {
        this.dischargedDate = dischargedDate;
    }

    public LocalDate getAdmittedDate() {
        return admittedDate;
    }

    public void setAdmittedDate(LocalDate admittedDate) {
        this.admittedDate = admittedDate;
    }

    public LocalDate getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(LocalDate confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public String getOtherFacts() {
        return otherFacts;
    }

    public void setOtherFacts(String otherFacts) {
        this.otherFacts = otherFacts;
    }

    public HospitalDto getAdmitHospital() {
        return admitHospital;
    }

    public void setAdmitHospital(HospitalDto admitHospital) {
        this.admitHospital = admitHospital;
    }

    public HospitalDto getConfirmedHospital() {
        return confirmedHospital;
    }

    public void setConfirmedHospital(HospitalDto confirmedHospital) {
        this.confirmedHospital = confirmedHospital;
    }
}
