package com.codeosseum.miles.util.date;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateConversion {
    private DateConversion() {
        /*
         * Cannot be constructed.
         */
    }

    public static LocalDateTime epochMillisecondsToLocalDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    public static long localDateTimeToEpochMilliseconds(final LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
