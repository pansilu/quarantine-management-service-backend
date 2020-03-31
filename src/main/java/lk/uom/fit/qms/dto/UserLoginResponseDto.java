package lk.uom.fit.qms.dto;

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
public class UserLoginResponseDto implements Serializable {

    private static final long serialVersionUID = 2507976978075663992L;

    private String token;

    public UserLoginResponseDto() {
    }

    public UserLoginResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserLoginResponseDto{" +
                "token='" + token + '\'' +
                '}';
    }
}
