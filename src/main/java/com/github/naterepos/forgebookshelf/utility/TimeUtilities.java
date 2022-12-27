package com.github.naterepos.forgebookshelf.utility;


import com.github.naterepos.forgebookshelf.Tuple;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public class TimeUtilities {

    public static final Map<TimeUnit, Tuple<String, String>> SHORT_UNITS;
    public static final Map<TimeUnit, Tuple<String, String>> LONG_UNITS;

    static {
        LONG_UNITS = new HashMap<>();
        LONG_UNITS.put(SECONDS, new Tuple<>(" second", " seconds"));
        LONG_UNITS.put(MINUTES, new Tuple<>(" minute", " minutes"));
        LONG_UNITS.put(HOURS, new Tuple<>(" hour", " hours"));
        LONG_UNITS.put(DAYS, new Tuple<>(" day", " days"));

        SHORT_UNITS = new HashMap<>();
        SHORT_UNITS.put(SECONDS, new Tuple<>("s", "s"));
        SHORT_UNITS.put(MINUTES, new Tuple<>("m", "m"));
        SHORT_UNITS.put(HOURS, new Tuple<>("h", "h"));
        SHORT_UNITS.put(DAYS, new Tuple<>("d", "d"));
    }

    public static final ZoneId TIMEZONE_EST = ZoneId.of("America/New_York");

    /**
     * Gets the current date and time at a particular zone
     * @param zone timezone
     * @return the current date time
     */
    public static LocalDateTime getCurrentDateTime(ZoneId zone) {
        return LocalDateTime.now(zone);
    }

    /**
     * Gets the current instant at a particular zone
     * @param zone timezone
     * @return the current instant
     */
    public static Instant getCurrentInstant(ZoneId zone) {
        return Instant.now().atZone(zone).toInstant();
    }

    /**
     * Gets the current date time at zone EST (America/New_York)
     * @return the current date time
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(TIMEZONE_EST);
    }

    /**
     * Gets the current instant at zone EST (America/New_York)
     * @return the current instant
     */
    public static Instant getCurrentInstant() {
        return Instant.now().atZone(TIMEZONE_EST).toInstant();
    }

    /**
     * Converts a {@link TimeUnit} to a {@link ChronoUnit}
     * @param unit to convert
     * @return a converted chrono unit
     */
    public static ChronoUnit timeUnitToChronoUnit(@NonNull TimeUnit unit) {
        switch (unit) {
            case DAYS:
                return ChronoUnit.DAYS;
            case HOURS:
                return ChronoUnit.HOURS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case SECONDS:
                return ChronoUnit.SECONDS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case NANOSECONDS:
                return ChronoUnit.NANOS;
        }
        return ChronoUnit.SECONDS;
    }

    public static String getTimeFormatted(long seconds, TimeUnit smallestUnit, Map<TimeUnit, Tuple<String, String>> units) {
        StringBuilder format = new StringBuilder();
        for(int i = values().length - 1; i > 2; i--) {
            TimeUnit unit = values()[i];
            long timeWithUnit = unit.convert(seconds, SECONDS);
            if(TimeUtilities.append(format, units, unit, values()[i - 1], seconds, timeWithUnit, smallestUnit)) {
                seconds -= SECONDS.convert(timeWithUnit, unit);
            } else {
                break;
            }
        }
        return format.charAt(format.length() - 2) == ',' ? format.substring(0, format.length() - 2) : format.toString();
    }

    private static boolean append(StringBuilder format, Map<TimeUnit, Tuple<String, String>> units, TimeUnit current, TimeUnit next, long seconds, long timeWithUnit, TimeUnit smallestUnit) {
        if(timeWithUnit > 0) {
            if(units.get(current).getFirst().length() == 1) {
                format.append(timeWithUnit).append(units.get(current).getFirst());
            } else {
                format.append(timeWithUnit).append(timeWithUnit > 1 ?  units.get(current).getSecond() + ", " : units.get(current).getFirst() + ", ");
            }
            if(next.convert(seconds, SECONDS) <= 0) {
                if(units.get(current).getFirst().length() > 1) {
                    format.setLength(format.capacity() - 2);
                }
                return false;
            }
        }
        if(smallestUnit.compareTo(current) == 0 && format.length() == 0) {
            format.append(0).append(units.get(current).getSecond());
        }

        return smallestUnit.compareTo(next) <= 0;
    }
}
