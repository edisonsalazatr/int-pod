package it.terna.nas.service.material.dto.worker;

import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.nas.service.material.utility.Constants.SEP5;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageStreamCounter implements Serializable
{
    private static final long serialVersionUID = -9204609979530634433L;
    private String key            = null;
    private Long numeroMessaggi   = 0L;
    private Long messaggiAttuali  = 0L;
    private Long policy           = null;
    private String eventType      = null;
    private Boolean addTronco     = null;
    private boolean hasSended     = false;
    
    public MessageStreamCounter addMessage(String newKey, WorkerFeedbackRequestEvent message)
    {
        this.eventType  = message.getEvent();
        this.policy     = message.getPolicy();
        this.key        = newKey;
        this.addTronco  = message.getAddTronco();
        this.numeroMessaggi = message.getNumberMessagePolicy();
        this.messaggiAttuali++;
        
        if (isNull(numeroMessaggi))
        {
            this.numeroMessaggi = 0L;
            log.error("NumberMessagePolicy is null");
        }
        
        if (isNull(policy))
        {
            log.error("Policy is null");
        }
        
        if (isNull(eventType))
        {
            log.error("Event is null");
        }
        
        if (log.isDebugEnabled()) 
        {
            StringBuilder buf = new StringBuilder();
            buf.append("call addMessage(Key: ").append(newKey).append(SEP5);
            buf.append("Correlation: ").append(message.getCorrelationId()).append(SEP5);
            buf.append("Esito: ").append(message.getEsito()).append(SEP5);
            buf.append("Event: ").append(eventType).append(SEP5);
            buf.append("Asset: ").append(message.getAsset()).append(SEP5);
            buf.append("Policy: ").append(policy).append(SEP5);
            buf.append("numeroMessaggi: ").append(message.getNumberMessagePolicy()).append(SEP5);
            buf.append("messaggiAttuali: ").append(this.messaggiAttuali).append(SEP5);
            buf.append("addTronco: ").append(this.addTronco).append(SEP5);
            buf.append("hasSended: ").append(this.hasSended);            
            buf.append(")");
            log.debug(buf.toString());
        }

        return this;
    }
    
    
    public MessageStreamCounter stopSendMsg()
    {
        if (hasSended)
        {
            log.info("Message with correlation {} already sent", key);
        }
        
        
        if (hasSended == false && messaggiAttuali >= numeroMessaggi)
        {
            this.hasSended = true;
            log.debug("Set hasSended = true for correlation {}", key);
        }
        
        return this;
    }
    
    
    public boolean inviaMessaggio()
    {
        if (hasSended == false && messaggiAttuali >= numeroMessaggi)
        {
            return true;
        }
        
        return false;
    }
}
