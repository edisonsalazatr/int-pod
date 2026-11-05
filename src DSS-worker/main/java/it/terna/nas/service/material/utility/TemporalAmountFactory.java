package it.terna.nas.service.material.utility;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

public class TemporalAmountFactory {

	public static TemporalAmount of(int amount, ChronoUnit unit) {
		switch (unit) {
		case NANOS:
			return Duration.ofNanos(amount);
		case MICROS:
			return Duration.of(amount, ChronoUnit.MICROS);
		case MILLIS:
			return Duration.ofMillis(amount);
		case SECONDS:
			return Duration.ofSeconds(amount);
		case MINUTES:
			return Duration.ofMinutes(amount);
		case HOURS:
			return Duration.ofHours(amount);
		case HALF_DAYS:
			return Duration.of(amount * 12L, ChronoUnit.HOURS);
		case DAYS:
			return Duration.ofDays(amount);
		case WEEKS:
			return Period.ofWeeks(amount);
		case MONTHS:
			return Period.ofMonths(amount);
		case YEARS:
			return Period.ofYears(amount);
		default:
			throw new IllegalArgumentException("Unità non supportata: " + unit);
		}
	}
}
