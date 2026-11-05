package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.dal.service.ApiKey;
import it.terna.nas.service.dal.service.MaximoApiService;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.X4WorkorderResponse;
import it.terna.nas.service.exposure.ws.sap.X4WorkOrderRequest;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class X4WorkOrderApiService {

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private MaximoApiService maximoApiService;

    @Autowired
    private ApiKey apiKey;

    public X4WorkorderResponse retrieveWorkorderByWonum(String wonum)
            throws RestException
    {
        log.debug("call retrieveWorkorderByWonum({})", wonum);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.WONUM + Constants.EQUAL + Constants.DOUBLE_QUOTES + wonum + Constants.DOUBLE_QUOTES);
        return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4WorkorderResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);
    }

    public X4WorkorderResponse retrieveWorkorderById(Long workorderid)
            throws RestException
    {
        log.debug("call retrieveWorkorderById({})", workorderid);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.WORKORDERID + Constants.EQUAL + workorderid);
        return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4WorkorderResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);
    }

    public MaximoResponse updateWorkorder(String uri, X4WorkOrderRequest request)
            throws RestException
    {
        log.debug("call updateWorkorder({}, {})", uri, request);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
        return maximoApiService.maximoUpdate(uri, new HttpHeaders(), MaximoResponse.class,
                restEntity.getComponent(), request.toJson(), null, null,
                apiKey.retrieveSplittedApiKey(), Constants.PATCH, true, false);
    }

}
