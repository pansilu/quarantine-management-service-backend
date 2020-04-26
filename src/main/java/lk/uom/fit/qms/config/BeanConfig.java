package lk.uom.fit.qms.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.uom.fit.qms.dto.QuarantineUserStatusDetail;
import lk.uom.fit.qms.model.DeceasedDetail;
import lk.uom.fit.qms.model.PositiveCovidDetail;
import lk.uom.fit.qms.model.SuspectCovidDetail;

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
