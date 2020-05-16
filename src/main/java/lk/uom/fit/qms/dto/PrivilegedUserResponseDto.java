package lk.uom.fit.qms.dto;

import lk.uom.fit.qms.model.Regiment;
import lk.uom.fit.qms.util.enums.Rank;
import lk.uom.fit.qms.util.enums.RoleType;

public class PrivilegedUserResponseDto extends UserRequestDto {
    private Rank rank;
    private RoleType role;
    private RegimentResponseDto regiment;

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

    public RegimentResponseDto getRegiment() {
        return regiment;
    }

    public void setRegiment(RegimentResponseDto regiment) {
        this.regiment = regiment;
    }
}
