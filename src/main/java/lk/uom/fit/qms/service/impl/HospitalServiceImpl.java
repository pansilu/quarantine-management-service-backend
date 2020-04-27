package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.HospitalDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Hospital;
import lk.uom.fit.qms.repository.HospitalRepository;
import lk.uom.fit.qms.service.HospitalService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HospitalRepository hospitalRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public HospitalServiceImpl(HospitalRepository hospitalRepository, ModelMapper modelMapper) {
        this.hospitalRepository = hospitalRepository;
        this.modelMapper = modelMapper;
    }

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

    @Override
    public Hospital findHospitalForGivenId(Long id) throws QmsException {

        Hospital hospital = hospitalRepository.findHospitalById(id);

        if(hospital == null) {
            logger.warn("Hospital didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Hospital Not Found!!!");
        }

        return hospital;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createOrEditHospital(HospitalDto hospitalDto) throws QmsException {

        if(hospitalDto.getId() != null) {
            findHospitalForGivenId(hospitalDto.getId());
        }

        Hospital hospital = modelMapper.map(hospitalDto, Hospital.class);
        hospitalRepository.save(hospital);
    }

    @Override
    public HospitalDto getHospitalDetails(Long id) throws QmsException {

        Hospital hospital = findHospitalForGivenId(id);
        return modelMapper.map(hospital, HospitalDto.class);
    }
}
