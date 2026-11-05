package it.terna.nas.service.dal.service.repository;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4RecuperoCavi;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4RecuperoCaviRepository extends JpaRepository<X4RecuperoCavi, String> {

   
    @Query(value="SELECT WONUM FROM X4_RECUPERO_CAVI rc WHERE rc.DA_PROCESSARE = 1", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<String> getListWonumToResend();

    
    public final static String WORKORDER_RECUPERO_CAVI = """
SELECT w.wonum,
       w.assetnum,
	   w.changedate,
	   w.targstartdate,
	   w.x4datamodello,
	   w.jpnum,
	   w.parent,
	   w.siteid,
	   w.status,
	   w.x4sapidasset,
	   w.x4tronconum,
	   a.x3ingrp,
	   a.x3gewrk
FROM workorder w
  JOIN asset a
    ON w.assetnum = a.assetnum
WHERE w.wonum = :wonum
    		""";
    @Query(value = WORKORDER_RECUPERO_CAVI, nativeQuery = true)
    public Tuple loadWorkOrder(@Param("wonum") String wonum);
    
    String WONUM_CHILDREN = """
SELECT w.wonum FROM workorder w 
  WHERE w.status NOT IN ('CHIUSO','NON ESEGUITO', 'ESEGUITO') 
  AND w.parent = :wonum
    		""";
    
    @Query(value = WONUM_CHILDREN, nativeQuery = true)
    public List<String> loadWorkorderChildren(@Param("wonum") String wonum);
    
   
}
