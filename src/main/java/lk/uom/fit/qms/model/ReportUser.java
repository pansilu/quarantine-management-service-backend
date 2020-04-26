package lk.uom.fit.qms.model;

import javax.persistence.*;

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

    @Column(columnDefinition = "varchar(20)", unique = true, nullable = false)
    private String officeId;

    private String showingName;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getShowingName() {
        return showingName;
    }

    public void setShowingName(String showingName) {
        this.showingName = showingName;
    }
}
