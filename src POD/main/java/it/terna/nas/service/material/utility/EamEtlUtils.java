package it.terna.nas.service.material.utility;

import static it.terna.nas.service.material.utility.Constants.MONTH;
import static it.terna.nas.service.material.utility.Constants.YEAR;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.persistence.Table;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.terna.kernel.basement.payload.ErrorContainer;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.validation.ThrowingConsumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EamEtlUtils
{
	@Value("${hostHeaderByPass}")
	private Boolean hostHeaderByPass;
	
	@Value("${apiIntraHostName}")
	private String apiIntraHostName;
	
    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    private static final ObjectMapper JAXB_MAPPER = new ObjectMapper().registerModule(new JaxbAnnotationModule()); // Aggiunge supporto per JAXB
	
	public static String getUri(RestEntityInfo entity)
	{

		if (Assorted.notNull(entity) && Assorted.notNull(entity.getUriStub()) && (!Constants.NOT_SUPPORTED_URI.equals(entity.getUriStub())))
		{
			return entity.getUriStub();
		}
		
		return entity.getUri();
	}

	
	public static Map<String, Object> buildEntries(List<String> keys, List<Object> values)
	{
		Map<String, Object> entries = new HashMap<>();
		IntStream.range(0, keys.size()).forEach(i -> {
			entries.put(keys.get(i), values.get(i));
		});
		return entries;
	}

	public static String currentDateAsString()
	{
		return dateAsInteger(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())).toString();
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

	public static Integer dateAsInteger(String date)
	{
		Assert.notNull(date, "date is null");
		Assert.hasText(date, "date is empty");
		return dateAsInteger(Assorted.stringToDate(date, Constants.DATE_WITHOUT_TIME_FORMAT));
	}
	
	public static String dateAsSqlString(Date date)
	{
		Assert.notNull(date, "date is null");
		SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.DATE_SQL_FORMAT);
		return outputFormat.format(date);
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
	
	public static String esitoDate(Instant instant) 
	{
		ZoneId zoneId = ZoneId.of(Constants.ITALY_TIMEZONE);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT);
	    return zdt.format(formatter);
    }
	
	public static String esitoDateStartElab(Instant instant) 
	{
		ZoneId zoneId = ZoneId.of(Constants.ITALY_TIMEZONE);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT_2);
	    return zdt.format(formatter);
    }
	
	public static String esitoDate(XMLGregorianCalendar calendar) 
	{
		Instant instant = Instant.ofEpochMilli(calendar.toGregorianCalendar().getTimeInMillis());
		ZoneId zoneId = ZoneId.of(Constants.ITALY_TIMEZONE);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT);
	    return zdt.format(formatter);
    }
	
	public static XMLGregorianCalendar esitoDateToXmlGC(String date) {
	    if (Assorted.notNull(date)) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT_2);  
	        try {
	            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
	            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(Constants.ITALY_TIMEZONE));
	            GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);
	            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	            return xmlGregorianCalendar;
	        } catch (DateTimeParseException | DatatypeConfigurationException e) {
	            log.error(e.getMessage());
	            return null;
	        }
	    } else {
	        return null;
	    }
	}
	
	public static XMLGregorianCalendar esitoDateToXmlGCGeoCall(String date) {
		if (Assorted.notNull(date)) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT_2);  
	        try {
	            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
	            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(Constants.UTC_TIMEZONE));
	            GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);
	            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	            xmlGregorianCalendar.setTimezone(0);
	            return xmlGregorianCalendar;
	        } catch (DateTimeParseException | DatatypeConfigurationException e) {
	            log.error(e.getMessage());
	            return null;
	        }
	    } else {
	        return null;
	    }
	}
	
	public static XMLGregorianCalendar stringDateToXmlGC(String date) {
	    if (Assorted.notNull(date)) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT);  
	        try {
	            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
	            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(Constants.ITALY_TIMEZONE));
	            GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);
	            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	            return xmlGregorianCalendar;
	        } catch (DateTimeParseException | DatatypeConfigurationException e) {
	            log.error(e.getMessage());
	            return null;
	        }
	    } else {
	        return null;
	    }
	}
	
	public static XMLGregorianCalendar stringDateToXmlGCGeoCall(String date) {
		if (Assorted.notNull(date)) {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT);  
	        try {
	            LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
	            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(Constants.UTC_TIMEZONE));
	            GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);
	            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	            xmlGregorianCalendar.setTimezone(0);
	            return xmlGregorianCalendar;
	        } catch (DateTimeParseException | DatatypeConfigurationException e) {
	            log.error(e.getMessage());
	            return null;
	        }
	    } else {
	        return null;
	    }
	}
	
	public static String currentDateTime() 
	{
		ZoneId zoneId = ZoneId.of(Constants.ITALY_TIMEZONE);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT_2);
	    return zdt.format(formatter);
    }
	
	public static String x4WBEDate(String originalDate) 
	{
		DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern(Constants.DATE_WITHOUT_TIME_FORMAT);
        LocalDate date = LocalDate.parse(originalDate, originalFormatter);
        DateTimeFormatter newFormatter = DateTimeFormatter.ofPattern(Constants.DATE_SQL_FORMAT);
        return date.format(newFormatter);
        
    }
	
	public static String errorGenericMessage(ErrorContainer error)
	{
		StringBuilder text = new StringBuilder();
		text.append(EAMErrorMapping.EAM_0000.getErrorDescription());
		
		if (Assorted.notNull(error))
		{
			text.append(": ").append("Code = ").append(error.getCode()).append(", Message = ").append(error.getMessage());
		} else {
			text.append(".");
		}
		
		return text.toString();
	}

	public static String removeOslcFromHref(String href) {
		if(Assorted.notNull(href)) {
			return href.replaceFirst(Constants.OSLC_URI_REGEX, Constants.API_URI);
		}
		else {
			return null;
		}	
	}
	
	public static Boolean isBiggerThanBufferSize(int size) 
	{
		if(size > Constants.CSV_BUFFER_SIZE) 
		{
			return Boolean.TRUE;
		}
		else 
		{
			return Boolean.FALSE;
		}
	}

	public static String constructCsvKey(int n) 
	{
		StringBuilder key = new StringBuilder();
		key.append(Constants.GEOCALL);
		key.append("_");
		key.append(String.valueOf(currentDateAsInteger()));
		key.append("_");
		key.append(currentDateTime().replace(Constants.COLONS, Constants.BLANK_STRING));
		key.append("_");
		key.append(String.valueOf(n));
		key.append(Constants.CSV_EXTENSION); 
		
		return key.toString();
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
	
	
    public static File createFileNameCustomCsv(String packagePath, String fileName) 
            throws IOException
    {
        String projectRoot = System.getProperty(Constants.USER_DIR);
        File targetDir = new File(projectRoot, packagePath);
        if (!targetDir.exists())
            targetDir.mkdirs();
        
        return new File(targetDir, fileName);
    }
    
	   
	public static String blankStringIfNull(Object object) 
	{
		if(Assorted.isNull(object)) 
			return Constants.BLANK_STRING;
		else 
			return object.toString();
	}
	
	public static List<String> splitPipeString(String input) {
        input = input.replaceAll(Constants.PIPE_REGEX, Constants.BLANK_STRING);
        String[] parts = input.split(Constants.PIPE_CHARACTER);
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            result.add(part);
        }
        return result;
    }
	
	public static String getFirst12Characters(String input) {
        if (Assorted.isNull(input) || input.length() <= 12) {
            return input;
        }
        return input.substring(0, 12);
    }

	
	 public static String convertToDateyyyyMMdd(String dateTimeString) {
		 if(dateTimeString == null || dateTimeString.isEmpty()) {
			 throw new IllegalArgumentException("La stringa della data non può essere nulla o vuota");
		 }

		 OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString);
		 return offsetDateTime.format(DateTimeFormatter.ofPattern(Constants.FORMAT_DATE_YYYYMMDD));
	 }

	public static String getMonthAndYear(String dateString, String typeResult) {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
		LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
		int year = dateTime.getYear();
		int month = dateTime.getMonthValue();

		switch (typeResult) {
			case YEAR: return String.valueOf(year);
			case MONTH: return String.valueOf(month);
			default: return null;
		}
	}

	 public HttpHeaders addHostHeader(HttpHeaders headers) 
	 {
		if(!hostHeaderByPass) {
			headers.add(Constants.HEADER_HOST, apiIntraHostName);	
		}	
		return headers;
	}

	public static void logErrorAndSetFailureStatus(Logger log, X4EsitoIntRequest x4EsitoIntRequest, Exception e, String customDescription, String customErrorCode) {
		if (e != null) {
			log.error(e.getMessage(), e);
			String description = (customDescription != null)
					? customDescription
					: EAMErrorMapping.EAM_0000.getErrorDescription() + " " + e.getMessage();
			String errorCode = (customErrorCode != null)
					? customErrorCode
					: EAMErrorMapping.EAM_0000.getEamCode();

			x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(truncate(errorCode, 25));
            String descEsito = x4EsitoIntRequest.getDesc_esito() != null ? x4EsitoIntRequest.getDesc_esito() : "";
			x4EsitoIntRequest.setDesc_esito(truncate(descEsito + "\n" + description, 1900));
		} else if (customDescription != null) {
			log.error("{} {}", customDescription, customErrorCode);
			x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(truncate(customErrorCode, 25));
			x4EsitoIntRequest.setDesc_esito(truncate(customDescription, 1900));
		}
	}

    public static String truncate(String s, int length) {
        return s != null && s.length() > length ? s.substring(0, length) : s;
    }

	public static void sendEsitoIntRequest(X4EsitoIntRequest x4EsitoIntRequest, X4EsitoIntApiService x4EsitoIntApiService) {
		try {
			x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
		} catch (Exception e) {
			log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}",
					Assorted.notNull(x4EsitoIntRequest) ? x4EsitoIntRequest.toString() : null, e.getMessage());
		}
	}

    public static X4EsitoIntRequest buildEsitoRequest(String topic, String payload, String uuid, Instant startDate, Catalog.SapInterface interfaceName, String source, String dest) {
        X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
        x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.OK.name());
        x4EsitoIntRequest.setIdinterfaccia(interfaceName.getName());
        x4EsitoIntRequest.setSource(source);
        x4EsitoIntRequest.setDest(dest);
        x4EsitoIntRequest.setKafkatopic(topic);
        x4EsitoIntRequest.setMessage(payload);
        x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
        x4EsitoIntRequest.setUniqueid(uuid);
        return x4EsitoIntRequest;
    }

	public static X4EsitoIntRequest buildEsitoRequest(String topic, String payload, String uuid, Instant startDate, String interfaceName, String source, String dest) {
		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.OK.name());
		x4EsitoIntRequest.setIdinterfaccia(interfaceName);
		x4EsitoIntRequest.setSource(source);
		x4EsitoIntRequest.setDest(dest);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setUniqueid(uuid);
		return x4EsitoIntRequest;
	}

	public static X4EsitoIntRequest buildEsitoRequest(String topic, String payload, String uuid, Instant startDate, Catalog.SapInterface interfaceName, String source, String dest, String step) {
		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.NA.name());
		x4EsitoIntRequest.setIdinterfaccia(interfaceName.getName());
		x4EsitoIntRequest.setSource(source);
		x4EsitoIntRequest.setDest(dest);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setUniqueid(uuid);
        x4EsitoIntRequest.setDescription(step);

		return x4EsitoIntRequest;
	}

	public static String currentDateAsFormattedString(String formatDate) {
		return LocalDate.now().format(DateTimeFormatter.ofPattern(formatDate));
	}

	public static java.sql.Date stringToSqlDate(String dateString) {
		if (dateString == null || dateString.isEmpty()) {
			throw new IllegalArgumentException("La stringa della data non può essere null o vuota.");
		}
		return java.sql.Date.valueOf(LocalDate.parse(dateString));
	}

    public static String writeObjectAsString(Object obj) {
        try {
            return DEFAULT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Error serializing object to JSON", e);
            return null;
        }
    }

    public static String writeJaxbObjectAsString(Object obj) {
        try {
            return JAXB_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Error serializing JAXB object to JSON", e);
            return null;
        }
    }
	 
	 public String getHostValue() 
	 {
		 if(!hostHeaderByPass)
			 return apiIntraHostName;
		 else
			 return null;
	 }
	 

		public static String listToJson(List<?> list) {
			try {
				return DEFAULT_MAPPER.writeValueAsString(list);
			} catch (Exception ex) {
				log.error(
						"- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned.",
						list != null ? list.getClass().getCanonicalName() : "null", ex);
				return null;
			}
		}
	
	public static String trimString(String s, Integer trim) {
        if (Assorted.isNull(s) || s.length() <= trim) {
            return s;
        }
        return s.substring(0, trim);
    }
	
	public static String formatTargstartdateForElive(String value) {
	    if (!StringUtils.hasText(value)) {
	        return value;
	    }

	    try {
	        return OffsetDateTime
	                .parse(value)
	                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
	                .replace("Z", "+00:00");
	    } catch (DateTimeParseException e) {
	        LocalDateTime localDateTime = LocalDateTime.parse(
	                value,
	                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")
	        );

	        return localDateTime
	                .atOffset(ZoneOffset.UTC)
	                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
	                .replace("Z", "+00:00");
	    }
	}

}
