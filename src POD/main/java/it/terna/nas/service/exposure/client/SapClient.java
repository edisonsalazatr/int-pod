package it.terna.nas.service.exposure.client;

import static it.terna.nas.service.material.utility.Catalog.EAMErrorMapping.EAM_0029;
import static it.terna.nas.service.material.utility.EamEtlUtils.writeObjectAsString;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;

import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SapClient
{

    @Autowired
    private WebServiceTemplate sapWSTemplateODM;

    @Autowired
    private WebServiceTemplate sapWSTemplateOI;

    @Autowired
    private WebServiceTemplate sapWSTemplateStorno;
    
    @Autowired
    private WebServiceTemplate sapWSTemplateDC;
    
    @Autowired
    private WebServiceTemplate sapWSTemplateCounter;

    @Autowired
    private WebServiceTemplate sapWSTemplateCheckPeriodo;

    @Autowired
    private WebServiceTemplate sapWSTemplateConsuntivazione;    

    @Autowired
    private WebServiceTemplate sapWSTemplateNtk;
    
    @Autowired
    private WebServiceTemplate sapWSTemplateCloseOdM;

    @Autowired
    private WebServiceTemplate sapWSTemplateCloseNtk;

    @Autowired
    private RestHandler restHandler;

    @Value("${http.basic.esb.username}")
    private String  username;

    @Value("${http.basic.esb.password}")
    private String  password;

    @Value("${isOAuth2}")
    private Boolean isOAuth2;

    public <T> T requestToSAPOdM(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateODM, request, EDataIntegrationEntity.ACQUISIZIONE_ODM, Objects.nonNull(accessToken) ? new Oauth2AccessToken(accessToken, null) : null);
    }

    public <T> T requestToSAPOI(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateOI, request, EDataIntegrationEntity.ACQUISIZIONE_OI, Objects.nonNull(accessToken) ? new Oauth2AccessToken(accessToken, null) : null);
    }

    public <T> T requestToSAPStorno(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateStorno, request, EDataIntegrationEntity.SAP_STORNO, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }

    public <T> T requestToSAPCounterOdM(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateCounter, request, EDataIntegrationEntity.SAP_COUNTERODM, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }
    
	public <T> T requestToSAPDC(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateDC, request, EDataIntegrationEntity.ACQUISIZIONE_DC, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }

    public <T> T requestToSAPCheckPeriodoContabile(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateCheckPeriodo, request, EDataIntegrationEntity.SAP_CHECK_PERIODO, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }

    public <T> T requestToSAPConsuntivazione(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateConsuntivazione, request, EDataIntegrationEntity.SAP_CONSUNTIVAZIONE, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }

    public <T> T requestToSAPNtk(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateNtk, request, EDataIntegrationEntity.ACQUISIZIONE_NTK, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }
    
    public <T> T requestToSAPCloseOdM(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateCloseOdM, request, EDataIntegrationEntity.SAP_CLOSE_ODM, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }

    public <T> T requestToSAPCloseNtk(Object request, String accessToken) throws EAMException {
        return requestToSAP(sapWSTemplateCloseNtk, request, EDataIntegrationEntity.SAP_CLOSE_NTK, Objects.nonNull(accessToken) && isOAuth2 ? new Oauth2AccessToken(accessToken, null) : new BasicAccessToken(username, password));
    }


    @SuppressWarnings("unchecked")
    public <T> T requestToSAP(WebServiceTemplate webServiceTemplate, Object request, EDataIntegrationEntity EDataEntity, WebServiceMessageCallback callback) throws EAMException {
        long startTime = System.currentTimeMillis();
        T response;
        RestEntityInfo entity = null;
        try {
            entity = restHandler.getRestEntity(EDataEntity.getEntityKey());
            String uri = EamEtlUtils.getUri(entity);
            log.debug("Callback: [{}]", Objects.nonNull(callback) ? callback.toString() : null);
            log.info("Start request to SAP for entity: [{}] at uri: [{}] - Request: [{}]", entity.getComponent(), uri, writeObjectAsString(request));
            response = Objects.nonNull(callback) ? (T) webServiceTemplate.marshalSendAndReceive(uri, request, callback) : (T) webServiceTemplate.marshalSendAndReceive(uri, request);
            long endTime = System.currentTimeMillis();
            log.info("End request to SAP for entity: [{}] at uri [{}] in total time [{}] ms - Response [{}]", entity.getComponent(), uri, endTime - startTime, writeObjectAsString(response));
        } catch (Exception e) {
            log.error("Error request to SAP: [{}] {}", (Objects.nonNull(entity) ? entity.getComponent() : null), e.getMessage());
            throw new EAMException(EAM_0029.getEamCode(), EAM_0029.getErrorDescription() + " - [" + (Objects.nonNull(entity) ? entity.getComponent() : null) + "] - " + e.getMessage(), e);
        }
        return response;
    }
    
}
