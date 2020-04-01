package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.GuardianDto;
import lk.uom.fit.qms.dto.QuarantineUserRequestDto;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.CountryService;
import lk.uom.fit.qms.service.QuarantineUserService;
import lk.uom.fit.qms.util.enums.RoleType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class QuarantineUserServiceImpl implements QuarantineUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private QuarantineUserRepository quarantineUserRepository;

    @Autowired
    private ReportUserRepository reportUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CountryService countryService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GramaSewaDevisionRepository gramaSewaDevisionRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void createUser(QuarantineUserRequestDto quarantineUserRequestDto, Long addedUserId) {

        logger.debug("addedUserId: {}", addedUserId);

        //Todo: validate if quarantineUser id exist
        //Tod: need to implemet rUser ---> quser

        QuarantineUser quarantineUser = modelMapper.map(quarantineUserRequestDto, QuarantineUser.class);
        quarantineUser.setArrivedCountry(countryService.findOne(quarantineUserRequestDto.getCountryId()));

        List<QuarantineUserInspectDetails> quarantineUserInspectDetailList = new ArrayList<>();
        if(quarantineUserRequestDto.getInspectorIds() != null) {
            quarantineUserRequestDto.getInspectorIds().forEach(inspectorId -> {

                QuarantineUserInspectDetails quarantineUserInspectDetails = new QuarantineUserInspectDetails();
                quarantineUserInspectDetails.setReportUser(reportUserRepository.findReportUserById(inspectorId));
                quarantineUserInspectDetails.setQuarantineUser(quarantineUser);

                quarantineUserInspectDetailList.add(quarantineUserInspectDetails);
            });
        }

        quarantineUser.setQuarantineUserInspectDetails(quarantineUserInspectDetailList);

        if(quarantineUserRequestDto.getGuardianDetails() != null) {
            GuardianDto guardianDto = quarantineUserRequestDto.getGuardianDetails();

            User guardian;
            if(guardianDto.getId() != null) {
               guardian = userRepository.findUserById(guardianDto.getId());
               guardian.setNic(guardianDto.getNic());
               guardian.setMobile(guardianDto.getMobile());
               guardian.setPassportNo(guardianDto.getPassportNo());
            }
            else {
                guardian = modelMapper.map(guardianDto, User.class);
            }
            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findRoleByName(RoleType.GUARDIAN));
            userRole.setUser(guardian);

            guardian.getUserRoles().add(userRole);
            quarantineUser.setGuardian(userRepository.save(guardian));
        }

        UserRole userRole = new UserRole();
        userRole.setRole(roleRepository.findRoleByName(RoleType.Q_USER));
        userRole.setUser(quarantineUser);

        quarantineUser.getUserRoles().add(userRole);
        quarantineUser.setAddedBy(userRepository.findUserById(addedUserId));

        Address address = quarantineUser.getAddress();
        address.setGramaSewaDivision(gramaSewaDevisionRepository.findGramaSewaDivisionById(quarantineUserRequestDto.getGramaSewaDivisionId()));

        quarantineUser.setAddress(addressRepository.save(address));
        quarantineUserRepository.save(quarantineUser);
    }
}
