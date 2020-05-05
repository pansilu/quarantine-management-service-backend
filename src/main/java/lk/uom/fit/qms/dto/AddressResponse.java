package lk.uom.fit.qms.dto;

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

public class AddressResponse {

    private Long id;
    private String policeArea;
    private String line;
    private String lat;
    private String lon;

    private GnDivisionResDto gnDivision;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoliceArea() {
        return policeArea;
    }

    public void setPoliceArea(String policeArea) {
        this.policeArea = policeArea;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
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

    public GnDivisionResDto getGnDivision() {
        return gnDivision;
    }

    public void setGnDivision(GnDivisionResDto gnDivision) {
        this.gnDivision = gnDivision;
    }
}
