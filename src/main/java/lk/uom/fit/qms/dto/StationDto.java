package lk.uom.fit.qms.dto;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/2/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class StationDto {

    private Long id;
    private String name;
    private List<GramaSewaDivisionDto> gramaSewaDivisions;

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

    public List<GramaSewaDivisionDto> getGramaSewaDivisions() {
        return gramaSewaDivisions;
    }

    public void setGramaSewaDivisions(List<GramaSewaDivisionDto> gramaSewaDivisions) {
        this.gramaSewaDivisions = gramaSewaDivisions;
    }
}
