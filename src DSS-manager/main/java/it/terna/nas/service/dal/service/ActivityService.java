package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.JobPlan;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityService
{
    @Autowired
    private DataAccessService   dataAccessService;

    private static final String QUERY_01 = "select c from " + JobPlan.class.getName() + " c where c.jpNum = :jpnum and c.plusRevNum = :version";

    @Cacheable(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_ATTIVITA, key = "{#root.methodName, #jpnum, #revNum}", unless = "#result==null OR #result.isEmpty()")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<JobPlan> getActivity(String jpnum, Integer revNum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getActivity({}, {}) |{}|", jpnum, revNum, startTime);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.JPNUM.getName(), jpnum);
        parameters.put(ESQLMappingParameter.VERSION.getName(), Assorted.isNull(revNum) ? null : revNum.toString());

        List<JobPlan> result = dataAccessService.search(JobPlan.class, QUERY_01, parameters, false, true);
        long endTime = System.currentTimeMillis();
        log.info("end getActivity({}, {}) ==> {} |{}|", jpnum, revNum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
}
