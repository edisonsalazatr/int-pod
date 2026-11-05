package it.terna.nas.service.dal.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4PolicyAggregateKP;
import it.terna.nas.service.dal.service.repository.X4PolicyAggregateKPRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicyAggregateKPService {
	
	private Set<String> TIPOLOGIE_STAZIONI = new HashSet<>();

	@Autowired
	private X4PolicyAggregateKPRepository repository;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4PolicyAggregateKP> getAll(){
		log.info("LOADING RAGGRUPPATORI KP PER ASSET DI TIPO STAZIONE");
		return repository.findAll();
	}
	
	private Set<String> TIPOLOGIE_STAZIONI_DEFAULT = Set.of(
													"INTERRUTTORE AIS",
													"SEZIONATORE AIS",
													"TV AIS",
													"TA AIS",
													"SCARICATORE AIS" );
	/**
	* Controlla che la tipologia asset della policy sia una stazione
	* @param policy
	* @return
	*/
	public boolean checkForStazione(X4Policy policy) {

		if(TIPOLOGIE_STAZIONI.isEmpty()) {
			synchronized (TIPOLOGIE_STAZIONI) {
				var kpStazioni = getAll();
				if(Assorted.isNotEmpty(kpStazioni)) {
					kpStazioni.forEach(x -> {
						TIPOLOGIE_STAZIONI.add(x.getTipologiaAsset());
					});
					TIPOLOGIE_STAZIONI.addAll(TIPOLOGIE_STAZIONI_DEFAULT);
				}
			}
		}

		return TIPOLOGIE_STAZIONI.contains(policy.getTipologiaAsset());
	}
}
