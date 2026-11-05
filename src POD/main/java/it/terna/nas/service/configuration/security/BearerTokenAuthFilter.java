package it.terna.nas.service.configuration.security;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.BearerToken;
import it.terna.nas.service.dal.service.BearerTokenQueryService;
import lombok.extern.slf4j.Slf4j;

import static it.terna.nas.service.material.utility.Constants.*;

@Slf4j
@Component
public class BearerTokenAuthFilter extends OncePerRequestFilter
{
	private static Map<String, String> clientIdExternalMap;
	@Value("${agile-client-id:null}")
	private String clientIdAgile;
	
	@Value("${cavi-client-id:null}")
	private String clientIdCavi;
	
	@Value("${elive-client-id:null}")
	private String clientIdElive;

	@Value("${geocall-client-id:null}")
	private String clientIdGeocall;
	
	@Value("${sisnet-client-id:null}")
	private String clientIdSisnet;

	@Value("${sap-client-id:null}")
	private String clientIdSAP;
	
	@Value("${tagetik-client-id:null}")
	private String clientIdTagetik;
	
	@Autowired
	private BearerTokenQueryService bearerTokenQueryService;
	
	private static final String costsRetrievalUriPattern = "/eam/tagetik/costsRetrieval/";
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
			throws ServletException, IOException
	{
		String authHeader 	= request.getHeader(AUTHORIZATION);
		String method		= request.getMethod();

		if (HttpMethod.POST.name().equals(method) &&  Assorted.notNull(authHeader) && authHeader.startsWith(BEARER) && (!authHeader.substring(7).isBlank()))
		{
			String accessToken 	= authHeader.substring(7);
			try {
				boolean valid = isTokenValid(request, accessToken);

				if (!valid)
				{
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}

			} catch (Exception q) {
				log.error(q.getMessage(), q);
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}

		}

		filterChain.doFilter(request, response);
	}

	
	private Map<String, String> getClientIdExternal()
	{
		if (Assorted.notNull(clientIdExternalMap))
			return clientIdExternalMap;
		else {
			clientIdExternalMap = new HashMap<>();
			clientIdExternalMap.put(GEOCALL, 		clientIdGeocall);
			clientIdExternalMap.put(ELIVE, 			clientIdElive);
			clientIdExternalMap.put(CAVI_INTERRATI, clientIdCavi);
			clientIdExternalMap.put(AGILE, 			clientIdAgile);
			clientIdExternalMap.put(SISNET, 		clientIdSisnet);
			clientIdExternalMap.put(SAP, 			clientIdSAP);
			clientIdExternalMap.put(TAGETIK, 		clientIdTagetik);
		}
		log.debug("clientIdExternalMap: {}", clientIdExternalMap);
		return clientIdExternalMap;
	}
	
	
	private boolean isTokenValid(HttpServletRequest request, String accessToken)
			throws ParseException, DataLayerException
	{
		JWT interstitialJwt 		= JWTParser.parse(accessToken);
		JWTClaimsSet jwtClaimsSet 	= interstitialJwt.getJWTClaimsSet();
		String clientIdFromAppId 	= jwtClaimsSet.getClaim(CLIENT_ID_CLAIM).toString();
		String uri 					= request.getRequestURI();
		boolean esito 				= false;
		
		log.debug("clientId: {} uri: {}", clientIdFromAppId, uri);
		if (Assorted.isNotEmpty(clientIdFromAppId) || Assorted.notNull(clientIdFromAppId))
		{
			if (uri.startsWith(costsRetrievalUriPattern)) {
	            uri = costsRetrievalUriPattern;
	        }
			Map<String, String> clientIdExternal = getClientIdExternal();
			Map<String, BearerToken> uriMap = bearerTokenQueryService.getBearerTokenUris();
			BearerToken bearerTokenInstance = uriMap.get(uri);
			if(Assorted.isNull(bearerTokenInstance))
				return esito;
			String clientIdFind = clientIdExternal.get(bearerTokenInstance.getClientName());
			log.debug("clientIdFind: {}", clientIdFind);
			log.trace("clientId: {}", clientIdFromAppId);
			if (clientIdFromAppId.equals(clientIdFind))
				esito = true;
		}		
		log.debug("isTokenValid ==> {}", esito);
		return esito;
	}
}
