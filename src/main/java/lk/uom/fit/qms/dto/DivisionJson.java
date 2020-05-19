package lk.uom.fit.qms.dto;

import java.util.List;

public class DivisionJson {

    private Long id;
    private String name;
    private String code;
    private List<GndJson> gnDivisions;

    public List<GndJson> getGnDivisions() {
        return gnDivisions;
    }

    public void setGnDivisions(List<GndJson> gnDivisions) {
        this.gnDivisions = gnDivisions;
    }

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
}
