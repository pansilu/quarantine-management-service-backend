package lk.uom.fit.qms.dto;

import java.util.List;

public class SlaUserMultiPageResDto {
    private int totalPages;

    private List<SlaUserResponseDto> data;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<SlaUserResponseDto> getData() {
        return data;
    }

    public void setData(List<SlaUserResponseDto> data) {
        this.data = data;
    }
}
