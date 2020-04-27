package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.DivisionResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Division;
import lk.uom.fit.qms.repository.DivisionRepository;
import lk.uom.fit.qms.service.DistrictService;
import lk.uom.fit.qms.service.DivisionService;

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

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/27/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DivisionServiceImpl implements DivisionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DivisionRepository divisionRepository;

    private final ModelMapper modelMapper;

    private final DistrictService districtService;

    @Autowired
    public DivisionServiceImpl(DivisionRepository divisionRepository, ModelMapper modelMapper, DistrictService districtService) {
        this.divisionRepository = divisionRepository;
        this.modelMapper = modelMapper;
        this.districtService = districtService;
    }

    @Override
    public List<DivisionResDto> getAllDivisionsInDistrict(Long districtId, String search) throws QmsException {

        districtService.checkDistrictExists(districtId);

        List<Division> divisions;

        if(StringUtils.isEmpty(search)) {
            divisions = divisionRepository.findAll();
        } else {
            String pattern = "%" + search + "%";
            divisions = divisionRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<DivisionResDto>>() {}.getType();
        return modelMapper.map(divisions, type);
    }

    @Override
    public void checkDivisionExists(Long id) throws QmsException {

        if(!divisionRepository.existsById(id)) {
            logger.warn("DS Division didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "DS Division Not Found!!!");
        }
    }
}
