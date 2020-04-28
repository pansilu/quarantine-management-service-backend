package lk.uom.fit.qms.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.AddressDto;
import lk.uom.fit.qms.dto.QuarantineUserRequestDto;
import lk.uom.fit.qms.dto.QuarantineUserResDto;
import lk.uom.fit.qms.dto.QuarantineUserStatusDetail;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.config.
 */
@Configuration
public class BeanConfig {

    @Value("${time.zone}")
    private String timeZone;

    @Value("${password.encoder.rounds}")
    private Integer passwordEncoderRounds;

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, SuspectCovidDetail>() {
            @Override
            protected void configure() {
                map().setAdmitDate(source.getStartDate());
                map().setDischargeDate(source.getEndDate());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, PositiveCovidDetail>() {
            @Override
            protected void configure() {
                map().setIdentifiedDate(source.getStartDate());
                map().setDischargeDate(source.getEndDate());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, DeceasedDetail>() {
            @Override
            protected void configure() {
                map().setDeceasedDate(source.getEndDate());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUser, QuarantineUserResDto>() {
            @Override
            protected void configure() {
                //map().getAddress().setGndId(source.getAddress().getGnDivision().getId());
                map().setDivisionId(source.getAddress().getGnDivision().getDivision().getId());
                map().setDistrictId(source.getAddress().getGnDivision().getDivision().getDistrict().getId());
                map().setProvinceId(source.getAddress().getGnDivision().getDivision().getDistrict().getProvince().getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<SuspectCovidDetail, QuarantineUserStatusDetail>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getAdmitDate());
                map().setEndDate(source.getDischargeDate());
                map().setHospitalId(source.getHospital().getId());
                map().setType(QuarantineUserStatus.SUSPECT_COVID);
            }
        });

        modelMapper.addMappings(new PropertyMap<PositiveCovidDetail, QuarantineUserStatusDetail>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getIdentifiedDate());
                map().setEndDate(source.getDischargeDate());
                map().setHospitalId(source.getHospital().getId());
                map().setType(QuarantineUserStatus.POSITIVE_COVID);
                map().setParentCaseNum(source.getParentCase() != null ? source.getParentCase().getCaseNum() : null);
            }
        });

        modelMapper.addMappings(new PropertyMap<DeceasedDetail, QuarantineUserStatusDetail>() {
            @Override
            protected void configure() {
                map().setEndDate(source.getDeceasedDate());
                map().setType(QuarantineUserStatus.DECEASED);
            }
        });

        modelMapper.addMappings(new PropertyMap<HomeQuarantineDetail, QuarantineUserStatusDetail>() {
            @Override
            protected void configure() {
                map().setType(QuarantineUserStatus.HOUSE_QUARANTINE);
            }
        });

        modelMapper.addMappings(new PropertyMap<RemoteQuarantineDetail, QuarantineUserStatusDetail>() {
            @Override
            protected void configure() {
                map().setqCenterId(source.getQuarantineCenter().getId());
                map().setType(QuarantineUserStatus.REMOTE_QUARANTINE);
            }
        });

        modelMapper.addMappings(new PropertyMap<Address, AddressDto>() {
            @Override
            protected void configure() {
                map().setGndId(source.getGnDivision().getId());
            }
        });

        /*modelMapper.addMappings(new PropertyMap<AddressDto, Address>() {
            @Override
            protected void configure() {
                map().setLine((source.getLine().trim()));
                *//*map().setPoliceArea(source.getPoliceArea() != null ? source.getPoliceArea().trim() : null);
                map().setVillage(source.getVillage() != null ? source.getVillage().trim() : null);
                map().setTown(source.getTown() != null ? source.getTown().trim() : null);*//*
            }
        });*/

        return modelMapper;
    }

    @Bean
    public ZoneId zoneId() {
        return ZoneId.of(timeZone);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(passwordEncoderRounds);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
