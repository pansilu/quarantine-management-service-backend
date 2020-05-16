package lk.uom.fit.qms.dto;

import java.util.List;

public class PrivilegedUserListResponse {

    private int totalPages;

    private List<PrivilegedUserResponseDto> data;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<PrivilegedUserResponseDto> getData() {
        return data;
    }

    public void setData(List<PrivilegedUserResponseDto> data) {
        this.data = data;
    }
}
