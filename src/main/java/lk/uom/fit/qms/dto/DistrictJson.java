package lk.uom.fit.qms.dto;

import java.util.List;

public class DistrictJson {

    private Long id;
    private String name;
    private String code;

    private List<DivisionJson> divisions;

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

    public List<DivisionJson> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<DivisionJson> divisions) {
        this.divisions = divisions;
    }
}
