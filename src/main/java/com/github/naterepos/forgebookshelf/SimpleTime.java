package com.github.naterepos.forgebookshelf;


import com.github.naterepos.forgebookshelf.utility.TimeUtilities;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Class holding an <b>immutable</b> time represented by a given {@link TimeUnit}.<br>
 * This class is designed to handle simple times such as days, hours, minutes, and seconds
 * for day-to-day tasks.<br>
 * @see SimpleTime#parseTimeUnit(String, TimeUnit)
 */
@ConfigSerializable
public class SimpleTime implements Comparable<SimpleTime> {

    public static transient final SimpleTime IMMEDIATELY = new SimpleTime(0, TimeUnit.SECONDS);
    private static transient final Map<Character, TimeUnit> validUnitCharacters;
    private String unit;
    private long time;

    static {
        validUnitCharacters = new HashMap<>();
        validUnitCharacters.put('d', TimeUnit.DAYS);
        validUnitCharacters.put('h', TimeUnit.HOURS);
        validUnitCharacters.put('m', TimeUnit.MINUTES);
        validUnitCharacters.put('s', TimeUnit.SECONDS);
    }

    /**
     * Parses in format {@code ###Unit###Unit###Unit}<br>
     * I.E: {@code 5d3h2m1s} <br>
     * Valid unit characters: {@code d [day], m [minute], h [hour], s [second]}
     * @param time to parse
     * @return plutonium time formatted from string
     */
    public static Optional<SimpleTime> parseTimeUnit(String time) {
        return parseTimeUnit(time, TimeUnit.SECONDS);
    }

    /**
     * Parses in format {@code ###Unit###Unit###Unit}<br>
     * I.E: {@code 5d3h2m1s} <br>
     * Valid unit characters: {@code d [day], m [minute], h [hour], s [second]}
     * @param time to parse
     * @param convertedUnit to convert the parsed time to
     * @return plutonium time formatted from string
     */
    public static Optional<SimpleTime> parseTimeUnit(String time, TimeUnit convertedUnit) {
        if(time.equalsIgnoreCase("now") || time.equalsIgnoreCase("immediately")) {
            return Optional.of(SimpleTime.IMMEDIATELY);
        }
        long secondsTotal = 0L;
        StringBuilder timeBuilder = new StringBuilder();
        boolean isCountingNumerals = false;
        for(char character : time.toCharArray()) {
            if(Character.isDigit(character)) {
                timeBuilder.append(character);
                isCountingNumerals = true;
            } else if(validUnitCharacters.containsKey(character)) {
                if(!isCountingNumerals) {
                    return Optional.empty();
                }
                secondsTotal += validUnitCharacters.get(character).toSeconds(Long.parseLong(timeBuilder.toString()));
                timeBuilder = new StringBuilder();
                isCountingNumerals = false;
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(new SimpleTime(convertedUnit.convert(secondsTotal, TimeUnit.SECONDS), convertedUnit));
    }


    public SimpleTime() {}

    public SimpleTime(long time, TimeUnit unit) {
        this.unit = unit.name();
        this.time = time >= 0 ? time : 0;
    }

    /**
     * Gets the current time converted to the specified unit
     * @param other unit to convert with
     * @return a converted raw representation
     */
    public long toUnit(TimeUnit other) {
        return other.convert(getRawTime(), getUnit());
    }

    /**
     * Gets the current attached unit with this object
     * @return the time unit
     */
    public TimeUnit getUnit() {
        return TimeUnit.valueOf(unit);
    }

    /**
     * Gets the raw time given at construction
     * @return raw time
     */
    public long getRawTime() {
        return time;
    }

    /**
     * Adds one time unit to the other and converts to the given convertedUnit.<br>
     * This method does not have persistent modification - the original object stays the same<br>
     * @param other time unit to add
     * @param convertedUnit to convert to
     * @return a new time unit
     */
    public SimpleTime plus(SimpleTime other, TimeUnit convertedUnit) {
        return new SimpleTime(convertedUnit.convert((getUnit().toSeconds(time) + other.getUnit().toSeconds(other.time)), convertedUnit), convertedUnit);
    }

    /**
     * Subtracts one time unit from the other and converts to the given convertedUnit.<br>
     * This method does not have persistent modification - the original object stays the same<br>
     * This method will return <b>0</b> if the result is negative
     * @param other time unit to subtract with
     * @param convertedUnit to convert to
     * @return a new time unit
     */
    public SimpleTime minus(SimpleTime other, TimeUnit convertedUnit) {
        return new SimpleTime(convertedUnit.convert((getUnit().toSeconds(time) - other.getUnit().toSeconds(other.time)), convertedUnit), convertedUnit);
    }

    /**
     * Returns true if this object is greater than the parameter object
     * @param other object to compare
     * @return true if greater
     */
    public boolean isGreaterThan(SimpleTime other) {
        return compareTo(other) > 0;
    }

    /**
     * Returns true if this object is less than the parameter object
     * @param other object to compare
     * @return true if less
     */
    public boolean isLessThan(SimpleTime other) {
        return compareTo(other) < 0;
    }

    /**
     * Will return true if this object has is greater than one unit of the provided parameter.
     * As an example, if this object is 50 seconds and the provided parameter is {@link TimeUnit#MINUTES} then
     * this will return false. However, if that same object was now 60 seconds, this would return true.
     * @param unit to check support
     * @return true if this object supports the given time unit
     */
    public boolean supports(TimeUnit unit) {
        return unit.convert(getRawTime(), getUnit()) > 0;
    }

    /**
     * Gets the time interval until the provided later time and converts to the provided unit
     * @param later time to space as the end interval
     * @param convertedUnit to convert the result
     * @return time until the provided later time
     */
    public SimpleTime until(SimpleTime later, TimeUnit convertedUnit) {
        return later.minus(this, convertedUnit);
    }

    /**
     * Gets the time between two times. This will result in the absolute value so the first parameter does not
     * necessarily have to be larger than the second.
     * @param start interval (order not needed)
     * @param end interval (order not needed)
     * @param convertedUnit to convert the result
     * @return the time between two times
     */
    public SimpleTime between(SimpleTime start, SimpleTime end, TimeUnit convertedUnit) {
        if(start.isGreaterThan(end)) {
            return start.minus(end, convertedUnit);
        } else {
            return end.minus(start, convertedUnit);
        }
    }

    /**
     * Gets a {@link LocalDateTime} with the SimpleTime object added
     * @param time to add SimpleTime to
     * @return the time after this object was added
     */
    public LocalDateTime after(LocalDateTime time) {
        return time.plus(getRawTime(), TimeUtilities.timeUnitToChronoUnit(getUnit()));
    }

    /**
     * Gets a {@link LocalDateTime} with the SimpleTime object added
     * @param time to add SimpleTime to
     * @return the time after this object was added
     */
    public Instant after(Instant time) {
        return time.plus(getRawTime(), TimeUtilities.timeUnitToChronoUnit(getUnit()));
    }

    /**
     * Long form string formatting such as {@code 30 hours, 10 minutes, 2 seconds}
     * @see TimeUtilities#getTimeFormatted(long, TimeUnit, Map)
     * @param precision to format with. If {@link TimeUnit#MINUTES} was used, 5 minutes and 2 seconds would be formatted as just 5 minutes
     * @return a string formatted
     */
    public String toStringFormattedLong(TimeUnit precision) {
        return TimeUtilities.getTimeFormatted(toUnit(TimeUnit.SECONDS), precision, TimeUtilities.LONG_UNITS);
    }

    /**
     * Short form string formatting such as {@code 30h10m2s}
     * @see TimeUtilities#getTimeFormatted(long, TimeUnit, Map)
     * @param precision to format with. If {@link TimeUnit#MINUTES} was used, 5 minutes and 2 seconds would be formatted as just 5m rather than 5m2s
     * @return a string formatted
     */
    public String toStringFormattedShort(TimeUnit precision) {
        return TimeUtilities.getTimeFormatted(toUnit(TimeUnit.SECONDS), precision, TimeUtilities.SHORT_UNITS);
    }

    @Override
    public String toString() {
        return time + " " + unit.toLowerCase();
    }

    @Override
    public int compareTo(@NonNull SimpleTime other) {
        return Long.compare(getUnit().toSeconds(time), other.toUnit(TimeUnit.SECONDS));
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof SimpleTime)) return false;
        if(other == this) return true;
        SimpleTime time = (SimpleTime) other;
        return time.toUnit(TimeUnit.MILLISECONDS) == toUnit(TimeUnit.MILLISECONDS);
    }
}
