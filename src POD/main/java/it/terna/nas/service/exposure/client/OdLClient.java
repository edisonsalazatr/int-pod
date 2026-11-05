package it.terna.nas.service.exposure.client;

import java.util.Objects;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaAddOrdineLavoro;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaUpdateOrdineLavoro;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.Risposta;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaBloccoOdlRequest;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaBloccoOdlResponse;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaBloccoOdlResponse2;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaBloccoOdl_Type;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaOperazioneOdl;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaOperazioneOdlRequest;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaOperazioneOdlResponse;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaOperazioneOdlResponse2;
import it.terna.kernel.soap.clientgen.odl.createodlv2.Create;
import it.terna.kernel.soap.clientgen.odl.createodlv2.CreateOdlRequest;
import it.terna.kernel.soap.clientgen.odl.createodlv2.CreateOdlResponse;
import it.terna.kernel.soap.clientgen.odl.createodlv2.CreateResponse;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.DatiTipoImpiantoRequest;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.DatiTipoImpiantoResponse;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.InvioTipoImpiantoResponse;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.InvioTipoImpianto_Type;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityRequest;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityResponse;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.Update;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.UpdateResponse;
import it.terna.kernel.soap.clientgen.odl.storno.StornoCall;
import it.terna.kernel.soap.clientgen.odl.storno.StornoCallResponse;
import it.terna.kernel.soap.clientgen.odl.storno.StornoRequest;
import it.terna.kernel.soap.clientgen.odl.storno.StornoResponse;
import it.terna.nas.service.configuration.properties.OAuth2Properties;
import it.terna.nas.service.exposure.handler.rest.JWTAwareRestClient;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.XmlUtility;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@SuppressWarnings("unchecked")
public class OdLClient
{
	@Autowired
	private RestHandler restHandler;

	@Autowired
	private WebServiceTemplate apimWsTemplate;
	
	@Autowired
	private OAuth2Properties oauth2Geocall;
	
	@Autowired
	private JWTAwareRestClient jwtAwareRestClient;
	
	@Autowired
	private WebServiceTemplate webServiceOdLTemplate;
	
	@Autowired
	private EamEtlUtils			    eamEtlUtils;
	
	@Value("${http.oauth2.geocall.api-key-geocall}")
	private String	apikey;
	
	@Value("${http.oauth2.agile.api-key-agile}")
	private String	apikeyAgile;

	@Value("${http.basic.esb.username}")
	private String  username;

	@Value("${http.basic.esb.password}")
	private String  password;

	@Value("${isOAuth2}")
	private Boolean isOAuth2;
	
	
	public AnnullaBloccoOdlResponse2 sendAnnullaBloccoOdL(AnnullaBloccoOdlRequest req, JwtTokenResponse jwtToken)
	{
		long startTime = System.currentTimeMillis();
		log.debug("call sendAnnullaBloccoOdL({}) |{}|", req.getCODICEORDINEESTERNO(), startTime);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.GEOCALL_ANNULLA_BLOCCO.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		AnnullaBloccoOdlResponse2 response = null;
		it.terna.kernel.soap.clientgen.odl.annulla.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.annulla.ObjectFactory();
		AnnullaBloccoOdl_Type annullaBloccoOdl_Type = factory.createAnnullaBloccoOdl_Type();
		annullaBloccoOdl_Type.setReq1(req);
		JAXBElement<AnnullaBloccoOdl_Type> annullaBloccoOdl = factory.createAnnullaBloccoOdl(annullaBloccoOdl_Type);	
		JAXBElement<AnnullaBloccoOdlResponse> responseType;

		if(Objects.isNull(jwtToken) || !isOAuth2) {
			responseType = (JAXBElement<AnnullaBloccoOdlResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, annullaBloccoOdl, new BasicAccessToken(username, password));
		} else if(Objects.nonNull(jwtToken.getHttpCode()) && jwtToken.getHttpCode() != HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			responseType = (JAXBElement<AnnullaBloccoOdlResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, annullaBloccoOdl, new Oauth2AccessToken(jwtToken.getAccessToken(), null));
		} else {
			response = new AnnullaBloccoOdlResponse2();
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			return response;
		}

		if (Assorted.notNull(responseType) && Assorted.notNull(responseType.getValue()))
			response = responseType.getValue().getAnnullaBloccoOdlReturn();
			
		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.debug("Response sendAnnullaBloccoOdL({}, {}, {}) |{}|", response.getESITO(), response.getCODICEERRORE(), response.getDESCRIZIONEERRORE(), endTime - startTime);
		else
			log.warn("NO Response for sendAnnullaBloccoOdL({}) |{}|", req.getCODICEORDINEESTERNO(), endTime - startTime);
		
		log.info("end call sendAnnullaBloccoOdL({}) |{}|", req.getCODICEORDINEESTERNO(), endTime - startTime);
		
		return response;
	}
	
	public AnnullaOperazioneOdlResponse2 sendAnnullaOperazioneOdL(AnnullaOperazioneOdlRequest req, JwtTokenResponse jwtToken)
	{
		long startTime = System.currentTimeMillis();
		log.debug("call sendAnnullaOperazioneOdL({}) |{}|", req.getCODICEORDINEESTERNO(), startTime);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.GEOCALL_ANNULLA_OPERAZIONE.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		AnnullaOperazioneOdlResponse2 response = null;
		it.terna.kernel.soap.clientgen.odl.annulla.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.annulla.ObjectFactory();
		AnnullaOperazioneOdl annullaOperazioneOdlType = factory.createAnnullaOperazioneOdl();
		annullaOperazioneOdlType.setReq(req);
		JAXBElement<AnnullaOperazioneOdl> annullaOperazioneOdl = factory.createAnnullaOperazioneOdl(annullaOperazioneOdlType);

		JAXBElement<AnnullaOperazioneOdlResponse> responseType;

		if(Objects.isNull(jwtToken) || !isOAuth2) {
			responseType = (JAXBElement<AnnullaOperazioneOdlResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, annullaOperazioneOdl, new BasicAccessToken(username, password));
		} else if(Objects.nonNull(jwtToken.getHttpCode()) && jwtToken.getHttpCode() != HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			responseType = (JAXBElement<AnnullaOperazioneOdlResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, annullaOperazioneOdl, new Oauth2AccessToken(jwtToken.getAccessToken(), null));
		} else {
			response = new AnnullaOperazioneOdlResponse2();
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			return response;
		}

		if (Assorted.notNull(responseType) && Assorted.notNull(responseType.getValue()))
			response = responseType.getValue().getAnnullaOperazioneOdlReturn();
			
		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.debug("Response sendAnnullaOperazioneOdL({}, {}, {}) |{}|", response.getESITO(), response.getCODICEERRORE(), response.getDESCRIZIONEERRORE(), endTime - startTime);
		else
			log.warn("NO Response for sendAnnullaOperazioneOdL({}) |{}|", req.getCODICEORDINEESTERNO(), endTime - startTime);
		
		log.info("end call sendAnnullaOperazioneOdL({}) |{}|", req.getCODICEORDINEESTERNO(), endTime - startTime);
		
		return response;
	}
	
	
	public FacilityResponse sendModificaImpiantoOdL(FacilityRequest req, JwtTokenResponse jwtToken)
	{
		long startTime = System.currentTimeMillis();
		log.debug("call sendModificaImpiantoOdL({}) |{}|", req.getCodiceOggetto(), startTime);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.GEOCALL_MODIFICA_IMPIANTO.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		FacilityResponse response = null;
		it.terna.kernel.soap.clientgen.odl.modificaimpianto.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.modificaimpianto.ObjectFactory();
		Update request = new Update();
		request.setArg0(req);
		JAXBElement<Update> modificaImpiantoRequest = factory.createUpdate(request);
		JAXBElement<UpdateResponse> responseType;

		if(Objects.isNull(jwtToken) || !isOAuth2) {
			responseType = (JAXBElement<UpdateResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, modificaImpiantoRequest, new BasicAccessToken(username, password));
		} else if(Objects.nonNull(jwtToken.getHttpCode()) && jwtToken.getHttpCode() != HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			responseType = (JAXBElement<UpdateResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, modificaImpiantoRequest, new Oauth2AccessToken(jwtToken.getAccessToken(), null));
		} else {
			response = new FacilityResponse();
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			return response;
		}

		if (Assorted.notNull(responseType) && Assorted.notNull(responseType.getValue()))
			response = responseType.getValue().getReturn();
			
		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.debug("Response sendModificaImpiantoOdL({}, {}, {}) |{}|", response.getESITO(), response.getCODICEERRORE(), response.getDESCRIZIONEERRORE(), endTime - startTime);
		else
			log.warn("NO Response for sendModificaImpiantoOdL({}) |{}|", req.getCodiceOggetto(), endTime - startTime);
		
		log.info("end call sendModificaImpiantoOdL({}) |{}|", req.getCodiceOggetto(), endTime - startTime);
		
		return response;
	}

	
	public JwtTokenResponse getToken(EDataIntegrationEntity uri)
	{
		RestEntityInfo entity = restHandler.getRestEntity(uri.getEntityKey());
		if (Assorted.notNull(entity.getUriStub()) && (!Constants.NOT_SUPPORTED_URI.equals(entity.getUriStub())))
		{
			return null;
		} else {
			return jwtAwareRestClient.retrieveJwtToken(	oauth2Geocall.getAzureUri(),
														oauth2Geocall.getTokenEndpoint(),
														oauth2Geocall.getClientId(),
														oauth2Geocall.getClientSecret(),
														oauth2Geocall.getClientCredential(),
														oauth2Geocall.getScope());
		}
	}

    public JwtTokenResponse getTokenOSB(EDataIntegrationEntity uri)
    {
        RestEntityInfo entity = restHandler.getRestEntity(uri.getEntityKey());
        if (Assorted.notNull(entity.getUriStub()) && (!Constants.NOT_SUPPORTED_URI.equals(entity.getUriStub())))
        {
            return null;
        } else {
            return jwtAwareRestClient.retrieveJwtToken(	oauth2Geocall.getAzureUri(),
                    oauth2Geocall.getTokenEndpoint(),
                    oauth2Geocall.getClientIdOSB(),
                    oauth2Geocall.getClientSecretOSB(),
                    oauth2Geocall.getClientCredential(),
                    oauth2Geocall.getScopeOSB());
        }
    }
	
	
	public DatiTipoImpiantoResponse sendTypeAsset(DatiTipoImpiantoRequest req, JwtTokenResponse jwtToken)
	{
		DatiTipoImpiantoResponse response = null;
		long startTime = System.currentTimeMillis();
		log.debug("call sendTypeAsset({}) |{}|", req.getCODICE(), startTime);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.GEOCALL_INVIO_TIPO_IMPIANTO.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		
		it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.ObjectFactory();
		InvioTipoImpianto_Type invioTipoImpiantoType = factory.createInvioTipoImpianto_Type();
		invioTipoImpiantoType.setReq(req);
		JAXBElement<InvioTipoImpianto_Type> invioTipoImpianto = factory.createInvioTipoImpianto(invioTipoImpiantoType);

		JAXBElement<InvioTipoImpiantoResponse> responseType;
		if (Assorted.isNull(jwtToken))
		{
			responseType = (JAXBElement<InvioTipoImpiantoResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, invioTipoImpianto);
		} else {

			if (Assorted.notNull(jwtToken.getHttpCode()) && HttpStatus.INTERNAL_SERVER_ERROR.value() != jwtToken.getHttpCode())
				responseType = (JAXBElement<InvioTipoImpiantoResponse>) apimWsTemplate.marshalSendAndReceive(uri, invioTipoImpianto, new ApimHeaderAccessToken(jwtToken.getAccessToken(), apikey, eamEtlUtils.getHostValue(), null));
			else {
				response = new DatiTipoImpiantoResponse();
				response.setESITO(StatusX4Esito.KO.name());
				response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
				response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
				return response;
			}
		}

		if (Assorted.notNull(responseType) && Assorted.notNull(responseType.getValue()))
			response = responseType.getValue().getInvioTipoImpiantoReturn();
			
		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.debug("Response sendTypeAsset({}, {}, {}) |{}|", response.getESITO(), response.getCODICEERRORE(), response.getDESCRIZIONEERRORE(), endTime - startTime);
		else
			log.warn("NO Response for sendTypeAsset({}) |{}|", req.getCODICE(), endTime - startTime);
		
		log.debug("end call sendTypeAsset({}) |{}|", req.getCODICE(), endTime - startTime);
		
		return response;
	}

	public CreateOdlResponse sendOdL(CreateOdlRequest req, JwtTokenResponse jwtToken)
	{
		long startTime = System.currentTimeMillis();
		log.debug("call sendOdL({}) |{}|", req.getCODICEORDINEESTERNO(), startTime);
		if(log.isDebugEnabled())
			log.debug(EamEtlUtils.writeObjectAsString(req));

		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.GEOCALL_INVIO_ODL.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);

		CreateOdlResponse response = null;
		it.terna.kernel.soap.clientgen.odl.createodlv2.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.createodlv2.ObjectFactory();
		Create creaOdlType = factory.createCreate();
		creaOdlType.setArg0(req);
		JAXBElement<Create> invioOdL = factory.createCreate(creaOdlType);
		JAXBElement<CreateResponse> responseType;

		if(Objects.isNull(jwtToken) || !isOAuth2) {
			responseType = (JAXBElement<CreateResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, invioOdL, new BasicAccessToken(username, password));
		} else if(Objects.nonNull(jwtToken.getHttpCode()) && jwtToken.getHttpCode() != HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			responseType = (JAXBElement<CreateResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, invioOdL, new Oauth2AccessToken(jwtToken.getAccessToken(), null));
		} else {
			response = new CreateOdlResponse();
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			return response;
		}

		if (Assorted.notNull(responseType) && Assorted.notNull(responseType.getValue()))
			response = responseType.getValue().getReturn();

		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.debug("Response sendOdL({}, {}, {}) |{}|", response.getESITO(), response.getCODICEERRORE(), response.getDESCRIZIONEERRORE(), endTime - startTime);
		else
			log.warn("NO Response for sendOdL({}) |{}|", req.getCODICEORDINEESTERNO(), endTime - startTime);

		log.info("end call sendOdL({}) |{}|", req.getCODICEORDINEESTERNO(), endTime - startTime);

		return response;
	}
	
	public StornoResponse sendStorno(StornoRequest req, JwtTokenResponse jwtToken)
	{
		long startTime = System.currentTimeMillis();
		log.debug("call sendStorno({}) |{}|", req.getCodiceOrdineEsterno(), startTime);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.GEOCALL_INVIO_STORNO.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		
		it.terna.kernel.soap.clientgen.odl.storno.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.storno.ObjectFactory();
		StornoCall stornoCall = factory.createStornoCall();
		stornoCall.setArg0(req);
		JAXBElement<StornoCall> invioStornoCall = factory.createStornoCall(stornoCall);	
		
		JAXBElement<StornoCallResponse> responseType = null;
		
		StornoResponse response = null;
		
		if (Assorted.isNull(jwtToken))
		{
			responseType = (JAXBElement<StornoCallResponse>) webServiceOdLTemplate.marshalSendAndReceive(uri, invioStornoCall);
		} else {
			
			if (Assorted.notNull(jwtToken.getHttpCode()) && HttpStatus.INTERNAL_SERVER_ERROR.value() != jwtToken.getHttpCode())
				responseType = (JAXBElement<StornoCallResponse>) apimWsTemplate.marshalSendAndReceive(uri, invioStornoCall, new ApimHeaderAccessToken(jwtToken.getAccessToken(), apikey, eamEtlUtils.getHostValue(), null));
			else {
				response = new StornoResponse();
				response.setESITO(StatusX4Esito.KO.name());
				response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
				response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
				return response;
			}
		}
		
		if (Assorted.notNull(responseType) && Assorted.notNull(responseType.getValue()))
			response = responseType.getValue().getReturn();
			
		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.debug("Response sendStorno({}, {}, {}) |{}|", response.getESITO(), response.getCODICEERRORE(), response.getDESCRIZIONEERRORE(), endTime - startTime);
		else
			log.warn("NO Response for sendStorno({}) |{}|", req.getCodiceOrdineEsterno(), endTime - startTime);
		
		log.info("end call sendStorno({}) |{}|", req.getCodiceOrdineEsterno(), endTime - startTime);
		
		return response;
	}
	
	public Risposta sendAgileAddUnavailabilityRequest(RichiestaAddOrdineLavoro req, JwtTokenResponse jwtToken)
	{
		Risposta response = null;
		long startTime = System.currentTimeMillis();
		log.info("call sendAgileAddUnavailabilityRequest({}) |{}|", req.getCodiceODL(), startTime);
		
		XmlUtility.logRichiestaAdd(req);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.AGILE_ADD_RICHIESTA_INDISPONIBILITA.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		
		if (Assorted.isNull(jwtToken))
		{
			response = (Risposta) webServiceOdLTemplate.marshalSendAndReceive(uri, req);
		} else {
			
			if (Assorted.notNull(jwtToken.getHttpCode()) && HttpStatus.INTERNAL_SERVER_ERROR.value() != jwtToken.getHttpCode())
				response = (Risposta) apimWsTemplate.marshalSendAndReceive(uri, req, new ApimHeaderAccessToken(jwtToken.getAccessToken(), apikeyAgile, eamEtlUtils.getHostValue(), null));
			
			else {
				response = new Risposta();
				response.setEsito(StatusX4Esito.KO.name());
				response.setCodiceErrore(EAMErrorMapping.EAM_0000.getEamCode());
				response.setDescrizioneErrore(EAMErrorMapping.EAM_0000.getErrorDescription());
				return response;
			}
		}

		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.info("Response sendAgileAddUnavailabilityRequest({}, {}, {}) |{}|", response.getEsito(), response.getCodiceErrore(), response.getDescrizioneErrore(), endTime - startTime);
		else
			log.warn("NO Response for sendAgileAddUnavailabilityRequest({}) |{}|", req.getCodiceODL(), endTime - startTime);
		
		return response;
	}
	
	public Risposta sendAgileUpdateUnavailabilityRequest(RichiestaUpdateOrdineLavoro req, JwtTokenResponse jwtToken)
	{
		Risposta response = null;
		long startTime = System.currentTimeMillis();
		log.info("call sendAgileUpdateUnavailabilityRequest({}) |{}|", req.getCodiceODL(), startTime);
		
		XmlUtility.logRichiestaUpdate(req);
		
		RestEntityInfo entity 	= restHandler.getRestEntity(EDataIntegrationEntity.AGILE_UPDATE_RICHIESTA_INDISPONIBILITA.getEntityKey());
		String uri 				= EamEtlUtils.getUri(entity);
		
		if (Assorted.isNull(jwtToken))
		{
			response = (Risposta) webServiceOdLTemplate.marshalSendAndReceive(uri, req);
		} else {
			
			if (Assorted.notNull(jwtToken.getHttpCode()) && HttpStatus.INTERNAL_SERVER_ERROR.value() != jwtToken.getHttpCode())
				response = (Risposta) apimWsTemplate.marshalSendAndReceive(uri, req, new ApimHeaderAccessToken(jwtToken.getAccessToken(), apikeyAgile, eamEtlUtils.getHostValue(), null));
			
			else {
				response = new Risposta();
				response.setEsito(StatusX4Esito.KO.name());
				response.setCodiceErrore(EAMErrorMapping.EAM_0000.getEamCode());
				response.setDescrizioneErrore(EAMErrorMapping.EAM_0000.getErrorDescription());
				return response;
			}
		}

		long endTime = System.currentTimeMillis();
		if (Assorted.notNull(response))
			log.info("Response sendAgileUpdateUnavailabilityRequest({}, {}, {}) |{}|", response.getEsito(), response.getCodiceErrore(), response.getDescrizioneErrore(), endTime - startTime);
		else
			log.info("NO Response for sendAgileUpdateUnavailabilityRequest({}) |{}|", req.getCodiceODL(), endTime - startTime);
		
		return response;
	}

}
