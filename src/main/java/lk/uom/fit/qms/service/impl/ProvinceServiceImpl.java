package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.ProvinceResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Province;
import lk.uom.fit.qms.repository.ProvinceRepository;
import lk.uom.fit.qms.service.ProvinceService;

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
public class ProvinceServiceImpl implements ProvinceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProvinceRepository provinceRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ModelMapper modelMapper) {
        this.provinceRepository = provinceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProvinceResDto> findAllProvinces(String search) {

        List<Province> provinces;

        if(StringUtils.isEmpty(search)) {
            provinces = provinceRepository.findAll();
        } else {
            String pattern = "%" + search + "%";
            provinces = provinceRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<ProvinceResDto>>() {}.getType();
        return modelMapper.map(provinces, type);
    }

    @Override
    public void checkProvinceExits(Long id) throws QmsException {

        if(!provinceRepository.existsById(id)) {
            logger.warn("Province didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Province Not Found!!!");
        }
    }
}
