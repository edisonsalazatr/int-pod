package it.terna.nas.service.exposure.schedule;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.process.ScheduleCatalog.EScheduleName;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.process.service.ProcessStatusInfoService;
import it.terna.nas.service.exposure.handler.SisnetHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4RenameSapIdScheduler
{
	@Autowired
	private SisnetHandler sisnetHandler;
	
	@Autowired
	private ProcessStatusInfoService processStatusInfoService;
	
	@Scheduled(fixedRateString = "${x4RenameSapIdFixedRate}", initialDelay = 500)
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void x4RenameSapIdDataSchedule() 
			throws DataLayerException
	{
		String	scheduleUUID = UUID.randomUUID().toString();
		
		try {
			if (!processStatusInfoService.isScheduleRunning(EScheduleName.X4RENAMESAPID))
			{
				// log collecting data process start
				log.info("  EAM Integration - Process - Scheduled process identified by [{}] has been triggered at |{}|", scheduleUUID, System.currentTimeMillis());
				MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);
				processStatusInfoService.startSchedule(EScheduleName.X4RENAMESAPID, scheduleUUID);
				
				try {
					checkRenameSapIdAndRelaunch(scheduleUUID);
				} catch(Exception p) {
					log.error("  EAM Integration - Process - Scheduled process error ", p);
				}  finally {
					// close schedule process
					processStatusInfoService.closeSchedule(EScheduleName.X4RENAMESAPID, scheduleUUID);
				}
				
			} else {
				//log already running scheduler
				log.warn("  - X4Esito Scheduler - [{}] scheduled process is already running. Schedule wait next call to retry data capture", EScheduleName.X4RENAMESAPID.name());
			}
		
		} finally {
			// remove set MDC values
			MDC.clear(); 			
		}
 	}


	private void checkRenameSapIdAndRelaunch(String scheduleUUID) 
			throws DataLayerException 
	{
	    log.debug("call checkRenameSapIdAndRelaunch({})", scheduleUUID);
	    sisnetHandler.checkRenameSapIdAndRelaunch();
	}
}
