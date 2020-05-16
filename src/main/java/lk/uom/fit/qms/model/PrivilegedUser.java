package lk.uom.fit.qms.model;

import lk.uom.fit.qms.util.enums.RoleType;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

// this class is handled the privilaged user information

@Entity
public class PrivilegedUser extends User {

    private static final long serialVersionUID = -6862191390484786975L;

    private String rank;
    private RoleType role;
    @ManyToOne
    private Regiment regiment;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public Regiment getRegiment() {
        return regiment;
    }

    public void setRegiment(Regiment regiment) {
        this.regiment = regiment;
    }
}
