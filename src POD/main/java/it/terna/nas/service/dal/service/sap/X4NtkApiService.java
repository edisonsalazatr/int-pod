package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.dal.service.ApiKey;
import it.terna.nas.service.dal.service.MaximoApiService;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.sap.X4NtkRequest;
import it.terna.nas.service.exposure.ws.sap.X4NtkResponse;
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
public class X4NtkApiService {

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private MaximoApiService maximoApiService;

    @Autowired
    private ApiKey apiKey;

    public MaximoResponse insert(X4NtkRequest request)
            throws RestException {
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4NTK.getEntityKey());
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

    @Datasource
    public X4NtkResponse getById(String id) throws RestException {
        log.debug("call getById");
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4NTK.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.IDNTK + Constants.EQUAL + Constants.DOUBLE_QUOTES + id + Constants.DOUBLE_QUOTES);
        return maximoApiService.maximoFetch(restEntity.getUri(),
                new HttpHeaders(),
                X4NtkResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);

    }

    public MaximoResponse update(String uri, X4NtkRequest request)
            throws RestException
    {
        log.debug("call update({}, {})", uri, request);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4NTK.getEntityKey());
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
