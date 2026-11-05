package it.terna.nas.service.dal.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.Ahscore;
import it.terna.nas.service.dal.service.repository.AhscoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AhscoreQueryService
{
	private final AhscoreRepository ahscoreRepository;

	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public Optional<Ahscore> getAhscoreById(Long ahscoreId)
	{
		log.debug("call getAhscoreById({})", ahscoreId);
		return ahscoreRepository.findById(ahscoreId);
	}
}
