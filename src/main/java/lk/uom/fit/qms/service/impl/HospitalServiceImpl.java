package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.model.Hospital;
import lk.uom.fit.qms.repository.HospitalRepository;
import lk.uom.fit.qms.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public List<Hospital> findHospitals() {
        return hospitalRepository.findAll();
    }
}
