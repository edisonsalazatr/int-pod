package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AssetForModificaAnagrafica;
import it.terna.nas.service.dal.entity.eam.FilialeForAsset;
import it.terna.nas.service.dal.entity.eam.SapForStation;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AssetQueryService
{
	private static final String QUERY_01 = "SELECT ASS.X3SWERK, ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM, ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, ASS.X3SAPID, ASS.X3GEWRK, ASS.X3INGRP, ASS.STATUS, ASS.SITEID FROM ASSET ASS WHERE ASS.SITEID = 'SIT01' AND ASS.ASSETNUM IN (SELECT B.OBJECTNUM FROM X3SAPASSET A, X3SAPTOMAXOBJECT B WHERE A.STRNO = :strno AND A.X3SAPASSETID = B.SAPASSETID AND B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0 AND B.OBJECTNUM = ASS.ASSETNUM AND B.SITEID = ASS.SITEID)";
	private static final String QUERY_02 = "SELECT ASS.X3SWERK, ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM, ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, ASS.X3SAPID, ASS.X3GEWRK, ASS.X3INGRP, ASS.STATUS, ASS.SITEID FROM ASSET ASS WHERE ASS.SITEID = 'SIT01' AND ASS.ASSETNUM IN (SELECT B.OBJECTNUM FROM X3SAPASSET A, X3SAPTOMAXOBJECT B WHERE A.STRNO = :strno AND A.X3SAPASSETID = B.SAPASSETID AND B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0 AND B.OBJECTNUM = ASS.ASSETNUM AND (B.HIERARCHYPATH = 'LINEA\\CAMPATA AEREA' OR B.HIERARCHYPATH = 'LINEA\\TRATTA CAVO') AND B.SITEID = ASS.SITEID)";
	private static final String QUERY_03 = "SELECT DISTINCT B.* FROM (SELECT ASS.X3SWERK, ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM, ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, ASS.X3SAPID, ASS.X3GEWRK, COALESCE(ASS.X3INGRP, ANCESTOR_ASSET.X3INGRP) AS X3INGRP, ASS.STATUS, ASS.SITEID FROM ASSET ASS LEFT JOIN ASSET ANCESTOR_ASSET ON ASS.ANCESTOR = ANCESTOR_ASSET.ASSETNUM AND ASS.SITEID = ANCESTOR_ASSET.SITEID WHERE ASS.SITEID = 'SIT01' AND (ASS.ASSETNUM IN (SELECT OBJECTNUM FROM X3SAPTOMAXOBJECT B, X3SAPASSET A WHERE B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0 AND A.STRNO = :strno AND B.OBJECTNUM = ASS.ASSETNUM AND B.SITEID = ASS.SITEID AND A.X3SAPASSETID = B.SAPASSETID) OR ASS.ASSETNUM NOT IN (SELECT OBJECTNUM FROM X3SAPTOMAXOBJECT B, X3SAPASSET A WHERE B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0 AND A.X3SAPASSETID = B.SAPASSETID AND B.SITEID = ASS.SITEID AND B.OBJECTNUM = ASS.ASSETNUM)) START WITH ASS.ASSETNUM IN (SELECT OBJECTNUM FROM X3SAPASSET A, X3SAPTOMAXOBJECT B WHERE A.STRNO = :strno AND A.X3SAPASSETID = B.SAPASSETID AND B.SITEID = 'SIT01' AND B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0) CONNECT BY PRIOR ASS.ASSETNUM = ASS.PARENT) B";
	private static final String QUERY_04 = "SELECT ASS.X3SWERK, ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM, ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, SAPMAP.X3SAPID, ASS.X3GEWRK, COALESCE(ASS.X3INGRP,(SELECT X3INGRP FROM ASSET WHERE PARENT IS NULL START WITH ASSETNUM = ASS.ASSETNUM CONNECT BY PRIOR PARENT = ASSETNUM AND ROWNUM = 1)) AS X3INGRP, ASS.STATUS, ASS.SITEID FROM ASSET ASS JOIN (SELECT CASE WHEN sa.SAPOBJECT = 'E' THEN SUBSTR(sa.STRNO, -10) ELSE sa.STRNO END AS MATCH_X3SAPID, CASE WHEN sa.SAPOBJECT = 'E' THEN sa.SAPUID ELSE sa.STRNO END AS X3SAPID FROM X3SAPASSET sa WHERE sa.ROOTASSETTYPE = 'Z' AND sa.SAPUID IS NOT NULL AND ((sa.SAPOBJECT = 'S' AND sa.STRNO = :strno) OR (sa.SAPOBJECT = 'E' AND sa.SAPUID = :strno))) SAPMAP ON ASS.X3SAPID = SAPMAP.MATCH_X3SAPID WHERE ASS.SITEID = 'SIT01'";
	private static final String QUERY_04_BIS = "SELECT ASS.X3SWERK, ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM, ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, NVL(ASS.X3SAPID, ASS.X3SAPIDEQ) AS X3SAPID, ASS.X3GEWRK, COALESCE(ASS.X3INGRP, (SELECT X3INGRP FROM ASSET WHERE PARENT IS NULL START WITH ASSETNUM = ASS.ASSETNUM CONNECT BY PRIOR PARENT = ASSETNUM AND ROWNUM = 1)) AS X3INGRP, ASS.STATUS, ASS.SITEID FROM ASSET ASS WHERE ASS.SITEID = 'SIT01' AND NVL(ASS.X3SAPID, ASS.X3SAPIDEQ) = :strno";
	private static final String QUERY_05 = "SELECT ASSETNUM, PARENT, X3SAPID FROM (SELECT sa.STRNO,a.ASSETNUM,a.PARENT,CASE WHEN sa.SAPOBJECT = 'E' THEN sa.SAPUID ELSE a.X3SAPID END X3SAPID FROM X3SAPASSET sa JOIN ASSET a ON (sa.SAPOBJECT = 'S' AND sa.STRNO = a.X3SAPID) OR (sa.SAPOBJECT = 'E' AND SUBSTR(sa.STRNO, -10) = a.X3SAPID) WHERE sa.ROOTASSETTYPE = 'Z' AND sa.SAPUID IS NOT NULL AND a.ASSETNUM = :codiceOggetto)";
    private static final String QUERY_06 = "SELECT ASS.X3SWERK, ASS.ASSETNUM, ASS.SITEID, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM, ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, ASS.X3SAPID, ASS.X3GEWRK, ASS.X3INGRP, ASS.STATUS FROM ASSET ASS WHERE ASS.ASSETNUM = '%s' AND ASS.SITEID = '%s'";
    private static final String QUERY_07 = "SELECT X3INGRP, ASSETNUM FROM ASSET WHERE ASSETNUM = :assetNum";
    private static final String QUERY_08 = "SELECT XG.CODICE_SAP2, XG.CODICE_RADICE2, P.ASSETNUM, P.CLASSSTRUCTUREID, P.X3SAPID, P.STATUS, P.X3GEWRK, P.DESCRIPTION, P.PARENT, P.X3ROOTASSETNUM, P.X3INGRP FROM X4GEOCALLADDRESS XG, ASSET P WHERE P.ASSETNUM = XG.CODICE_OGGETTO START WITH CODICE_OGGETTO = :assetNum CONNECT BY PRIOR CODICE_OGGETTO = CODICE_PADRE";
    private static final String QUERY_09_A = "SELECT * FROM (SELECT LEVEL AS LIVELLO, XG.CODICE_SAP2, XG.CODICE_RADICE2, P.ASSETNUM, P.CLASSSTRUCTUREID, P.X3SAPID, P.STATUS, P.X3GEWRK, P.DESCRIPTION, P.PARENT, P.X3ROOTASSETNUM, P.X3INGRP FROM X4GEOCALLADDRESS XG, ASSET P WHERE P.ASSETNUM = XG.CODICE_OGGETTO START WITH CODICE_OGGETTO IN (";
    private static final String QUERY_09_B = " ) CONNECT BY PRIOR CODICE_OGGETTO = CODICE_PADRE) B ORDER BY LIVELLO";
    private static final String QUERY_10 = "SELECT X3INGRP, ASSETNUM FROM ASSET WHERE PARENT IS NULL START WITH ASSETNUM = :assetNum CONNECT BY PRIOR PARENT = ASSETNUM";
    private static final String QUERY_11 = "SELECT ANCESTOR, ASSETNUM, NULL CLASSSTRUCTUREID, NULL DESCRIPTION, NULL PARENT, NULL STATUS, NULL X3GEWRK, NULL X3INGRP, NULL X3ROOTASSETNUM, NULL X3SAPID, NULL SITEID,  NULL X3SWERK from ASSET where assetnum=:assetNum and siteid=:siteid";
    private static final String QUERY_12 = "SELECT X3INGRP, ASSETNUM, NULL ANCESTOR, NULL CLASSSTRUCTUREID, NULL DESCRIPTION, NULL PARENT, NULL STATUS, NULL X3GEWRK, NULL X3ROOTASSETNUM, NULL X3SAPID, NULL SITEID,  NULL X3SWERK FROM asset where assetnum in (SELECT ancestor FROM assetancestor where assetnum=:assetNum AND siteid=:siteid AND hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum=:assetNum AND siteid=:siteid ))";
    private static final String QUERY_13 = "SELECT a FROM Asset a WHERE a.assetNum = :assetNum AND a.siteId = 'SIT01'";
    private static final String QUERY_14 = "SELECT ASSETNUM, PARENT, X3SAPID FROM (SELECT ASSETNUM, PARENT, X3SAPID, LEVEL AS lvl FROM ASSET START WITH ASSETNUM = :codiceOggetto CONNECT BY PRIOR PARENT = ASSETNUM) WHERE lvl > 1 AND X3SAPID IS NOT NULL ORDER BY lvl FETCH FIRST 1 ROW ONLY";

    
	@Autowired
    private DataAccessService   dataAccessService;
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findX3ingrpByAssetNumAndSiteId(String assetNum, String siteId) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
		log.debug("call findX3ingrpByAssetNumAndSiteId({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		parameters.put(ESQLMappingParameter.SITEID.getName(), siteId);
		List<Asset> result = dataAccessService.search(Asset.class, QUERY_12, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search findX3ingrpByAssetNumAndSiteId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if (Assorted.isNotEmpty(result))
		{
			Asset row = result.get(0);
			
			if (Assorted.isNotEmpty(row.getX3Ingrp()))
				return row.getX3Ingrp();
		}
		
		return null;
	}
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findAncestorByAssetNumAndSiteId(String assetNum, String siteId) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
		log.debug("call findAncestorByAssetNumAndSiteId({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		parameters.put(ESQLMappingParameter.SITEID.getName(), siteId);
		List<Asset> result = dataAccessService.search(Asset.class, QUERY_11, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findAncestorByAssetNumAndSiteId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if (Assorted.isNotEmpty(result))
		{
			Asset row = result.get(0);
			
			if (Assorted.isNotEmpty(row.getAncestor()))
				return row.getAncestor();
		}
		
		return null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findSapFotStazioniByAssetNum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call findSapFotStazioniByAssetNum(assetNum={}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), assetNum);
		List<SapForStation> result = dataAccessService.search(SapForStation.class, QUERY_05, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call findSapFotStazioniByAssetNum (assetNum={}) found [{}] |{}|", assetNum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		if (Assorted.isNotEmpty(result))
		{
			SapForStation row = result.get(0);
			
			if (Assorted.isNotEmpty(row.getX3SapId()))
				return row.getX3SapId();
			else 
				return null;
		}
		else 
		{
			result = dataAccessService.search(SapForStation.class, QUERY_14, parameters, true, true);
			if(Assorted.isNotEmpty(result)) 
			{
				SapForStation row = result.get(0);
			
				if (Assorted.isNotEmpty(row.getX3SapId()))
					return row.getX3SapId();
				else 
					return null;
			}
		}
		return null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findFilialeByAncestor(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call findFilialeByAncestor({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		List<FilialeForAsset> result = dataAccessService.search(FilialeForAsset.class, QUERY_07, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findFilialeByAncestor found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if (Assorted.isNotEmpty(result))
		{
			FilialeForAsset row = result.get(0);
			
			if (Assorted.isNotEmpty(row.getX3Ingrp()))
				return row.getX3Ingrp();
			else 
				return null;
		}
		
		return null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findFilialeByAssetnum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call findFilialeByAssetnum({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		List<FilialeForAsset> result = dataAccessService.search(FilialeForAsset.class, QUERY_10, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findFilialeByAssetnum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if (Assorted.isNotEmpty(result))
		{
			FilialeForAsset row = result.get(0);
			
			if (Assorted.isNotEmpty(row.getX3Ingrp()))
				return row.getX3Ingrp();
			else 
				return null;
		}
		
		return null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
   	public List<AssetForModificaAnagrafica> findAssetForModificaAnagrafica(String assetNum) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call findAssetForModificaAnagrafica({}) |{}|", assetNum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
   		List<AssetForModificaAnagrafica> result = dataAccessService.search(AssetForModificaAnagrafica.class, QUERY_08, parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search findAssetForModificaAnagrafica found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   		
   		if(Assorted.isNotEmpty(result))
   			return result;
   		else
   			return null;
   	}

    @Datasource(datasourceKey = EDatasourceKey.EAM)
   	public List<AssetForModificaAnagrafica> findDoppiaTernaForModificaAnagrafica(List<String> rootList, String assetNum) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call findDoppiaTernaForModificaAnagrafica({}) |{}|", rootList.size(), startTime);		
   		Map <String, String> parameters = new HashMap<>();
   		StringBuilder sql = new StringBuilder(QUERY_09_A);
		for (int i = 0; i < rootList.size(); i++)
		{
			parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName() + "_" + i, rootList.get(i));
			sql.append(":" + ESQLMappingParameter.CODICEOGGETTO.getName() + "_" + i);
			if ((i + 1) < rootList.size())
				sql.append(", ");
		}
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		sql.append(", :" + ESQLMappingParameter.ASSETNUM.getName());
		sql.append(QUERY_09_B);
		
   		List<AssetForModificaAnagrafica> result = dataAccessService.search(AssetForModificaAnagrafica.class, sql.toString(), parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search findDoppiaTernaForModificaAnagrafica found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   		
   		if(Assorted.isNotEmpty(result))
   			return result;
   		else
   			return null;
   	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Asset> getAssetLineeLevel1ByStrno(String strno) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getAssetLineeLevel1ByStrno({}) |{}|", strno, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.STRNO.getName(), strno);
		List<Asset> result = dataAccessService.search(Asset.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getAssetLineeLevel1ByStrno found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Asset> getAssetLineeLevel2ByStrno(String strno) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getAssetLineeLevel2ByStrno({}) |{}|", strno, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.STRNO.getName(), strno);
		List<Asset> result = dataAccessService.search(Asset.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getAssetLineeLevel2ByStrno found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Asset> getAssetLineeLevel34ByStrno(String strno) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getAssetLineeLevel34ByStrno({}) |{}|", strno, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.STRNO.getName(), strno);
		List<Asset> result = dataAccessService.search(Asset.class, QUERY_03, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getAssetLineeLevel34ByStrno found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
 	public List<Asset> getAssetUfficioVoltaicoByStrno(String strno) 
 			throws DataLayerException
 	{
 		long startTime = System.currentTimeMillis();
 		log.debug("call getAssetStazioniByStrno({}) |{}|", strno, startTime);		

 		Map <String, String> parameters = new HashMap<>();
 		parameters.put(ESQLMappingParameter.STRNO.getName(), strno);
 		List<Asset> result = dataAccessService.search(Asset.class, QUERY_04_BIS, parameters, true, true);
 		
 		long endTime = System.currentTimeMillis();
 		log.debug("  - Search getAssetStazioniByStrno found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
 	
 		return result;
 	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
 	public List<Asset> getAssetStazioniByStrno(String strno) 
 			throws DataLayerException
 	{
 		long startTime = System.currentTimeMillis();
 		log.debug("call getAssetStazioniByStrno({}) |{}|", strno, startTime);		

 		Map <String, String> parameters = new HashMap<>();
 		parameters.put(ESQLMappingParameter.STRNO.getName(), strno);
 		List<Asset> result = dataAccessService.search(Asset.class, QUERY_04, parameters, true, true);
 		
 		long endTime = System.currentTimeMillis();
 		log.debug("  - Search getAssetStazioniByStrno found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
 	
 		return result;
 	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
 	public Asset getAssetForCaviInterrati(String assetnum, String siteId) 
 	{
    	try {
	 		long startTime = System.currentTimeMillis();
	 		log.debug("call getAssetForCaviInterrati({}, {}) |{}|", assetnum, siteId, startTime);
	
	 		Map <String, String> parameters = new HashMap<>();
	 		
	 		String query = String.format(QUERY_06, assetnum, siteId);
	 		
	 		List<Asset> result = dataAccessService.search(Asset.class, query, parameters, true, true);
	 		
	 		long endTime = System.currentTimeMillis();
	 		log.debug("  - Search getAssetForCaviInterrati found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	 	
	 		return result != null 
	 				&& result.size()==1
	 				? result.get(0)
	 				: null;
 		
 		}catch (DataLayerException ex){
 			log.debug("Error executing query getAssetForCaviInterrati");
 			
 		}

		return null;
 	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Asset> getAssetByAssetNum(String assetNum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetByAssetNum({}) |{}|", assetNum, startTime);     

        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
        List<Asset> result = dataAccessService.search(Asset.class, QUERY_13, parameters, false, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("end getAssetByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
    
        return result;
    }
}
