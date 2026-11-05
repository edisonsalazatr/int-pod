package it.terna.nas.service.material.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalQuery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.persistence.Table;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.basement.payload.ErrorContainer;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.material.dto.policy.AttributesDTO;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.PolicyTimeUnit;
import it.terna.nas.service.validation.ThrowingConsumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EamEtlUtils
{
	public static final TemporalQuery<Boolean> WEEKEND_QUERY = t -> t.get(ChronoField.DAY_OF_WEEK) > 5;
	
	public static final BigDecimal workMinutesPerDay =  new BigDecimal (8 * 60 + 36);
	
	
	public static Set<String> splitByField(String value, String splitField) 
	{
        if (Assorted.isNotEmpty(value) && Assorted.isNotEmpty(splitField)) 
        {
            return Set.of(value.split(splitField));
        }
        
        return new HashSet<>();
    }


    public static boolean isNumeric(String val)
    {
        try {
            Long.parseLong(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    
	public static Map<String, Object> buildEntries(List<String> keys, List<Object> values)
	{
		Map<String, Object> entries = new HashMap<>();
		IntStream.range(0, keys.size()).forEach(i -> {
			entries.put(keys.get(i), values.get(i));
		});
		return entries;
	}

	public static Integer currentDateAsInteger()
	{
		return dateAsInteger(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	public static Integer dateAsInteger(Date date)
	{
		Assert.notNull(date, "date is null");
		return dateAsInteger(LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()));
	}

	public static Integer dateAsInteger(LocalDate date)
	{
		Assert.notNull(date, "date is null");
		String dateAsString = date.format(DateTimeFormatter.BASIC_ISO_DATE);
		Integer value = Integer.valueOf(dateAsString);
		return value;
	}

	public static boolean findError(String x, List<String> errors)
	{
		if (Assorted.notNull(x)) {
			for (String e : errors) {
				String replacedError = e.replaceAll("%", "").replaceAll("'", "");
				if (x.contains(replacedError))
					return true;
			}
		}
		return false;
	}

	public static Throwable getCause(Throwable throwable)
	{
		Assert.notNull(throwable, "throwable is null");
		Throwable cause = throwable;
		while (cause.getCause() != null && cause.getCause() != cause) {
			cause = cause.getCause();
		}
		return cause;
	}

	public static String getTableName(Class<?> entity)
	{
		Assert.notNull(entity, "entity is null");

		return entity.isAnnotationPresent(Table.class) ? entity.getAnnotation(Table.class).name() : null;
	}
	
	public static String errorGenericMessage(ErrorContainer error)
	{
		StringBuilder text = new StringBuilder();
		text.append(EAMErrorMapping.EAM_9999.getErrorDescription());
		
		if (Assorted.notNull(error))
		{
			text.append(": ").append("Code = ").append(error.getCode()).append(", Message = ").append(error.getMessage());
		} else {
			text.append(".");
		}
		
		return text.toString();
	}
	
	
	public static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) 
	{ 
		return i -> {
			try {
				throwingConsumer.accept(i);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		};
	}
	
	public static String listToJson(List<?> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(list);
            return json;
        } catch (Exception ex) {
			// log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", list.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
        }
	}
	
	public static LocalDate addToDate(LocalDate actualDate, long increment, PolicyTimeUnit timeUnit) {
		
		return increment >= 0
				? actualDate.plus(increment, timeUnit.getTemporalUnit())
				: actualDate.minus(-increment, timeUnit.getTemporalUnit());
	}
	
	public static LocalDateTime addToDate(LocalDateTime actualDate, long increment, PolicyTimeUnit timeUnit) {
		
		return increment >= 0
				? actualDate.plus(increment, timeUnit.getTemporalUnit())
				: actualDate.minus(-increment, timeUnit.getTemporalUnit());
	}
	
	public static Date toDate(LocalDate actualDate) {
		Assert.notNull(actualDate, "actualDate is null");
		return Date.from(actualDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	public static Date toDate(LocalDateTime actualDate) {
		Assert.notNull(actualDate, "actualDate is null");
		return Date.from(actualDate.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime addToLocalDateTime(LocalDateTime actualDate, long increment, PolicyTimeUnit timeUnit) {	
		return increment >= 0
				? actualDate.plus(increment, timeUnit.getTemporalUnit())
				: actualDate.minus(increment, timeUnit.getTemporalUnit());
	}

	public static boolean isOverOneYear(Integer value, PolicyTimeUnit unit) {
        if (value == null || unit == null) {
            throw new IllegalArgumentException("Value and unit must not be null");
        }
        switch (unit) {
            case YEAR:
                return value > 1;
            case MONTH:
                return value > 12; 
            case WEEK:
                return value > 52; 
            case DAY:
                return value > 365; 
            default:
                throw new UnsupportedOperationException("Unit not supported: " + unit);
        }
    }
	
	public static LocalDateTime calculateLastDayOfMonth(int year, int month) {
	    LocalDate lastDayOfMonth;
	    
	    switch (month) {
	        case 1: case 3: case 5: case 7: case 8: case 10: case 12:
	            lastDayOfMonth = LocalDate.of(year, month, 31);
	            break;
	        case 4: case 6: case 9: case 11:
	            lastDayOfMonth = LocalDate.of(year, month, 30);
	            break;
	        case 2:
	            lastDayOfMonth = Year.of(year).isLeap() 
	                ? LocalDate.of(year, month, 29) 
	                : LocalDate.of(year, month, 28);
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid month: " + month);
	    }

	    return lastDayOfMonth.atTime(0, 0, 0, 0);
	}
	
	public static long convertToDays(int frequency, ChronoUnit unit) {
	    switch (unit) {
	        case DAYS:
	            return frequency;
	        case WEEKS:
	            return frequency * 7;
	        case MONTHS:
	            return frequency * 30;  
	        case YEARS:
	            return frequency * 365; 
	        default:
	            throw new IllegalArgumentException("Unsupported ChronoUnit: " + unit);
	    }
	}
	
	 public static boolean isFestiveDay(LocalDateTime date) {
	        boolean isWeekend = date.query(WEEKEND_QUERY);
	        boolean isHoliday = isHoliday(date);
	        log.debug("Date: {} - isWeekend: {}, isHoliday: {}", date, isWeekend, isHoliday);        
	        return isWeekend || isHoliday;
	    }
	
	public static boolean isHoliday(LocalDateTime date) {
		Set<MonthDay> holidays = HolidayCalendar.getHolidaysByYear(date.get(ChronoField.YEAR));
		return holidays.contains(MonthDay.from(date));
	}
	
	public static Date dataPianificataInizio(LocalDateTime dataModello, BigDecimal duration) {
		
		Assert.notNull(dataModello, "dataModello is null");
		Assert.notNull(duration, "duration is null");
		
		// convert jpduration in minutes
		BigDecimal durationMinutes = duration.multiply(new BigDecimal(60));
		
		BigDecimal[] durationMinutesParts = durationMinutes.divideAndRemainder(workMinutesPerDay);
		
		BigDecimal workDays = durationMinutesParts[0];
		
		// subtract working days needed for activiry
		LocalDateTime dataPianificataInizio = addToDate(dataModello, 	
				workDays != null
					? - workDays.intValue()
					: 0,
				PolicyTimeUnit.DAY);
		
		// if starting date is not a working day
		if(isFestiveDay(dataPianificataInizio)) {
			do {
				// subtract 1 days untill the date became a working day
				dataPianificataInizio = addToDate(dataPianificataInizio, -1, PolicyTimeUnit.DAY);
			} while(isFestiveDay(dataPianificataInizio));
		}
		
		// adjust time to 8 am
		dataPianificataInizio = dataPianificataInizio.with(LocalTime.of(8, 0));
		
		return toDate(dataPianificataInizio);
		
	}
	
	/**
	 * 
	 * @param baseDataModello
	 * @param timeIncrement
	 * @param timeUnit
	 * @param duration
	 * @return
	 */
	public static LocalDateTime calcolaDataModello(LocalDateTime baseDataModello,
									int timeIncrement, 
									PolicyTimeUnit timeUnit, 
									BigDecimal duration) {
		
		// aggiungo la differibilita/frequenza' alla data che rappresenta la base di calcolo della data modello
		LocalDateTime dataModello = addToDate(baseDataModello, 
				  timeIncrement,
				  timeUnit);
		
		/*
		 * working day start at 8 so set hour to 8 am 
		 */
		dataModello = dataModello.with(LocalTime.of(8, 0));
		
		// convert jpduration in minutes
		BigDecimal durationMinutes = duration.multiply(new BigDecimal(60));
		
		BigDecimal[] durationMinutesParts = durationMinutes.divideAndRemainder(workMinutesPerDay);
		
		BigDecimal hourRemainder = durationMinutesParts[1];
		
		// aggiungi il resto alla data modello
		dataModello = addToDate(dataModello, 
				  hourRemainder.intValue(),
				  PolicyTimeUnit.MINUTES);
		 
		if(isFestiveDay(dataModello)) {
			do {
				dataModello = addToDate(dataModello, -1, PolicyTimeUnit.DAY);
			} while(isFestiveDay(dataModello));
		}
		
		return dataModello;
		
	}
	
	/**
	 * Data non prima di: Data modello – Massimo anticipo attivita; 
	 * inserito nella TARGSTARTDATE.SNECONSTRAINT
	 * 
	 * CR del 10/12/2025 se maxAnticipo == null imposta alla data corrente.
	 * 
	 * @param dataModello
	 * @param maxAnticipo
	 * @return
	 */
	public static Date calcolaDataNonPrimaDi(LocalDateTime dataModello, Integer maxAnticipo) {
		log.debug(" - Calculate dataNonPrimaDi(dataModello=[{}], maxAnticipo=[{}])");
		LocalDateTime notBeforeThan = (maxAnticipo != null && maxAnticipo != 0)
										? addToDate(dataModello, - maxAnticipo.longValue(), 
													PolicyTimeUnit.DAY)
										: LocalDateTime.now();
		return notBeforeThan != null
				? toDate(notBeforeThan)
				: null;
	}
	
	public static boolean notEmptyAttributes(AttributesDTO attributes) {
	    if (attributes == null) {
	        return false;
	    }
	    return attributes.getDeferrability() != null ||
	           attributes.getFrequency() != null ||
	           attributes.getMaxAdvance() != null ||
	           attributes.getMaxSnooze() != null ||
	           attributes.getRifTemporale() != null;
	}
	
	public static Date truncDate(Date input) 
	{
	    if (Assorted.notNull(input))
	    {
	        Calendar cal = Calendar.getInstance(); 
	        cal.setTime(input);
	        cal.set(Calendar.HOUR_OF_DAY, 0);
	        cal.set(Calendar.MINUTE, 0);
	        cal.set(Calendar.SECOND, 0);
	        cal.set(Calendar.MILLISECOND, 0);
	        return new Date(cal.getTimeInMillis());
	    }
	    
	    return input;
	}
	
    public static <T> List<List<T>> partition(final List<T> list, final int size)
    {
        if (list == null)
            throw new NullPointerException("List must not be null");

        if (size <= 0)
            throw new IllegalArgumentException("Size must be greater than 0");

        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int end = Math.min(list.size(), i + size);
            lists.add(list.subList(i, end));
        }

        return lists;
    }
    
	public static boolean shouldUpdateZonizzazione(String correlationId) {
	    if (correlationId == null) return false;

	    int lastUnderscore = correlationId.lastIndexOf("-");

	    if (lastUnderscore == -1) {
	        return true;
	    }

	    String suffix = correlationId.substring(lastUnderscore + 1);
	    return suffix.equals("1") || lastUnderscore == correlationId.length() - 1;
	}
	
	public static Integer extractYear(Date date) {
	    if (date == null)
	        return null;

	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    return cal.get(Calendar.YEAR);
	}
}
