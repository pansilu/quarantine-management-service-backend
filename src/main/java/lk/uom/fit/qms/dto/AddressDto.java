package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lk.uom.fit.qms.util.enums.LocationState;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
public class AddressDto {

    private Long id;

    @NotEmpty(message = "Address line need to be entered")
    @Size(max = 300, message = "Address line should need to be characters less than 300")
    private String line;
    @Size(max = 100, message = "Police Area name should need to be characters less than 100")
    private String policeArea;
    @Pattern(regexp = "^(-?\\d+(\\.\\d+)?)$", message = "Invalid latitude pattern")
    @Size(max = 20, message = "Latitude should need to be characters less than 20")
    private String lat;
    @Pattern(regexp = "^(-?\\d+(\\.\\d+)?)$", message = "Invalid longitude pattern")
    @Size(max = 20, message = "Longitude should need to be characters less than 20")
    private String lon;
    @NotNull(message = "Need to select GN Division to proceed")
    private Long gndId;

    @JsonIgnore
    private LocationState locationState = LocationState.OK;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getPoliceArea() {
        return policeArea;
    }

    public void setPoliceArea(String policeArea) {
        this.policeArea = policeArea;
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

    public Long getGndId() {
        return gndId;
    }

    public void setGndId(Long gndId) {
        this.gndId = gndId;
    }

    public LocationState getLocationState() {
        return locationState;
    }

    public void setLocationState(LocationState locationState) {
        this.locationState = locationState;
    }
}
