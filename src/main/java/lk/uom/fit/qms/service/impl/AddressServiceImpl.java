package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.Address;
import lk.uom.fit.qms.repository.AddressRepository;
import lk.uom.fit.qms.service.AddressService;

import lk.uom.fit.qms.service.GramaNiladariDivisionService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @created on 4/12/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AddressServiceImpl implements AddressService {

    private final ModelMapper modelMapper;

    private final AddressRepository addressRepository;

    private final GramaNiladariDivisionService gramaNiladariDivisionService;

    @Autowired
    public AddressServiceImpl(ModelMapper modelMapper, AddressRepository addressRepository, GramaNiladariDivisionService gramaNiladariDivisionService) {
        this.modelMapper = modelMapper;
        this.addressRepository = addressRepository;
        this.gramaNiladariDivisionService = gramaNiladariDivisionService;
    }

    @Override
    public List<AddressDto> getAllAddress(Long gndId, String police, String line) throws QmsException {

        gramaNiladariDivisionService.getGramaNiladariDivision(gndId);

        String patternLine;
        String patternPolice;

        List<Address> addresses;

        if (!StringUtils.isEmpty(line) && !StringUtils.isEmpty(police)) {

            patternLine = "%" + line + "%";
            patternPolice = "%" + police + "%";

            addresses = addressRepository.filterByLineAndPoliceArea(gndId, patternLine, patternPolice);
        } else if (!StringUtils.isEmpty(police)) {

            patternPolice = "%" + police + "%";

            addresses = addressRepository.filterByPoliceArea(gndId, patternPolice);
        } else if (!StringUtils.isEmpty(line)) {

            patternLine = "%" + line + "%";

            addresses = addressRepository.filterByLine(gndId, patternLine);
        } else {
            addresses = addressRepository.filterByGnd(gndId);
        }

        Type type = new TypeToken<List<AddressDto>>() {}.getType();
        return modelMapper.map(addresses, type);
    }
}
