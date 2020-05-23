package lk.uom.fit.qms.service;

import lk.uom.fit.qms.dto.HospitalDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.Hospital;

import java.util.List;

public interface HospitalService {

    List<HospitalDto> findHospitals(String search);

    Hospital findHospitalForGivenId(Long id) throws QmsException;

    void createOrEditHospital(HospitalDto hospitalDto) throws QmsException;

    HospitalDto getHospitalDetails(Long id) throws QmsException;

    Long getHospitalIdFromHospitalMappingName(String hospitalMappingName);
}
