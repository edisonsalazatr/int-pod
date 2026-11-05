package it.terna.nas.service.material.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.Table;

import org.springframework.util.Assert;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.validation.ThrowingConsumer;


public class EamEtlUtils
{
    public static void detach(EntityManager entityManager, Boolean flush, Object ... entity)
    {
        if (Assorted.notNull(entityManager) && entity != null && entity.length > 0)
        {
            if (Boolean.TRUE.equals(flush))
                entityManager.flush();
            
            for (Object obj: entity)
            {
                entityManager.detach(obj);
            }
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
	
	
	public static <T> List<List<T>> partition(final List<T> list, final int size)
    {
         if (list == null) 
             throw new NullPointerException("List must not be null");
            
         if (size <= 0)
             throw new IllegalArgumentException("Size must be greater than 0");
            
        List<List<T>> lists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) 
        {
            int end = Math.min(list.size(), i + size);
            lists.add(list.subList(i, end));
        }
        
        return lists;
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
    
    public static LocalDateTime addToDate(LocalDateTime actualDate, long increment, ChronoUnit timeUnit) {
		
		return increment >= 0
				? actualDate.plus(increment, timeUnit)
				: actualDate.minus(-increment, timeUnit);
	}
}
