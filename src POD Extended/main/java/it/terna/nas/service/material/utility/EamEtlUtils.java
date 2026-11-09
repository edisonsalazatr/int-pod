package it.terna.nas.service.material.utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.dal.service.X4EsitoIntSnowApiService;
import it.terna.nas.service.material.dto.X4EsitoIntSnowRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EamEtlUtils
{
	@Value("${hostHeaderByPass}")
	private Boolean hostHeaderByPass;
	
	@Value("${apiIntraHostName}")
	private String apiIntraHostName;
	
	public static String getUri(RestEntityInfo entity)
	{

		if (Assorted.notNull(entity) && Assorted.notNull(entity.getUriStub()) && (!Constants.NOT_SUPPORTED_URI.equals(entity.getUriStub())))
		{
			return entity.getUriStub();
		}
		
		return entity.getUri();
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

	public static String esitoDate(Instant instant) 
	{
		ZoneId zoneId = ZoneId.of(Constants.ITALY_TIMEZONE);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ESITO_DATE_FORMAT);
	    return zdt.format(formatter);
    }
	
	public static String currentDateTime() 
	{
		ZoneId zoneId = ZoneId.of(Constants.ITALY_TIMEZONE);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT_2);
	    return zdt.format(formatter);
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

	public static String constructServiceNowCsvKey(int n) 
	{
		StringBuilder key = new StringBuilder();
		key.append(Constants.SERVICENOW);
		key.append("_");
		key.append(String.valueOf(currentDateAsInteger()));
		key.append("_");
		key.append(currentDateTime().replace(Constants.COLONS, Constants.BLANK_STRING));
		key.append("_");
		key.append(String.valueOf(n));
		key.append(Constants.CSV_EXTENSION); 
		
		return key.toString();
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
	   
	public static String blankStringIfNullAndReplaceCharacters(Object object) 
	{
		if(Assorted.isNull(object)) 
			return Constants.BLANK_STRING;
		else {
			String returningString = object.toString().replace(Constants.HASH, Constants.BLANK_STRING);
			return returningString.replace(Constants.DOUBLE_QUOTES, Constants.BLANK_STRING);
		}
	}
	
	public HttpHeaders addHostHeader(HttpHeaders headers) 
	 {
		if(!hostHeaderByPass) {
			headers.add(Constants.HEADER_HOST, apiIntraHostName);	
		}	
		return headers;
	}

	public static void sendEsitoIntRequest(X4EsitoIntSnowRequest x4EsitoIntRequest, X4EsitoIntSnowApiService x4EsitoIntApiService) {
		try {
			x4EsitoIntApiService.sendEsitoIntSnow(x4EsitoIntRequest);
		} catch (Exception e) {
			log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}",
					Assorted.notNull(x4EsitoIntRequest) ? x4EsitoIntRequest.toString() : null, e.getMessage());
		}
	}

	public static String snowDateAsString(Date date)
	{
		if(Assorted.notNull(date)) 
		{
			SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.ESITO_DATE_FORMAT_2);
			return outputFormat.format(date);
		}
		else 
			return null;
	}
}
