package it.terna.nas.service.dal.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.X3Tronchi;

@Repository
public interface X3TronchiRepository extends JpaRepository<X3Tronchi, Long>
{
}
