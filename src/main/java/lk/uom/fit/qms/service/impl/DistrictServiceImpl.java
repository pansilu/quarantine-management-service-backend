package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.DistrictResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.District;
import lk.uom.fit.qms.repository.DistrictRepository;
import lk.uom.fit.qms.service.DistrictService;
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
public class DistrictServiceImpl implements DistrictService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final DistrictRepository districtRepository;

    private final ModelMapper modelMapper;

    private final ProvinceService provinceService;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, ModelMapper modelMapper, ProvinceService provinceService) {
        this.districtRepository = districtRepository;
        this.modelMapper = modelMapper;
        this.provinceService = provinceService;
    }

    @Override
    public List<DistrictResDto> getAllDistrictsInProvince(Long provinceId, String search) throws QmsException {

        provinceService.checkProvinceExits(provinceId);

        List<District> districts;

        if(StringUtils.isEmpty(search)) {
            districts = districtRepository.findDistrictsByProvinceIdOrderByName(provinceId);
        } else {
            String pattern = "%" + search + "%";
            districts = districtRepository.filterBySearch(provinceId, pattern);
        }

        Type type = new TypeToken<List<DistrictResDto>>() {}.getType();
        return modelMapper.map(districts, type);
    }

    @Override
    public void checkDistrictExists(Long id) throws QmsException {

        if(!districtRepository.existsById(id)) {
            logger.warn("District didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "District Not Found!!!");
        }
    }
}
