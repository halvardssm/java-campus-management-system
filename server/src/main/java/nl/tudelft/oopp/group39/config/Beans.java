package nl.tudelft.oopp.group39.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class Beans {
    /**
     * Bean for Jackson JSON formatter.
     *
     * @return builder
     */
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder
            .timeZone(Constants.DEFAULT_TIMEZONE)
            .indentOutput(true)
            .featuresToDisable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
            )
            .serializers(
                new LocalDateSerializer(Constants.FORMATTER_DATE),
                new LocalTimeSerializer(Constants.FORMATTER_TIME),
                new LocalDateTimeSerializer(Constants.FORMATTER_DATE_TIME)
            )
            .deserializers(
                new LocalDateDeserializer(Constants.FORMATTER_DATE),
                new LocalTimeDeserializer(Constants.FORMATTER_TIME),
                new LocalDateTimeDeserializer(Constants.FORMATTER_DATE_TIME)
            )
            .modules(
                new JavaTimeModule(),
                new ParameterNamesModule(),
                new Jdk8Module()
            );

        return builder;
    }
}
