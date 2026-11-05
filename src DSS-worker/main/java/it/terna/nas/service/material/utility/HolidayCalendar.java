package it.terna.nas.service.material.utility;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HolidayCalendar {
	
	private static final Map<Year, Set<MonthDay>> MAP_YEAR_HOLIDAYS = new ConcurrentHashMap<>();

	private static final List<MonthDay> HOLIDAYS = List.of(
			MonthDay.of(Month.JANUARY, 1),		// newYear
			MonthDay.of(Month.JANUARY, 6),		// epiphany
			MonthDay.of(Month.APRIL, 25),		// liberation
			MonthDay.of(Month.MAY, 1),			// labourDay
			MonthDay.of(Month.JUNE, 2),			// republicDay
			MonthDay.of(Month.AUGUST, 15),		// assumptionDay
			MonthDay.of(Month.NOVEMBER, 1),		// allSaintsDay
			MonthDay.of(Month.DECEMBER, 8),		// immaculateConception
			MonthDay.of(Month.DECEMBER, 25),	// xmas
			MonthDay.of(Month.DECEMBER, 26)		// stephensDay
	);
	
	
	public static Set<MonthDay> getHolidaysByYear(int yearInt){
		Year year = Year.of(yearInt);
		return getHolidaysByYear(year);
	}
	
	public static Set<MonthDay> getHolidaysByYear(Year year){
		Set<MonthDay> yearHolidays = MAP_YEAR_HOLIDAYS.computeIfAbsent(year, HolidayCalendar::buildHolidaysForYear);
		return Collections.unmodifiableSet(yearHolidays);
	}
	
	private static Set<MonthDay> buildHolidaysForYear(Year year) {
		Set<MonthDay> yearHolidays = new HashSet<>(HOLIDAYS);
		
		// add monday easter
		LocalDate mondayEasterLocalDate = calculateEasterMonday(year);
		if(mondayEasterLocalDate != null) {
			yearHolidays.add(MonthDay.of(mondayEasterLocalDate.getMonth(), 
									 mondayEasterLocalDate.getDayOfMonth()));
		} else {
			log.error(" --- Easter calculation error: NO RESULT for Year={}", year);
		}
		return yearHolidays;
	}
	
	public static LocalDate calculateEasterMonday(final Year year) {
		LocalDate easter = calculateEaster(year);
		if(easter != null) {
			return easter.plusDays(1);
		}
		return null;
	}
	
	public static LocalDate calculateEaster(final Year year) {
		int a, b, c, d, e, f, g, h, i, j, k, l;
		int x, month, day;
		a = year.getValue() % 19;
		b = year.getValue() / 100;
		c = year.getValue() % 100;
		d = b / 4;
		e = b % 4;
		f = (b + 8) / 25;
		g = (b - f + 1) / 3;
		h = (19 * a + b - d - g + 15) % 30;
		i = c / 4;
		j = c % 4;
		k = (32 + 2 * e + 2 * i - h - j) % 7;
		l = (a + 11 * h + 22 * k) / 451;
		x = h + k - 7 * l + 114;
		month = x / 31;
		day = (x % 31) + 1;
		return LocalDate.of(year.getValue(), (month == 3 ? Month.MARCH : Month.APRIL), day);
	}
}
