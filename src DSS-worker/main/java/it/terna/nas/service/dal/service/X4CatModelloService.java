package it.terna.nas.service.dal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Values;
import it.terna.nas.service.dal.entity.eam.X4CatModello;
import it.terna.nas.service.dal.repository.X4CatModelloRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4CatModelloService 
{
	@Autowired 
	private X4CatModelloRepository repository;
    
    public X4CatModello getByModelloId(String modelloId) 
    {
        long startTime = System.currentTimeMillis();
        log.info("call getByModelloId({}) |{}|", modelloId, startTime);
        Assert.notNull(modelloId, "modelloAssetId is null");
        List<X4CatModello> catModelloList = repository.findByModelloId(modelloId);
       
        
        log.info(" - Expecting 1 record, found {}", catModelloList != null ? catModelloList.size() : 0 );
          
        return Assorted.isNotEmpty(catModelloList)
        			? catModelloList.get(Values.FIRST_ELEMENT)
        			: null;
    }
   
}
