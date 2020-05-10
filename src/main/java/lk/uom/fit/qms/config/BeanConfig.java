package lk.uom.fit.qms.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.*;
import lk.uom.fit.qms.model.*;
import lk.uom.fit.qms.util.enums.QuarantineUserStatus;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
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

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, HomeQuarantineDetail>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getStartDateClass());
                map().setEndDate(source.getEndDateClass());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, RemoteQuarantineDetail>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getStartDateClass());
                map().setEndDate(source.getEndDateClass());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, SuspectCovidDetail>() {
            @Override
            protected void configure() {
                map().setAdmitDate(source.getStartDateClass());
                map().setDischargeDate(source.getEndDateClass());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, PositiveCovidDetail>() {
            @Override
            protected void configure() {
                map().setIdentifiedDate(source.getStartDateClass());
                map().setDischargeDate(source.getEndDateClass());
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUserStatusDetail, DeceasedDetail>() {
            @Override
            protected void configure() {
                map().setDeceasedDate(source.getEndDateClass());
            }
        });

        Converter<Country, Long> countryIdConverter = new AbstractConverter<Country, Long>() {
            protected Long convert(Country source) {
                return source == null ? null : source.getId();
            }
        };

        modelMapper.addMappings(new PropertyMap<QuarantineUser, QuarantineUserResDto>() {
            @Override
            protected void configure() {
                map().getAddress().setGndId(source.getAddress().getGnDivision().getId());
                map().setDivisionId(source.getAddress().getGnDivision().getDivision().getId());
                map().setDistrictId(source.getAddress().getGnDivision().getDivision().getDistrict().getId());
                map().setProvinceId(source.getAddress().getGnDivision().getDivision().getDistrict().getProvince().getId());
                map().setGndId(source.getAddress().getGnDivision().getId());
                using(countryIdConverter).map(source.getArrivedCountry()).setCountryId(null);
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

        Converter<PositiveCovidDetail, String> parentCaseNumConverter = new AbstractConverter<PositiveCovidDetail, String>() {
            protected String convert(PositiveCovidDetail source) {
                return source == null ? null : source.getCaseNum();
            }
        };

        modelMapper.addMappings(new PropertyMap<PositiveCovidDetail, QuarantineUserStatusDetail>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getIdentifiedDate());
                map().setEndDate(source.getDischargeDate());
                map().setHospitalId(source.getHospital().getId());
                map().setType(QuarantineUserStatus.POSITIVE_COVID);
                using(parentCaseNumConverter).map(source.getParentCase()).setParentCaseNum(null);
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

        Converter<String, String> trimConverter = new AbstractConverter<String, String>() {
            protected String convert(String source) {
                return source == null ? null : source.trim();
            }
        };

        modelMapper.addMappings(new PropertyMap<QuarantineUserRequestDto, QuarantineUser>() {
            @Override
            protected void configure() {
                using(trimConverter).map(source.getName()).setName(null);
                using(trimConverter).map(source.getPassportNo()).setPassportNo(null);
                using(trimConverter).map(source.getAddress().getLine()).getAddress().setLine(null);
                using(trimConverter).map(source.getAddress().getPoliceArea()).getAddress().setPoliceArea(null);
            }
        });

        Converter<PositiveCovidDetail, String> caseShowingNameConverter = new AbstractConverter<PositiveCovidDetail, String>() {
            protected String convert(PositiveCovidDetail source) {
                return source.getCaseNum() + " " + source.getUser().getName();
            }
        };

        modelMapper.addMappings(new PropertyMap<PositiveCovidDetail, PositiveCovidCaseDetail>() {
            @Override
            protected void configure() {
                map().setName(source.getUser().getName());
                using(caseShowingNameConverter).map(source).setShowingName(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<GramaNiladariDivision, GnDivisionResDto>() {
            @Override
            protected void configure() {
                skip().getDivision().getDistrict().setFeature(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<Division, DivisionResDto>() {
            @Override
            protected void configure() {
                skip().getDistrict().setFeature(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<QuarantineUser, MapResponse>() {
            @Override
            protected void configure() {
                map().setLat(source.getAddress().getLat());
                map().setLon(source.getAddress().getLon());
            }
        });

        modelMapper.addMappings(new PropertyMap<SuspectCovidDetail, QuarantineUserStatusDetailResponse>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getAdmitDate());
                map().setEndDate(source.getDischargeDate());
                map().setType(QuarantineUserStatus.SUSPECT_COVID);
            }
        });

        modelMapper.addMappings(new PropertyMap<PositiveCovidDetail, QuarantineUserStatusDetailResponse>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getIdentifiedDate());
                map().setEndDate(source.getDischargeDate());
                map().setType(QuarantineUserStatus.POSITIVE_COVID);
                using(parentCaseNumConverter).map(source.getParentCase()).setParentCaseNum(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<DeceasedDetail, QuarantineUserStatusDetailResponse>() {
            @Override
            protected void configure() {
                map().setEndDate(source.getDeceasedDate());
                map().setType(QuarantineUserStatus.DECEASED);
            }
        });

        modelMapper.addMappings(new PropertyMap<HomeQuarantineDetail, QuarantineUserStatusDetailResponse>() {
            @Override
            protected void configure() {
                map().setType(QuarantineUserStatus.HOUSE_QUARANTINE);
            }
        });

        modelMapper.addMappings(new PropertyMap<RemoteQuarantineDetail, QuarantineUserStatusDetailResponse>() {
            @Override
            protected void configure() {
                map().setType(QuarantineUserStatus.REMOTE_QUARANTINE);
            }
        });

        modelMapper.addMappings(new PropertyMap<GndRiskDetail, GndRiskDetailResponse>() {
            @Override
            protected void configure() {
                map().setId(source.getGnDivision().getId());
                map().setRiskType(source.getRiskType());
                map().setFeature(source.getGnDivision().getFeature());
            }
        });

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
