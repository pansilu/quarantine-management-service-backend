package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.util.enums.Rank;
import lk.uom.fit.qms.util.enums.RoleType;

import javax.validation.constraints.NotEmpty;

public class PrivilegedUserRequestDto extends UserRequestDto {
    private Rank rank;
    private RoleType role;
    @NotEmpty(message = "Please Enter Regiment")
    private String regiment;

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getRegiment() {
        return regiment;
    }

    public void setRegiment(String regiment) {
        this.regiment = regiment;
    }
}
