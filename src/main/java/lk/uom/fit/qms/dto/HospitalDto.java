package lk.uom.fit.qms.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
public class HospitalDto {

    private Long id;
    @NotEmpty(message = "Hospital name need to be entered")
    @Size(max = 150, message = "Hospital name should need to be characters less than 150")
    private String name;
    @Pattern(regexp = "^(-?\\d+(\\.\\d+)?)$", message = "Invalid latitude pattern")
    @Size(max = 20, message = "Latitude should need to be characters less than 20")
    private String lat;
    @Pattern(regexp = "^(-?\\d+(\\.\\d+)?)$", message = "Invalid longitude pattern")
    @Size(max = 20, message = "Longitude should need to be characters less than 20")
    private String lon;

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
