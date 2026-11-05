package it.terna.nas.service.material.utility;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlUtility
{
			
	public static final String convertToString(String packageName, Object jaxbItem)
	{
		try {
			JAXBContext context = JAXBContext.newInstance(packageName);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();
			m.marshal(jaxbItem, sw);

			return sw.toString();
		} catch(JAXBException b) {
			log.error(b.getMessage(), b);
		}
		
		return null;
	}
}
