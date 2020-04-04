package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lk.uom.fit.qms.util.enums.Rank;

import javax.persistence.*;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
public class ReportUser extends User{

    private static final long serialVersionUID = -6862191390484786975L;

    @Column(unique = true)
    private String officeId;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Rank rank;
    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "user_station", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "station_id", referencedColumnName = "id")}
    )
    private List<Station> stations;

    private String showingName;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setStations(List<Station> stations) {
        this.stations = stations;
    }

    public String getShowingName() {
        return showingName;
    }

    public void setShowingName(String showingName) {
        this.showingName = showingName;
    }
}
