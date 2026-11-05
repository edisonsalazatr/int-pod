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
import it.terna.nas.service.exposure.handler.X4EsitoIntHandler;
import it.terna.nas.service.exposure.handler.X4EsitoIntResubmitHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4EsitoIntScheduler
{
	@Autowired
	private X4EsitoIntResubmitHandler x4EsitoIntResubmitHandler;
	
	@Autowired
	private X4EsitoIntHandler x4EsitoIntHandler;
	
	@Autowired
	private ProcessStatusInfoService processStatusInfoService;
	
	@Scheduled(cron = "${x4EsitoIntSchedulerCron}")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void x4EsitoIntDataSchedule() 
			throws DataLayerException
	{
		String	scheduleUUID = UUID.randomUUID().toString();
		
		try {
			if (!processStatusInfoService.isScheduleRunning(EScheduleName.X4ESITOINT_RESTART))
			{
				// log collecting data process start
				log.info("  EAM Integration - Process - Scheduled process identified by [{}] has been triggered at |{}|", scheduleUUID, System.currentTimeMillis());
				MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);
				processStatusInfoService.startSchedule(EScheduleName.X4ESITOINT_RESTART, scheduleUUID);
				
				try {
					eamExtraction(scheduleUUID);
				} catch(Exception p) {
					log.error("  EAM Integration - Process - Scheduled process error ", p);
				}  finally {
					// close schedule process
					processStatusInfoService.closeSchedule(EScheduleName.X4ESITOINT_RESTART, scheduleUUID);
				}
				
			} else {
				//log already running scheduler
				log.warn("  - X4Esito Scheduler - [{}] scheduled process is already running. Schedule wait next call to retry data capture", EScheduleName.X4ESITOINT_RESTART.name());
			}
		
		} finally {
			// remove set MDC values
			MDC.clear(); 			
		}
 	}
	
	@Scheduled(cron = "${x4EsitoIntCleanupCron}")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void cleanupOldRecordsSchedule() throws DataLayerException {
        String scheduleUUID = UUID.randomUUID().toString();

        try {
            if (!processStatusInfoService.isScheduleRunning(EScheduleName.X4ESITOINT_CLEANUP)) {
                log.info("EAM Integration - Cleanup - Scheduler [{}] triggered at |{}|", scheduleUUID, System.currentTimeMillis());
                MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);
                processStatusInfoService.startSchedule(EScheduleName.X4ESITOINT_CLEANUP, scheduleUUID);

                try {
                    cleanupOldRecords(scheduleUUID);
                } catch (Exception ex) {
                    log.error("EAM Integration - Cleanup - Scheduler error", ex);
                } finally {
                    processStatusInfoService.closeSchedule(EScheduleName.X4ESITOINT_CLEANUP, scheduleUUID);
                }

            } else {
                log.warn("EAM Integration - Cleanup Scheduler - [{}] already running. Waiting for next execution.", EScheduleName.X4ESITOINT_CLEANUP.name());
            }
        } finally {
            MDC.clear();
        }
    }


	private void cleanupOldRecords(String scheduleUUID) {
	    log.debug("call cleanupOldRecords({})", scheduleUUID);
	    x4EsitoIntHandler.cleanupOldRecords();		
	}

	private void eamExtraction(String scheduleUUID) 
			throws DataLayerException 
	{
	    log.debug("call eamExtraction({})", scheduleUUID);
	    x4EsitoIntResubmitHandler.elaborate();
	}
}
