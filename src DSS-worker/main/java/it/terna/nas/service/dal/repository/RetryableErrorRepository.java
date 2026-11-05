package it.terna.nas.service.dal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.RetryableErrorEntity;

@Repository
public interface RetryableErrorRepository extends JpaRepository<RetryableErrorEntity, Long> {

    @Query("SELECT r FROM RetryableErrorEntity r WHERE r.active = 'Y'")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    List<RetryableErrorEntity> findAllActive();
}

