package lk.uom.fit.qms.dto;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/4/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class QuarantineUserMultiPageResDto {

    private List<QuarantineMultiUserResDto> data;

    private int totalPages;

    public List<QuarantineMultiUserResDto> getData() {
        return data;
    }

    public void setData(List<QuarantineMultiUserResDto> data) {
        this.data = data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
