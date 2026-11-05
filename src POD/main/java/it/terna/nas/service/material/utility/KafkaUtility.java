package it.terna.nas.service.material.utility;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;

public class KafkaUtility
{
	public static String getParamByteToString(@Headers MessageHeaders headers, String name)
	{
		Object object = headers.get(name);
		if (object instanceof byte[])
		{
			return new String((byte [])object);
		}
		
		return null;
	}
}
