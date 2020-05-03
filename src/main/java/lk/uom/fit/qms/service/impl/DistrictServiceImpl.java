package lk.uom.fit.qms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.uom.fit.qms.dto.DistrictFeatureDetail;
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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
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

    private final ObjectMapper objectMapper;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository, ModelMapper modelMapper, ProvinceService provinceService, ObjectMapper objectMapper) {
        this.districtRepository = districtRepository;
        this.modelMapper = modelMapper;
        this.provinceService = provinceService;
        this.objectMapper = objectMapper;
    }

    /*@PostConstruct
    private void init() {
        logger.info("start init district feature method");
        setDistrictFeature();
    }*/

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

    @Override
    public District findDistrictById(Long id) throws QmsException {

        District district = districtRepository.findDistrictById(id);

        if(district == null) {
            logger.warn("District didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "District Not Found!!!");
        }
        return district;
    }

    @Override
    public DistrictResDto getDistrictDetailsById(Long id) throws QmsException {

        District district = findDistrictById(id);
        return modelMapper.map(district, DistrictResDto.class);
    }

    @Override
    public List<DistrictResDto> getAllDistricts(String search) {

        List<District> districts;

        if(StringUtils.isEmpty(search)) {
            districts = districtRepository.getOrderedDistrictList();
        } else {
            String pattern = "%" + search + "%";
            districts = districtRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<DistrictResDto>>() {}.getType();
        return modelMapper.map(districts, type);
    }

    private void setDistrictFeature() {

        try {
            DistrictFeatureDetail districtFeatureDetail = objectMapper.readValue(new File("src/main/resources/distrits_wit_feature.json"), DistrictFeatureDetail.class);

            districtFeatureDetail.getFeatures().forEach(districtFeature -> {
                District district = districtRepository.findDistrictByName(districtFeature.getProperties().getName2());
                try {
                    if(district != null) {
                        String feature = objectMapper.writeValueAsString(districtFeature);
                        district.setFeature(feature);
                        districtRepository.save(district);
                    } else {
                        logger.warn("district not found for the name: {}", districtFeature.getProperties().getName2());
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
