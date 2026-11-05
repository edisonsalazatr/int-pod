package it.terna.nas.service.exposure.handler;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.repository.X4EsitoIntRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class X4EsitoIntHandler {
	
	@Autowired
	private X4EsitoIntRepository x4EsitoIntRepository;
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public void cleanupOldRecords() {
		
        LocalDate thresholdLocalDate = LocalDate.now().minusMonths(4);
        Date thresholdDate = java.sql.Date.valueOf(thresholdLocalDate);

        int deleted = x4EsitoIntRepository.deleteOlderThan(thresholdDate);
        log.info("EAM Integration - Cleanup X4ESITOINT - Deleted {} older records than {}", deleted, thresholdDate);
	}

}
