package lk.uom.fit.qms.dto;

import javax.validation.constraints.NotEmpty;

public class SecretDto {

    @NotEmpty(message = "Please enter secret")
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
