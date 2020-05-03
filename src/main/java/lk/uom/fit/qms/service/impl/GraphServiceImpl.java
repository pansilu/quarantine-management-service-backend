package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.District;
import lk.uom.fit.qms.service.*;
import lk.uom.fit.qms.util.enums.CovidCaseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/11/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GraphServiceImpl implements GraphService {

    @Value("${start.date}")
    private String startDate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ZoneId zoneId;

    private final ProvinceService provinceService;

    private final DistrictService districtService;

    private final DivisionService divisionService;

    private final GramaNiladariDivisionService gramaNiladariDivisionService;

    private final PositiveCovidDetailService positiveCovidDetailService;

    private final DeceasedDetailService deceasedDetailService;

    @Autowired
    public GraphServiceImpl(
            ZoneId zoneId, ProvinceService provinceService, DistrictService districtService,
            DivisionService divisionService, GramaNiladariDivisionService gramaNiladariDivisionService,
            PositiveCovidDetailService positiveCovidDetailService, DeceasedDetailService deceasedDetailService) {

        this.zoneId = zoneId;
        this.provinceService = provinceService;
        this.districtService = districtService;
        this.divisionService = divisionService;
        this.gramaNiladariDivisionService = gramaNiladariDivisionService;
        this.positiveCovidDetailService = positiveCovidDetailService;
        this.deceasedDetailService = deceasedDetailService;
    }

    @Override
    public Object getGraphDetails(GraphRequestDto graphRequestDto, Long userId, List<UserRoleDto> userRoles) throws QmsException {

        logger.info("GraphRequest: {}, requestUser: {}", graphRequestDto, userId);

        if (graphRequestDto.getGndId() != null) {
            gramaNiladariDivisionService.getGramaNiladariDivision(graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            divisionService.checkDivisionExists(graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            districtService.checkDistrictExists(graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            provinceService.checkProvinceExits(graphRequestDto.getProvinceId());
        }

        switch (graphRequestDto.getGraphType()) {

            case AGE:
                return getAgeGraphDetails(graphRequestDto);
            case DAILY_COVID:
                return getDailyCovidGraph(graphRequestDto);
            case DISTRICT_COMPARISION:
                return getDistrictComparisionGraphDetails(graphRequestDto);
            default:
                return getCumulativeCovidCaseGraphDetails(graphRequestDto);
        }
    }

    private Object getAgeGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        validateCovidCaseType(graphRequestDto);

        List<Long []> ageGraphData;

        if(graphRequestDto.getCovidCaseType() == CovidCaseType.ACTIVE) {
            ageGraphData = getActiveCaseCountAgainstAge(graphRequestDto);
        } else if (graphRequestDto.getCovidCaseType() == CovidCaseType.RECOVERED) {
            ageGraphData = getRecoveredCaseCountAgainstAge(graphRequestDto);
        } else if (graphRequestDto.getCovidCaseType() == CovidCaseType.DECEASED) {
            ageGraphData = getDeceasedCaseCountAgainstAge(graphRequestDto);
        } else {
            ageGraphData = getAllCaseCountAgainstAge(graphRequestDto);
        }

        Long [] valueArray = ageGraphData.get(0);

        GraphResponse graphResponse = new GraphResponse();
        List<GraphData> data = new ArrayList<>();

        data.add(new GraphData("Under 18", valueArray[0] == null ? 0 : valueArray[0]));
        data.add(new GraphData("18-24", valueArray[1] == null ? 0 : valueArray[1]));
        data.add(new GraphData("25-34", valueArray[2] == null ? 0 : valueArray[2]));
        data.add(new GraphData("35-50", valueArray[3] == null ? 0 : valueArray[3]));
        data.add(new GraphData("51-65", valueArray[4] == null ? 0 : valueArray[4]));
        data.add(new GraphData("Over 65", valueArray[5] == null ? 0 : valueArray[5]));
        data.add(new GraphData("Not Recorded", valueArray[6] == null ? 0 : valueArray[6]));

        graphResponse.setData(data);
        return graphResponse;
    }

    private Object getDailyCovidGraph(GraphRequestDto graphRequestDto) throws QmsException {

        validateCovidCaseType(graphRequestDto);

        setDateRange(graphRequestDto, true);

        int diff = (int) DAYS.between(graphRequestDto.getStartDate(), graphRequestDto.getEndDate());

        GraphResponse graphResponse = new GraphResponse();
        List<GraphData> data = new ArrayList<>();

        LocalDate initDate = graphRequestDto.getStartDate();

        for(int day = 0; day <= diff; day++) {

            LocalDate date = initDate.plusDays(day);
            long dailyNewCases;
            if(graphRequestDto.getCovidCaseType() == CovidCaseType.DECEASED) {
                dailyNewCases = getNewDeceasedCasesPerDate(graphRequestDto, date);
            } else if (graphRequestDto.getCovidCaseType() == CovidCaseType.RECOVERED) {
                dailyNewCases = getNewRecoveredCasesPerDate(graphRequestDto, date);
            } else {
                dailyNewCases = getNewCasesPerDate(graphRequestDto, date);
            }
            data.add(new GraphData(date.toString(), dailyNewCases));
        }

        graphResponse.setData(data);
        return graphResponse;
    }

    private Object getCumulativeCovidCaseGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        setDateRange(graphRequestDto, true);

        int diff = (int) DAYS.between(graphRequestDto.getStartDate(), graphRequestDto.getEndDate());

        GraphResponseV3 graphResponse = new GraphResponseV3();
        List<GraphDataV3> data = new ArrayList<>();

        LocalDate initDate = graphRequestDto.getStartDate();

        long totalCases = 0;
        long totalActiveCases;
        long totalRecoveredCases = 0;
        long totalDeceasedCases = 0;

        for(int day = 0; day <= diff; day++) {

            LocalDate date = initDate.plusDays(day);

            long newCasesPerDay = getNewCasesPerDate(graphRequestDto, date);
            long newRecoveredCasesPerDay = getNewRecoveredCasesPerDate(graphRequestDto, date);
            long newDeceasedPerDay = getNewDeceasedCasesPerDate(graphRequestDto, date);

            totalCases += newCasesPerDay;
            totalRecoveredCases += newRecoveredCasesPerDay;
            totalDeceasedCases += newDeceasedPerDay;

            totalActiveCases = totalCases - (totalRecoveredCases + totalDeceasedCases);

            data.add(new GraphDataV3(date.toString(), totalCases, totalActiveCases, totalRecoveredCases, totalDeceasedCases));
        }

        graphResponse.setData(data);

        return graphResponse;
    }

    private Object getDistrictComparisionGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        validateCovidCaseType(graphRequestDto);

        if(CollectionUtils.isEmpty(graphRequestDto.getDistrictIdList())) {
            logger.warn("No districts were selected for district comparision graph");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to select at least one district");
        }

        List<String> selectedDistricts = new ArrayList<>();
        for(Long districtId : graphRequestDto.getDistrictIdList()) {
            selectedDistricts.add(districtService.findDistrictById(districtId).getName());
        }

        setDateRange(graphRequestDto, true);

        int diff = (int) DAYS.between(graphRequestDto.getStartDate(), graphRequestDto.getEndDate());
        LocalDate initDate = graphRequestDto.getStartDate();

        List<String> keys = new ArrayList<>();
        TreeMap<String, GraphDataArray> districtDataArrayMap = new TreeMap<>();

        for(int day = 0; day <= diff; day++) {

            LocalDate date = initDate.plusDays(day);
            keys.add(date.toString());

            List<Object []> districtData;

            if(graphRequestDto.getCovidCaseType() == CovidCaseType.DECEASED) {
                districtData = deceasedDetailService.getNewDeceasedCasesPerDateForGivenDistricts(graphRequestDto.getDistrictIdList(), date);
            } else if (graphRequestDto.getCovidCaseType() == CovidCaseType.RECOVERED) {
                districtData = positiveCovidDetailService.getNewCasesPerDateForGivenDistricts(graphRequestDto.getDistrictIdList(), date);
            } else {
                districtData = positiveCovidDetailService.getNewRecoveredCasesPerDateForGivenDistricts(graphRequestDto.getDistrictIdList(), date);
            }

            evaluateDistrictData(districtDataArrayMap, districtData, selectedDistricts);
        }

        return new GraphArrayResponse(keys, new ArrayList<>(districtDataArrayMap.values()));
    }

    private void setDateRange(GraphRequestDto graphRequestDto, boolean isDefault) throws QmsException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(graphRequestDto.getStartDate() == null || isDefault) {
            graphRequestDto.setStartDate(LocalDate.parse(startDate, dtf));
        }

        if(graphRequestDto.getEndDate() == null || isDefault) {
            graphRequestDto.setEndDate(LocalDate.now(zoneId));
        }

        if(graphRequestDto.getStartDate().isAfter(graphRequestDto.getEndDate())) {
            logger.warn("Start date is after end date");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Start date should behind the end date");
        }
    }

    private long getNewCasesPerDate(GraphRequestDto graphRequestDto, LocalDate date) {

        if (graphRequestDto.getGndId() != null) {
            return positiveCovidDetailService.getNewCasesPerDateAndGnd(date, graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return positiveCovidDetailService.getNewCasesPerDateAndDivision(date, graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return positiveCovidDetailService.getNewCasesPerDateAndDistrict(date, graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return positiveCovidDetailService.getNewCasesPerDateAndProvince(date, graphRequestDto.getProvinceId());
        } else {
            return positiveCovidDetailService.getNewCasesPerDate(date);
        }
    }

    private long getNewRecoveredCasesPerDate(GraphRequestDto graphRequestDto, LocalDate date) {

        if (graphRequestDto.getGndId() != null) {
            return positiveCovidDetailService.getNewRecoveredCasesPerDateAndGnd(date, graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return positiveCovidDetailService.getNewRecoveredCasesPerDateAndDivision(date, graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return positiveCovidDetailService.getNewRecoveredCasesPerDateAndDistrict(date, graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return positiveCovidDetailService.getNewRecoveredCasesPerDateAndProvince(date, graphRequestDto.getProvinceId());
        } else {
            return positiveCovidDetailService.getNewRecoveredCasesPerDate(date);
        }
    }

    private long getNewDeceasedCasesPerDate(GraphRequestDto graphRequestDto, LocalDate date) {

        if (graphRequestDto.getGndId() != null) {
            return deceasedDetailService.getNewDeceasedCasesPerDateAndGnd(date, graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return deceasedDetailService.getNewDeceasedCasesPerDateAndDivision(date, graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return deceasedDetailService.getNewDeceasedCasesPerDateAndDistrict(date, graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return deceasedDetailService.getNewDeceasedCasesPerDateAndProvince(date, graphRequestDto.getProvinceId());
        } else {
            return deceasedDetailService.getNewDeceasedCasesPerDate(date);
        }
    }

    private List<Long []> getActiveCaseCountAgainstAge(GraphRequestDto graphRequestDto) {

        if (graphRequestDto.getGndId() != null) {
            return positiveCovidDetailService.getActiveCaseCountAgainstAgeGroupAndGnd(graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return positiveCovidDetailService.getActiveCaseCountAgainstAgeGroupAndDivision(graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return positiveCovidDetailService.getActiveCaseCountAgainstAgeGroupAndDistrict(graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return positiveCovidDetailService.getActiveCaseCountAgainstAgeGroupAndProvince(graphRequestDto.getProvinceId());
        } else {
            return positiveCovidDetailService.getActiveCaseCountAgainstAgeGroup();
        }
    }

    private List<Long []> getAllCaseCountAgainstAge(GraphRequestDto graphRequestDto) {

        if (graphRequestDto.getGndId() != null) {
            return positiveCovidDetailService.getAllPositiveCaseCountAgainstAgeGroupAndGnd(graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return positiveCovidDetailService.getAllPositiveCaseCountAgainstAgeGroupAndDivision(graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return positiveCovidDetailService.getAllPositiveCaseCountAgainstAgeGroupAndDistrict(graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return positiveCovidDetailService.getAllPositiveCaseCountAgainstAgeGroupAndProvince(graphRequestDto.getProvinceId());
        } else {
            return positiveCovidDetailService.getAllPositiveCaseCountAgainstAgeGroup();
        }
    }

    private List<Long []> getRecoveredCaseCountAgainstAge(GraphRequestDto graphRequestDto) {

        if (graphRequestDto.getGndId() != null) {
            return positiveCovidDetailService.getRecoveredCaseCountAgainstAgeGroupAndGnd(graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return positiveCovidDetailService.getRecoveredCaseCountAgainstAgeGroupAndDivision(graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return positiveCovidDetailService.getRecoveredCaseCountAgainstAgeGroupAndDistrict(graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return positiveCovidDetailService.getRecoveredCaseCountAgainstAgeGroupAndProvince(graphRequestDto.getProvinceId());
        } else {
            return positiveCovidDetailService.getRecoveredCaseCountAgainstAgeGroup();
        }
    }

    private List<Long []> getDeceasedCaseCountAgainstAge(GraphRequestDto graphRequestDto) {

        if (graphRequestDto.getGndId() != null) {
            return deceasedDetailService.getDeceasedCaseCountAgainstAgeGroupAndGnd(graphRequestDto.getGndId());
        } else if (graphRequestDto.getDivisionId() != null) {
            return deceasedDetailService.getDeceasedCaseCountAgainstAgeGroupAndDivision(graphRequestDto.getDivisionId());
        } else if (graphRequestDto.getDistrictId() != null) {
            return deceasedDetailService.getDeceasedCaseCountAgainstAgeGroupAndDistrict(graphRequestDto.getDistrictId());
        } else if (graphRequestDto.getProvinceId() != null) {
            return deceasedDetailService.getDeceasedCaseCountAgainstAgeGroupAndProvince(graphRequestDto.getProvinceId());
        } else {
            return deceasedDetailService.getDeceasedCaseCountAgainstAgeGroup();
        }
    }

    private void validateCovidCaseType(GraphRequestDto graphRequestDto) throws QmsException {
        if(graphRequestDto.getCovidCaseType() == null) {
            logger.warn("Covid case type not selected");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Need to select covid case type");
    }
    }

    private void evaluateDistrictData(TreeMap<String, GraphDataArray> districtDataArrayMap, List<Object []> districtData, List<String> selectedDistricts) {

        List<String> districtWithValue = new ArrayList<>();

        List<String> districtWithoutValue = new ArrayList<>(selectedDistricts);

        for(Object[] result : districtData) {

            String district = (String) result[0];
            Long value = ((Number) result[1]).longValue();

            districtWithValue.add(district);

            if(!districtDataArrayMap.containsKey(district)) {
                districtDataArrayMap.put(district, new GraphDataArray(district, value));
            } else {
                districtDataArrayMap.get(district).getData().add(value);
            }
        }

        districtWithoutValue.removeAll(districtWithValue);

        districtWithoutValue.forEach(district -> {
            if(!districtDataArrayMap.containsKey(district)) {
                districtDataArrayMap.put(district, new GraphDataArray(district, 0L));
            } else {
                districtDataArrayMap.get(district).getData().add(0L);
            }
        });
    }
}
