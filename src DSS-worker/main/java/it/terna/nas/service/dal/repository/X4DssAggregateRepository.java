package it.terna.nas.service.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.X4DssAggregate;

@Repository
public interface X4DssAggregateRepository extends JpaRepository<X4DssAggregate, Long>
{}
