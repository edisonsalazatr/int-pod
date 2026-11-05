package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4SisnetAddress;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4SisnetAddressQueryService
{
	private static final String QUERY_01 				= "select c from " + X4SisnetAddress.class.getName() + " c where c.codiceOggetto = :codiceOggetto";
	private static final String QUERY_02				= "SELECT * FROM X4SISNETADDRESS WHERE DA_NOTIFICARE_GEOCALL = 'Y'";
	private static final String QUERY_03 				= "select c from " + X4SisnetAddress.class.getName() + " c where c.x4SisnetAddressId = :x4SisnetAddressId";
	private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update X4SisnetAddress with codiceOggetto '%s'";
	private static final String ENTITY_UPDATE_MESSAGE_2 = "Unable to update X4SisnetAddress with x4SisnetAddressId '%s'";
	 
    @Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional
	public List<X4SisnetAddress> getObjectsToNotify() 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getObjectsToNotify() |{}|", startTime);		
		
		List<X4SisnetAddress> result = dataAccessService.search(X4SisnetAddress.class, QUERY_02, null, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getObjectsToNotify found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result : null;
	}


	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional
	public List<X4SisnetAddress> getObjectsBySapId(String sapId) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getObjectsBySapId({}) |{}|", sapId, startTime);
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), sapId);
		List<X4SisnetAddress> result = dataAccessService.search(X4SisnetAddress.class, QUERY_01, parameters, false, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getObjectsBySapId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result : null;
	}


	@Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
	public void saveOrUpdate(X4SisnetAddress sisnetRow) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call saveOrUpdate(sisnetRow={}) |{}|", sisnetRow, startTime);		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), sisnetRow.getCodiceOggetto());
		List<X4SisnetAddress> result = dataAccessService.search(X4SisnetAddress.class, QUERY_01, parameters, false, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search X4SisnetAddress found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		startTime = endTime;
		
		if (Assorted.isEmpty(result))
			dataAccessService.save(sisnetRow, X4SisnetAddress.class.getName(), ENTITY_UPDATE_MESSAGE, sisnetRow.getCodiceOggetto());
		else {
			X4SisnetAddress rowUpdate = result.get(0);
			rowUpdate.setAreaLavorazione(sisnetRow.getAreaLavorazione());
			rowUpdate.setCap(sisnetRow.getCap());
			rowUpdate.setCentroDiLavoro(sisnetRow.getCentroDiLavoro());
			rowUpdate.setCivico(sisnetRow.getCivico());
			rowUpdate.setCodicePadre(sisnetRow.getCodicePadre());
			rowUpdate.setComune(sisnetRow.getComune());
			rowUpdate.setDescrizione(sisnetRow.getDescrizione());
			rowUpdate.setFiliale(sisnetRow.getFiliale());
			rowUpdate.setIstat(sisnetRow.getIstat());
			rowUpdate.setLatitudine1(sisnetRow.getLatitudine1());
			rowUpdate.setLatitudine2(sisnetRow.getLatitudine2());
			rowUpdate.setLocalita(sisnetRow.getLocalita());
			rowUpdate.setLongitudine1(sisnetRow.getLongitudine1());
			rowUpdate.setLongitudine2(sisnetRow.getLongitudine2());
			rowUpdate.setProvincia(sisnetRow.getProvincia());
			rowUpdate.setStatoOggetto(sisnetRow.getStatoOggetto());
			rowUpdate.setTipoOggetto(sisnetRow.getTipoOggetto());
			rowUpdate.setToponimo(sisnetRow.getToponimo());
			rowUpdate.setUpdateDate(new java.sql.Date(new java.util.Date().getTime()));
			rowUpdate.setVia(sisnetRow.getVia());
			rowUpdate.setDaNotificareGeocall(sisnetRow.getDaNotificareGeocall());
			rowUpdate.setNotes(sisnetRow.getNotes());
			dataAccessService.save(rowUpdate, X4SisnetAddress.class.getName(), ENTITY_UPDATE_MESSAGE, sisnetRow.getCodiceOggetto());
		}
		
		endTime = System.currentTimeMillis();
		log.info("end call saveOrUpdate(sisnetRow={}) |{}|", sisnetRow, endTime - startTime);
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
	public void updateNotiDetails(String codiceOggetto, String flagDaNotificare, String notes) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call updateNotiDetails(codiceOggetto={}, flagDaNotificare={}, notes={}) |{}|", codiceOggetto, flagDaNotificare, notes, startTime);		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), codiceOggetto);
		
		List<X4SisnetAddress> result = dataAccessService.search(X4SisnetAddress.class, QUERY_01, parameters, false, true);
		if (Assorted.isNotEmpty(result)) 
		{
			X4SisnetAddress rowUpdate = result.get(0);
			rowUpdate.setDaNotificareGeocall(flagDaNotificare);
			rowUpdate.setNotes(notes);
			dataAccessService.save(rowUpdate, X4SisnetAddress.class.getName(), ENTITY_UPDATE_MESSAGE, codiceOggetto);
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("end call updateNotiDetails(codiceOggetto={}, flagDaNotificare={}, notes={}) |{}|", codiceOggetto, flagDaNotificare, notes, endTime - startTime);
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
	public void updateObjectCode(Long x4SisnetAddressId, String codiceOggetto) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call updateObjectCode({}, {}) |{}|", x4SisnetAddressId, codiceOggetto, startTime);		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.X4SISNETADDRESSID.getName(), String.valueOf(x4SisnetAddressId));
		
		List<X4SisnetAddress> result = dataAccessService.search(X4SisnetAddress.class, QUERY_03, parameters, false, true);
		if (Assorted.isNotEmpty(result)) 
		{
			X4SisnetAddress rowUpdate = result.get(0);
			rowUpdate.setCodiceOggetto(codiceOggetto);
			dataAccessService.save(rowUpdate, X4SisnetAddress.class.getName(), ENTITY_UPDATE_MESSAGE_2, x4SisnetAddressId);
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("  - End updateObjectCode |{}|", endTime - startTime);
	}
}
