package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.Division;
import lk.uom.fit.qms.model.Station;
import lk.uom.fit.qms.repository.DivisionRepository;
import lk.uom.fit.qms.repository.QuarantineUserRepository;
import lk.uom.fit.qms.repository.StationRepository;
import lk.uom.fit.qms.service.GraphService;
import lk.uom.fit.qms.service.ReportUserService;
import lk.uom.fit.qms.service.UserService;
import lk.uom.fit.qms.util.enums.GraphType;
import lk.uom.fit.qms.util.enums.QuserType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    private final UserService userService;

    private final QuarantineUserRepository quarantineUserRepository;

    private final ReportUserService reportUserService;

    private final StationRepository stationRepository;

    private final DivisionRepository divisionRepository;

    private final ZoneId zoneId;

    @Autowired
    public GraphServiceImpl(
            UserService userService, QuarantineUserRepository quarantineUserRepository, ZoneId zoneId,
            ReportUserService reportUserService, StationRepository stationRepository, DivisionRepository divisionRepository) {
        this.userService = userService;
        this.quarantineUserRepository = quarantineUserRepository;
        this.reportUserService = reportUserService;
        this.stationRepository = stationRepository;
        this.divisionRepository = divisionRepository;
        this.zoneId = zoneId;
    }

    @Override
    public Object getGraphDetails(GraphRequestDto graphRequestDto, Long userId, List<UserRoleDto> userRoles) throws QmsException {

        logger.info("GraphRequest: {}, requestUser: {}", graphRequestDto, userId);

        boolean isRoot = userService.checkUserIsRoot(userRoles);

        if(graphRequestDto.getStationIds() != null) {

            graphRequestDto.setStationIds(graphRequestDto.getStationIds().stream().distinct().collect(Collectors.toList()));

            if(!isRoot) {
                List<Long> adminAllowedStationIds = reportUserService.getAdminUserStations(userId);
                graphRequestDto.getStationIds().retainAll(adminAllowedStationIds);
            }
        } else {
            graphRequestDto.setStationIds(new ArrayList<>());
        }

        if(graphRequestDto.getGraphType() == GraphType.DIVISION) {
            if (!ObjectUtils.isEmpty(graphRequestDto.getDivisionIds())) {
                List<Long> stationIdList = divisionRepository.getStationIdsForGivenDivisions(graphRequestDto.getDivisionIds());
                if (!isRoot) {
                    List<Long> adminAllowedStationIds = reportUserService.getAdminUserStations(userId);
                    stationIdList.retainAll(adminAllowedStationIds);
                }
                graphRequestDto.setStationIds(stationIdList);
            } else {
                graphRequestDto.setDivisionIds(new ArrayList<>());
            }
        }

        switch (graphRequestDto.getGraphType()) {

            case AGE:
                return getAgeGraphDetails(graphRequestDto);
            case DATE:
                return getDateGraphDetails(graphRequestDto);
            case GROWTH:
                return getGrowthGraphDetails(graphRequestDto);
            case STATION:
                return getStationGraphDetails(graphRequestDto);
            default:
                return getDivisionGraphDetails(graphRequestDto);
        }
    }

    private Object getAgeGraphDetails(GraphRequestDto graphRequestDto) {

        List<Long []> ageGraphData;

        if(graphRequestDto.getQuserType() == QuserType.BOTH) {
            ageGraphData = quarantineUserRepository.getQuserCountAgainstAgeGroup(graphRequestDto.getStationIds());
        } else {
            boolean isCompleted = true;
            if(graphRequestDto.getQuserType() == QuserType.NOT_DONE) {
                isCompleted = false;
            }
            ageGraphData = quarantineUserRepository.getQuserCountAgainstAgeGroup(isCompleted, graphRequestDto.getStationIds());
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

    private Object getStationGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        if(graphRequestDto.getStationIds().isEmpty()) {
            logger.warn("Stations not selected");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Select at lease one station");
        }

        List<Object []> stationGraphData;

        if(graphRequestDto.getQuserType() == QuserType.BOTH) {
            stationGraphData = stationRepository.getQuserAgainstStation(graphRequestDto.getStationIds());
        } else {
            boolean isCompleted = true;
            if(graphRequestDto.getQuserType() == QuserType.NOT_DONE) {
                isCompleted = false;
            }
            stationGraphData = stationRepository.getQuserAgainstStation(isCompleted, graphRequestDto.getStationIds());
        }

        TreeMap<String, Long> dataMap = new TreeMap<>();
        List<Long> stationWithValue = new ArrayList<>();

        for(Object[] result : stationGraphData) {
            dataMap.put((String) result[0], ((Number) result[2]).longValue());
            stationWithValue.add(((Number) result[1]).longValue());
        }

        graphRequestDto.getStationIds().removeAll(stationWithValue);
        List<Station> stationsWithoutValue = stationRepository.findStationsByGivenIdList(graphRequestDto.getStationIds());
        stationsWithoutValue.forEach(station -> dataMap.put(station.getName(), 0L));

        GraphResponse graphResponse = new GraphResponse();
        List<GraphData> data = new ArrayList<>();

        dataMap.forEach((key, value) -> data.add(new GraphData(key, value)));
        graphResponse.setData(data);

        return graphResponse;
    }

    private Object getDivisionGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        if(graphRequestDto.getStationIds().isEmpty()) {
            logger.warn("Divisions not selected");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Select at lease one division");
        }

        List<Object []> divisionGraphData;

        if(graphRequestDto.getQuserType() == QuserType.BOTH) {
            divisionGraphData = divisionRepository.getQuserAgainstDivision(graphRequestDto.getStationIds(), graphRequestDto.getDivisionIds());
        } else {
            boolean isCompleted = true;
            if(graphRequestDto.getQuserType() == QuserType.NOT_DONE) {
                isCompleted = false;
            }
            divisionGraphData = divisionRepository.getQuserAgainstDivision(isCompleted, graphRequestDto.getStationIds(), graphRequestDto.getDivisionIds());
        }

        TreeMap<String, Long> dataMap = new TreeMap<>();
        List<Long> divisionWithValue = new ArrayList<>();

        for(Object[] result : divisionGraphData) {
            dataMap.put((String) result[0], ((Number) result[2]).longValue());
            divisionWithValue.add(((Number) result[1]).longValue());
        }

        graphRequestDto.getDivisionIds().removeAll(divisionWithValue);
        List<Division> divisonsWithoutValue = divisionRepository.findDivisionsByIdIn(graphRequestDto.getDivisionIds());
        divisonsWithoutValue.forEach(division -> dataMap.put(division.getName(), 0L));

        GraphResponse graphResponse = new GraphResponse();
        List<GraphData> data = new ArrayList<>();

        dataMap.forEach((key, value) -> data.add(new GraphData(key, value)));
        graphResponse.setData(data);

        return graphResponse;
    }

    private Object getDateGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        setDateRange(graphRequestDto);

        int diff = (int) DAYS.between(graphRequestDto.getStartDate(), graphRequestDto.getEndDate());

        GraphResponse graphResponse = new GraphResponse();
        List<GraphData> data = new ArrayList<>();

        LocalDate initDate = graphRequestDto.getStartDate();

        for(int day = 0; day <= diff; day++) {

            LocalDate date = initDate.plusDays(day);
            data.add(new GraphData(date.toString(), quarantineUserRepository.getQuserCountAgainstReportedDate(graphRequestDto.getStationIds(), date)));
        }

        graphResponse.setData(data);
        return graphResponse;
    }

    private Object getGrowthGraphDetails(GraphRequestDto graphRequestDto) throws QmsException {

        setDateRange(graphRequestDto);

        int diff = (int) DAYS.between(graphRequestDto.getStartDate(), graphRequestDto.getEndDate());

        GraphResponseV2 graphResponse = new GraphResponseV2();
        List<GraphDataV2> data = new ArrayList<>();

        LocalDate initDate = graphRequestDto.getStartDate();

        long totalQuarantineUsers = 0;
        long totalQuarantinePeriodOverUsers = 0;

        for(int day = 0; day <= diff; day++) {

            LocalDate date = initDate.plusDays(day);

            long qUsersPerDay = quarantineUserRepository.getQuserCountAgainstReportedDate(graphRequestDto.getStationIds(), date);
            long overUsersPerDay = quarantineUserRepository.getQuserCountAgainstCompletedDate(graphRequestDto.getStationIds(), date);

            totalQuarantineUsers = (totalQuarantineUsers + qUsersPerDay) - overUsersPerDay;
            totalQuarantinePeriodOverUsers += overUsersPerDay;

            data.add(new GraphDataV2(date.toString(), totalQuarantineUsers, totalQuarantinePeriodOverUsers));
        }

        graphResponse.setData(data);

        return graphResponse;
    }

    private void setDateRange(GraphRequestDto graphRequestDto) throws QmsException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(graphRequestDto.getStartDate() == null) {
            graphRequestDto.setStartDate(LocalDate.parse(startDate, dtf));
        }

        if(graphRequestDto.getEndDate() == null) {
            graphRequestDto.setEndDate(LocalDate.now(zoneId));
        }

        if(graphRequestDto.getStartDate().isAfter(graphRequestDto.getEndDate())) {
            logger.warn("Start date is after end date");
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.BAD_REQUEST, "Start date should behind the end date");
        }
    }
}
