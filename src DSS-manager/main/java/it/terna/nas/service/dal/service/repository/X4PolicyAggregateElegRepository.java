package it.terna.nas.service.dal.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.X4PolicyAggregateEleg;

@Repository
public interface X4PolicyAggregateElegRepository extends JpaRepository<X4PolicyAggregateEleg, Long>
{
    public X4PolicyAggregateEleg findByParameter(String parameter);
}
