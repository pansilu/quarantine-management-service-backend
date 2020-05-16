package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.dto.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtTokenDto implements Serializable {

    private static final long serialVersionUID = 7170536875423392164L;

    private String username;
    private Long userId;
    private List<UserRoleDto> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleDto> roles) {
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
