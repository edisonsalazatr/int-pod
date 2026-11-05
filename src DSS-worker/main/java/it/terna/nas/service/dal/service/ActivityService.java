package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.CondizioneAssociazione;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityService {
		
	@Autowired
    private DataAccessService   dataAccessService;
	
	public static final String QUERY_ACTIVITY_ASSOCIATION = """
			SELECT rownum AS ID,
				ass.ATTIVITA,  
				cond.TARGET, 
				cond.VALUE,
				ass.attivita_ext, 
				ass.X4ATTIVITAASSOCIAZIONEID, 
				ass.MACROATTIVITA_EXT,
				cls.CLASSIFICATIONID,
				jp.CLASSSTRUCTUREID,
				jp.DESCRIPTION,
				jp.JPDURATION,
				jp.X4OUTSOURCING,
				jp.X4ATRIDONEITA
			FROM JOBPLAN jp
			JOIN CLASSSTRUCTURE cls on jp.CLASSSTRUCTUREID = cls.CLASSSTRUCTUREID
			JOIN X4ATTIVITAASSOCIAZIONE ass on jp.jobplanid = ass.attivita
			LEFT JOIN X4CONDIZIONEASSOCIAZIONE cond 
			    ON cond.X4ATTIVITAASSOCIAZIONEID = ass.X4ATTIVITAASSOCIAZIONEID
			WHERE jp.X4MACROATTIVITA = :macroActivityId
			    AND jp.X4ATTPREVMETODO = '1'
			    AND jp.STATUS = 'ACTIVE'
		""";
	
	private List<CondizioneAssociazione> getCondizioniAssociazioni(String macroAttivitaId) {
		List<CondizioneAssociazione> result = null;
		try {
			log.info(" - Starting to retrieve data for macroattivita': {}", macroAttivitaId);
			 
		    long startTime = System.currentTimeMillis();

			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.MACROATTIVITA.getName(), macroAttivitaId);
			result = dataAccessService.search(CondizioneAssociazione.class, QUERY_ACTIVITY_ASSOCIATION, parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.debug("  - Search Macro activities associations found [{}] |{}|", Assorted.isNotEmpty(result) 
																							? result.size() 
																							: 0, 
																				  endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve activities associations by macroactivity {} \n {}", macroAttivitaId, dle);
		}
	    
		return result != null
				? result
				: new ArrayList<>();
	}

	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
 		       value = Constants.CACHE_MACROATTIVITA, 
 		       key = "#macroAttivitaId", 
 		       unless = "#result==null OR #result.isEmpty()")
	public List<CondizioneAssociazione> loadAssociations(String macroAttivitaId) {
		log.info("  - Loading association cache: {}", macroAttivitaId);
		return getCondizioniAssociazioni(macroAttivitaId);
	}
	
	public Activity getActivityByJpnum(String jpnum) {
		Activity result = null;
		try {
			log.info(" - Starting to retrieve data for activity': {}", jpnum);
			 
		    long startTime = System.currentTimeMillis();

			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.ACTIVITY_ID.getName(), jpnum);
			result = dataAccessService.find(Activity.class, parameters);
			
			long endTime = System.currentTimeMillis();
			log.debug("  - Search activity by jpnum found [{}] |{}|", 1, endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve activities associations by macroactivity {} \n {}", jpnum, dle);
		}
	    
		return result;
	} 

}
