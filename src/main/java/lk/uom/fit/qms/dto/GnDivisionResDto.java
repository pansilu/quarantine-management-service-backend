package lk.uom.fit.qms.dto;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/27/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GnDivisionResDto {

    private Long id;
    private String name;
    private String code;
    private String gndNo;

    private DivisionResDto division;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGndNo() {
        return gndNo;
    }

    public void setGndNo(String gndNo) {
        this.gndNo = gndNo;
    }

    public DivisionResDto getDivision() {
        return division;
    }

    public void setDivision(DivisionResDto division) {
        this.division = division;
    }
}
