package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.GndRiskDetailResponse;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.GndRiskDetailRepository;
import lk.uom.fit.qms.service.*;
import lk.uom.fit.qms.util.enums.RiskType;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/9/2020
 * @Package lk.uom.fit.qms.service.impl
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class GndRiskDetailServiceImpl implements GndRiskDetailService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${gnd.high.risk.period}")
    private Integer highRiskPeriod;

    @Value("${gnd.moderate.risk.period}")
    private Integer moderateRiskPeriod;

    @Value("${default.risk.analyse.page.size}")
    private int pageSize;

    private final PositiveCovidDetailService positiveCovidDetailService;

    private final GndRiskTypeTimeService gndRiskTypeTimeService;

    private final ZoneId zoneId;

    private final GndRiskDetailRepository gndRiskDetailRepository;

    private final NearestGndDetailService nearestGndDetailService;

    private final DistrictService districtService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public GndRiskDetailServiceImpl(
            PositiveCovidDetailService positiveCovidDetailService, GndRiskTypeTimeService gndRiskTypeTimeService,
            ZoneId zoneId, GndRiskDetailRepository gndRiskDetailRepository,
            NearestGndDetailService nearestGndDetailService, DistrictService districtService) {

        this.positiveCovidDetailService = positiveCovidDetailService;
        this.gndRiskTypeTimeService = gndRiskTypeTimeService;
        this.zoneId = zoneId;
        this.gndRiskDetailRepository = gndRiskDetailRepository;
        this.nearestGndDetailService = nearestGndDetailService;
        this.districtService = districtService;
    }

    @PostConstruct
    private void init() {
        logger.info("start init method for already exists pc details");
        addGndRiskDetailForAlreadyExistsPcDetail();
    }

    @Async
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateGndRiskDetail(QuarantineUser quarantineUser) {

        logger.debug("update gnd risk map for user: {}", quarantineUser.getId());

        GramaNiladariDivision gramaNiladariDivision = quarantineUser.getAddress().getGnDivision();
        Long userGndId = gramaNiladariDivision.getId();

        PositiveCovidDetail latestPcDetail = positiveCovidDetailService.getLatestPcDetailForGivenGnd(userGndId);
        GndRiskDetail persistGndRiskDetail = gndRiskDetailRepository.findGndRiskDetailByGnDivisionId(userGndId);

        if (latestPcDetail != null) {

            GndRiskDetail gndRiskDetail = getGndRiskDetailFromPcDetail(latestPcDetail, gramaNiladariDivision);

            if (persistGndRiskDetail != null) {
                analyzeGndRiskDetail(persistGndRiskDetail, gndRiskDetail);
            } else {
                analyzeGndRiskDetail(gndRiskDetail);
            }
        } else {

            if (persistGndRiskDetail != null) {
                analyzePersistGndRiskDetail(persistGndRiskDetail);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateGndRiskDetails() {

        Pageable pageable = PageRequest.of(0, pageSize);

        Page<GndRiskDetail> initGndRiskDetailPage = gndRiskDetailRepository.findAll(pageable);
        initGndRiskDetailPage.forEach(this::evaluateGndRiskDetail);

        int totalPages = initGndRiskDetailPage.getTotalPages();

        if(totalPages > 1) {

            for(int page = 1; page < totalPages; page++) {

                pageable = PageRequest.of(page, pageSize);
                gndRiskDetailRepository.findAll(pageable).forEach(this::evaluateGndRiskDetail);
            }
        }
    }

    @Override
    public List<GndRiskDetailResponse> getGndRiskDetailsForDistrict(Long districtId) throws QmsException {

        districtService.checkDistrictExists(districtId);
        Map<Long, GndRiskDetail> gndRiskDetailMap = new HashMap<>();

        Map<Long, GndRiskDetail> secondaryGndRiskDetailMap = new HashMap<>();

        gndRiskDetailRepository.findGndRiskDetailsByDistrict(districtId)
                .forEach(gndRiskDetail -> gndRiskDetailMap.put(gndRiskDetail.getId(), gndRiskDetail));

        gndRiskDetailMap.forEach((id, primary) -> {

            gndRiskDetailRepository.findGndRiskDetailsByParentRiskDetailId(id).stream()
                    .filter(secondary -> !gndRiskDetailMap.containsKey(secondary.getId()))
                    .forEach(secondary -> secondaryGndRiskDetailMap.put(secondary.getId(), secondary));

            if(primary.getParentRiskDetail() != null && !gndRiskDetailMap.containsKey(primary.getParentRiskDetail().getId())) {
                secondaryGndRiskDetailMap.put(primary.getParentRiskDetail().getId(), primary.getParentRiskDetail());
            }
        });

        gndRiskDetailMap.putAll(secondaryGndRiskDetailMap);

        Type type = new TypeToken<List<GndRiskDetailResponse>>() {}.getType();
        return modelMapper.map(gndRiskDetailMap.values(), type);
    }

    private GndRiskDetail getGndRiskDetailFromPcDetail(PositiveCovidDetail latestPcDetail, GramaNiladariDivision gramaNiladariDivision) {

        GndRiskDetail gndRiskDetail = new GndRiskDetail();

        Integer highRiskDays = highRiskPeriod;
        Integer moderateRiskDays = moderateRiskPeriod;

        int remainingDays = 0;
        RiskType riskType = RiskType.NO_RISK;
        LocalDate startDate = null;
        LocalDate endDate = null;

        GndRiskTypeTime gndRiskTypeTime = gndRiskTypeTimeService.findGndRiskTypeTimeForGnd(gramaNiladariDivision.getId());
        if(gndRiskTypeTime != null) {

            if (gndRiskTypeTime.getHighRiskPeriod() != null) {
                highRiskDays = gndRiskTypeTime.getHighRiskPeriod();
            }
            if (gndRiskTypeTime.getModerateRiskPeriod() != null) {
                moderateRiskDays = gndRiskTypeTime.getModerateRiskPeriod();
            }
        }

        LocalDate currentDate = LocalDate.now(zoneId);
        int diff = (int) DAYS.between(latestPcDetail.getIdentifiedDate(), currentDate);

        if (diff <= highRiskDays) {

            riskType = RiskType.HIGH;
            startDate = latestPcDetail.getIdentifiedDate();
            endDate = startDate.plusDays(highRiskDays + 1L);
        } else if (diff <= (highRiskDays + moderateRiskDays)) {

            riskType = RiskType.MODERATE;
            startDate = latestPcDetail.getIdentifiedDate().plusDays(highRiskDays + 1L);
            endDate = startDate.plusDays(moderateRiskDays + 1L);
        }

        if (diff == 0) {

            remainingDays = highRiskDays;
        } else if (diff <= highRiskDays) {

            remainingDays = highRiskDays - (diff - 1);
        } else if (diff == (highRiskDays + 1)) {

            remainingDays = moderateRiskDays;
        } else if (diff <= (highRiskDays + moderateRiskDays)) {

            remainingDays = (highRiskDays + moderateRiskDays) - (diff - 1);
        }

        gndRiskDetail.setEndDate(endDate);
        gndRiskDetail.setStartDate(startDate);
        gndRiskDetail.setGnDivision(gramaNiladariDivision);
        gndRiskDetail.setLatestPcDetail(latestPcDetail);
        gndRiskDetail.setRemainingPeriod(remainingDays);
        gndRiskDetail.setRiskType(riskType);
        return gndRiskDetail;
    }

    // when persist and update gnd risk detail exists
    private void analyzeGndRiskDetail(GndRiskDetail persistGndRiskDetail, GndRiskDetail gndRiskDetail) {

        RiskType riskType = gndRiskDetail.getRiskType();
        RiskType persistRiskType = persistGndRiskDetail.getRiskType();

        gndRiskDetail.setId(persistGndRiskDetail.getId());

        if (riskType == RiskType.HIGH) {

            setChildrenRiskGndDetails(gndRiskDetail);
        } else if (riskType == RiskType.MODERATE) {

            if (persistRiskType == RiskType.HIGH) {

                setChildrenRiskGndDetails(gndRiskDetail);
                checkGndWithModerateOrNoRiskHasNearestGndWithHighRisk(gndRiskDetail, null);

            } else if (persistRiskType == RiskType.MODERATE && persistGndRiskDetail.getParentRiskDetail() != null &&
                    persistGndRiskDetail.getStartDate().isAfter(gndRiskDetail.getStartDate())) {

                gndRiskDetail.setParentRiskDetail(persistGndRiskDetail.getParentRiskDetail());
                gndRiskDetail.setRemainingPeriod(persistGndRiskDetail.getRemainingPeriod());
                gndRiskDetail.setEndDate(persistGndRiskDetail.getEndDate());
                gndRiskDetail.setStartDate(persistGndRiskDetail.getStartDate());

                setChildrenRiskGndDetails(gndRiskDetail);
            } else {

                setChildrenRiskGndDetails(gndRiskDetail);
            }
        } else {
            if ((persistRiskType == RiskType.MODERATE && persistGndRiskDetail.getParentRiskDetail() == null) || persistRiskType == RiskType.HIGH) {

                removeChildGndRiskDetailsForGivenGndRiskDetail(gndRiskDetail);
                checkGndWithModerateOrNoRiskHasNearestGndWithHighRisk(gndRiskDetail, null);
            } else {

                gndRiskDetail.setParentRiskDetail(persistGndRiskDetail.getParentRiskDetail());
                gndRiskDetail.setRemainingPeriod(persistGndRiskDetail.getRemainingPeriod());
                gndRiskDetail.setEndDate(persistGndRiskDetail.getEndDate());
                gndRiskDetail.setStartDate(persistGndRiskDetail.getStartDate());
                gndRiskDetail.setRiskType(persistGndRiskDetail.getRiskType());
                gndRiskDetail.setChildrenRiskDetail(persistGndRiskDetail.getChildrenRiskDetail());
            }
        }

        gndRiskDetailRepository.save(gndRiskDetail);
    }

    // only update gnd risk detail exists
    private void analyzeGndRiskDetail(GndRiskDetail gndRiskDetail) {

        if(gndRiskDetail.getRiskType() != RiskType.NO_RISK) {

            setChildrenRiskGndDetails(gndRiskDetail);
            gndRiskDetailRepository.save(gndRiskDetail);
        }

    }

    // only persist risk detail exists
    private void analyzePersistGndRiskDetail(GndRiskDetail persistGndRiskDetail) {

        RiskType persistRiskType = persistGndRiskDetail.getRiskType();

        if ((persistRiskType == RiskType.MODERATE && persistGndRiskDetail.getParentRiskDetail() == null) || persistRiskType == RiskType.HIGH) {

            removeChildGndRiskDetailsForGivenGndRiskDetail(persistGndRiskDetail);
            checkGndWithModerateOrNoRiskHasNearestGndWithHighRisk(persistGndRiskDetail, null);
        }

        gndRiskDetailRepository.save(persistGndRiskDetail);
    }

    private void setChildrenRiskGndDetails(GndRiskDetail gndRiskDetail) {

        LocalDate startDate = gndRiskDetail.getLatestPcDetail().getIdentifiedDate();

        Map<Long, GramaNiladariDivision> nearestGnDivisionMap = new HashMap<>();

        nearestGndDetailService.getNearestGnDivisionDetailsForGivenGnd(gndRiskDetail.getGnDivision().getId())
                .forEach(gramaNiladariDivision -> nearestGnDivisionMap.put(gramaNiladariDivision.getId(), gramaNiladariDivision));

        Map<Long, GndRiskDetail> nearestGnDivisionRiskDetailMap = new HashMap<>();

        gndRiskDetailRepository.findGndRiskDetailsForGivenGndIds(nearestGnDivisionMap.keySet())
                .forEach(nearestGndRiskDetail -> nearestGnDivisionRiskDetailMap.put(nearestGndRiskDetail.getGnDivision().getId(), nearestGndRiskDetail));

        nearestGnDivisionMap.forEach((nearestGndId, nearestGnDivision) -> {

            GndRiskDetail nearestGndRiskDetail = nearestGnDivisionRiskDetailMap.get(nearestGndId);

            if (nearestGndRiskDetail == null) {

                nearestGndRiskDetail = getChildRiskDetail(startDate, nearestGnDivision);

                if (nearestGndRiskDetail.getRiskType() != RiskType.NO_RISK) {

                    nearestGndRiskDetail.setParentRiskDetail(gndRiskDetail);
                    gndRiskDetail.getChildrenRiskDetail().add(nearestGndRiskDetail);
                }
            } else {

                if ((nearestGndRiskDetail.getRiskType() == RiskType.MODERATE && startDate.isAfter(nearestGndRiskDetail.getStartDate()))
                        || (nearestGndRiskDetail.getParentRiskDetail() != null && Objects.equals(nearestGndRiskDetail.getParentRiskDetail().getId(), gndRiskDetail.getId()))) {

                    GndRiskDetail newChildGndRiskDetail = getChildRiskDetail(startDate, nearestGnDivision);
                    newChildGndRiskDetail.setId(nearestGndRiskDetail.getId());

                    if (newChildGndRiskDetail.getRiskType() != RiskType.NO_RISK) {

                        newChildGndRiskDetail.setParentRiskDetail(gndRiskDetail);
                        gndRiskDetail.getChildrenRiskDetail().add(newChildGndRiskDetail);
                    } else {
                        checkChildGndWithNoRiskHasNearestGndWithHighRisk(newChildGndRiskDetail, gndRiskDetail.getGnDivision().getId());
                    }
                }
            }
        });
    }

    private GndRiskDetail getChildRiskDetail(LocalDate startDate, GramaNiladariDivision gramaNiladariDivision) {

        GndRiskDetail gndRiskDetail = new GndRiskDetail();

        Integer moderateRiskDays = moderateRiskPeriod;

        int remainingDays = 0;
        RiskType riskType = RiskType.NO_RISK;
        LocalDate endDate = null;

        GndRiskTypeTime gndRiskTypeTime = gndRiskTypeTimeService.findGndRiskTypeTimeForGnd(gramaNiladariDivision.getId());
        if(gndRiskTypeTime != null && gndRiskTypeTime.getModerateRiskPeriod() != null) {

            moderateRiskDays = gndRiskTypeTime.getModerateRiskPeriod();
        }

        LocalDate currentDate = LocalDate.now(zoneId);
        int diff = (int) DAYS.between(startDate, currentDate);

        if (diff <= moderateRiskDays) {

            riskType = RiskType.MODERATE;
            endDate = startDate.plusDays(moderateRiskDays + 1L);
        }

        if (diff == 0) {

            remainingDays = moderateRiskDays;
        } else if (diff <= moderateRiskDays) {

            remainingDays = moderateRiskDays - (diff - 1);
        }

        gndRiskDetail.setEndDate(endDate);
        gndRiskDetail.setStartDate(startDate);
        gndRiskDetail.setGnDivision(gramaNiladariDivision);
        gndRiskDetail.setRemainingPeriod(remainingDays);
        gndRiskDetail.setRiskType(riskType);

        return gndRiskDetail;
    }

    private void checkGndWithModerateOrNoRiskHasNearestGndWithHighRisk(GndRiskDetail gndRiskDetail, Long parentGndId) {

        List<Long> nearestGndIds = new ArrayList<>();

        nearestGndDetailService.getNearestGnDivisionDetailsForGivenGnd(gndRiskDetail.getGnDivision().getId()).stream()
                .filter(gramaNiladariDivision -> !Objects.equals(gramaNiladariDivision.getId(), parentGndId))
                .forEach(gramaNiladariDivision -> nearestGndIds.add(gramaNiladariDivision.getId()));

        GndRiskDetail nearestGndWithHighRisk = null;

        if(!nearestGndIds.isEmpty()) {
            nearestGndWithHighRisk = gndRiskDetailRepository.findTopByGnDivisionIdIsInAndRiskTypeOrderByStartDateDesc(nearestGndIds, RiskType.HIGH);
        }

        if(gndRiskDetail.getRiskType() == RiskType.NO_RISK && nearestGndWithHighRisk == null) {

            gndRiskDetail.setDeleted(true);
        } else if (nearestGndWithHighRisk != null && (gndRiskDetail.getRiskType() == RiskType.NO_RISK ||
                (gndRiskDetail.getRiskType() == RiskType.MODERATE && nearestGndWithHighRisk.getStartDate().isAfter(gndRiskDetail.getStartDate())))) {

            GndRiskDetail newGndRiskDetail = getChildRiskDetail(nearestGndWithHighRisk.getStartDate(), gndRiskDetail.getGnDivision());

            gndRiskDetail.setParentRiskDetail(nearestGndWithHighRisk);
            gndRiskDetail.setRemainingPeriod(newGndRiskDetail.getRemainingPeriod());
            gndRiskDetail.setEndDate(newGndRiskDetail.getEndDate());
            gndRiskDetail.setStartDate(newGndRiskDetail.getStartDate());
            gndRiskDetail.setRiskType(newGndRiskDetail.getRiskType());
        }
    }

    private void checkChildGndWithNoRiskHasNearestGndWithHighRisk(GndRiskDetail childGndRiskDetail, Long parentGndId) {

        checkGndWithModerateOrNoRiskHasNearestGndWithHighRisk(childGndRiskDetail, parentGndId);
        gndRiskDetailRepository.save(childGndRiskDetail);
    }

    private void removeChildGndRiskDetailsForGivenGndRiskDetail(GndRiskDetail gndRiskDetail) {

        gndRiskDetail.setChildrenRiskDetail(new ArrayList<>());

        gndRiskDetailRepository.findGndRiskDetailsByParentRiskDetailId(gndRiskDetail.getId())
                .forEach(childGndRisk -> checkChildGndWithNoRiskHasNearestGndWithHighRisk(childGndRisk, gndRiskDetail.getGnDivision().getId()));
    }

    private void evaluateGndRiskDetail(GndRiskDetail gndRiskDetail) {

        Integer highRiskDays = highRiskPeriod;
        Integer moderateRiskDays = moderateRiskPeriod;

        int remainingDays = 0;
        RiskType riskType = gndRiskDetail.getRiskType();
        LocalDate startDate = gndRiskDetail.getStartDate();
        LocalDate endDate = gndRiskDetail.getStartDate();

        GndRiskTypeTime gndRiskTypeTime = gndRiskTypeTimeService.findGndRiskTypeTimeForGnd(gndRiskDetail.getGnDivision().getId());
        if(gndRiskTypeTime != null) {

            if (gndRiskTypeTime.getHighRiskPeriod() != null) {
                highRiskDays = gndRiskTypeTime.getHighRiskPeriod();
            }
            if (gndRiskTypeTime.getModerateRiskPeriod() != null) {
                moderateRiskDays = gndRiskTypeTime.getModerateRiskPeriod();
            }
        }

        LocalDate currentDate = LocalDate.now(zoneId);
        int diff = (int) DAYS.between(startDate, currentDate);

        if(riskType == RiskType.HIGH) {

            if (diff == 0) {

                remainingDays = highRiskDays;
            } else if (diff <= highRiskDays) {

                remainingDays = highRiskDays - (diff - 1);
            }
        } else {

            if (diff == 0) {

                remainingDays = moderateRiskDays;
            } else if (diff <= moderateRiskDays) {

                remainingDays = moderateRiskDays - (diff - 1);
            }
        }

        if (riskType == RiskType.HIGH && diff > highRiskDays) {

            riskType = RiskType.MODERATE;
            startDate = endDate;
            endDate = startDate.plusDays(moderateRiskDays + 1L);
            remainingDays = moderateRiskDays;
        } else if (riskType == RiskType.MODERATE && diff > moderateRiskDays) {

            riskType = RiskType.NO_RISK;
            gndRiskDetail.setDeleted(true);
        }

        gndRiskDetail.setEndDate(endDate);
        gndRiskDetail.setStartDate(startDate);
        gndRiskDetail.setRemainingPeriod(remainingDays);
        gndRiskDetail.setRiskType(riskType);

        gndRiskDetailRepository.save(gndRiskDetail);
    }

    private void addGndRiskDetailForAlreadyExistsPcDetail() {

        Map<Long, GramaNiladariDivision> gramaNiladariDivisionMap = new HashMap<>();

        positiveCovidDetailService.getAllGnDivisionDetailWithCovidPatent()
                .forEach(gramaNiladariDivision -> gramaNiladariDivisionMap.put(gramaNiladariDivision.getId(), gramaNiladariDivision));

        gramaNiladariDivisionMap.forEach((gndId, gnDivision) -> {

            PositiveCovidDetail latestPcDetail = positiveCovidDetailService.getLatestPcDetailForGivenGnd(gndId);

            GndRiskDetail gndRiskDetail = getGndRiskDetailFromPcDetail(latestPcDetail, gnDivision);

            analyzeGndRiskDetail(gndRiskDetail);
        });
    }
}
