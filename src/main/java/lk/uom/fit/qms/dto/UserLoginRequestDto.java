package lk.uom.fit.qms.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

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
public class UserLoginRequestDto implements Serializable {

    private static final long serialVersionUID = -3476420191353282625L;

    @NotEmpty(message = "Please enter username")
    private String username;

    @NotEmpty(message = "Please enter password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
