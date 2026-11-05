package it.terna.nas.service.exposure.handler;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.repository.X4RecuperoCaviRepository;
import it.terna.nas.service.material.dto.cavi.CaviRecoveryResult;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaviInterratiRecuperoHandler {
	
	@Autowired
	private X4RecuperoCaviRepository repository;
	
	@Autowired
	private CaviInterratiRecuperoTXHandler recuperoTXHandler;

	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional(readOnly = true)
	public void doRecovery()
	{
		long start = System.currentTimeMillis();
		log.info("- Processing workorders to resend to CAVI starting |{}|.", start);
		try (Stream<String> wonums = repository.getListWonumToResend())
		{
			CaviRecoveryResult result = new CaviRecoveryResult();
			wonums.forEach(w -> recuperoTXHandler.processWonum(w, result));
			
		}
		log.info(" Processing workorders to resend to CAVI - END - Elapsed time |{}|." , System.currentTimeMillis() - start);
	}
}
