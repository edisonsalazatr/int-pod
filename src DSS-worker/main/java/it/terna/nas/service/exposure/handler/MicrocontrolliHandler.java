package it.terna.nas.service.exposure.handler;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.dal.entity.eam.X4MicrocontrolloMigBatch;
import it.terna.nas.service.dal.service.MicroControlService;
import it.terna.nas.service.dal.service.X4MicrocontrolliQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.MicrocontrolliRequestEvent;
import it.terna.nas.service.exposure.service.RetryableMicrocontrolliService;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.MicrocontrolloRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderBulkResponse;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.Task;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;;


@Service
@Slf4j
public class MicrocontrolliHandler extends AbstractHandler
{   
    @Autowired
	private MicroControlService microControllService;
        
    @Autowired
    private X4MicrocontrolliQueryService microcontrolliQueryService;
    
    @Autowired
  	private RetryableMicrocontrolliService retryableMicrocontrolliService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional	
    public void processMessage(MicrocontrolliRequestEvent event)
            throws EAMRetryException
    {
        EsitoElab esito = new EsitoElab();
        esito.setEsito(EAMErrorMapping.EAM_0000);

		long startTime = System.currentTimeMillis();

		try {
	    	
	    	log.debug("  - Retriving tasks by wonum={} |{}|", event.getWonum(), startTime);
	    	List<Task> listTasks = microControllService.manageMicrocontrols(event.getAssetnum(), event.getJpnum());
	    	log.debug("  - Retrieved {} MicrocontrolloRequest(s) for sending.", listTasks != null
	    																			? listTasks.size()
	    																			: 0);
	    	
	    	if(Assorted.isNotEmpty(listTasks)) 
	    	{
	    		
	    		List<MicrocontrolloRequest> listRequest = listTasks.stream().map(x -> prepareRequest(event.getWonum(), x)).toList();
			    log.debug("Prepared {} MicrocontrolloRequest(s) for sending.", listRequest.size());
			        
				if (Assorted.isNotEmpty(listRequest)) 
				{
					MaximoResponseArray<WorkorderBulkResponse> responsePostApi = retryableMicrocontrolliService.callMicrocontrolliApiWithRetry(listRequest);
		            log.info("All orders sent successfully without errors.");
		            List<WorkorderBulkResponse> responseWorkorder = responsePostApi.getResponses();
		           
		            if (log.isDebugEnabled())
		                log.debug("End sendOrders ==> {} ", Assorted.isNotEmpty(responseWorkorder) ? responseWorkorder.toString() : null);
				}
				
				long endTime     = System.currentTimeMillis();
				log.info("  - End sending microcontrolli for order with id='{}' |{}|", event.getWonum(), endTime - startTime);
	    	} else {
	    		esito.setEsito(EAMErrorMapping.EAM_0020);
	    	}    	
		} catch(EAMException q) {
		    esito.setEsito(EAMErrorMapping.findCausaleEsito(q.getErrorCode()));
		    esito.setDescription(q.getMessage());
		    log.error(q.getMessage(), q);
            if (q instanceof EAMRetryException)
                throw (EAMRetryException) q;    		    
		} catch(Exception e) {
		    esito.setEsito(EAMErrorMapping.EAM_9999);
		    esito.setDescription(e.getMessage());
			log.error(" - Error while evaluating microcontrolli", e);
		} finally {
		    updateStatus(event.getRecordId(), esito);
		}
		
	}
    
    private MicrocontrolloRequest prepareRequest(String wonum, Task task) 
    {
    	if(task == null) {
    		log.debug("task is null, nothing to do");
    		return null;
    	}
    	MicrocontrolloRequest request = new MicrocontrolloRequest();
    	request.setAssetnum(task.getAssetnum());
    	request.setTaskid(task.getTaskid());
    	request.setParent(wonum);
    	request.setDescription(task.getDescription());
    	request.setX4microcontrollo(task.getX4microcontrollo());
    	request.setSiteid(Constants.SITEID);
    	request.setWoclass("ACTIVITY");
    	request.setIstask("1");
    	return request;
    }
    
    
    protected void updateStatus(String recordId, EsitoElab esito) 
    {
        try {
        	X4MicrocontrolloMigBatch row = microcontrolliQueryService.getById(recordId);
            
            if (Assorted.notNull(row))
            {
                row.setCode(null);
                row.setDescription(null);
                row.setStatus(Constants.CLOSED_STATUS);
                row.setChangeDate(new java.sql.Date((System.currentTimeMillis())));
                if (esito != null) 
                {
                    EAMErrorMapping esitoMapping = esito.getEsito();
                    
                    if (Assorted.notNull(esitoMapping))
                    {    
                        row.setCode(esitoMapping.getEamCode());
                        row.setDescription(esitoMapping.getErrorDescription());
                    }
                    
                    if (Assorted.isNotEmpty(esito.getDescription()))
                        row.setDescription(esito.getDescription());                 
                }
                
                if (Assorted.isNotEmpty(row.getDescription()) && row.getDescription().length() > 4000)
                    row.setDescription(row.getDescription().substring(0, 4000));
                
                microcontrolliQueryService.save(row);
            }
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
	
}
