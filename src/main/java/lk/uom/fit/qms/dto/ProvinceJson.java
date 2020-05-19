package lk.uom.fit.qms.dto;

import java.util.List;

public class ProvinceJson extends ProvinceResDto {

    private List<DistrictJson> districts;

    public List<DistrictJson> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DistrictJson> districts) {
        this.districts = districts;
    }
}
