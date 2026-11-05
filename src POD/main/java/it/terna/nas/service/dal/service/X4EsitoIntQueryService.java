package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Catalog.RetryInterfacesCatalog;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4EsitoIntQueryService
{	
	private static final String QUERY_01 = "select c from " + X4EsitoInt.class.getName() + " c where c.x4EsitoIntId = :x4esitointid";
	private static final String QUERY_02 = "SELECT * FROM X4ESITOINT WHERE UNIQUEID = :uniqueid AND PARENT IS NULL";
	private static final String QUERY_03_WONUM = "SELECT NULL AS CODESITO, NULL AS DESC_ESITO, NULL AS DEST, NULL AS END_ELAB, NULL AS HASLD, NULL AS IDINTERFACCIA, NULL AS KAFKATOPIC, NULL AS MESSAGE, NULL AS PARENT, NULL AS ESITO, NULL AS ROWSTAMP, NULL AS SOURCE, NULL AS START_ELAB, NULL AS UNIQUEID, NULL AS RETRY, NULL AS WONUM, NULL AS ASSETNUM, X4ESITOINTID FROM X4ESITOINT WHERE ESITO = 'OK' AND IDINTERFACCIA = :idinterfaccia AND START_ELAB > TO_DATE(:startelab, 'yyyy-mm-dd hh24:mi:ss') AND WONUM = :wonum AND ROWNUM = 1";
	private static final String QUERY_03_ASSETNUM = "SELECT NULL AS CODESITO, NULL AS DESC_ESITO, NULL AS DEST, NULL AS END_ELAB, NULL AS HASLD, NULL AS IDINTERFACCIA, NULL AS KAFKATOPIC, NULL AS MESSAGE, NULL AS PARENT, NULL AS ESITO, NULL AS ROWSTAMP, NULL AS SOURCE, NULL AS START_ELAB, NULL AS UNIQUEID, NULL AS RETRY, NULL AS WONUM, NULL AS ASSETNUM, X4ESITOINTID FROM X4ESITOINT WHERE ESITO = 'OK' AND IDINTERFACCIA = :idinterfaccia AND START_ELAB > TO_DATE(:startelab, 'yyyy-mm-dd hh24:mi:ss') AND ASSETNUM = :assetnum AND ROWNUM = 1";
	private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update X4EsitoInt with X4EsitoIntId '%s'";

    @Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
   	public X4EsitoInt getListServiceToRestartContentAndUpdate(Long x4esitointid) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call getListServiceToRestartContentAndUpdate() |{}|", startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.X4ESITOINTID.getName(), x4esitointid.toString());
   		List<X4EsitoInt> result = dataAccessService.search(X4EsitoInt.class, QUERY_01, parameters, false, true);
   		
   		X4EsitoInt rowUpdate = null;
   		
   		if (Assorted.isNotEmpty(result)) 
		{
   			rowUpdate = result.get(0);
   			List<X4EsitoInt> resultInnerQuery = null;
   			Map <String, String> innerQueryParameters = new HashMap<>();
   			String idInterface = rowUpdate.getIdInterface();
   			RetryInterfacesCatalog currentParameter = RetryInterfacesCatalog.findRetryParameter(idInterface);
   			
   			innerQueryParameters.put(ESQLMappingParameter.IDINTERFACCIA.getName(), idInterface);
   			innerQueryParameters.put(ESQLMappingParameter.START_ELAB.getName(), EamEtlUtils.esitoDateStartElab(new java.util.Date(rowUpdate.getStartElab().getTime()).toInstant()));
   			
   			
   			if(RetryInterfacesCatalog.WONUM.equals(currentParameter)) {
   				innerQueryParameters.put(ESQLMappingParameter.WONUM.getName(), rowUpdate.getWonum());
   				resultInnerQuery = dataAccessService.search(X4EsitoInt.class, QUERY_03_WONUM, innerQueryParameters, true, true);
   			}
   			else if (RetryInterfacesCatalog.ASSETNUM.equals(currentParameter)) {
   				innerQueryParameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), rowUpdate.getAssetnum());
   				resultInnerQuery = dataAccessService.search(X4EsitoInt.class, QUERY_03_ASSETNUM, innerQueryParameters, true, true);
   			}
   			
   			if(Assorted.isNotEmpty(resultInnerQuery))
   				rowUpdate.setRetry(null);
   			else
   				rowUpdate.setRetry(rowUpdate.getRetry() + 1);
   			
			dataAccessService.save(rowUpdate, X4EsitoInt.class.getName(), ENTITY_UPDATE_MESSAGE, rowUpdate.getX4EsitoIntId());
		}
   		
   		long endTime = System.currentTimeMillis();
   		log.info("  - Search getListServiceToRestartContentAndUpdate found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   		
   		return rowUpdate;
   	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4EsitoInt getEsitoParent(String uniqueid) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getEsitoParent({}) |{}|", uniqueid, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.UNIQUEID.getName(), uniqueid);
		List<X4EsitoInt> result = dataAccessService.search(X4EsitoInt.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getEsitoParent found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}

}