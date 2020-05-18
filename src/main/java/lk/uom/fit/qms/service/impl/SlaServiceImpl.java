package lk.uom.fit.qms.service.impl;

import lk.uom.fit.qms.dto.SlaUserDto;
import lk.uom.fit.qms.dto.SlaUserMultiPageResDto;
import lk.uom.fit.qms.dto.SlaUserResponseDto;
import lk.uom.fit.qms.exception.QmsException;
import lk.uom.fit.qms.exception.pojo.QmsExceptionCode;
import lk.uom.fit.qms.model.PrivilegedUser;
import lk.uom.fit.qms.model.SlaUser;
import lk.uom.fit.qms.model.User;
import lk.uom.fit.qms.repository.*;
import lk.uom.fit.qms.service.SlaService;
import lk.uom.fit.qms.service.UserService;
import lk.uom.fit.qms.util.enums.BloodGroup;
import lk.uom.fit.qms.util.enums.RoleType;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SlaServiceImpl implements SlaService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private SlaRepository slaRepository;

    @Autowired
    private RegimentsRepository regimentsRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ReportUserRepository reportUserRepository;

    @Autowired
    private  AddressRepository addressRepository;

    @Autowired
    private PrivilegedUserRepository privilegedUserRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void createUser(SlaUserDto slaUserRequestDto, Long userId, String userRole) throws QmsException{

        if(userRole.equals(RoleType.SUPER_VIEW.name().trim())){
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.UNAUTHORIZED, "Super USer is not allowed this operation");
        }else if(userRole.equals(RoleType.DATA_FEEDER.name().trim()) || userRole.equals(RoleType.OFFICER.name().trim())  ){
            PrivilegedUser privilegedUser = privilegedUserRepository.findPrivilegedUserById(userId);

            if(privilegedUser == null){
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.UNAUTHORIZED, "Access Not Found");
            }

            if(!privilegedUser.getRegiment().getCode().equals(slaUserRequestDto.getRegiment().trim())){
                throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.UNAUTHORIZED, "This regiment is not allowed");
            }
        }else if(!userRole.equals(RoleType.ROOT.name().trim()) && !userRole.equals(RoleType.ADMIN.name().trim())){
            throw new QmsException(QmsExceptionCode.USR00X, HttpStatus.UNAUTHORIZED, "Unauthorized Access");
        }

        SlaUser slaUser = modelMapper.map(slaUserRequestDto, SlaUser.class);

        slaUser.setRegiment(regimentsRepository.findRegimentByCode(slaUserRequestDto.getRegiment()));
        slaUser.setUnit(unitRepository.findByName(slaUserRequestDto.getUnit()));
        slaUser.setAddress(addressRepository.save(slaUser.getAddress()));
        slaUser.setAddedBy(userService.findUserById(userId));
        slaUser.setBloodGroup(BloodGroup.findByValue(slaUserRequestDto.getBloodGroup()));

        slaRepository.save(slaUser);
    }

    public void getUser(String filter) throws QmsException{
       String []templates = filter.split(",");
    }

    public SlaUserMultiPageResDto getAllUsers(Pageable pageable, Long userId, boolean isRoot) throws QmsException{

        PrivilegedUser privilegedUser = privilegedUserRepository.findPrivilegedUserById(userId);

        Page<SlaUser> users;
        if(isRoot){
            users = slaRepository.findAll(pageable);
        }else{
            users = slaRepository.findSlaUsersByRegiment(privilegedUser.getRegiment(),pageable);
        }

        List<SlaUserResponseDto> slaUserDtos = new ArrayList<>();

        users.forEach(user->{
            SlaUserResponseDto slaUserDto = modelMapper.map(user, SlaUserResponseDto.class);
            slaUserDto.setUnit(user.getUnit().getName());
            slaUserDto.setRegiment(user.getRegiment().getCode());
            slaUserDto.setDob(user.getDob().toString());
            slaUserDto.setBloodGroup(user.getBloodGroup().label);
            slaUserDtos.add(slaUserDto);
        });

        SlaUserMultiPageResDto slaUserMultiPageResDto = new SlaUserMultiPageResDto();

        slaUserMultiPageResDto.setData(slaUserDtos);
        slaUserMultiPageResDto.setTotalPages(users.getTotalPages());

        return slaUserMultiPageResDto;
    }

    public SlaUserMultiPageResDto getFilteredUsers(Pageable pageable, String filter,Long userId, boolean isRoot) throws QmsException{

        PrivilegedUser privilegedUser = privilegedUserRepository.findPrivilegedUserById(userId);

        Page<SlaUser> users = null;
        String []filters = filter.split(",");

        // find by name
        if(filters.length==1){
            if(isRoot){
                users = slaRepository.findSlaUsersByNameStartsWithOrNicStartingWith(filters[0].trim(), filters[0].trim(), pageable);
            }else{
                users = slaRepository.findSlaUsersByNameStartsWithOrNicStartsWithAndRegiment(filters[0].trim(),filters[0].trim(),privilegedUser.getRegiment(), pageable);
            }
        }

        // find by name
        if(filters.length==2){
            if(isRoot){
                users = slaRepository.findSlaUsersByNameStartsWithAndNicStartsWith(filters[0].trim(),filters[1].trim(), pageable);
            }else{
                users = slaRepository.findSlaUsersByNameStartsWithAndNicStartsWithAndRegiment(filters[0].trim(),filters[1].trim(),privilegedUser.getRegiment(), pageable);
            }
        }

        List<SlaUserResponseDto> slaUserDtos = new ArrayList<>();

        users.forEach(user->{
            SlaUserResponseDto slaUserDto = modelMapper.map(user, SlaUserResponseDto.class);
            slaUserDto.setUnit(user.getUnit().getName());
            slaUserDto.setRegiment(user.getRegiment().getCode());
            slaUserDto.setDob(user.getDob().toString());
            slaUserDto.setBloodGroup(user.getBloodGroup().label);
            slaUserDtos.add(slaUserDto);
        });

        SlaUserMultiPageResDto slaUserMultiPageResDto = new SlaUserMultiPageResDto();

        slaUserMultiPageResDto.setData(slaUserDtos);
        slaUserMultiPageResDto.setTotalPages(users.getTotalPages());

        return slaUserMultiPageResDto;
    }
}
