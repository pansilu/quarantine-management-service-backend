package lk.uom.fit.qms.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.District;
import lk.uom.fit.qms.model.Division;
import lk.uom.fit.qms.model.GramaNiladariDivision;
import lk.uom.fit.qms.model.Province;
import lk.uom.fit.qms.repository.ProvinceRepository;
import lk.uom.fit.qms.service.ProvinceService;

import org.apache.commons.text.WordUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final ObjectMapper objectMapper;

    @Autowired
    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.provinceRepository = provinceRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    /*@PostConstruct
    private void init() {
        logger.info("start init location method");
        initLocation();
    }*/

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

    private void initLocation() {

        try {
            TypeReference<Map<String, Map<String, List<LocationDto>>>> typeRef = new TypeReference<Map<String, Map<String, List<LocationDto>>>>() {
            };
            Map<String, Map<String, List<LocationDto>>> locationMap = objectMapper.readValue(new File("src/main/resources/dns.json"), typeRef);

            Map<String, ProvinceMap> provinceMaps = new HashMap<>();

            locationMap.forEach((district, divisionMap) -> divisionMap.forEach((division, locationList) ->
                locationList.forEach(locationDto ->
                    addLocation(provinceMaps, locationDto)
                )
            ));

            logger.info("test");

            provinceMaps.forEach((key, provinceMap) -> {
                logger.info(key);
                provinceRepository.save(provinceMap.getProvince());
            });

            logger.info("test");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLocation(Map<String, ProvinceMap> provinceMaps, LocationDto locationDto) {

        if(!provinceMaps.containsKey(locationDto.getProvince_n())) {

            Province province = new Province(WordUtils.capitalizeFully(locationDto.getProvince_n()), locationDto.getProvince_c());

            District district = new District(WordUtils.capitalizeFully(locationDto.getDistrict_n()), locationDto.getDistrict_c());
            district.setProvince(province);

            Division division = new Division(WordUtils.capitalizeFully(locationDto.getDsd_n()), locationDto.getDsd_c());
            division.setDistrict(district);

            GramaNiladariDivision gramaNiladariDivision = new GramaNiladariDivision(locationDto.getGnd_n(), locationDto.getGnd_c(), locationDto.getGnd_no(), locationDto.getObjectid_1());
            gramaNiladariDivision.setDivision(division);

            division.getGnDivisions().add(gramaNiladariDivision);
            district.getDivisions().add(division);
            province.getDistricts().add(district);

            // set all location maps
            Map<String, DivisionMap> divisionMaps = new HashMap<>();
            divisionMaps.put(locationDto.getDsd_n(), new DivisionMap(division));

            Map<String, DistrictMap> districtMaps = new HashMap<>();
            districtMaps.put(locationDto.getDistrict_n(), new DistrictMap(district, divisionMaps));

            provinceMaps.put(locationDto.getProvince_n(), new ProvinceMap(province, districtMaps));
        }
        else {

            ProvinceMap provinceMap = provinceMaps.get(locationDto.getProvince_n());

            if(!provinceMap.getDistrictMaps().containsKey(locationDto.getDistrict_n())) {

                District district = new District(WordUtils.capitalizeFully(locationDto.getDistrict_n()), locationDto.getDistrict_c());
                district.setProvince(provinceMap.getProvince());

                Division division = new Division(WordUtils.capitalizeFully(locationDto.getDsd_n()), locationDto.getDsd_c());
                division.setDistrict(district);

                GramaNiladariDivision gramaNiladariDivision = new GramaNiladariDivision(locationDto.getGnd_n(), locationDto.getGnd_c(), locationDto.getGnd_no(), locationDto.getObjectid_1());
                gramaNiladariDivision.setDivision(division);

                division.getGnDivisions().add(gramaNiladariDivision);
                district.getDivisions().add(division);
                provinceMap.getProvince().getDistricts().add(district);

                Map<String, DivisionMap> divisionMaps = new HashMap<>();
                divisionMaps.put(locationDto.getDsd_n(), new DivisionMap(division));

                provinceMap.getDistrictMaps().put(locationDto.getDistrict_n(), new DistrictMap(district, divisionMaps));
            }
            else {

                DistrictMap districtMap = provinceMap.getDistrictMaps().get(locationDto.getDistrict_n());

                if(!districtMap.getDivisionMaps().containsKey(locationDto.getDsd_n())) {

                    Division division = new Division(WordUtils.capitalizeFully(locationDto.getDsd_n()), locationDto.getDsd_c());
                    division.setDistrict(districtMap.getDistrict());

                    GramaNiladariDivision gramaNiladariDivision = new GramaNiladariDivision(locationDto.getGnd_n(), locationDto.getGnd_c(), locationDto.getGnd_no(), locationDto.getObjectid_1());
                    gramaNiladariDivision.setDivision(division);

                    division.getGnDivisions().add(gramaNiladariDivision);
                    districtMap.getDistrict().getDivisions().add(division);

                    districtMap.getDivisionMaps().put(locationDto.getDsd_n(), new DivisionMap(division));
                }
                else {

                    DivisionMap divisionMap = districtMap.getDivisionMaps().get(locationDto.getDsd_n());

                    GramaNiladariDivision gramaNiladariDivision = new GramaNiladariDivision(locationDto.getGnd_n(), locationDto.getGnd_c(), locationDto.getGnd_no(), locationDto.getObjectid_1());
                    gramaNiladariDivision.setDivision(divisionMap.getDivision());

                    divisionMap.getDivision().getGnDivisions().add(gramaNiladariDivision);
                }
            }
        }
    }
}
