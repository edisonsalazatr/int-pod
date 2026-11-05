package it.terna.nas.service.dal.service.sap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.ObjectFactory;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.RichiestaConsuntivazioneCallback;
import it.terna.nas.service.exposure.handler.SapHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBElement;

import static it.terna.nas.service.material.utility.Constants.PARZIALE;
import static it.terna.nas.service.material.utility.Constants.TOTALE;

@Service
@Slf4j
public class AsyncSapService {

    @Autowired
    private SapHandler sapHandler;

    @Async
    public void runSAP(String type, String loaded, String body) throws JsonProcessingException, RestException {
        switch (type) {
            case "odm" -> sapHandler.acquisitionOdM(null, null, "{}");
            case "oi" -> sapHandler.acquisitionOrdInt(null, null, "{}");
            case "wbe" -> sapHandler.acquisitionCountableData(null, null, "{}");
            case "ntk" -> sapHandler.acquisitionNtk(null, null, "{}");
            case "closeodm" -> sapHandler.closureOdM(null, null, body);
            case "closentk" -> sapHandler.closureNtk(null, null, body);
            case "consuntivi" -> sapHandler.invioConsuntivi(null, null, body);
            case "stornototale" -> sapHandler.storno(null, null, body, TOTALE);
            case "stornoparziale" -> sapHandler.storno(null, null, body, PARZIALE);
            case "sendstorno" -> sapHandler.sendStorno(loaded);
            case "callback" -> {
                ObjectFactory factory = new ObjectFactory();
                RichiestaConsuntivazioneCallback value = new RichiestaConsuntivazioneCallback();
                RichiestaConsuntivazioneCallback.Item item = new RichiestaConsuntivazioneCallback.Item();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(body);

                String eamkey = node.get("eamkey").asText();
                String result = node.get("result").asText();
                item.setEamKey(Long.parseLong(eamkey));
                item.setResult(result);
                value.getItem().add(item);
                JAXBElement<RichiestaConsuntivazioneCallback> request = factory.createRichiestaConsuntivazioneCallback(value);
                sapHandler.richiestaConsuntivazioneCallback(request, null);
            }
            default -> log.warn("  - Acquisition type [{}] not supported in async execution", type);
        }
    }

}
