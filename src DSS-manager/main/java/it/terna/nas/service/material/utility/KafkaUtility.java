package it.terna.nas.service.material.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import it.terna.kernel.basement.json.gson.serdes.OffsetDateTimeSerDes;
import it.terna.kernel.basement.json.gson.strategy.exclusion.GsonExclusionStrategy;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.material.utility.Catalog.DssKafka;

public class KafkaUtility
{
    public static String getTopicName(DssKafka kafka, Map<String, BrokerEntityInfo> endPoints) 
    {
        if (kafka != null && endPoints != null)
        {
            String alias = kafka.getName();
            BrokerEntityInfo brokerEntityInfo = endPoints.get(alias);
            
            if (Assorted.notNull(brokerEntityInfo))
            {
                return brokerEntityInfo.getEntityName();
            }
        }
        
        return null;
    }
    
    
	public static String getCorrelationId(String correlation)
    {
        List<String> asListSplit = Arrays.asList(correlation.split(Constants.SEP4));
        StringBuilder strb       = new StringBuilder();
        if (Assorted.isNotEmpty(asListSplit)) 
        {
            int sizeField = asListSplit.size() - 1;
            
            for (int i = 0; i < sizeField; i++)
            {
                strb.append(asListSplit.get(i));
             
                if ((i + 1) < sizeField)
                    strb.append(Constants.SEP4);
            }
        }
    
        return strb.toString();
    }


    public static String getParamByteToString(@Headers MessageHeaders headers, String name)
	{
		Object object = headers.get(name);
		if (object instanceof byte[])
		{
			return new String((byte [])object);
		} else if (object instanceof String) {
			return (String) object;
		} else if (object instanceof Long) {
			return ((Long) object).toString();
		}
			
		
		return null;
	}
	
	
	public static String getTimeStamp(String correlation)
	{
		List<String> asListSplit = Arrays.asList(correlation.split(Constants.SEP4));
		if (Assorted.isNotEmpty(asListSplit))
		{
			return asListSplit.get(asListSplit.size() - 1);
		}
		
		return null;
	}
	
	
    public static Gson createGson()
    {
        return new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy())
                                .registerTypeHierarchyAdapter(OffsetDateTime.class, new OffsetDateTimeSerDes.Serializer())
                                .registerTypeHierarchyAdapter(OffsetDateTime.class, new OffsetDateTimeSerDes.Deserializer())
                                .excludeFieldsWithModifiers(Modifier.STATIC)
                                .setDateFormat(Constants.DATE_JSON_FORMAT)
                                .create();
    }
}
