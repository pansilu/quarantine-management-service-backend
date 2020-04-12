package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.HospitalDto;
import java.util.List;

public interface HospitalService {

    List<HospitalDto> findHospitals();
}
