package it.terna.nas.service.dal.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.repository.X4GinRecoveryRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GinRecoveryUpdateService {

    @Autowired
    private X4GinRecoveryRepository repository;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsProcessedBatch(List<String> wonums) {
        if (wonums == null || wonums.isEmpty()) {
            return;
        }

        int updated = repository.markAsProcessedBatch(wonums);

        if (updated != wonums.size()) {
            log.warn("Marked as processed mismatch -  requested |{}| - updated |{}|",
                    wonums.size(),
                    updated);
        } else {
            log.info("Marked as processed -  requested |{}| - updated |{}|",
                    wonums.size(),
                    updated);
        }
    }
}