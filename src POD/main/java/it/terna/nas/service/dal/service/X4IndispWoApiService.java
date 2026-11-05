package it.terna.nas.service.dal.service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.material.dto.agile.X4IndispWoRequest;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class X4IndispWoApiService {

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private MaximoApiService maximoApiService;

    @Autowired
    private ApiKey apiKey;

    public MaximoResponse saveIndispWo(X4IndispWoRequest request)
            throws RestException{
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4INDISPWO.getEntityKey());
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
}
