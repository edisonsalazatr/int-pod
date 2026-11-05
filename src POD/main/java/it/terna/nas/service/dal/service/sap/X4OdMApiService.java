package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.dal.service.ApiKey;
import it.terna.nas.service.dal.service.MaximoApiService;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.sap.X4OdMRequest;
import it.terna.nas.service.exposure.ws.sap.X4OdMResponse;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class X4OdMApiService {

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private MaximoApiService maximoApiService;

    @Autowired
    private ApiKey apiKey;

    public MaximoResponse insert(X4OdMRequest request)
            throws RestException {
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4ODM.getEntityKey());
        return maximoApiService.maximoInsert(restEntity.getUri(),
                                            new HttpHeaders(),
                                            MaximoResponse.class,
                                            restEntity.getComponent(),
                                            request.toJson(),
                                            null,
                                            null,
                                            apiKey.retrieveSplittedApiKey(),
                                            false);
    }

    public X4OdMResponse getAll() throws RestException {
        log.debug("call getAllOrdInt");
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4ODM.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        return maximoApiService.maximoFetch(restEntity.getUri(),
                                            new HttpHeaders(),
                                            X4OdMResponse.class,
                                            restEntity.getComponent(),
                                            ExposureRequest.convertQueryParamsForHTTPCall(params),
                                            apiKey.retrieveSplittedApiKey(),
                                            false);

    }

    public X4OdMResponse getById(String id) throws RestException {
        log.debug("call getById [{}]", id);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4ODM.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.IDODM + Constants.EQUAL + id);
        return maximoApiService.maximoFetch(restEntity.getUri(),
                                            new HttpHeaders(),
                                            X4OdMResponse.class,
                                            restEntity.getComponent(),
                                            ExposureRequest.convertQueryParamsForHTTPCall(params),
                                            apiKey.retrieveSplittedApiKey(),
                                            false);

    }

    public MaximoResponse update(String uri, X4OdMRequest request)
            throws RestException
    {
        log.debug("call update({}, {})", uri, request);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4ODM.getEntityKey());
        return maximoApiService.maximoUpdate(uri,
                                            new HttpHeaders(),
                                            MaximoResponse.class,
                                            restEntity.getComponent(),
                                            request.toJson(),
                                            null,
                                            null,
                                            apiKey.retrieveSplittedApiKey(),
                                            Constants.PATCH,
                                            false,
                                            false);
    }
}
