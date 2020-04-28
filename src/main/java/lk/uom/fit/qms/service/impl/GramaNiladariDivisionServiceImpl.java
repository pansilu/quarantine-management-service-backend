package lk.uom.fit.qms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.DistrictGndFeatureDetail;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private final ObjectMapper objectMapper;

    @Autowired
    public GramaNiladariDivisionServiceImpl(
            GramaNiladariDivisionRepository gramaNiladariDivisionRepository, ModelMapper modelMapper,
            DivisionService divisionService, ObjectMapper objectMapper) {

        this.gramaNiladariDivisionRepository = gramaNiladariDivisionRepository;
        this.modelMapper = modelMapper;
        this.divisionService = divisionService;
        this.objectMapper = objectMapper;
    }

    /*@PostConstruct
    private void init() {
        logger.info("start init location method");
        setGndFeature();
    }*/

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
            gramaNiladariDivisions = gramaNiladariDivisionRepository.findGramaNiladariDivisionsByDivisionIdOrderByName(divisionId);
        } else {
            String pattern = "%" + search + "%";
            gramaNiladariDivisions = gramaNiladariDivisionRepository.filterBySearch(divisionId, pattern);
        }

        Type type = new TypeToken<List<GnDivisionResDto>>() {}.getType();
        return modelMapper.map(gramaNiladariDivisions, type);
    }

    @Override
    public List<GnDivisionResDto> getAllGnDivisions(String search) {

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

    @Override
    public GnDivisionResDto getGnDivisionDetailsById(Long id) throws QmsException {

        GramaNiladariDivision gramaNiladariDivision = getGramaNiladariDivision(id);
        return modelMapper.map(gramaNiladariDivision, GnDivisionResDto.class);
    }

    private void setGndFeature() {

        try (Stream<Path> walk = Files.walk(Paths.get("src/main/resources/districts"))) {

            List<String> results = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

            Pattern patObjectId = Pattern.compile("OBJECTID_1</td>\\r\\n\\r\\n<td>(.*?)</td>", Pattern.DOTALL);
            Pattern patGnName = Pattern.compile("GND_N</td>\\r\\n\\r\\n<td>(.*?)</td>", Pattern.DOTALL);
            Pattern patGnNo = Pattern.compile("GND_NO</td>\\r\\n\\r\\n<td>(.*?)</td>", Pattern.DOTALL);
            Pattern patGnCode = Pattern.compile("GND_C</td>\\r\\n\\r\\n<td>(.*?)</td>", Pattern.DOTALL);
            Pattern patDsName = Pattern.compile("DSD_N</td>\\r\\n\\r\\n<td>(.*?)</td>", Pattern.DOTALL);
            Pattern patDsCode = Pattern.compile("DSD_C</td>\\r\\n\\r\\n<td>(.*?)</td>", Pattern.DOTALL);

            for(String result : results) {

                logger.info(result);
                DistrictGndFeatureDetail districtGndFeatureDetail = objectMapper.readValue(new File(result), DistrictGndFeatureDetail.class);

                districtGndFeatureDetail.getFeatures().forEach(gndFeature -> {
                    String description = gndFeature.getProperties().getDescription();
                    Matcher matchObject = patObjectId.matcher(description);
                    Matcher matchGnName = patGnName.matcher(description);
                    Matcher matchGnNo = patGnNo.matcher(description);
                    Matcher matchGnCode = patGnCode.matcher(description);

                    if(matchObject.find()) {
                        String objectId = matchObject.group().replaceAll("OBJECTID_1</td>\\r\\n\\r\\n<td>", "").replace("</td>", "");
                        String gnName = matchGnName.find() ? matchGnName.group().replaceAll("GND_N</td>\\r\\n\\r\\n<td>", "").replace("</td>", "").trim() : null;
                        String gnNo = matchGnNo.find() ? matchGnNo.group().replaceAll("GND_NO</td>\\r\\n\\r\\n<td>", "").replace("</td>", "") : null;
                        String gnCode = matchGnCode.find() ? matchGnCode.group().replaceAll("GND_C</td>\\r\\n\\r\\n<td>", "").replace("</td>", "") : null;

                        GramaNiladariDivision gramaNiladariDivision = gramaNiladariDivisionRepository.findGramaNiladariDivisionByObjectId(objectId);
                        if(gramaNiladariDivision != null) {

                            if(StringUtils.isEmpty(gramaNiladariDivision.getName().trim())) {
                                if(!StringUtils.isEmpty(gnName)) {
                                    logger.warn("previouly name null value found: {}, new name: {}", objectId, gnName);
                                    gramaNiladariDivision.setName(gnName);
                                    gramaNiladariDivision.setCode(gnCode);
                                    gramaNiladariDivision.setGndNo(gnNo);
                                } else {
                                    gramaNiladariDivision.setName("No GN Name_" + objectId);
                                }
                            }
                            try {
                                String feature = objectMapper.writeValueAsString(gndFeature);
                                gramaNiladariDivision.setFeature(feature);
                                gramaNiladariDivisionRepository.save(gramaNiladariDivision);
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Matcher matchDsName = patDsName.matcher(description);
                            Matcher matchDsCode = patDsCode.matcher(description);

                            if(matchDsName.find()) {

                                String dsName = matchDsName.group().replaceAll("DSD_N</td>\\r\\n\\r\\n<td>", "").replace("</td>", "");
                                String dsCode = matchDsCode.find() ? matchDsCode.group().replaceAll("DSD_C</td>\\r\\n\\r\\n<td>", "").replace("</td>", "") : null;
                                Division division = divisionService.findDivisionByName(dsName);

                                if (StringUtils.isEmpty(gnName)) {
                                    gnName = "No GN Name_" + objectId;
                                }

                                GramaNiladariDivision gramaNiladariDivisionNew = new GramaNiladariDivision(gnName, gnCode, gnNo, objectId);

                                if(division != null) {
                                    gramaNiladariDivisionNew.setDivision(division);
                                } else {
                                    Division divisionNew = new Division(dsName, dsCode);
                                    gramaNiladariDivisionNew.setDivision(divisionService.createNewDivision(divisionNew));
                                    logger.warn("no divison found for object: {}", objectId);
                                }
                                gramaNiladariDivisionRepository.save(gramaNiladariDivisionNew);
                            }
                        }
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
