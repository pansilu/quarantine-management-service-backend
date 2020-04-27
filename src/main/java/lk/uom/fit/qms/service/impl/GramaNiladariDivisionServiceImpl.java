package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.DivisionResDto;
import lk.uom.fit.qms.dto.GnDivisionResDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Division;
import lk.uom.fit.qms.model.GramaNiladariDivision;
import lk.uom.fit.qms.repository.GramaNiladariDivisionRepository;
import lk.uom.fit.qms.service.DivisionService;
import lk.uom.fit.qms.service.GramaNiladariDivisionService;

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
 * @created on 4/26/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GramaNiladariDivisionServiceImpl implements GramaNiladariDivisionService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GramaNiladariDivisionRepository gramaNiladariDivisionRepository;

    private final ModelMapper modelMapper;

    private final DivisionService divisionService;

    @Autowired
    public GramaNiladariDivisionServiceImpl(GramaNiladariDivisionRepository gramaNiladariDivisionRepository, ModelMapper modelMapper, DivisionService divisionService) {
        this.gramaNiladariDivisionRepository = gramaNiladariDivisionRepository;
        this.modelMapper = modelMapper;
        this.divisionService = divisionService;
    }

    @Override
    public GramaNiladariDivision getGramaNiladariDivision(Long id) throws QmsException {

        GramaNiladariDivision gramaNiladariDivision = gramaNiladariDivisionRepository.findGramaNiladariDivisionById(id);

        if(gramaNiladariDivision == null) {
            logger.warn("GN division didn't exist for id: {}", id);
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.NOT_FOUND, "GN Division Not Found!!!");
        }

        return gramaNiladariDivision;
    }

    @Override
    public List<GnDivisionResDto> getAllGnDivisionsInDsDivision(Long divisionId, String search) throws QmsException {

        divisionService.checkDivisionExists(divisionId);

        List<GramaNiladariDivision> gramaNiladariDivisions;

        if(StringUtils.isEmpty(search)) {
            gramaNiladariDivisions = gramaNiladariDivisionRepository.findAll();
        } else {
            String pattern = "%" + search + "%";
            gramaNiladariDivisions = gramaNiladariDivisionRepository.filterBySearch(pattern);
        }

        Type type = new TypeToken<List<GnDivisionResDto>>() {}.getType();
        return modelMapper.map(gramaNiladariDivisions, type);
    }
}
