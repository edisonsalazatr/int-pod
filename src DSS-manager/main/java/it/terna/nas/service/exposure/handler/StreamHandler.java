package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.Constants.CLASS_SOSTITUZIONE;
import static it.terna.nas.service.material.utility.Constants.CLASS_TRONCO_ISPEZIONE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StreamHandler
{
    @Autowired
    private AggregateCondFuneTelHandler aggregateCondFuneHandler;
    
    @Autowired
    private PolicyQueryService policyQueryService;
    
	@Autowired
    private TrainatoTrainanteHandler trainatoTrainanteHandler;
	
	@Autowired
	private TroncoHandler troncoHandler;
	
	private boolean checkHasAggregate(X4Policy policy, MessageStreamCounter request)
    {
	    boolean esito = Constants.IS_POLICY_AGGREGATE.equals(policy.getPolicyAggr()) && CLASS_SOSTITUZIONE.equals(request.getEventType());
	    log.debug("end checkHasAggregate ==> {}", esito);
	    return esito;
    }


    private boolean checkHasTrainatoTrainante(X4Policy policy, MessageStreamCounter request)
    {
        boolean esito = false;
        esito = (!Constants.IS_POLICY_AGGREGATE.equals(policy.getPolicyAggr())) && CLASS_SOSTITUZIONE.equals(request.getEventType());       
        log.debug("end checkHasTrainatoTrainante ==> {}", esito);
        return esito;
    }


    private boolean checkHasTronco(MessageStreamCounter request)
    {
        boolean esito = false;
        esito = CLASS_TRONCO_ISPEZIONE.equals(request.getEventType());       
        log.debug("end checkHasTronco ==> {}", esito);
        return esito;
    }


    public void handleEvent(MessageStreamCounter request)
        throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.info("call StreamHandler.handleEvent({}) |{}|", request, startTime);
        
        try {
            X4Policy policy = policyQueryService.getPolicy(Long.toString(request.getPolicy()));
            if (checkHasTrainatoTrainante(policy, request))
                trainatoTrainanteHandler.handleEvents(request);
            else if (checkHasTronco(request))
                troncoHandler.handleEvents(request);
            else if (checkHasAggregate(policy, request))
                aggregateCondFuneHandler.handleEvents(request);
            
            //Add altri algoritmi e invocare Handler custom
        } catch(EAMRetryException q) {
            log.error(q.getMessage(), q);
            throw q;
        } catch(Exception q) {
            log.error(q.getMessage(), q);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), q.getMessage(), q);
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("end StreamHandler.handleEvent({}) |{}|", request, endTime - startTime);
        }
    }
}
