package it.terna.nas.service.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.repository.X4EliveRecoveryRepository;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;

@Service
public class EliveRecoveryUpdateService {

    @Autowired
    private X4EliveRecoveryRepository repository;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsProcessed(String batchCode,
                                KafkaOdlToEliveRequest request) {
        if (request == null || !StringUtils.hasText(request.getWonum())) {
            return;
        }

        repository.markAsProcessed(
                batchCode,
                request.getWonum()
        );
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsKo(String batchCode, String wonum, String errorMessage) {
        repository.markAsKo(batchCode, wonum, errorMessage);
    }
}
