package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.HospitalDto;
import lk.uom.fit.qms.model.Hospital;
import lk.uom.fit.qms.repository.HospitalRepository;
import lk.uom.fit.qms.service.HospitalService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<HospitalDto> findHospitals(String search) {

        List<Hospital> hospitals;

        if(StringUtils.isEmpty(search)) {
            hospitals = hospitalRepository.findAll();
        } else {
            String pattern = "%" + search + "%";
            hospitals = hospitalRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<HospitalDto>>() {}.getType();
        return modelMapper.map(hospitals, type);
    }
}
