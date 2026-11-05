package it.terna.nas.service.material.utility;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaAddOrdineLavoro;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaUpdateOrdineLavoro;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XmlUtility {
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
			
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

	public static void logRichiestaAdd(RichiestaAddOrdineLavoro richiesta) {
	    logRichiesta("RichiestaAddOrdineLavoro", richiesta);
	}

	public static void logRichiestaUpdate(RichiestaUpdateOrdineLavoro richiesta) {
	    logRichiesta("RichiestaUpdateOrdineLavoro", richiesta);
	}

	private static void logRichiesta(String tipoRichiesta, Object richiesta) {
	    if (richiesta == null) {
	        log.info("{}: null", tipoRichiesta);
	        return;
	    }

	    log.info("REQUEST_AGILE|{}", toJsonSafe(richiesta));
	}

	private static String toJsonSafe(Object richiesta) {
	    try {
	        return OBJECT_MAPPER.writeValueAsString(richiesta);
	    } catch (JsonProcessingException e) {
	        return "Errore serializzazione richiesta: " + e.getMessage();
	    }
	}
}
