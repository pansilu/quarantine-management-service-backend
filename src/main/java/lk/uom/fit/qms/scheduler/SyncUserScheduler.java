package lk.uom.fit.qms.scheduler;

import lk.uom.fit.qms.service.AddressService;
import lk.uom.fit.qms.service.QuarantineUserService;
import lk.uom.fit.qms.util.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/12/2020
 * @Package lk.uom.fit.qms.scheduler
 * @company Axiata Digital Labs (pvt)Ltd.
 */

@Component
public class SyncUserScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final QuarantineUserService quarantineUserService;

    private final AddressService addressService;

    @Autowired
    public SyncUserScheduler(QuarantineUserService quarantineUserService, AddressService addressService) {
        this.quarantineUserService = quarantineUserService;
        this.addressService = addressService;
    }

    @Scheduled(cron = "${sync.cron}", zone = "${time.zone}") // reset user remaining days every mid night
    public void calUserRemainingDays() {

        MDC.put(Constant.LOG_IDENTIFIER_KEY, UUID.randomUUID().toString());

        logger.info("Inside the calUserRemainingDays method.");
        quarantineUserService.calUserRemainingDays();

        addressService.deleteIsolateAddresses();
    }
}
