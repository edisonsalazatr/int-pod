package it.terna.nas.service.dal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4CatModello;

@Repository
public interface X4CatModelloRepository extends JpaRepository<X4CatModello, Long> 
{
	public List<X4CatModello> findByModelloId(String modelloId);
	
}
