package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/28/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GndProperties {

    private String name;
    private String styleUrl;
    private String styleHash;
    private String description;
    private String stroke;
    private String fill;
    @JsonProperty("stroke-opacity")
    private BigDecimal strokeOpacity;
    @JsonProperty("stroke-width")
    private BigDecimal strokeWidth;
    @JsonProperty("fill-opacity")
    private BigDecimal fillOpacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyleUrl() {
        return styleUrl;
    }

    public void setStyleUrl(String styleUrl) {
        this.styleUrl = styleUrl;
    }

    public String getStyleHash() {
        return styleHash;
    }

    public void setStyleHash(String styleHash) {
        this.styleHash = styleHash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public BigDecimal getStrokeOpacity() {
        return strokeOpacity;
    }

    public void setStrokeOpacity(BigDecimal strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
    }

    public BigDecimal getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(BigDecimal strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public BigDecimal getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(BigDecimal fillOpacity) {
        this.fillOpacity = fillOpacity;
    }
}
