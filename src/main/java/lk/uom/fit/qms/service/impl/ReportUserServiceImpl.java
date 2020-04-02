package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.ReportUserRequestDto;
import lk.uom.fit.qms.exception.BadRequestException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.ReportUserService;
import lk.uom.fit.qms.util.enums.RoleType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.service.impl.
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class ReportUserServiceImpl implements ReportUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ReportUserRepository reportUserRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(ReportUserRequestDto reportUserRequestDto, Long addedUserId) throws BadRequestException {

        logger.debug("addedUserId: {}", addedUserId);

        if(reportUserRequestDto.getMobile() == null) {
            logger.warn("Empty mobile num!");
            throw new BadRequestException(QmsExceptionCode.USR00X, "Mobile num can't be null");
        }

        if(reportUserRequestDto.getNic() == null) {
            logger.warn("Empty nic num!");
            throw new BadRequestException(QmsExceptionCode.USR00X, "NIC can't be null");
        }

        ReportUser reportUser = modelMapper.map(reportUserRequestDto, ReportUser.class);

        if(reportUserRequestDto.getId() == null) {
            reportUser.setUsername(reportUserRequestDto.getMobile());
            reportUser.setPassword(passwordEncoder.encode(reportUser.getNic()));
        }

        List<Station> grantLocations = stationRepository.findStationsByGivenIdList(reportUserRequestDto.getStationIdList());
        reportUser.setStations(grantLocations);

        UserRole userRole = new UserRole();
        userRole.setRole(roleRepository.findRoleByName(RoleType.Q_USER));
        userRole.setUser(reportUser);

        reportUser.getUserRoles().add(userRole);

        reportUserRepository.save(reportUser);
    }
}
