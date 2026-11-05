package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.dal.service.ApiKey;
import it.terna.nas.service.dal.service.MaximoApiService;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.sap.X4NtkOpRequest;
import it.terna.nas.service.exposure.ws.sap.X4NtkOpResponse;
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
public class X4NtkOpApiService {

    @Autowired
    private MaximoApiService maximoApiService;

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private ApiKey apiKey;

    public MaximoResponse insert(X4NtkOpRequest request)
            throws RestException {
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4OPNTK.getEntityKey());
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

    public X4NtkOpResponse getByOpAndIdNtk(String op, String idNtk) throws RestException {
        log.debug("call getByOpAndIdNtk [{}],[{}]", op, idNtk);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4OPNTK.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.IDOPERAZIONE + Constants.EQUAL + Constants.DOUBLE_QUOTES + op + Constants.DOUBLE_QUOTES + Constants.AND + Constants.IDNTK + Constants.EQUAL + Constants.DOUBLE_QUOTES + idNtk + Constants.DOUBLE_QUOTES);
        return maximoApiService.maximoFetch(restEntity.getUri(),
                new HttpHeaders(),
                X4NtkOpResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);

    }

    public MaximoResponse update(String uri, X4NtkOpRequest request)
            throws RestException
    {
        log.debug("call update({}, {})", uri, request);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4OPNTK.getEntityKey());
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
