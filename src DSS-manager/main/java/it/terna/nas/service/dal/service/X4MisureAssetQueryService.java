package it.terna.nas.service.dal.service;

import java.util.Date;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.repository.X4MisureAssetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class X4MisureAssetQueryService 
{
	private final X4MisureAssetRepository x4MisureAssetRepository;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public boolean isMeasureExists(Long x4MisureAssetId, Date ultimaRilevazione) 
    {
		log.debug("Checking if measure exists for x4MisureAssetId {} with ultimaRilevazione >= {}", x4MisureAssetId, ultimaRilevazione);
		return (x4MisureAssetRepository.countByIdAndUltimaRilevazioneGreaterThanEqual(x4MisureAssetId, ultimaRilevazione) > 0);
	}
}
