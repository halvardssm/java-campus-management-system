package nl.tudelft.oopp.group39.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public interface Constants {
    String FORMAT_DATE = "yyyy-MM-dd";
    String FORMAT_TIME = "HH:mm:ss";
    String FORMAT_TIME_SHORT = "HH:mm";
    String FORMAT_DATE_TIME = FORMAT_DATE + " " + FORMAT_TIME;
    DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(FORMAT_DATE);
    DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(FORMAT_TIME);
    DateTimeFormatter FORMATTER_DATE_TIME = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
    DateFormat DATE_FORMAT_DATE_TIME = new SimpleDateFormat(FORMAT_DATE_TIME);
    String HEADER_BEARER = "Bearer ";
    String DEFAULT_TIMEZONE = "Europe/Paris";

}
