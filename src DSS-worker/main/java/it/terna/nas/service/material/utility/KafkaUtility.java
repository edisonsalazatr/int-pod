package it.terna.nas.service.material.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import it.terna.kernel.basement.json.gson.serdes.OffsetDateTimeSerDes;
import it.terna.kernel.basement.json.gson.strategy.exclusion.GsonExclusionStrategy;
import it.terna.kernel.basement.utility.Assorted;

public class KafkaUtility
{
    
    public static String getCorrelationIdTronco(String correlation)
    {
        List<String> asListSplit = Arrays.asList(correlation.split(Constants.SEP7));
        StringBuilder strb       = new StringBuilder();
        if (Assorted.isNotEmpty(asListSplit) && asListSplit.size() >= 3) 
        {
            for (int i = 0; i < 3; i++)
            {
                strb.append(asListSplit.get(i));
             
                if ((i + 1) < 3)
                    strb.append(Constants.SEP7);
            }
        }
    
        return strb.toString();
    }
    
    
    public static String getParamByteToString(@Headers MessageHeaders headers, String name)
    {
        Object object = headers.get(name);
        if (object instanceof byte[]) {
            return new String((byte[]) object);
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
