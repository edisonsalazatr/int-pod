#X3ETLL4AT
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from com.terna.integration.service import IntegrationService
integrationService = IntegrationService.getService()
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

if 1==1: 
    i=0
    log("INICIO","Carga Nivel 4 - 1022 FUNE DI GUARDIA DESTRA","","","Inicio Carga Nivel 4 - 1022 FUNE DI GUARDIA DESTRA")
    AssetSet=mbo.getMboSet("ASSETFORINSERT")
    X3CaratSapSet=mbo.getMboSet("$X3CSP2$","X3CARATSAP","LOADED=1 AND BOO_TYP = 0 AND ATNAM='PMT_TIPO_CDG_DX' AND CHAR_TYP IS NOT NULL AND OBJECT_ID IN ('21322B1-001------G','21322B1-002------G','21322B1-003------G','21322B1-004------G') AND KLASSE = 'PMT_CDG'")
    X3CaratSapSet.reset()
    c=X3CaratSapSet.count()
    for i in range(c):
        caratsa = X3CaratSapSet.getMbo(i)
        if caratsa is not None:
            STRNO=caratsa.getString("OBJECT_ID")
            SourceAssetSet=mbo.getMboSet("$ASS2$","ASSET","X3SAPIDETL='"+ str(STRNO) + "' AND CLASSSTRUCTUREID = '1016' AND SITEID='SIT01'")
            SourceAssetSet.reset()
            AssParentMbo=SourceAssetSet.getMbo(0)
            if AssParentMbo is not None:
                assnum=AssParentMbo.getString("ASSETNUM")
                NewAsset1 = AssetSet.add()
                NewAsset1.setValue("STATUS","ESER",11L)
                NewAsset1.setValue("CLASSSTRUCTUREID","1022")
                NewAsset1.setValue("PARENT",assnum)
                NewAsset1.setValue("LOADED","1")
                NewAsset1.setValue("DESCRIPTION","FUNE DI GUARDIA DESTRA")
                NewAsset1.setValue("EQ1",mbo.getString("X3ETLPROCID"))
                NewAsset1.setValue("X3SAPID",STRNO)
                NewAsset1.setValue("X3SAPIDETL",STRNO)
                integrationService.setScenario(2)
                AssetSet.save()
                i=i+1
            SourceAssetSet.close()
    X3CaratSapSet.close() 
    AssetSet.close()
    log("FIN","Carga Nivel 4 - 1022 FUNE DI GUARDIA DESTRA",i,"","Fin Carga Nivel 4 - 1022 FUNE DI GUARDIA DESTRA")

if 1==1: 
    i=0
    log("INICIO","Carga Nivel 4 - 1022 FUNE DI GUARDIA SINISTRA","","","Inicio Carga Nivel 4 - 1022 FUNE DI GUARDIA SINISTRA")
    AssetSet=mbo.getMboSet("ASSETFORINSERT")
    X3CaratSapSet=mbo.getMboSet("$X3CSP2$","X3CARATSAP","LOADED=1 AND BOO_TYP = 0 AND ATNAM='PMT_TIPO_CDG_SX' AND CHAR_TYP IS NOT NULL AND KLASSE = 'PMT_CDG' AND OBJECT_ID IN ('21322B1-001------G','21322B1-002------G','21322B1-003------G','21322B1-004------G')")
    X3CaratSapSet.reset()
    c=X3CaratSapSet.count()
    for i in range(c):
        caratsa = X3CaratSapSet.getMbo(i)
        if caratsa is not None:
            STRNO=caratsa.getString("OBJECT_ID")
            SourceAssetSet=mbo.getMboSet("$ASS2$","ASSET","X3SAPIDETL='"+ str(STRNO) + "' AND CLASSSTRUCTUREID = '1016' AND SITEID='SIT01'")
            SourceAssetSet.reset()
            AssParentMbo=SourceAssetSet.getMbo(0)
            if AssParentMbo is not None:
                assnum=AssParentMbo.getString("ASSETNUM")
                NewAsset1 = AssetSet.add()
                NewAsset1.setValue("STATUS","ESER",11L)
                NewAsset1.setValue("CLASSSTRUCTUREID","1022")
                NewAsset1.setValue("PARENT",assnum)
                NewAsset1.setValue("LOADED","1")
                NewAsset1.setValue("DESCRIPTION","FUNE DI GUARDIA SINISTRA")
                NewAsset1.setValue("EQ1",mbo.getString("X3ETLPROCID"))
                NewAsset1.setValue("X3SAPID",STRNO)
                NewAsset1.setValue("X3SAPIDETL",STRNO)
                integrationService.setScenario(2)
                AssetSet.save()
                i=i+1
            SourceAssetSet.close()
    X3CaratSapSet.close() 
    AssetSet.close()
    log("FIN","Carga Nivel 4 - 1022 FUNE DI GUARDIA SINISTRA",i,"","Fin Carga Nivel 4 - 1022 FUNE DI GUARDIA SINISTRA")

if 1==1:
    i=0
    log("INICIO","Carga Nivel 4 - 1022 FUNE DI GUARDIA TERZA","","","Inicio Carga Nivel 4 - 1022 FUNE DI GUARDIA TERZA")
    AssetSet=mbo.getMboSet("ASSETFORINSERT")
    X3CaratSapSet=mbo.getMboSet("$X3CSP3$","X3CARATSAP","LOADED=1 AND BOO_TYP = 0 AND ATNAM='PMT_TIPO_CDG_3' AND CHAR_TYP IS NOT NULL AND KLASSE = 'PMT_CDG' AND OBJECT_ID IN ('21322B1-001------G','21322B1-002------G','21322B1-003------G','21322B1-004------G')")
    X3CaratSapSet.reset()
    c=X3CaratSapSet.count()
    for i in range(c):
        caratsa = X3CaratSapSet.getMbo(i)
        if caratsa is not None:
            STRNO=caratsa.getString("OBJECT_ID")
            SourceAssetSet=mbo.getMboSet("$ASS3$","ASSET","X3SAPIDETL='"+ str(STRNO) + "' AND CLASSSTRUCTUREID = '1016' AND SITEID='SIT01'")
            SourceAssetSet.reset()
            AssParentMbo=SourceAssetSet.getMbo(0)
            if AssParentMbo is not None:
                assnum=AssParentMbo.getString("ASSETNUM")
                NewAsset1 = AssetSet.add()
                NewAsset1.setValue("STATUS","ESER",11L)
                NewAsset1.setValue("CLASSSTRUCTUREID","1022")
                NewAsset1.setValue("PARENT",assnum)
                NewAsset1.setValue("LOADED","1")
                NewAsset1.setValue("DESCRIPTION","FUNE DI GUARDIA TERZA")
                NewAsset1.setValue("EQ1",mbo.getString("X3ETLPROCID"))
                NewAsset1.setValue("X3SAPID",STRNO)
                NewAsset1.setValue("X3SAPIDETL",STRNO)
                integrationService.setScenario(2)
                AssetSet.save()
                i=i+1
            SourceAssetSet.close()
    X3CaratSapSet.close() 
    AssetSet.close()  
    log("FIN","Carga Nivel 4 - 1022 FUNE DI GUARDIA TERZA",i,"","Fin Carga Nivel 4 - 1022 FUNE DI GUARDIA TERZA")