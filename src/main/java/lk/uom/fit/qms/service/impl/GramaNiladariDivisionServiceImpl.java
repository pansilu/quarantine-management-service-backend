package lk.uom.fit.qms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Division;
import lk.uom.fit.qms.model.GndCoordinateDetail;
import lk.uom.fit.qms.model.GramaNiladariDivision;
import lk.uom.fit.qms.model.NearestGndDetail;
import lk.uom.fit.qms.repository.GndCoordinateDetailRepository;
import lk.uom.fit.qms.repository.GramaNiladariDivisionRepository;
import lk.uom.fit.qms.service.DivisionService;
import lk.uom.fit.qms.service.GramaNiladariDivisionService;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;
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

    private final GndCoordinateDetailRepository gndCoordinateDetailRepository;

    @Autowired
    public GramaNiladariDivisionServiceImpl(
            GramaNiladariDivisionRepository gramaNiladariDivisionRepository, ModelMapper modelMapper,
            DivisionService divisionService, ObjectMapper objectMapper, GndCoordinateDetailRepository gndCoordinateDetailRepository) {

        this.gramaNiladariDivisionRepository = gramaNiladariDivisionRepository;
        this.modelMapper = modelMapper;
        this.divisionService = divisionService;
        this.objectMapper = objectMapper;
        this.gndCoordinateDetailRepository = gndCoordinateDetailRepository;
    }

    /*@PostConstruct
    private void init() {
        logger.info("start init location method");
        //setGndFeature();
        //calGndCoordinates();
        //findNearestGndsForAnyDistrictGnd();
        //findNearestGndsBetweenDistrictBorderLine();
        //fillNearestGndTable();
        setGndCenterCoordinates();
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

    private void calGndCoordinates() {

        Map<Long, String> gndCoordinatesMap = new ConcurrentHashMap<>();

        divisionService.getAllDivisions().forEach(division -> {

            logger.info("divisonId: {}, districtId: {}", division.getId(), division.getDistrict().getId());

            List<GramaNiladariDivision> gramaNiladariDivisions = gramaNiladariDivisionRepository.findGramaNiladariDivisionsByDivisionIdOrderByName(division.getId());

            new ForkJoinPool(10).submit(
                    () -> gramaNiladariDivisions.parallelStream().forEach(
                            gramaNiladariDivision -> {
                                try {
                                    GndFeature gndFeature = objectMapper.readValue(gramaNiladariDivision.getFeature(), GndFeature.class);

                                    String geoData = objectMapper.writeValueAsString(gndFeature.getGeometry());

                                    if(gndFeature.getGeometry().containsKey("geometries")) {

                                        logger.info("found many coordiantes set for gnd: {}", gramaNiladariDivision.getId());

                                        Geometry geometry = objectMapper.readValue(geoData, Geometry.class);
                                        gndCoordinatesMap.put(gramaNiladariDivision.getId(), objectMapper.writeValueAsString(geometry.getGeometries()));

                                    } else {
                                        CoordinatesSet coordinatesSet = objectMapper.readValue(geoData, CoordinatesSet.class);

                                        gndCoordinatesMap.put(gramaNiladariDivision.getId(), objectMapper.writeValueAsString(Collections.singletonList(coordinatesSet)));
                                    }
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                    )
            ).join();

            logger.info("=================================================");
        });

        gndCoordinatesMap.forEach((gndId, stringCoordinates) -> {

            GndCoordinateDetail gndCoordinateDetail = new GndCoordinateDetail(stringCoordinates, gramaNiladariDivisionRepository.findGramaNiladariDivisionById(gndId));
            gndCoordinateDetailRepository.save(gndCoordinateDetail);
        });
    }

    private void findNearestGndsForAnyDistrictGnd() {

        ForkJoinPool forkJoinPool = new ForkJoinPool(5);

        ArrayList<Long> districtIds = new ArrayList<>(Arrays.asList(8L,9L,10L,11L,12L,13L,14L,15L,16L,17L,18L,19L,20L,21L,22L,23L,24L,25L));

        TypeReference<List<CoordinatesSet>> typeRef = new TypeReference<List<CoordinatesSet>>() {};

        districtIds.forEach(districtId -> {

            List<GndCoordinateDetail> primaryDistrictGndCoordinateDetails = new CopyOnWriteArrayList<>(gndCoordinateDetailRepository.findGndCoordinateDetailsForDistrictId(districtId));
            List<GndCoordinateDetail> secondaryDistrictGndCoordinateDetails = new CopyOnWriteArrayList<>(primaryDistrictGndCoordinateDetails);

            Map<Long, List<CoordinatesSet>> gndCoordinateSetsMap = new ConcurrentHashMap<>();

            logger.info("###########################################################################");
            forkJoinPool.submit(
                    () -> primaryDistrictGndCoordinateDetails.parallelStream().forEach(
                            primary -> {
                                try {
                                    gndCoordinateSetsMap.put(primary.getId(), objectMapper.readValue(primary.getCoordinates(), typeRef));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                    )
            ).join();

            AtomicInteger count = new AtomicInteger(0);

            logger.info("***************************************************************");

            forkJoinPool.submit(
                    () -> primaryDistrictGndCoordinateDetails.parallelStream().forEach(
                            primary -> {

                                List<Long> nearestGndIds = new ArrayList<>();
                                List<CoordinatesSet> primaryCoordinateSets = gndCoordinateSetsMap.get(primary.getId());

                                secondaryDistrictGndCoordinateDetails.stream().filter(secondary -> !secondary.getId().equals(primary.getId()))
                                        .forEach(secondary -> {

                                            List<CoordinatesSet> secondaryCoordinateSets = gndCoordinateSetsMap.get(secondary.getId());
                                            if(checkGivenTwoGndsAreRelated(primaryCoordinateSets, secondaryCoordinateSets)) {
                                                nearestGndIds.add(secondary.getGnDivision().getId());
                                            }
                                        });

                                count.getAndIncrement();

                                if(nearestGndIds.isEmpty()) {
                                    logger.warn("Count: {}, no nearest gnds found for gnd: {}, in division: {}, in district: {}", count.get(), primary.getGnDivision().getId(), primary.getGnDivision().getDivision().getId(), districtId);
                                } else {

                                    String nearestGnds = org.apache.commons.lang3.StringUtils.join(nearestGndIds, ',');
                                    primary.setNearestGndIds(nearestGnds);

                                    logger.info("Count: {}, found nearest gnds: {} for gnd: {} in district: {}", count.get(), nearestGnds, primary.getGnDivision().getId(), districtId);
                                }
                            }
                    )
            ).join();

            logger.info("***************************************************************");
            gndCoordinateDetailRepository.saveAll(primaryDistrictGndCoordinateDetails);
            logger.info("===================================================================");
            logger.info("nearest gnds find complete for district: {}", districtId);
            logger.info("===================================================================");
        });
    }

    private void findNearestGndsBetweenDistrictBorderLine() {

        Map<Long, List<Long>> districtMap = new HashMap<>();
        districtMap.put(2L, new ArrayList<>(Arrays.asList(1L,3L,17L,24L)));
        districtMap.put(4L, new ArrayList<>(Arrays.asList(3L)));
        districtMap.put(15L, new ArrayList<>(Arrays.asList(1L,13L,16L,17L)));
        districtMap.put(3L, new ArrayList<>(Arrays.asList(1L,14L,24L)));
        districtMap.put(14L, new ArrayList<>(Arrays.asList(1L,6L,8L,13L)));
        districtMap.put(9L, new ArrayList<>(Arrays.asList(6L,8L,11L)));
        districtMap.put(10L, new ArrayList<>(Arrays.asList(11L,12L,16L,17L)));
        districtMap.put(1L, new ArrayList<>(Arrays.asList(13L,17L)));
        districtMap.put(16L, new ArrayList<>(Arrays.asList(8L,11L,13L,17L)));
        districtMap.put(17L, new ArrayList<>(Arrays.asList(12L,23L,24L,25L)));
        districtMap.put(24L, new ArrayList<>(Arrays.asList(25L)));
        districtMap.put(25L, new ArrayList<>(Arrays.asList(23L)));
        districtMap.put(6L, new ArrayList<>(Arrays.asList(8L)));
        districtMap.put(12L, new ArrayList<>(Arrays.asList(23L)));
        districtMap.put(11L, new ArrayList<>(Arrays.asList(8L)));
        districtMap.put(13L, new ArrayList<>(Arrays.asList(8L)));

        ForkJoinPool forkJoinPool = new ForkJoinPool(10);

        districtMap.forEach((primaryDistrictId, secondaryDistrictIdList) -> {

            logger.info("************* primary district: {} *************", primaryDistrictId);

            List<GndCoordinateDetail> primaryDistrictGndCoordinateDetails = new CopyOnWriteArrayList<>(gndCoordinateDetailRepository.findGndCoordinateDetailsForDistrictId(primaryDistrictId));

            TypeReference<List<CoordinatesSet>> typeRef = new TypeReference<List<CoordinatesSet>>() {};

            Map<Long, List<CoordinatesSet>> gndCoordinateSetsMap = new ConcurrentHashMap<>();

            forkJoinPool.submit(
                    () -> primaryDistrictGndCoordinateDetails.parallelStream().forEach(
                            primary -> {
                                try {
                                    gndCoordinateSetsMap.put(primary.getId(), objectMapper.readValue(primary.getCoordinates(), typeRef));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            }
                    )
            ).join();

            secondaryDistrictIdList.forEach(secondaryDistrictId -> {

                Map<Long, List<CoordinatesSet>> secondaryGndCoordinateSetsMap = new ConcurrentHashMap<>();
                List<GndCoordinateDetail> secondaryDistrictGndCoordinateDetails = new CopyOnWriteArrayList<>(gndCoordinateDetailRepository.findGndCoordinateDetailsForDistrictId(secondaryDistrictId));

                logger.info("########################################################################### dis_id:{}", secondaryDistrictId);
                forkJoinPool.submit(
                        () -> secondaryDistrictGndCoordinateDetails.parallelStream().forEach(
                                secondary -> {
                                    try {
                                        secondaryGndCoordinateSetsMap.put(secondary.getId(), objectMapper.readValue(secondary.getCoordinates(), typeRef));
                                    } catch (JsonProcessingException e) {
                                        e.printStackTrace();
                                    }
                                }
                        )
                ).join();
                logger.info("*****************************************************************************");

                AtomicInteger count = new AtomicInteger(0);

                forkJoinPool.submit(
                        () -> primaryDistrictGndCoordinateDetails.parallelStream().forEach(
                                primary -> {

                                    List<Long> nearestGndIds = new ArrayList<>();
                                    List<CoordinatesSet> primaryCoordinateSets = gndCoordinateSetsMap.get(primary.getId());

                                    secondaryDistrictGndCoordinateDetails.forEach(secondary -> {

                                        List<CoordinatesSet> secondaryCoordinateSets = secondaryGndCoordinateSetsMap.get(secondary.getId());
                                        if(checkGivenTwoGndsAreRelated(primaryCoordinateSets, secondaryCoordinateSets)) {
                                            nearestGndIds.add(secondary.getGnDivision().getId());
                                            if(secondary.getNearestGndIds() != null) {
                                                String nearestGnds = secondary.getNearestGndIds() + ',' + primary.getGnDivision().getId();
                                                secondary.setNearestGndIds(nearestGnds);
                                                logger.info("set primary gnd: {} for secondary gnd: {} in s_district: {}", primary.getGnDivision().getId(), secondary.getGnDivision().getId(), secondaryDistrictId);
                                            } else {
                                                secondary.setNearestGndIds(primary.getGnDivision().getId().toString());
                                                logger.info("found s_gnd: {} in s_district: {} without nearest gnds. add p_gnd: {}", secondary.getGnDivision().getId(), secondaryDistrictId, primary.getGnDivision().getId());
                                            }
                                        }
                                    });

                                    count.getAndIncrement();
                                    //logger.info("Count: {}", count.get());

                                    if(!nearestGndIds.isEmpty()) {

                                        if(primary.getNearestGndIds() != null) {

                                            String nearestGnds = primary.getNearestGndIds() + ',' + org.apache.commons.lang3.StringUtils.join(nearestGndIds, ',');
                                            primary.setNearestGndIds(nearestGnds);

                                            logger.info("Count: {}, found nearest gnds: {} in s_district: {} for p_gnd: {}", count.get(), nearestGnds, secondaryDistrictId, primary.getGnDivision().getId());
                                        } else {
                                            String nearestGnds = org.apache.commons.lang3.StringUtils.join(nearestGndIds, ',');
                                            primary.setNearestGndIds(nearestGnds);
                                            logger.info("Count: {}, found nearest gnds: {} in s_district: {} for p_gnd: {} without nearest gnds.", count.get(), nearestGnds, secondaryDistrictId, primary.getGnDivision().getId());
                                        }
                                    }
                                }
                        )
                ).join();

                logger.info("***************************************************************");
                gndCoordinateDetailRepository.saveAll(secondaryDistrictGndCoordinateDetails);
                logger.info("===================================================================");
                logger.info("nearest gnds find complete for secondary district: {}", secondaryDistrictId);
                logger.info("===================================================================");

            });

            logger.info("***************************************************************");
            gndCoordinateDetailRepository.saveAll(primaryDistrictGndCoordinateDetails);
            logger.info("===================================================================");
            logger.info("nearest gnds find complete for primary district");
            logger.info("===================================================================");
        });


    }

    private boolean checkGivenTwoGndsAreRelated(List<CoordinatesSet> primary, List<CoordinatesSet> secondary) {

        for(CoordinatesSet coordinatesSetPrimary : primary) {
            for(List<BigDecimal[]> primaryCoordinates : coordinatesSetPrimary.getCoordinates()) {
                for(BigDecimal[] primaryCoordinate : primaryCoordinates) {
                    if(checkPrimaryCoordinateWithSecondaryCoordinateSets(primaryCoordinate, secondary)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkPrimaryCoordinateWithSecondaryCoordinateSets(BigDecimal[] primaryCoordinate, List<CoordinatesSet> secondary) {

        for(CoordinatesSet coordinatesSetSecondary : secondary) {
            for(List<BigDecimal[]> secondaryCoordinates : coordinatesSetSecondary.getCoordinates()) {
                for(BigDecimal[] secondaryCoordinate : secondaryCoordinates) {
                    if(primaryCoordinate[0].compareTo(secondaryCoordinate[0]) == 0 && primaryCoordinate[1].compareTo(secondaryCoordinate[1]) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void fillNearestGndTable() {

        List<NearestGndDetail> nearestGndDetails;

        for(Division division : divisionService.getAllDivisions()) {

            for (GndCoordinateDetail gndCoordinateDetail : gndCoordinateDetailRepository.findGndCoordinateDetailsForDivisionId(division.getId())) {

                if (gndCoordinateDetail.getNearestGndIds() != null) {

                    nearestGndDetails = new ArrayList<>();

                    for (GramaNiladariDivision gramaNiladariDivision : gramaNiladariDivisionRepository.findGramaNiladariDivisionsByIdIn(Stream.of(gndCoordinateDetail.getNearestGndIds().split(","))
                            .map(String::trim)
                            .map(Long::parseLong).collect(Collectors.toSet()))) {

                        nearestGndDetails.add(new NearestGndDetail(gndCoordinateDetail, gramaNiladariDivision));
                    }

                    gndCoordinateDetail.setNearestGndDetails(nearestGndDetails);

                    gndCoordinateDetailRepository.save(gndCoordinateDetail);

                    //logger.info("Save nearest gnd details for gnd:{}", gndCoordinateDetail.getGnDivision().getId());
                } else {
                    logger.warn("No nearest gnds for gnd: {}", gndCoordinateDetail.getGnDivision().getId());
                }

            }

            logger.info("Save nearest gnd details for division:{}", division.getId());
        }
    }

    private void setGndCenterCoordinates() {

        try(FileInputStream excelFile = new FileInputStream(new File("src/main/resources/GN_Boundary_TableToExcel_1 (3).xls"))) {

            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(0);

            GramaNiladariDivision[] gramaNiladariDivisions = new GramaNiladariDivision[1];
            GndCoordinateDetail[] gndCoordinateDetails = new GndCoordinateDetail[1];

            sheet.forEach(row -> {

                gramaNiladariDivisions[0] =
                        gramaNiladariDivisionRepository
                                .findGramaNiladariDivisionByDivisionNameAndDivisionDistrictNameAndDivisionDistrictProvinceNameAndGndNo(
                                        row.getCell(3).getStringCellValue(),
                                        row.getCell(2).getStringCellValue(),
                                        row.getCell(1).getStringCellValue(),
                                        row.getCell(5).getStringCellValue());

                if(gramaNiladariDivisions[0] != null) {
                    gndCoordinateDetails[0] = gndCoordinateDetailRepository.findGndCoordinateDetailByGnDivisionId(gramaNiladariDivisions[0].getId());
                    gndCoordinateDetails[0].setLon(String.valueOf(row.getCell(7).getNumericCellValue()));
                    gndCoordinateDetails[0].setLat(String.valueOf(row.getCell(6).getNumericCellValue()));

                    gndCoordinateDetailRepository.save(gndCoordinateDetails[0]);
                } else {
                    if(row.getRowNum() != 0) {
                        logger.error("unable to find gnd for fid: {}, {}", row.getCell(0).getNumericCellValue(), row.getCell(2).getStringCellValue());
                    }
                }

                //logger.info("process row index: {}", row.getRowNum());
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
