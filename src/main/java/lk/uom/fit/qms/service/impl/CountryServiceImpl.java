package lk.uom.fit.qms.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.CountryDetail;
import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Country;
import lk.uom.fit.qms.repository.CountryRepository;
import lk.uom.fit.qms.service.CountryService;

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
import java.util.Map;
import java.util.Objects;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.service.impl.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CountryServiceImpl implements CountryService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CountryRepository countryRepository;

    private final ModelMapper modelMapper;

    private final ObjectMapper objectMapper;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    /*@PostConstruct
    private void init() {
        logger.info("start init hospital method");
        initCountries();
    }*/

    @Override
    public Country findOne(Long id) throws QmsException {

        Country country = countryRepository.findCountryById(id);

        if(country == null) {
            logger.warn("Country didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "Country Not Found!!!");
        }

        return country;
    }

    @Override
    public List<CountryDto> findAll(String search) {

        List<Country> countries;

        if(StringUtils.isEmpty(search)) {
            countries = countryRepository.getOrderedCountryList();
        } else {
            String pattern = "%" + search + "%";
            countries = countryRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<CountryDto>>() {}.getType();
        return modelMapper.map(countries, type);
    }

    private void initCountries() {

        TypeReference<List<CountryDetail>> typeRef = new TypeReference<List<CountryDetail>>() {
        };

        try {
            List<CountryDetail> countryDetails = objectMapper.readValue(new File("src/main/resources/countries.json"), typeRef);
            countryDetails.stream().filter(countryDetail -> !Objects.equals(countryDetail.getName(), "Sri Lanka"))
                    .forEach(countryDetail -> countryRepository.save(new Country(countryDetail.getName())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
