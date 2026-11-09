package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.Constants.MSG_005;
import static it.terna.nas.service.material.utility.Constants.SNOW_ATTRIBUTES;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.kafka.common.Uuid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.dal.entity.eam.AssetClassStructure;
import it.terna.nas.service.dal.entity.eam.AssetRestricted;
import it.terna.nas.service.dal.entity.eam.ClassStructureHierarchy;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4EsitoIntSnow;
import it.terna.nas.service.dal.entity.eam.X4RenameSapIdSnow;
import it.terna.nas.service.dal.entity.eam.X4Snow;
import it.terna.nas.service.dal.entity.eam.X4SnowAsset;
import it.terna.nas.service.dal.entity.eam.X4SnowAttributes;
import it.terna.nas.service.dal.service.ClassificationService;
import it.terna.nas.service.dal.service.SnowAssetQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntSnowApiService;
import it.terna.nas.service.dal.service.X4EsitoIntSnowQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.handler.rest.ServiceNowRestClient;
import it.terna.nas.service.exposure.ws.maximo.GenericEsitoResponse;
import it.terna.nas.service.exposure.ws.snow.SnowAttribute;
import it.terna.nas.service.exposure.ws.snow.SnowModifyAssetRequest;
import it.terna.nas.service.material.dto.AssetDTO;
import it.terna.nas.service.material.dto.X4EsitoIntSnowRequest;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Catalog.ClassificationType;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.ServiceNowInterface;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceNowHandler extends AbstractHandler
{
	@Autowired
	private ClassificationService classificationService;
	
	@Autowired
	private ServiceNowRestClient serviceNowRestClient;
	
	@Autowired
    private SnowAssetQueryService snowAssetQueryService;
	
	@Autowired
	private X4EsitoIntSnowApiService x4EsitoIntApiService;
	
	@Autowired
	private X4EsitoIntSnowQueryService x4EsitoIntQueryService;
	

	private List<String[]> constructCsvAssetHierarchyData(List<X4Snow> dataList) {
		List<String[]> returningDataList = new ArrayList<>();
		for(X4Snow data : dataList) 
		{
			String[] returningData = {
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodiceOggetto()), //assetum
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(EamEtlUtils.snowDateAsString(data.getChangeDate())), //changeDate
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3Ingrp()), //ingrp
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3Gewrk()), //gewrk
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(EamEtlUtils.snowDateAsString(data.getInstallDate())), //installDate
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getSequence1()), //sequence1
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getSequence2()), //sequence2
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStatus()), //status
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(EamEtlUtils.snowDateAsString(data.getStatusDate())), //statusDate
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getSwerk()), //swerk
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getSwerkDescription()), //swerkDescription
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getClassificationId()), //classificationId
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getRootSapId1()), //rootSapId1
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getRootSapId2()), //rootSapId2
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getDescription()), //description
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3CostruttoreComponente()), //costruttoreComponente
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3CostruttoreComponenteDescr()), //costruttoreComponenteDescr
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3FunzioneElemento()), //funzioneElemento
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3RiferimentoFase()), //riferimentoFase
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getX3TipoComponente()), //tipoComponente
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodiceSap()), //sapId
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodiceSap2()), //sapId2
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodicePadre()), //codicePadre
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodiceRadice()), //codiceRadice
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodicePadre2()), //codicePadre2
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCodiceRadice2()), //codiceRadice2
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinLunghezza()), //LIN_LUNGHEZZA
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinConduttoreAereo()), //LIN_CONDUTTORE_AEREO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinAnnoInstallazione()), //LIN_ANNO_INSTALLAZIONE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinRifFune()), //LIN_RIF_FUNE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoFuneGuardia()), //LIN_TIPO_FUNE_GUARDIA
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinCostruttoreGiunto()), //LIN_COSTRUTTORE_GIUNTO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoGiunto()), //LIN_TIPO_GIUNTO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinCostruttoreTerminale()), //LIN_COSTRUTTORE_TERMINALE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoTerminale()), //LIN_TIPO_TERMINALE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinRifAssetArmCond()), //LIN_RIF_ASSET_ARM_COND
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoIsolatoriLin()), //LIN_TIPO_ISOLATORI_LIN
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinNumCatene()), //LIN_NUM_CATENE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoIsolatoriDer()), //LIN_TIPO_ISOLATORI_DER
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinRifAssetArmFdg()), //LIN_RIF_ASSET_ARM_FDG
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoIsolatori()), //LIN_TIPO_ISOLATORI
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoPalificazione()), //LIN_TIPO_PALIFICAZIONE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoSostegno()), //LIN_TIPO_SOSTEGNO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinStrutturaSostegno()), //LIN_STRUTTURA_SOSTEGNO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinAnnoCostruzione()), //LIN_ANNO_COSTRUZIONE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinCostruttoreCavo()), //LIN_COSTRUTTORE_CAVO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinConduttoreCavo()), //LIN_CONDUTTORE_CAVO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinIsolamentoCavo()), //LIN_ISOLAMENTO_CAVO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinTipoConduttoreCavo()), //LIN_TIPO_CONDUTTORE_CAVO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinSezioneConduttoreCavo()), //LIN_SEZIONE_CONDUTTORE_CAVO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinRifFase()), //LIN_RIF_FASE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinRifPosizione()), //LIN_RIF_POSIZIONE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCoordinateLong()), //COORDINATE_LONG
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getCoordinateLat()), //COORDINATE_LAT
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getLinElementoIndisp()), //LIN_ELEMENTO_INDISP
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStzDataCostruzione()), //STZ_DATA_COSTRUZIONE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStzTensioneNominale()), //STZ_TENSIONE_NOMINALE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStzFunzionamentoComandi()), //STZ_FUNZIONAMENTO_COMANDI
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStzTipoComparto()), //STZ_TIPO_COMPARTO
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStzPotenzaNominale()), //STZ_POTENZA_NOMINALE
					EamEtlUtils.blankStringIfNullAndReplaceCharacters(data.getStzElementoIndisp()), //STZ_ELEMENTO_INDISP
			};
			returningDataList.add(returningData);
		}
		return returningDataList;
	}
	

	private void constructCsvList(List<AssetRestricted> objectList) 
								  throws DataLayerException, IOException 
	{
		if (Assorted.isNotEmpty(objectList)) 
		{
			Set<String> lineElaborated = new HashSet<>();
			CSVWriter csv              = null;
			int numRecord              = 0;
			int numFile                = 0;
			FileWriter fileWriter      = null;
			
			try {
			    
        		for (AssetRestricted object: objectList)
        		{
        		    if (lineElaborated.contains(object.getCodiceOggetto()))
        		        continue;
        		    
                    List<X4Snow> hierarchyList = new ArrayList<>();

        			List<String> rootCodes = new ArrayList<>(findAllRoot(object.getCodiceOggetto()));
        				
        			if(Assorted.isNotEmpty(rootCodes)) 
        			{
        				rootCodes = rootCodes.stream().filter(x -> !lineElaborated.contains(x)).toList();
        				if(Assorted.isNotEmpty(rootCodes))
        					hierarchyList = snowAssetQueryService.getHierarchy(rootCodes);
        				lineElaborated.addAll(rootCodes);
        			}
        	
        			if (Assorted.isNotEmpty(hierarchyList))
        			{
        			    if (EamEtlUtils.isBiggerThanBufferSize(hierarchyList.size() + numRecord))
        			    {
        			        IOUtils.closeQuietly(fileWriter);
        	                IOUtils.closeQuietly(csv);
        			        csv = null;
        			        numRecord = 0;
        			    }
        			    
        			    if (Assorted.isNull(csv))
        			    {
        			        fileWriter  = new FileWriter(EamEtlUtils.createFileNameCustomCsv(Constants.PKG_X4SNOW_FILEPATH, EamEtlUtils.constructServiceNowCsvKey(++numFile))); 
        			        csv         = new CSVWriter(fileWriter, 
        			                                    Constants.CSV_SEPARATOR_SNOW, 
        		                                        CSVWriter.NO_QUOTE_CHARACTER, 
        		                                        CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
        		                                        CSVWriter.DEFAULT_LINE_END); 
        			        
        			        csv.writeNext(Constants.HEADERS_ANAGRAFICA_MASSIVA);
        			        csv.flush();
        			    }
        			    
        			    csv.writeAll(constructCsvAssetHierarchyData(hierarchyList));
        			    csv.flush();
        			    numRecord += hierarchyList.size();
        			}
        		}
        		
			} finally {
			    IOUtils.closeQuietly(fileWriter);
			    IOUtils.closeQuietly(csv);
			}
		}
	}
	
	

	private SnowModifyAssetRequest convertToSnowRequest(List<X4SnowAsset> snowAssets, 
														List<X4SnowAttributes> snowAttributes) 
	{
		X4SnowAsset firstRow = snowAssets.get(0);
		X4SnowAsset secondRow = snowAssets.size() >= 2 ? snowAssets.get(1) : null;
		boolean isSecondRowActive = Assorted.notNull(secondRow) ? true : false;
		SnowModifyAssetRequest req = new SnowModifyAssetRequest();
		req.setAssetNum(EamEtlUtils.blankStringIfNull(firstRow.getAssetnum()));
		req.setChangeDate(EamEtlUtils.blankStringIfNull(EamEtlUtils.snowDateAsString(firstRow.getChangeDate())));
		req.setGewrk(EamEtlUtils.blankStringIfNull(firstRow.getGewrk()));
		req.setIngrp(EamEtlUtils.blankStringIfNull(firstRow.getIngrp()));
		req.setInstallDate(EamEtlUtils.blankStringIfNull(EamEtlUtils.snowDateAsString(firstRow.getInstallDate())));
		req.setSequence1(EamEtlUtils.blankStringIfNull(firstRow.getSequence()));
		req.setSequence2(EamEtlUtils.blankStringIfNull(isSecondRowActive ? secondRow.getSequence() : null));
		req.setStatus(EamEtlUtils.blankStringIfNull(firstRow.getStatus()));
		req.setStatusDate(EamEtlUtils.blankStringIfNull(EamEtlUtils.snowDateAsString(firstRow.getStatusDate())));
		req.setSwerk(EamEtlUtils.blankStringIfNull(firstRow.getSwerk()));
		req.setSwerkDescription(EamEtlUtils.blankStringIfNull(firstRow.getSwerkDescription()));
		req.setClassificationId(EamEtlUtils.blankStringIfNull(firstRow.getClassificationId()));
		req.setRootSapId1(EamEtlUtils.blankStringIfNull(firstRow.getRootSapId()));
		req.setRootSapId2(EamEtlUtils.blankStringIfNull(isSecondRowActive ? secondRow.getRootSapId() : null));
		req.setDescription(EamEtlUtils.blankStringIfNull(firstRow.getDescription()));
		req.setCostruttoreComponente(EamEtlUtils.blankStringIfNull(firstRow.getCostruttoreComponente()));
		req.setCostruttoreComponenteDescr(EamEtlUtils.blankStringIfNull(firstRow.getCostruttoreComponenteDescr()));
		req.setFunzioneElemento(EamEtlUtils.blankStringIfNull(firstRow.getFunzioneElemento()));
		req.setRiferimentoFase(EamEtlUtils.blankStringIfNull(firstRow.getRiferimentoFase()));
		req.setTipoComponente(EamEtlUtils.blankStringIfNull(firstRow.getTipoComponente()));
		req.setSapId(EamEtlUtils.blankStringIfNull(firstRow.getSapId()));
		req.setSapId2(EamEtlUtils.blankStringIfNull(isSecondRowActive ? secondRow.getSapId() : null));
		req.setCodicePadre(EamEtlUtils.blankStringIfNull(firstRow.getCodicePadre()));
		req.setCodiceRadice(EamEtlUtils.blankStringIfNull(firstRow.getCodiceRadice()));
		req.setCodicePadre2(EamEtlUtils.blankStringIfNull(isSecondRowActive ? secondRow.getCodicePadre() : null));
		req.setCodiceRadice2(EamEtlUtils.blankStringIfNull(isSecondRowActive ? secondRow.getCodiceRadice() : null));
		List<SnowAttribute> attributeList = new ArrayList<>();
		Map<String, X4SnowAttributes> snowAttributesMap = Assorted.isNotEmpty(snowAttributes) 
														  ? snowAttributes.stream()
													        .collect(Collectors.toMap(
													        	     X4SnowAttributes::getAssetAttrId, 
													        	     Function.identity())) 
													      : null;
		for (String requiredAttrId : SNOW_ATTRIBUTES) 
		{
	        SnowAttribute attribute = new SnowAttribute();
	        attribute.setAssetAttrId(requiredAttrId);
	        X4SnowAttributes snowAttribute = Assorted.notNull(snowAttributesMap) ? snowAttributesMap.get(requiredAttrId) : null;
	        String value = null;
	        if (Assorted.notNull(snowAttribute)) {
	            value = getProcessedValue(snowAttribute);
	        }
	        attribute.setValue(EamEtlUtils.blankStringIfNull(value)); 
	        attributeList.add(attribute);
	    }
	    req.setAttributes(attributeList);
		return req;
	}
	
	private String getProcessedValue(X4SnowAttributes snowAttribute) 
	{
	    String dataType = snowAttribute.getDataType();
	    if (Assorted.isNull(dataType)) 
	        return null;
	    if (Catalog.AttributesDataTypes.NUMERIC.toString().equals(dataType)) 
	        return Assorted.notNull(snowAttribute.getNumValue()) ? snowAttribute.getNumValue().toString() : null;
	    else if (Catalog.AttributesDataTypes.DATE.toString().equals(dataType)) 
	        return EamEtlUtils.snowDateAsString(snowAttribute.getDateValue());
	    else if (Catalog.AttributesDataTypes.TABLE.toString().equals(dataType)) 
	        return (Assorted.isNotEmpty(snowAttribute.getTableValue())) ?  snowAttribute.getTableValue() : null;
	    else if (Catalog.AttributesDataTypes.ALN.toString().equals(dataType)) 
	        return (Assorted.isNotEmpty(snowAttribute.getAlnValue())) ?  snowAttribute.getAlnValue() : null;	            
	    else 
	        return null; 
	}

	private Set<String> findAllRoot(String rootLine) 
	        throws DataLayerException
	{
	    Set<String> result         = new HashSet<>();
	    Set<String> daElaborare    = new HashSet<>();
	    daElaborare.add(rootLine);	
	    
	    while (Assorted.isNotEmpty(daElaborare))
	    {
	        Set<String> daElaborareTmp = new HashSet<>();
	        result.addAll(daElaborare);
	        for (String asset: daElaborare)
	        {
	            List<String> rootCodes = snowAssetQueryService.getDirectAssetRoots(asset);
	            
	            if(Assorted.isNotEmpty(rootCodes)) 
	            {
		            for(String assetJ: rootCodes) 
		            {
		                if (!result.contains(assetJ))
		                    daElaborareTmp.add(assetJ);	            
		            }
	            }
	        }
	        
	        daElaborare = daElaborareTmp;
	    }
	    
	    return result;
	}

	
	@Async
	public void modificaAnagraficaMassiva() 
	{
		modificaAnagraficaMassivaInternal();
	}	
	
	
	private void modificaAnagraficaMassivaInternal() 
	{
	    long startTime = System.currentTimeMillis();
	    
		try {
			log.debug("starting modificaAnagraficaMassivaInternal() at |{}|", startTime);
			
			snowAssetQueryService.executeSnowStoredProcedure();
			
			Map<String, List<ClassStructureHierarchy>> linesMap        = classificationService.getMapClassStructure(ClassificationType.LINEA);
			Map<String, List<ClassStructureHierarchy>> stationMap      = classificationService.getMapClassStructure(ClassificationType.STAZIONE);
			Map<String, List<ClassStructureHierarchy>> ufficioMap      = classificationService.getMapClassStructure(ClassificationType.UFFICIO);
			Map<String, List<ClassStructureHierarchy>> fotovoltaicoMap = classificationService.getMapClassStructure(ClassificationType.FOTOVOLTAICO);
			
            String lineId         = null;
			String stationId      = null;
			String ufficioId      = null;
			String fotovoltaicoId = null;
			
            if (Assorted.isNotEmpty(linesMap) && Assorted.isNotEmpty(linesMap.get(ClassificationType.LINEA.name()))) 
				lineId = linesMap.get(ClassificationType.LINEA.name()).get(0).getClassStructureId();
			
            if(Assorted.isNotEmpty(stationMap) && Assorted.isNotEmpty(stationMap.get(ClassificationType.STAZIONE.name()))) 
				stationId = stationMap.get(ClassificationType.STAZIONE.name()).get(0).getClassStructureId();
            
            if(Assorted.isNotEmpty(ufficioMap) && Assorted.isNotEmpty(ufficioMap.get(ClassificationType.UFFICIO.getName()))) 
            	ufficioId = ufficioMap.get(ClassificationType.UFFICIO.getName()).get(0).getClassStructureId();
            
            if(Assorted.isNotEmpty(fotovoltaicoMap) && Assorted.isNotEmpty(fotovoltaicoMap.get(ClassificationType.FOTOVOLTAICO.getName()))) 
            	fotovoltaicoId = fotovoltaicoMap.get(ClassificationType.FOTOVOLTAICO.getName()).get(0).getClassStructureId();

			List<AssetRestricted> lineObjectsList          = snowAssetQueryService.getAssetObjectsByClassStructure(lineId);
			List<AssetRestricted> stationObjectsList       = snowAssetQueryService.getAssetObjectsByClassStructure(stationId);
			List<AssetRestricted> ufficioObjectsList       = snowAssetQueryService.getAssetObjectsByClassStructure(ufficioId);
			List<AssetRestricted> fotovoltaicoObjectsList  = snowAssetQueryService.getAssetObjectsByClassStructure(fotovoltaicoId);
            List<AssetRestricted> allObjectList       = new ArrayList<>();
			
			if (Assorted.isNotEmpty(lineObjectsList)) 
			    allObjectList.addAll(lineObjectsList);
			
			if (Assorted.isNotEmpty(stationObjectsList)) 
			    allObjectList.addAll(stationObjectsList);
			
			if (Assorted.isNotEmpty(ufficioObjectsList)) 
			    allObjectList.addAll(ufficioObjectsList);
			
			if (Assorted.isNotEmpty(fotovoltaicoObjectsList)) 
			    allObjectList.addAll(fotovoltaicoObjectsList);
			
			constructCsvList(allObjectList);
		} 
		catch (Exception e) 
		{
			log.error("Generic Error: " + e.getMessage(), e);
		}
		
		long endTime = System.currentTimeMillis();
		log.info("CSV files written at |{}|", endTime - startTime);
	}
	
	public void sendAssetInformation(String topic, String uuidResend, String payload) 
	{
		Instant startDate		= Instant.now();
		String uuid 			= Uuid.randomUuid().toString();

		X4EsitoIntSnowRequest x4EsitoIntRequest = new X4EsitoIntSnowRequest();
		x4EsitoIntRequest.setIdinterfaccia(ServiceNowInterface.SEND_ASSET_INTERFACE.getName());
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		try {
	        if(Assorted.notNull(payload))
	        {
	        	if(Assorted.notNull(uuidResend)) 
	        	{
	        		x4EsitoIntRequest.setUniqueid(uuidResend);
	        		X4EsitoIntSnow parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
	        		x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntSnowId() : null);
	        	}
	        	else
	        		x4EsitoIntRequest.setRetry(0L);
	        	
	        	AssetDTO asset = new Gson().fromJson(payload, AssetDTO.class);
	        	x4EsitoIntRequest.setAssetnum(asset.getAssetnum());
	        	if(Assorted.notNull(asset.getClassstructureid())) {
		        	ClassStructureTipology classStructureTipology = classificationService.getClassStructureTipology(asset.getClassstructureid());
		        	if(Assorted.isNull(classStructureTipology)) {
		        		x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0014.getEamCode());
		        		x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0014.getErrorDescription());
		        	}
		        	else {
		        		List<X4SnowAsset> snowAssets = null;
		        		List<X4SnowAttributes> snowAttributes = null;
		        		if(ClassificationType.LINEA.getName().substring(0, 1).equals(classStructureTipology.getType())) {
		        			//Linea
		        			snowAssets = snowAssetQueryService.getX4SnowLinee(asset.getAssetnum());
		        			if(Assorted.isNotEmpty(snowAssets))
		        				snowAttributes = snowAssetQueryService.getX4SnowLineeAttributes(asset.getAssetnum(), snowAssets.get(0).getRootSapId());
		        		}
		        		else if (ClassificationType.STAZIONE.getName().substring(0, 1).equals(classStructureTipology.getType())) {
		        			//Stazione
		        			snowAssets = snowAssetQueryService.getX4SnowStazioni(asset.getAssetnum());
		        			
		        			if (Assorted.isNotEmpty(snowAssets) && Assorted.isEmpty(snowAssets.get(0).getSapId()) && Assorted.isNotEmpty(snowAssets.get(0).getCodicePadre())) 
		        			    snowAssets.get(0).setSapId(getSapIdParentStazioni(snowAssets.get(0).getCodicePadre()));
		        			    
		        			snowAttributes = snowAssetQueryService.getX4SnowStazioniAttributes(asset.getAssetnum());
		        		
		        		} else {
		        			//Ufficio - Fotovoltaico
		        			snowAssets = snowAssetQueryService.getX4SnowUfficioVoltaico(asset.getAssetnum());
		        		}
		        		
		        		if(Assorted.isNotEmpty(snowAssets)) 
		        		{
		        			SnowModifyAssetRequest requestForSnow = convertToSnowRequest(snowAssets, snowAttributes);
		        			if(!Constants.BLANK_STRING.equals(requestForSnow.getSapId())) 
		        			{
		        				GenericEsitoResponse response = serviceNowRestClient.sendModifyAssetSnow(requestForSnow);
			        			if (StatusX4Esito.KO.name().equals(response.getEsito()) || Assorted.isNull(response))
									throw new EAMException(response.getErrorCode(), response.getErrorDescription());
		        			}
		        			else 
		        				throw new EAMException(EAMErrorMapping.EAM_0012.getEamCode(), String.format(Constants.X4SNOWASSET_SAPID_NOT_FOUND_MESSAGE, asset.getAssetnum()));
		        		}
		        		else
		        			x4EsitoIntRequest.setDesc_esito(String.format(Constants.X4SNOWASSET_NOT_FOUND_MESSAGE, asset.getAssetnum()));
		        	}
	        	}
	        	else {
	        		String msg = String.format(MSG_005, asset.getAssetnum());
	        		log.info(msg);
	        		x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0014.getEamCode());
	        		x4EsitoIntRequest.setDesc_esito(msg);
	        	}
	        }
		        		
		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());			
		} finally {
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
		}
		
		try {
			x4EsitoIntApiService.sendEsitoIntSnow(x4EsitoIntRequest);
		} catch(java.lang.Exception e) {
			log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
					  Assorted.notNull(x4EsitoIntRequest)
					  ? x4EsitoIntRequest.toString() 
					  : null,
					  e.getMessage());
		}
	}
	
	
	private String getSapIdParentStazioni(String assetNumParent) 
	        throws DataLayerException 
    {
	    String sapIdParent = null;
        List<X4SnowAsset> x4SnowStazioniParent = snowAssetQueryService.getX4SnowStazioni(assetNumParent);
        if (Assorted.isNotEmpty(x4SnowStazioniParent))
        {
            sapIdParent = x4SnowStazioniParent.get(0).getSapId();
            
            if (Assorted.isEmpty(sapIdParent) && Assorted.isNotEmpty(x4SnowStazioniParent.get(0).getCodicePadre())) 
                sapIdParent = getSapIdParentStazioni(x4SnowStazioniParent.get(0).getCodicePadre());
        }
        
        return sapIdParent;
    }
	
	
	public void checkRenameSapIdAndRelaunch() 
	{
		try 
		{
			List<X4RenameSapIdSnow> objectsToNotify = snowAssetQueryService.getObjectsToNotifyRename();
			if(Assorted.isNotEmpty(objectsToNotify)) 
			{
				for(X4RenameSapIdSnow item : objectsToNotify) 
				{
					try 
					{
						List<AssetClassStructure> assets = snowAssetQueryService.getClassstructureId(item.getAssetnum());
						if(Assorted.isNotEmpty(assets)) {
							for(AssetClassStructure asset : assets) {
								ClassStructureTipology classStructureTipology = classificationService.getClassStructureTipology(asset.getClassStructureId());
								List<X4SnowAsset> snowAssets = null;
				        		List<X4SnowAttributes> snowAttributes = null;
				        		if(ClassificationType.LINEA.getName().substring(0, 1).equals(classStructureTipology.getType())) {
				        			//Linea
				        			snowAssets = snowAssetQueryService.getX4SnowLinee(asset.getAssetNum());
				        			if(Assorted.isNotEmpty(snowAssets))
				        				snowAttributes = snowAssetQueryService.getX4SnowLineeAttributes(asset.getAssetNum(), snowAssets.get(0).getRootSapId());
				        		}
				        		else if (ClassificationType.STAZIONE.getName().substring(0, 1).equals(classStructureTipology.getType())) {
				        			//Stazione
				        			snowAssets = snowAssetQueryService.getX4SnowStazioni(asset.getAssetNum());
				        			snowAttributes = snowAssetQueryService.getX4SnowStazioniAttributes(asset.getAssetNum());
				        		}
				        		else {
				        			//Ufficio - Fotovoltaico
				        			snowAssets = snowAssetQueryService.getX4SnowUfficioVoltaico(asset.getAssetNum());
				        		}
				        		if(Assorted.isNotEmpty(snowAssets)) {
				        			SnowModifyAssetRequest requestForSnow = convertToSnowRequest(snowAssets, snowAttributes);
				        			//SAPID Modificato
				        			requestForSnow.setSapId(item.getSapIdDest());
						        	GenericEsitoResponse response = serviceNowRestClient.sendModifyAssetSnow(requestForSnow); 
									if (StatusX4Esito.KO.name().equals(response.getEsito()) || Assorted.isNull(response))
										throw new EAMException(response.getErrorCode(), response.getErrorDescription());
				        		}
								snowAssetQueryService.updateNotiDetails(item.getX4RenameSapIdSnowId(), null);
							}
						}
						else
							snowAssetQueryService.updateNotiDetails(item.getX4RenameSapIdSnowId(), Constants.NOTI_SNOW);
					}
					catch(Exception e) {
						log.error(e.getMessage(), e);
						snowAssetQueryService.updateNotiDetails(item.getX4RenameSapIdSnowId(), Constants.NOTI_SNOW);
					}
				}
			}
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
