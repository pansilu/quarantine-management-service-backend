package lk.uom.fit.qms.dto;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class SuccessResponse {

    private String successMessage;

    public SuccessResponse(String successMessage){
        this.successMessage = successMessage;
    }

    public SuccessResponse(){}

    public String getSuccessMessage() {
        return successMessage;
    }
    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
