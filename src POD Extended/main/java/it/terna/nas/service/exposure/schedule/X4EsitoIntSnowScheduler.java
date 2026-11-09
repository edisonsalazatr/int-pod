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
import it.terna.nas.service.exposure.handler.X4EsitoIntSnowResubmitHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4EsitoIntSnowScheduler
{
	@Autowired
	private X4EsitoIntSnowResubmitHandler x4EsitoIntSnowResubmitHandler;
	
	@Autowired
	private ProcessStatusInfoService processStatusInfoService;
	
	@Scheduled(cron = "${x4EsitoIntSchedulerCron}")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void x4EsitoIntSnowDataSchedule() 
			throws DataLayerException
	{
		String	scheduleUUID = UUID.randomUUID().toString();
		
		try {
			if (!processStatusInfoService.isScheduleRunning(EScheduleName.X4ESITOINTSNOW_RESTART))
			{
				// log collecting data process start
				log.info("  EAM Integration Extended - Process - Scheduled process identified by [{}] has been triggered at |{}|", scheduleUUID, System.currentTimeMillis());
				MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);
				processStatusInfoService.startSchedule(EScheduleName.X4ESITOINTSNOW_RESTART, scheduleUUID);
				
				try {
					eamExtraction(scheduleUUID);
				} catch(Exception p) {
					log.error("  EAM Integration Extended - Process - Scheduled process error ", p);
				}  finally {
					// close schedule process
					processStatusInfoService.closeSchedule(EScheduleName.X4ESITOINTSNOW_RESTART, scheduleUUID);
				}
				
			} else {
				//log already running scheduler
				log.warn("  - X4EsitoIntSnow Scheduler - [{}] scheduled process is already running. Schedule wait next call to retry data capture", EScheduleName.X4ESITOINTSNOW_RESTART.name());
			}
		
		} finally {
			// remove set MDC values
			MDC.clear(); 			
		}
 	}


	private void eamExtraction(String scheduleUUID) 
			throws DataLayerException 
	{
	    log.debug("call eamExtraction({})", scheduleUUID);
	    x4EsitoIntSnowResubmitHandler.elaborate();
	}
}
