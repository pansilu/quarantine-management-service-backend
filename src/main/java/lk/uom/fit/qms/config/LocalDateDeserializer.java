package lk.uom.fit.qms.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lk.uom.fit.qms.exception.ServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.config.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(p.getText(), formatter);
        }
        catch (JsonProcessingException ex){

            logger.warn("Invalid date time format. Inavlid string value: {}", p.getText());
            throw new ServiceCallException("Invalid Time Format");
        }
    }
}
