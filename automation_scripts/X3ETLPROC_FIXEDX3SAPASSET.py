#X3ETLPROC_FIXEDX3SAPASSET
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from psdi.mbo import DBShortcut
from com.terna.integration.service import IntegrationService
import time

def log(nivel,descripcion,numrows,error,notas):
    logSet=mbo.getMboSet("X3SCRIPTLOG")
    NewLog = logSet.add()
    NewLog.setValue("DESCRIPTION",nivel+" - "+descripcion)
    NewLog.setValue("X3SCRIPT",scriptName)
    NewLog.setValue("TIMESTAMP",MXServer.getMXServer().getDate())
    NewLog.setValue("NUMROWS",numrows)
    NewLog.setValue("X3ERROR",error)
    NewLog.setValue("X3NOTES",notas)
    logSet.save()
    logSet.close()
    time.sleep(2)

param = mbo.getString("PARAMETER")
integrationService = IntegrationService.getService()

mxServer = MXServer.getMXServer()
dbShortcut=DBShortcut()
dbShortcut.connect(mxServer.getSystemUserInfo().getConnectionKey())
dbShortcutParent=DBShortcut()
dbShortcutParent.connect(mxServer.getSystemUserInfo().getConnectionKey())
dbShortcutSource=DBShortcut()
dbShortcutSource.connect(mxServer.getSystemUserInfo().getConnectionKey())

fecha = mxServer.getDate()

if param == 'PMTSTLIN':
    i=0
    log("INICIO","Carga PMTSTLIN Nivel 1 Líneas","","","Inicio Carga PMTSTLIN Nivel 1 Líneas")
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        x3sst.KOSTL,
        x3sst.MSGRP
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1003
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIN' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.DUPLICATE = '0'
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        STTXU=GenAssetSet.getString("STTXU")
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        KOSTL=GenAssetSet.getString("KOSTL")
        MSGRP=GenAssetSet.getString("MSGRP")
        
        AssetSet=mbo.getMboSet("ASSETFORINSERT")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",STTXU,11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP,11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR)
        NewAsset.setValue("X3BUKRS",BUKRS,11L)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        assnum=NewAsset.getString("ASSETNUM")
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        NewAsset.setValue("X3KOSTL",KOSTL)
        NewAsset.setValue("X3MSGRP",MSGRP)
        NewAsset.setValue("CLASSSTRUCTUREID",'1003')
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()        
        
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSET")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNO)
        NewX3SAPAsset.setValue("SAPCLASS",'LINEA')
        NewX3SAPAsset.setValue("SAPLEVEL",'1')
        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIN Nivel 1 Líneas",i,"","Fin Carga PMTSTLIN Nivel 1 Líneas")


if param == 'PMTSTLIL':
    AssetSet=mbo.getMboSet("ASSETFORINSERT")
    X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
    AssetLocRelationSet=mbo.getMboSet("ASSETLOCRELATION")
    i=0
    log("INICIO","Carga PMTSTLIL Nivel 2 Campatas (no duplicadas)","","","Inicio Carga PMTSTLIL Nivel 2 Campatas (no duplicadas)")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1004
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIL' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.DUPLICATE = '0'
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        STTXU=GenAssetSet.getString("STTXU")
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        POSNR=GenAssetSet.getString("POSNR")

        
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",STTXU,11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR)
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        assnum=NewAsset.getString("ASSETNUM")
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        NewAsset.setValue("CLASSSTRUCTUREID",'1004')
        integrationService.setScenario(2)
        AssetSet.save()
        
		
        
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'CAMPATA')
        NewX3SAPAsset.setValue("SAPLEVEL",'2')
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
        
        SapParentSet.close()
        X3SAPAssetSet.save()
        
        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND X3SAPIDETL = '"""+ str(TPLMA) +"""'
        """

        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())

        while SourceSet.next():
            NewAssetLocRelation = AssetLocRelationSet.add()
            SourceAssetnum=SourceSet.getString("ASSETNUM")
            NewAssetLocRelation.setValue("SITEID",'SIT01')
            NewAssetLocRelation.setValue("ORGID",'ORG01')
            NewAssetLocRelation.setValue("ASSETRELATIONNUM",'INCLUDES')
            NewAssetLocRelation.setValue("CREATEDDATE",fecha)
            NewAssetLocRelation.setValue("ISLINEARREF",'0')
            NewAssetLocRelation.setValue("LINEARASSETLOCRELATIONID",'0')
            NewAssetLocRelation.setValue("SOURCEASSETNUM",str(SourceAssetnum),11L)
            NewAssetLocRelation.setValue("TARGETASSETNUM",assnum,11L)
            NewAssetLocRelation.setValue("LOADED",'1')
            NewAssetLocRelation.setValue("X3SAPID",STRNO)
            NewAssetLocRelation.setValue("X3POSNR",POSNR)
            AssetLocRelationSet.save()
            
        SourceSet.close()
        i=i+1
    AssetSet.close()
    AssetLocRelationSet.close()
    X3SAPAssetSet.close()
    GenAssetSet.close()
    log("FIN","Carga PMTSTLIL Nivel 2 Campatas (no duplicadas)",i,"","Fin Carga PMTSTLIL Nivel 2 Campatas (no duplicadas)")

  
if param == 'PMTSTLIS':
    i=0
    log("INICIO","Carga PMTSTLIS Nivel 2 Sostegnos (no duplicados)","","","Inicio Carga PMTSTLIS Nivel 2 Sostegnos (no duplicados)")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1036
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIS' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.DUPLICATE = '0'
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        STTXU=GenAssetSet.getString("STTXU")
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
		
        AssetSet=mbo.getMboSet("ASSET")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",STTXU,11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR)
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        assnum=NewAsset.getString("ASSETNUM")
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        NewAsset.setValue("CLASSSTRUCTUREID",'1036')
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
		
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSET")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        STRNOTRUNC=STRNO[:7]
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'SOSTEGNO')
        NewX3SAPAsset.setValue("SAPLEVEL",'3')
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        
        
        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND X3SAPIDETL = '"""+ str(STRNOTRUNC) +"""'
        """

        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())

        while SourceSet.next():
            SourceAssetnum=SourceSet.getString("ASSETNUM")
            AssetLocRelationSet=mbo.getMboSet("ASSETLOCRELATION")
            NewAssetLocRelation = AssetLocRelationSet.add()
            NewAssetLocRelation.setValue("SITEID",'SIT01')
            NewAssetLocRelation.setValue("ORGID",'ORG01')
            NewAssetLocRelation.setValue("ASSETRELATIONNUM",'INCLUDES')
            NewAssetLocRelation.setValue("CREATEDDATE",fecha)
            NewAssetLocRelation.setValue("ISLINEARREF",'0')
            NewAssetLocRelation.setValue("LINEARASSETLOCRELATIONID",'0')
            NewAssetLocRelation.setValue("SOURCEASSETNUM",str(SourceAssetnum),11L)
            NewAssetLocRelation.setValue("TARGETASSETNUM",assnum,11L)
            NewAssetLocRelation.setValue("LOADED",'1')
            NewAssetLocRelation.setValue("X3SAPID",STRNO)
            AssetLocRelationSet.save()
            AssetLocRelationSet.close()   
        SourceSet.close()

        i=i+1

    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIS Nivel 2 Sostegnos (no duplicados)",i,"","Fin Carga PMTSTLIS Nivel 2 Sostegnos (no duplicados)")



if param == 'PMTSTLIZ':
    i=0
    log("INICIO","Carga PMTSTLIZ Nivel 2 Tratta in cavo","","","Inicio Carga PMTSTLIZ Nivel 2 Tratta in cavo")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1050
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIZ' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        STTXU=GenAssetSet.getString("STTXU")
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        POSNR=GenAssetSet.getString("POSNR")
		
        AssetSet=mbo.getMboSet("ASSETFORINSERT")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",STTXU,11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR)
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        assnum=NewAsset.getString("ASSETNUM")
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        NewAsset.setValue("CLASSSTRUCTUREID",'1050')
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
		
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'TRATTA CAVO')
        NewX3SAPAsset.setValue("SAPLEVEL",'2')

        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        
        
        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND X3SAPIDETL = '"""+ str(TPLMA) +"""'
        """

        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())

        while SourceSet.next():
            AssetLocRelationSet=mbo.getMboSet("ASSETLOCRELATION")
            NewAssetLocRelation = AssetLocRelationSet.add()
            SourceAssetnum=SourceSet.getString("ASSETNUM")
            NewAssetLocRelation.setValue("SITEID",'SIT01')
            NewAssetLocRelation.setValue("ORGID",'ORG01')
            NewAssetLocRelation.setValue("ASSETRELATIONNUM",'INCLUDES')
            NewAssetLocRelation.setValue("CREATEDDATE",fecha)
            NewAssetLocRelation.setValue("ISLINEARREF",'0')
            NewAssetLocRelation.setValue("LINEARASSETLOCRELATIONID",'0')
            NewAssetLocRelation.setValue("SOURCEASSETNUM",str(SourceAssetnum),11L)
            NewAssetLocRelation.setValue("TARGETASSETNUM",assnum,11L)
            NewAssetLocRelation.setValue("LOADED",'1')
            NewAssetLocRelation.setValue("X3SAPID",STRNO)
            NewAssetLocRelation.setValue("X3POSNR",POSNR)
            AssetLocRelationSet.save()
            AssetLocRelationSet.close()   
        SourceSet.close()

        i=i+1

    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIZ Nivel 2 Tratta in cavo",i,"","Fin Carga PMTSTLIZ Nivel 2 Tratta in cavo")


if param == 'PMTSTLIC':
    i=0
    log("INICIO","Carga PMTSTLIC nivel 3 Conduttori","","","Inicio Carga PMTSTLIC nivel 3 Conduttori")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1005
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIC' 
    AND x3sst.FLTYP = 'L'
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
		
        AssetSet=mbo.getMboSet("ASSETFORINSERT")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",'DATT',11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR) 
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        STRNOTRUNC1=STRNO[:7]
        STRNOTRUNC2=STRNO[:16]
        NewAsset.setValue("CLASSSTRUCTUREID",'1005')
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        
        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND X3SAPIDETL = '"""+ str(STRNOTRUNC2) +"""'
        """

        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())

        while SourceSet.next():
            PARENT=SourceSet.getString("ASSETNUM")
            NewAsset.setValue("PARENT",PARENT) 
        SourceSet.close()        

        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
		
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC1)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'CONDUTTORE')
        NewX3SAPAsset.setValue("SAPLEVEL",'3')
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()

        i=i+1

    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIC nivel 3 Conduttori",i,"","Fin Carga PMTSTLIC nivel 3 Conduttori")


if param == 'PMTSTLIG':
    i=0
    log("INICIO","Carga PMTSTLIG nivel 3 Funi Guardia","","","Inicio Carga PMTSTLIG nivel 3 Funi Guardia")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1016
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIG' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.DUPLICATE = '0'
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
		
        AssetSet=mbo.getMboSet("ASSETFORINSERT")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",'DATT',11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR) 
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        NewAsset.setValue("CLASSSTRUCTUREID",'1016')
        assnum=NewAsset.getString("ASSETNUM")
        STRNOTRUNC1=STRNO[:7]
        STRNOTRUNC2=STRNO[:16]

        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND X3SAPIDETL = '"""+ str(STRNOTRUNC2) +"""'
        """

        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())

        while SourceSet.next():
            PARENT=SourceSet.getString("ASSETNUM")
            NewAsset.setValue("PARENT",PARENT)   
        SourceSet.close()

        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
		
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC1)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'CORDA DI GUARDIA')
        NewX3SAPAsset.setValue("SAPLEVEL",'3')

        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        
        i=i+1

    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIG nivel 3 Funi Guardia",i,"","Fin Carga PMTSTLIG nivel 3 Funi Guardia")


if param == 'PMTSTLIV':
    i=0
    log("INICIO","Carga PMTSTLIV nivel 3 Cavi","","","Inicio Carga PMTSTLIV nivel 3 Cavi")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1051
            AND asst.LOADED = 1
            AND asst.SITEID ='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIV' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
		
        AssetSet=mbo.getMboSet("ASSET")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",'DATT',11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR) 
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset.setValue("CLASSSTRUCTUREID",'1051')
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        
        sqlParent = """
        SELECT
            *
        FROM ASSET
        WHERE X3SAPIDETL = '"""+ str(TPLMA) +"""'
        """
        sqfQueryParent=SqlFormat(sqlParent)
        ASSParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())
        
        while ASSParentSet.next():
            PARENT=ASSParentSet.getString("ASSETNUM")
            NewAsset.setValue("PARENT",PARENT)
        ASSParentSet.close()
        
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
		
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSET")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("SAPCLASS",'CAVO')
        NewX3SAPAsset.setValue("SAPLEVEL",'3')
        STRNOTRUNC=STRNO[:7]
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()

        i=i+1

    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIV nivel 3 Cavi",i,"","Fin Carga PMTSTLIV nivel 3 Cavi")


if param == 'PMTSTLICG':
    i=0
    log("INICIO","Carga PMTSTLICG nivel 3 Cammera Giuntti","","","Inicio Carga PMTSTLICG nivel 3 Cammera Giuntti")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1027
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLICG' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
		
        AssetSet=mbo.getMboSet("ASSET")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",'DATT',11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP, 11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR) 
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset.setValue("CLASSSTRUCTUREID",'1027')
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        STRNOTRUNC7=STRNO[:7]
        STRNOTRUNC17=STRNO[:17]
        
        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND CLASSSTRUCTUREID = '1026'
        AND X3SAPIDETL = '"""+ str(STRNO) +"""'
        """

        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())

        while SourceSet.next():
            PARENT=SourceSet.getString("ASSETNUM")
            NewAsset.setValue("PARENT",PARENT)
        
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
		
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSET")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC7)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'CAMERA GIUNTI')
        NewX3SAPAsset.setValue("SAPLEVEL",'3')
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT)
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()

        i=i+1

    GenAssetSet.close()    
    log("FIN","Carga PMTSTLICG nivel 3 Cammera Giuntti",i,"","Fin Carga PMTSTLICG nivel 3 Cammera Giuntti")


if param == 'PMTSTLIT':
    i=0
    log("INICIO","Carga PMTSTLIT nivel 3 Terminali","","","Inicio Carga PMTSTLIT nivel 3 Terminali")
	
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.BUTXT,
        x3sst.CITY,
        x3sst.COUNTRY,
        x3sst.CREATDATE,
        x3sst.DESCRIPTION,
        x3sst.DUPLICATE,
        x3sst.EQFNR,
        x3sst.FLTYP,
        x3sst.GEWRK,
        x3sst.HASLD,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.INNAM,
        x3sst.IWERK,
        x3sst.IWERKNAME,
        x3sst.KOKRS,
        x3sst.KOSTL,
        x3sst.KTEXT,
        x3sst.LIVELLOSEDETECNICA,
        x3sst.LOADED,
        x3sst.MSGRP,
        x3sst.NAME1,
        x3sst.NAME2,
        x3sst.OBJNR,
        x3sst.PLTXT,
        x3sst.POSNR,
        x3sst.POST_CODE1,
        x3sst.RBNR,
        x3sst.RBNRX,
        x3sst.REGION,
        x3sst.STREET,
        x3sst.STRNO,
        x3sst.STTXT,
        x3sst.STTXU,
        x3sst.SUBMT,
        x3sst.SWERK,
        x3sst.TPLKZ,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.TXT_GEWRK,
        x3sst.TXT_TPLKZ,
        x3sst.TYPTX,
        x3sst.WERGW,
        x3sst.X3SEDITECHSAPID,
        x3sst.ZZITO_ORD41,
        x3sst.ZZITO_UMWKZ,
        x3sst.ZZSOCP,
        x3sst.ROWSTAMP,
        --x3sst.INPUTFILE,
        x3sst.X3FICT
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1032
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLIT' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.X3FICT IS NULL
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
	
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        
        AssetSet=mbo.getMboSet("ASSETFORINSERT")
        NewAsset = AssetSet.add()
        NewAsset.setValue("STATUS",'DATT',11L)		
        NewAsset.setValue("INSTALLDATE",INBDT)
        NewAsset.setValue("X3SWERK",SWERK,11L)
        NewAsset.setValue("X3INGRP",INGRP,11L)
        NewAsset.setValue("X3ZZSOCP",ZZSOCP,11L)
        NewAsset.setValue("DESCRIPTION",PLTXT)
        NewAsset.setValue("X3EQFNR",EQFNR) 
        NewAsset.setValue("X3BUKRS",BUKRS)
        NewAsset.setValue("X3KOKRS",KOKRS)
        NewAsset.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset.setValue("LOCATION",'SIT01')
        NewAsset.setValue("LOADED",'1')
        #NewAsset.setValue("CHANGEBY",) Which user should appear here?
        NewAsset.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset.setValue("CLASSSTRUCTUREID",'1032')
        NewAsset.setValue("X3SAPID",STRNO)
        NewAsset.setValue("X3SAPIDETL",STRNO)
        STRNOTRUNC7=STRNO[:7]
        STRNOTRUNC17=STRNO[:17]
        
        sqlSource = """
        SELECT
            *
        FROM ASSET
        WHERE LOADED = 1
        AND X3SAPIDETL = '"""+ str(STRNO) +"""'
        AND CLASSSTRUCTUREID = '1026'
        """
        
        sqfQuerySource=SqlFormat(sqlSource)
        SourceSet=dbShortcutSource.executeQuery(sqfQuerySource.format())
        
        while SourceSet.next():
            PARENT=SourceSet.getString("ASSETNUM")
            NewAsset.setValue("PARENT",PARENT)
        SourceSet.close()
        
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close()
        
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR)
        NewX3SAPAsset.setValue("STRNO",STRNO)
        NewX3SAPAsset.setValue("SAPOBJECT",'S')
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
        NewX3SAPAsset.setValue("SYNCHACTION",'')
        NewX3SAPAsset.setValue("MAPPINGERROR",'0')
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC7)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
        NewX3SAPAsset.setValue("SAPCLASS",'TERMINALE')
        NewX3SAPAsset.setValue("SAPLEVEL",'3')
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """
        
        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())
        
        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT)
        SapParentSet.close()
        
        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        
        i=i+1
        
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLIT nivel 3 Terminali",i,"","Fin Carga PMTSTLIT nivel 3 Terminali")
 
 
if param == 'PMTSTLII':
    i=0
    log("INICIO","Carga PMTSTLII nivel 4 Isolatore Conduttore SINISTRO","","","Inicio Carga PMTSTLII nivel 4 Isolatore Conduttore SINISTRO")
    
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        asstchild.ASSETNUM
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asstpar
    	ON (SUBSTR(x3sst.STRNO,1,16)||'-S' = asstpar.X3SAPID
    		AND asstpar.CLASSSTRUCTUREID = '1036'
    		AND asstpar.LOADED = 1
    		AND asstpar.SITEID='SIT01')
    LEFT JOIN ASSET asstchild
      	ON (asstpar.ASSETNUM = asstchild.PARENT
       		AND asstchild.LOADED = 1
       		AND asstchild.CLASSSTRUCTUREID = '1037'
       		AND asstchild.SITEID='SIT01')    
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1039
            AND asst.DESCRIPTION = 'ISOLATORE CONDUTTORE SINISTRO'
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLII' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    AND x3sst.X3ELENCOEX = 0
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        PARENT=GenAssetSet.getString("ASSETNUM")

        AssetSet=mbo.getMboSet("ASSETFORINSERT")
        NewAsset1 = AssetSet.add()
        NewAsset1.setValue("STATUS",'ESER',11L)		
        NewAsset1.setValue("INSTALLDATE",INBDT)
        NewAsset1.setValue("X3SWERK",SWERK,11L)
        NewAsset1.setValue("X3INGRP",INGRP, 11L)
        NewAsset1.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset1.setValue("X3EQFNR",EQFNR) 
        NewAsset1.setValue("X3BUKRS",BUKRS)
        NewAsset1.setValue("X3KOKRS",KOKRS)
        NewAsset1.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset1.setValue("LOCATION",'SIT01')
        NewAsset1.setValue("LOADED",'1')
        #NewAsset1.setValue("CHANGEBY",) Which user should appear here?
        NewAsset1.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset1.setValue("X3SAPID",STRNO)
        NewAsset1.setValue("X3SAPIDETL",STRNO)
        NewAsset1.setValue("PARENT",PARENT)
        NewAsset1.setValue("CLASSSTRUCTUREID",'1039')
        NewAsset1.setValue("DESCRIPTION",'ISOLATORE CONDUTTORE SINISTRO')
        integrationService.setScenario(2)
        AssetSet.save()
        AssetSet.close() 
        
        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLII nivel 4 Isolatore Conduttore SINISTRO",i,"","Fin Carga PMTSTLII nivel 4 Isolatore Conduttore SINISTRO")

   
    i=0
    log("INICIO","Carga PMTSTLII nivel 4 Isolatore Conduttore DESTRO","","","Inicio Carga PMTSTLII nivel 4 Isolatore Conduttore DESTRO")
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        asstchild.ASSETNUM
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asstpar
    	ON (SUBSTR(x3sst.STRNO,1,16)||'-S' = asstpar.X3SAPIDETL
    		AND asstpar.CLASSSTRUCTUREID = '1036'
    		AND asstpar.LOADED = 1
    		AND asstpar.SITEID='SIT01')
    LEFT JOIN ASSET asstchild
      	ON (asstpar.ASSETNUM = asstchild.PARENT
       		AND asstchild.LOADED = 1
       		AND asstchild.CLASSSTRUCTUREID = '1037'
       		AND asstchild.SITEID='SIT01')    
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1039
            AND asst.DESCRIPTION = 'ISOLATORE CONDUTTORE DESTRO'
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLII' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    AND x3sst.X3ELENCOEX = 0
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        PARENT=GenAssetSet.getString("ASSETNUM")

        AssetSet1=mbo.getMboSet("ASSETFORINSERT")
        NewAsset2 = AssetSet1.add()
        NewAsset2.setValue("STATUS",'ESER',11L)		
        NewAsset2.setValue("INSTALLDATE",INBDT)
        NewAsset2.setValue("X3SWERK",SWERK,11L)
        NewAsset2.setValue("X3INGRP",INGRP, 11L)
        NewAsset2.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset2.setValue("X3EQFNR",EQFNR) 
        NewAsset2.setValue("X3BUKRS",BUKRS)
        NewAsset2.setValue("X3KOKRS",KOKRS)
        NewAsset2.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset2.setValue("LOCATION",'SIT01')
        NewAsset2.setValue("LOADED",'1')
        #NewAsset2.setValue("CHANGEBY",) Which user should appear here?
        NewAsset2.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset2.setValue("X3SAPID",STRNO)
        NewAsset2.setValue("X3SAPIDETL",STRNO)
        NewAsset2.setValue("PARENT",PARENT)
        NewAsset2.setValue("CLASSSTRUCTUREID",'1039')
        NewAsset2.setValue("DESCRIPTION",'ISOLATORE CONDUTTORE DESTRO')
        integrationService.setScenario(2)
        AssetSet1.save()
        AssetSet1.close()  

        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLII nivel 4 Isolatore Conduttore DESTRO",i,"","Fin Carga PMTSTLII nivel 4 Isolatore Conduttore DESTRO")
    
    i=0
    log("INICIO","Carga PMTSTLII nivel 4 Isolatore Conduttore TERZO","","","Inicio Carga PMTSTLII nivel 4 Isolatore Conduttore TERZO")
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        asstchild.ASSETNUM
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asstpar
    	ON (SUBSTR(x3sst.STRNO,1,16)||'-S' = asstpar.X3SAPIDETL
    		AND asstpar.CLASSSTRUCTUREID = '1036'
    		AND asstpar.LOADED = 1
    		AND asstpar.SITEID='SIT01')
    LEFT JOIN ASSET asstchild
      	ON (asstpar.ASSETNUM = asstchild.PARENT
       		AND asstchild.LOADED = 1
       		AND asstchild.CLASSSTRUCTUREID = '1037'
       		AND asstchild.SITEID='SIT01')    
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1039
            AND asst.DESCRIPTION = 'ISOLATORE CONDUTTORE TERZO'
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLII' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    AND x3sst.X3ELENCOEX = 0
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        PARENT=GenAssetSet.getString("ASSETNUM")
        STRNOTRUNCLIN=STRNO[:7]
        
        AssetSet2=mbo.getMboSet("ASSETFORINSERT")
        NewAsset3 = AssetSet2.add()
        NewAsset3.setValue("STATUS",'ESER',11L)		
        NewAsset3.setValue("INSTALLDATE",INBDT)
        NewAsset3.setValue("X3SWERK",SWERK,11L)
        NewAsset3.setValue("X3INGRP",INGRP, 11L)
        NewAsset3.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset3.setValue("X3EQFNR",EQFNR) 
        NewAsset3.setValue("X3BUKRS",BUKRS)
        NewAsset3.setValue("X3KOKRS",KOKRS)
        NewAsset3.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset3.setValue("LOCATION",'SIT01')
        NewAsset3.setValue("LOADED",'1')
        #NewAsset3.setValue("CHANGEBY",) Which user should appear here?
        NewAsset3.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset3.setValue("X3SAPID",STRNO)
        NewAsset3.setValue("X3SAPIDETL",STRNO)
        NewAsset3.setValue("PARENT",PARENT)
        NewAsset3.setValue("CLASSSTRUCTUREID",'1039')
        NewAsset3.setValue("DESCRIPTION",'ISOLATORE CONDUTTORE TERZO')
        integrationService.setScenario(2)
        AssetSet2.save()
        AssetSet2.close()        
        
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR,11L)
        NewX3SAPAsset.setValue("STRNO",STRNO,11L)
        NewX3SAPAsset.setValue("SAPOBJECT",'S',11L)
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L',11L)
        NewX3SAPAsset.setValue("SYNCHACTION",'',11L)
        NewX3SAPAsset.setValue("MAPPINGERROR",'0',11L)
        NewX3SAPAsset.setValue("SAPCLASS",'ISOLATORE',11L)
        NewX3SAPAsset.setValue("SAPLEVEL",'3',11L)
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNCLIN,11L)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA,11L)
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT,11L)
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLII nivel 4 Isolatore Conduttore TERZO",i,"","Fin Carga PMTSTLII nivel 4 Isolatore Conduttore TERZO")

    i=0
    log("INICIO","Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 1","","","Inicio Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 1")
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        asstchild.ASSETNUM
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asstpar
    	ON (SUBSTR(x3sst.STRNO,1,16)||'-S' = asstpar.X3SAPIDETL
    		AND asstpar.CLASSSTRUCTUREID = '1036'
    		AND asstpar.LOADED = 1
    		AND asstpar.SITEID='SIT01')
    LEFT JOIN ASSET asstchild
      	ON (asstpar.ASSETNUM = asstchild.PARENT
       		AND asstchild.LOADED = 1
       		AND asstchild.CLASSSTRUCTUREID = '1037'
       		AND asstchild.SITEID='SIT01')    
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1039
            AND asst.DESCRIPTION = 'ISOLATORE CONDUTTORE 1'
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLII' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    AND x3sst.X3ELENCOEX = 2
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        PARENT=GenAssetSet.getString("ASSETNUM")

        AssetSet1=mbo.getMboSet("ASSETFORINSERT")
        NewAsset2 = AssetSet1.add()
        NewAsset2.setValue("STATUS",'ESER',11L)		
        NewAsset2.setValue("INSTALLDATE",INBDT)
        NewAsset2.setValue("X3SWERK",SWERK,11L)
        NewAsset2.setValue("X3INGRP",INGRP, 11L)
        NewAsset2.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset2.setValue("X3EQFNR",EQFNR) 
        NewAsset2.setValue("X3BUKRS",BUKRS)
        NewAsset2.setValue("X3KOKRS",KOKRS)
        NewAsset2.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset2.setValue("LOCATION",'SIT01')
        NewAsset2.setValue("LOADED",'1')
        #NewAsset2.setValue("CHANGEBY",) Which user should appear here?
        NewAsset2.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset2.setValue("X3SAPID",STRNO)
        NewAsset2.setValue("X3SAPIDETL",STRNO)
        NewAsset2.setValue("PARENT",PARENT)
        NewAsset2.setValue("CLASSSTRUCTUREID",'1039')
        NewAsset2.setValue("DESCRIPTION",'ISOLATORE CONDUTTORE 1')
        integrationService.setScenario(2)
        AssetSet1.save()
        AssetSet1.close()  

        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 1",i,"","Fin Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 1")

    i=0
    log("INICIO","Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 2","","","Inicio Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 2")
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        asstchild.ASSETNUM
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asstpar
    	ON (SUBSTR(x3sst.STRNO,1,16)||'-S' = asstpar.X3SAPIDETL
    		AND asstpar.CLASSSTRUCTUREID = '1036'
    		AND asstpar.LOADED = 1
    		AND asstpar.SITEID='SIT01')
    LEFT JOIN ASSET asstchild
      	ON (asstpar.ASSETNUM = asstchild.PARENT
       		AND asstchild.LOADED = 1
       		AND asstchild.CLASSSTRUCTUREID = '1037'
       		AND asstchild.SITEID='SIT01')    
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1039
            AND asst.DESCRIPTION = 'ISOLATORE CONDUTTORE 2'
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLII' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    AND x3sst.X3ELENCOEX = 2
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        PARENT=GenAssetSet.getString("ASSETNUM")
        STRNOTRUNCLIN=STRNO[:7]
        
        AssetSet2=mbo.getMboSet("ASSETFORINSERT")
        NewAsset3 = AssetSet2.add()
        NewAsset3.setValue("STATUS",'ESER',11L)		
        NewAsset3.setValue("INSTALLDATE",INBDT)
        NewAsset3.setValue("X3SWERK",SWERK,11L)
        NewAsset3.setValue("X3INGRP",INGRP, 11L)
        NewAsset3.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset3.setValue("X3EQFNR",EQFNR) 
        NewAsset3.setValue("X3BUKRS",BUKRS)
        NewAsset3.setValue("X3KOKRS",KOKRS)
        NewAsset3.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset3.setValue("LOCATION",'SIT01')
        NewAsset3.setValue("LOADED",'1')
        #NewAsset3.setValue("CHANGEBY",) Which user should appear here?
        NewAsset3.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset3.setValue("X3SAPID",STRNO)
        NewAsset3.setValue("X3SAPIDETL",STRNO)
        NewAsset3.setValue("PARENT",PARENT)
        NewAsset3.setValue("CLASSSTRUCTUREID",'1039')
        NewAsset3.setValue("DESCRIPTION",'ISOLATORE CONDUTTORE 2')
        integrationService.setScenario(2)
        AssetSet2.save()
        AssetSet2.close()        
        
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR,11L)
        NewX3SAPAsset.setValue("STRNO",STRNO,11L)
        NewX3SAPAsset.setValue("SAPOBJECT",'S',11L)
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L',11L)
        NewX3SAPAsset.setValue("SYNCHACTION",'',11L)
        NewX3SAPAsset.setValue("MAPPINGERROR",'0',11L)
        NewX3SAPAsset.setValue("SAPCLASS",'ISOLATORE',11L)
        NewX3SAPAsset.setValue("SAPLEVEL",'3',11L)
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNCLIN,11L)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA,11L)
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT,11L)
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 2",i,"","Fin Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 2 2")

    i=0
    log("INICIO","Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 1","","","Inicio Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 1")
    sql = """
    SELECT
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.PLTXT,
        x3sst.STRNO,
        x3sst.STTXU,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        asstchild.ASSETNUM
    FROM X3SEDITECHSAP x3sst
    LEFT JOIN ASSET asstpar
    	ON (SUBSTR(x3sst.STRNO,1,16)||'-S' = asstpar.X3SAPIDETL
    		AND asstpar.CLASSSTRUCTUREID = '1036'
    		AND asstpar.LOADED = 1
    		AND asstpar.SITEID='SIT01')
    LEFT JOIN ASSET asstchild
      	ON (asstpar.ASSETNUM = asstchild.PARENT
       		AND asstchild.LOADED = 1
       		AND asstchild.CLASSSTRUCTUREID = '1037'
       		AND asstchild.SITEID='SIT01')    
    LEFT JOIN ASSET asst
        ON (asst.X3SAPIDETL = x3sst.STRNO
            AND asst.CLASSSTRUCTUREID = 1039
            AND asst.DESCRIPTION = 'ISOLATORE CONDUTTORE'
            AND asst.LOADED = 1
            AND asst.SITEID='SIT01')
    WHERE x3sst.RBNR = 'PMTSTLII' 
    AND x3sst.FLTYP = 'L' 
    AND x3sst.LOADED = 1
    AND asst.ASSETNUM IS NULL
    AND x3sst.X3ELENCOEX = 1
    """    
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        INBDT=GenAssetSet.getString("INBDT")		
        SWERK=GenAssetSet.getString("SWERK")
        INGRP=GenAssetSet.getString("INGRP")
        ZZSOCP=GenAssetSet.getString("ZZSOCP")
        PLTXT=GenAssetSet.getString("PLTXT")		
        EQFNR=GenAssetSet.getString("EQFNR")
        BUKRS=GenAssetSet.getString("BUKRS")
        KOKRS=GenAssetSet.getString("KOKRS")
        GEWRK=GenAssetSet.getString("GEWRK")	
        TPLNR=GenAssetSet.getString("TPLNR")	
        STRNO=GenAssetSet.getString("STRNO")
        TPLMA=GenAssetSet.getString("TPLMA")
        PARENT=GenAssetSet.getString("ASSETNUM")
        STRNOTRUNCLIN=STRNO[:7]
        
        AssetSet2=mbo.getMboSet("ASSETFORINSERT")
        NewAsset3 = AssetSet2.add()
        NewAsset3.setValue("STATUS",'ESER',11L)		
        NewAsset3.setValue("INSTALLDATE",INBDT)
        NewAsset3.setValue("X3SWERK",SWERK,11L)
        NewAsset3.setValue("X3INGRP",INGRP, 11L)
        NewAsset3.setValue("X3ZZSOCP",ZZSOCP)
        NewAsset3.setValue("X3EQFNR",EQFNR) 
        NewAsset3.setValue("X3BUKRS",BUKRS)
        NewAsset3.setValue("X3KOKRS",KOKRS)
        NewAsset3.setValue("X3GEWRK",GEWRK,11L)
        #NewAsset3.setValue("LOCATION",'SIT01')
        NewAsset3.setValue("LOADED",'1')
        #NewAsset3.setValue("CHANGEBY",) Which user should appear here?
        NewAsset3.setValue("EQ1",mbo.getString("X3ETLPROCID"))
        NewAsset3.setValue("X3SAPID",STRNO)
        NewAsset3.setValue("X3SAPIDETL",STRNO)
        NewAsset3.setValue("PARENT",PARENT)
        NewAsset3.setValue("CLASSSTRUCTUREID",'1039')
        NewAsset3.setValue("DESCRIPTION",'ISOLATORE CONDUTTORE')
        integrationService.setScenario(2)
        AssetSet2.save()
        AssetSet2.close()        
        
        X3SAPAssetSet=mbo.getMboSet("X3SAPASSETFORINSERT")
        NewX3SAPAsset = X3SAPAssetSet.add()
        NewX3SAPAsset.setValue("SAPUID",TPLNR,11L)
        NewX3SAPAsset.setValue("STRNO",STRNO,11L)
        NewX3SAPAsset.setValue("SAPOBJECT",'S',11L)
        NewX3SAPAsset.setValue("ROOTASSETTYPE",'L',11L)
        NewX3SAPAsset.setValue("SYNCHACTION",'',11L)
        NewX3SAPAsset.setValue("MAPPINGERROR",'0',11L)
        NewX3SAPAsset.setValue("SAPCLASS",'ISOLATORE',11L)
        NewX3SAPAsset.setValue("SAPLEVEL",'3',11L)
        NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNCLIN,11L)
        NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA,11L)
        
        sqlParent = """
        SELECT
            *
        FROM X3SAPASSET
        WHERE STRNO = '"""+ str(TPLMA) +"""'
        """

        sqfQueryParent=SqlFormat(sqlParent)
        SapParentSet=dbShortcutParent.executeQuery(sqfQueryParent.format())

        while SapParentSet.next():
            TPLNRPARENT=SapParentSet.getString("SAPUID")
            NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT,11L)
        SapParentSet.close()

        X3SAPAssetSet.save()
        X3SAPAssetSet.close()
        i=i+1
    
    GenAssetSet.close()    
    log("FIN","Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 1",i,"","Fin Carga PMTSTLII nivel 4 Isolatore Conduttore ELENCOEX 1")

dbShortcut.close()
dbShortcutParent.close()
dbShortcutSource.close()