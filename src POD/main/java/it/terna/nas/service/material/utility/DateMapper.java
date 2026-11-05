package it.terna.nas.service.material.utility;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateMapper {

    private DateMapper() {}
    
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static LocalDateTime toLocalDateTime(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof LocalDateTime ldt) {
            return ldt;
        }

        if (value instanceof Timestamp ts) {
            return ts.toLocalDateTime();
        }

        throw new IllegalArgumentException(
            "Unsupported date type: " + value.getClass().getName()
        );
    }
    
    public static String toIsoOffsetString(Object value) {

        if (value == null) {
            return null;
        }

        OffsetDateTime odt;

        if (value instanceof OffsetDateTime v) {
            odt = v;
        }
        else if (value instanceof ZonedDateTime v) {
            odt = v.toOffsetDateTime();
        }
        else if (value instanceof LocalDateTime v) {
            odt = v.atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }
        else if (value instanceof Timestamp v) {
            odt = v.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toOffsetDateTime();
        }
        else if (value instanceof Date v) {
            odt = v.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toOffsetDateTime();
        }
        else {
            throw new IllegalArgumentException(
                "Unsupported date type: " + value.getClass().getName()
            );
        }

        return FORMATTER.format(odt);
    }
}