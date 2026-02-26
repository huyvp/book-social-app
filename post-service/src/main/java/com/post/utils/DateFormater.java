package com.post.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateFormater {
    Map<Long, Function<Instant, String>> map = new LinkedHashMap<>();

    public DateFormater() {
        map.put(60L, this::formatInSeconds);
        map.put(3600L, this::formatInMinutes);
        map.put(86400L, this::formatInHours);
        map.put(Long.MAX_VALUE, this::formatDate);
    }

    public String format(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        var strategy = map.entrySet().stream().filter(longFunctionEntry -> elapseSeconds < longFunctionEntry.getKey()).findFirst().get();
        return strategy.getValue().apply(instant);
    }

    private String formatInSeconds(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return elapseSeconds + " seconds";
    }

    private String formatInMinutes(Instant instant) {
        long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return elapseMinutes + " minutes";
    }

    private String formatInHours(Instant instant) {
        long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
        return elapseHours + " hours";
    }

    private String formatDate(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return DateTimeFormatter.ISO_DATE.format(localDateTime);
    }
}
