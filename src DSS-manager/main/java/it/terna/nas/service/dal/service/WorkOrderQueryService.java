package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.SequenceValue;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.dal.service.repository.WorkOrderRepository;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkOrderQueryService
{
    private static final String QUERY_02    = "SELECT WORKORDERSEQ.NEXTVAL FROM DUAL";
    
	@Autowired
    private DataAccessService   dataAccessService;

	@Autowired
	private WorkOrderRepository workOrderRepository;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<SequenceValue> getSequenceForAggregate() 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getSequence() |{}|", startTime);      

        List<SequenceValue> result = dataAccessService.search(SequenceValue.class, QUERY_02, null, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getSequence found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
    
        return result;
    }
	
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<String> getCaviChild(String wonum) 
            throws DataLayerException
    {
        List<String> output = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        log.debug("call getCaviChild({}) |{}|", wonum, startTime);      

        List<WorkOrder> caviChildCandidate = workOrderRepository.findCaviChildCandidate(wonum);
        if (Assorted.isNotEmpty(caviChildCandidate))
        {
            Encoder encoder = Base64.getEncoder();
            caviChildCandidate.stream().forEach(x -> {
                String value = x.getSiteId() + Constants.SEP8 + x.getWonum();
                value = encoder.encodeToString(value.getBytes()).replace('=', '-');
                output.add(value);
            });
        }
        
        long endTime = System.currentTimeMillis();
        log.debug("end getCaviChild({}) found [{}] |{}|", wonum, Assorted.isNotEmpty(output) ? output.size() : 0, endTime - startTime);
    
        return output;
    }	


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public boolean isCavi45(String wonum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call isCavi45({}) |{}|", wonum, startTime);      

        List<WorkOrder> caviCandidate = workOrderRepository.findCaviCandidate45(wonum);
        
        long endTime = System.currentTimeMillis();
        log.debug("end isCavi45({}) found [{}] |{}|", wonum, Assorted.isNotEmpty(caviCandidate) ? caviCandidate.size() : 0, endTime - startTime);
    
        return Assorted.isNotEmpty(caviCandidate);
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public boolean isAnnullaBloccoGeocall(String wonum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call isAnnullaBloccoGeocall({}) |{}|", wonum, startTime);      

        List<WorkOrder> listWo = workOrderRepository.findAnnullaGeocall(wonum);
        
        long endTime = System.currentTimeMillis();
        log.debug("end isAnnullaBloccoGeocall({}) found [{}] |{}|", wonum, Assorted.isNotEmpty(listWo) ? listWo.size() : 0, endTime - startTime);
    
        return Assorted.isNotEmpty(listWo);
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public boolean existsByWonumAndChangedate(String wonum, Date changedate)
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call existsByWonumAndChangedate({}, {}) |{}|", wonum, changedate, startTime);

        List<WorkOrder> listWo = workOrderRepository.findByWonumAndChangedate(wonum, changedate);

        long endTime = System.currentTimeMillis();
        log.debug("end existsByWonumAndChangedate({}, {}) found [{}] |{}|", wonum, changedate,
                Assorted.isNotEmpty(listWo) ? listWo.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(listWo);
    }
}
