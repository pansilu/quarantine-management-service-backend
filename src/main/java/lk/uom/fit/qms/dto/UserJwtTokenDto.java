package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class UserJwtTokenDto {

    private String username;

    private String iat;

    private String exp;

    private Long userId;

    private String phone;

    private String name;
}
