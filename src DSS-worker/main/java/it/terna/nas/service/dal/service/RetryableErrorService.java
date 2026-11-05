package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.RetryableErrorEntity;
import it.terna.nas.service.dal.repository.RetryableErrorRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RetryableErrorService {

    @Autowired
    private RetryableErrorRepository retryableErrorRepository;

    private final Map<String, List<String>> retryableErrors = new ConcurrentHashMap<>();

    @PostConstruct
    public void loadRetryableErrors() {
        retryableErrors.clear();
        List<RetryableErrorEntity> errors = retryableErrorRepository.findAllActive();
        errors.forEach(e -> retryableErrors
            .computeIfAbsent(e.getErrorCode().toUpperCase(), k -> new ArrayList<>())
            .add(e.getMessagePattern().toUpperCase())
        );

        log.info("Loaded {} retryable errors from DB", errors.size());
    }

	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public boolean isRetryable(String message) {
        if (message == null) return false;
        String upper = message.toUpperCase();
        return retryableErrors.entrySet().stream().anyMatch(entry ->
            upper.contains(entry.getKey()) ||
            entry.getValue().stream().anyMatch(upper::contains)
        );
    }
    
}

