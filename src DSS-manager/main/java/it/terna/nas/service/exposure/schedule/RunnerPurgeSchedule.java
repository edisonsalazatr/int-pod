package it.terna.nas.service.exposure.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.exceptions.EAMException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RunnerPurgeSchedule {
	
	@Value("${archiveRetentionDays:60}")
	private Integer retentionDays;
	
	@Value("${archiveDeleteBatchSize:1000}")
	private Integer batchSize;
	
	@Autowired
	private X4DssPolicyRunnerQueryService dssPolicyRunnerQueryService;
	
	
	@Scheduled(fixedDelayString	= "${archiveScheduleRate:14400000}",
			   initialDelay		= 1000)
//	@Scheduled(cron = "${archiveScheduleCron:0 0 1 * * ?}", zone = "Europe/Rome")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void processSchedule() throws EAMException {
		log.info("  - RunnerPurge Scheduler  - START");
		
		long startTime = System.currentTimeMillis();
		
		Boolean result = false;
		try {
			result = dssPolicyRunnerQueryService.purgeOldRunner(retentionDays, batchSize);
		}
		catch (Exception e) {
			log.error(" - Error executing archive operations", e);
		}
		
		log.info("  - Archive Scheduler took {} - {} - END", (System.currentTimeMillis() - startTime),
															  result != null && result
															  	? "OPERATION SUCCESSFULLY COMPLETED"
															  	: "OPERATION FAILED");
	}
	
	


	
	
}
