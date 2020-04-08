package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.CountryDto;
import lk.uom.fit.qms.model.Country;
import lk.uom.fit.qms.repository.CountryRepository;
import lk.uom.fit.qms.service.CountryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;

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

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Country findOne(Long id) {
        return countryRepository.findCountryById(id);
    }

    @Override
    public List<CountryDto> findAll() {
        List<Country> countries = countryRepository.findAll();
        Type type = new TypeToken<List<CountryDto>>() {}.getType();
        return modelMapper.map(countries, type);
    }
}
