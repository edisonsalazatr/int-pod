#X3ETLPROCRESTLEV4ATTEXTRAN
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from psdi.mbo import DBShortcut
from psdi.txn import MXTransaction
from com.terna.integration.service import IntegrationService
from psdi.util import MXException
from java.lang import Exception as JException
from java.sql import SQLException
import time

def log(nivel,descripcion,numrows,error,notas):
    logSet = MXServer.getMXServer().getMboSet("X3SCRIPTLOG", MXServer.getMXServer().getSystemUserInfo());
    NewLog = logSet.add()
    NewLog.setValue("DESCRIPTION",nivel+" - "+descripcion)
    NewLog.setValue("X3SCRIPT","X3ETLPROCRESTLEV4ATTEXTRAN")
    NewLog.setValue("TIMESTAMP",MXServer.getMXServer().getDate())
    NewLog.setValue("NUMROWS",numrows)
    NewLog.setValue("X3ERROR",error)
    NewLog.setValue("X3NOTES",notas)
    logSet.save()
    logSet.close()
    time.sleep(2)

param = request.getQueryParam("ASSETTYPE")
chunkid = request.getQueryParam("CHUNKID")
integrationService = IntegrationService.getService()

mxServer = MXServer.getMXServer()
fecha = mxServer.getDate()
dbShortcut=DBShortcut()
dbShortcut.connect(mxServer.getSystemUserInfo().getConnectionKey())

classstructureid = 0
assettype = 'NONE'
assetlevel = 0

i=0
totalOK=0

try:
    if param == 'ANTIROTAZIONALICONDUTTORE':
        classstructureid = 1006
        assettype = 'ANTIROTAZIONALI CONDUTTORE'
        assetlevel = 4
    elif param == "ANTIVIBRANTICONDUTTORE":
        classstructureid = 1007
        assettype = 'ANTIVIBRANTI CONDUTTORE'
        assetlevel = 4
    elif param == "BARRETTAPREFORMATACONDUTTORE":
        classstructureid = 1008
        assettype = 'BARRETTA PREFORMATA CONDUTTORE'
        assetlevel = 4
    elif param == "DISSUASORIAVIFAUNACONDUTTORE":
        classstructureid = 1010
        assettype = 'DISSUASORI AVIFAUNA CONDUTTORE'
        assetlevel = 4
    elif param == "DISTANZIATORI":
        classstructureid = 1011
        assettype = 'DISTANZIATORI'
        assetlevel = 4
    elif param == "DISTANZIATORIINTERFASE":
        classstructureid = 1012
        assettype = 'DISTANZIATORI INTERFASE'
        assetlevel = 4
    elif param == "FIBRAOTTICACONDUTTORE":
        classstructureid = 1013
        assettype = 'FIBRA OTTICA CONDUTTORE'
        assetlevel = 4
    elif param == "GIUNTOCONDUTTORE":
        classstructureid = 1014
        assettype = 'GIUNTO CONDUTTORE'
        assetlevel = 4
    elif param == "SFEREDISEGNALAZIONE":
        classstructureid = 1015
        assettype = 'SFERE DI SEGNALAZIONE CONDUTTORE'
        assetlevel = 4
    elif param == "ANTIROTAZIONALIFUNEDIGUARDIA":
        classstructureid = 1017
        assettype = 'ANTIROTAZIONALI FUNE DI GUARDIA'
        assetlevel = 4
    elif param == "ANTIVIBRANTIFUNEDIGUARDIA1":
        classstructureid = 1018
        assettype = 'ANTIVIBRANTI FUNE DI GUARDIA 1'
        assetlevel = 4
    elif param == "ANTIVIBRANTIFUNEDIGUARDIA2":
        classstructureid = 1018
        assettype = 'ANTIVIBRANTI FUNE DI GUARDIA 2'
        assetlevel = 4
    elif param == "ANTIVIBRANTIFUNEDIGUARDIA3":
        classstructureid = 1018
        assettype = 'ANTIVIBRANTI FUNE DI GUARDIA 3'
        assetlevel = 4
    elif param == "BARRETTAPREFORMATAFDG1":
        classstructureid = 1019
        assettype = 'BARRETTA PREFORMATA FDG 1'
        assetlevel = 4
    elif param == "BARRETTAPREFORMATAFDG2":
        classstructureid = 1019
        assettype = 'BARRETTA PREFORMATA FDG 2'
        assetlevel = 4
    elif param == "BARRETTAPREFORMATAFDG3":
        classstructureid = 1019
        assettype = 'BARRETTA PREFORMATA FDG 3'
        assetlevel = 4
    elif param == "DISSUASORIAVIFAUNAFUNEDIGUARDIA":
        classstructureid = 1020
        assettype = 'DISSUASORI AVIFAUNA FUNE DI GUARDIA'
        assetlevel = 4
    elif param == "FIBRAOTTICAFUNEDIGUARDIA1":
        classstructureid = 1021
        assettype = 'FIBRA OTTICA FUNE DI GUARDIA 1'
        assetlevel = 4
    elif param == "FIBRAOTTICAFUNEDIGUARDIA2":
        classstructureid = 1021
        assettype = 'FIBRA OTTICA FUNE DI GUARDIA 2'
        assetlevel = 4
    elif param == "FIBRAOTTICAFUNEDIGUARDIA3":
        classstructureid = 1021
        assettype = 'FIBRA OTTICA FUNE DI GUARDIA 3'
        assetlevel = 4
    elif param == "FUNEDIGUARDIA1":
        classstructureid = 1022
        assettype = 'FUNE DI GUARDIA 1'
        assetlevel = 4
    elif param == "FUNEDIGUARDIA2":
        classstructureid = 1022
        assettype = 'FUNE DI GUARDIA 2'
        assetlevel = 4
    elif param == "FUNEDIGUARDIA3":
        classstructureid = 1022
        assettype = 'FUNE DI GUARDIA 3'
        assetlevel = 4
    elif param == "GIUNTOFUNEDIGUARDIA":
        classstructureid = 1023
        assettype = 'GIUNTO FUNE DI GUARDIA'
        assetlevel = 4
    elif param == "SFEREDISEGNALAZIONEFUNEDIGUARDIA":
        classstructureid = 1024
        assettype = 'SFERE DI SEGNALAZIONE FUNE DI GUARDIA'
        assetlevel = 4
    elif param == "CAMERAISPEZIONABILE":
        classstructureid = 1028
        assettype = 'CAMERA ISPEZIONABILE'
        assetlevel = 4
    elif param == "CASSETTADISEZIONAMENTOGIUNTI":
        classstructureid = 1029
        assettype = 'CASSETTA DI SEZIONAMENTO GIUNTI'
        assetlevel = 4
    elif param == "MONITORAGGIOIOTDIGICGIUNTI":
        classstructureid = 1031
        assettype = 'MONITORAGGIO IOT (DIGIC) GIUNTI'
        assetlevel = 4
    elif param == "MONITORAGGIOIOTDIGICTERMINALI":
        classstructureid = 1034
        assettype = 'MONITORAGGIO IOT (DIGIC) TERMINALI'
        assetlevel = 4
    elif param == "ARMAMENTOFUNEDIGUARDIA1":
        classstructureid = 1041
        assettype = 'ARMAMENTO FUNE DI GUARDIA 1'
        assetlevel = 4
    elif param == "ARMAMENTOFUNEDIGUARDIA2":
        classstructureid = 1041
        assettype = 'ARMAMENTO FUNE DI GUARDIA 2'
        assetlevel = 4
    elif param == "ARMAMENTOFUNEDIGUARDIA3":
        classstructureid = 1041
        assettype = 'ARMAMENTO FUNE DI GUARDIA 3'
        assetlevel = 4
    elif param == "DISPOSITIVOTLC":
        classstructureid = 1042
        assettype = 'DISPOSITIVO TLC'
        assetlevel = 4
    elif param == "GIUNZIONEFIBRAOTTICA":
        classstructureid = 1043
        assettype = 'GIUNZIONE FIBRA OTTICA'
        assetlevel = 4
    elif param == "ISOLATOREFUNEDIGUARDIA":
        classstructureid = 1044
        assettype = 'ISOLATORE FUNE DI GUARDIA'
        assetlevel = 4
    elif param == "NIDOAVIFAUNA":
        classstructureid = 1046
        assettype = 'NIDO AVIFAUNA'
        assetlevel = 4
    elif param == "SEGNALAZIONELUMINOSAOSTACOLOVOLO":
        classstructureid = 1047
        assettype = 'SEGNALAZIONE LUMINOSA OSTACOLO VOLO'
        assetlevel = 4
    elif param == "SEZIONATORE":
        classstructureid = 1048
        assettype = 'SEZIONATORE'
        assetlevel = 4
    elif param == "GIUNTODIRIPARAZIONE":
        classstructureid = 1053
        assettype = 'GIUNTO DI RIPARAZIONE'
        assetlevel = 4
    elif param == "MONITORAGGIOIOTDIGICCAVI":
        classstructureid = 1054
        assettype = 'MONITORAGGIO IOT (DIGIC) CAVI'
        assetlevel = 4
    else:
        raise Exception("Invalid ASSETTYPE")
  
    sql = """
    SELECT
        x3g4at.X3GENLVL4FROMATTID,
        x3g4at.DESCRIPTION,
        x3g4at.STATUS,
        x3g4at.LOADEDASSET,
        x3g4at.PARENT,
        x3g4at.CLASSSTRUCTUREID,
        x3g4at.X3SAPID,
        TO_CHAR(x3g4at.INBDT,'YYYY-MM-DD') As INBDT
    FROM X3GENLVL4FROMATT x3g4at
    WHERE x3g4at.CHUNK_ID = """+ str(chunkid) +"""
    AND x3g4at.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
    AND x3g4at.DESCRIPTION = '"""+ str(assettype) +"""'
    AND x3g4at.EAM_ASSET_ID IS NULL
    """

    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())

    while GenAssetSet.next():
        try:
            startdate = mxServer.getDate()
            etlResult = 'SUCCESS'
            etlResultDescription = 'SUCCESS'
            
            X3GENLVL4FROMATTID=GenAssetSet.getString("X3GENLVL4FROMATTID")
            x3Set=MXServer.getMXServer().getMboSet("X3GENLVL4FROMATT", MXServer.getMXServer().getSystemUserInfo());
            x3Set.setWhere("X3GENLVL4FROMATTID='"+ X3GENLVL4FROMATTID + "'")
            x3Row=x3Set.getMbo(0)
            
            AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
            AssetSet.setWhere("1=2")
            NewAsset = AssetSet.add()
    
            DESCRIPTION=GenAssetSet.getString("DESCRIPTION")
            STATUS=GenAssetSet.getString("STATUS")
            LOADEDASSET=GenAssetSet.getString("LOADEDASSET")
            CLASSSTRUCTUREID=GenAssetSet.getString("CLASSSTRUCTUREID")
            PARENTASSETNUM = GenAssetSet.getString("PARENT")
            X3SAPID  = GenAssetSet.getString("X3SAPID")
            INBDT = GenAssetSet.getString("INBDT")
        
            NewAsset.setValue("STATUS",STATUS,11L)
            NewAsset.setValue("LOADED",LOADEDASSET)
            NewAsset.setValue("PARENT",PARENTASSETNUM)
            NewAsset.setValue("CLASSSTRUCTUREID",CLASSSTRUCTUREID)
            NewAsset.setValue("DESCRIPTION",DESCRIPTION)
            NewAsset.setValue("X3SAPID",X3SAPID)
            NewAsset.setValue("X3SAPIDETL",X3SAPID)
            NewAsset.setValue("INSTALLDATE",INBDT)
            assnum=NewAsset.getString("ASSETNUM")
            integrationService.setScenario(2)

            enddate = mxServer.getDate()
        
            AssetSet.save()
            
            if not x3Row is None:
                x3Row.setValue("EAM_ASSET_ID",assnum)
            
            totalOK=totalOK+1
            
        except Exception as e:
            enddate = mxServer.getDate()
            etlResult = 'ERROR JYTHON'
            etlResultDescription = e.message[0:99]
        except MXException as e:
            enddate = mxServer.getDate()
            etlResult = 'ERROR MX'
            etlResultDescription = e.message[0:99]    
        except JException as e:
            enddate = mxServer.getDate()
            etlResult = 'ERROR JAVA'
            etlResultDescription = e.message[0:99]
        finally:
            try:
                if not x3Row is None:
                    x3Row.setValue("TIMESTAMP_EXECUTION_START",startdate)
                    x3Row.setValue("TIMESTAMP_EXECUTION_END",enddate)
                    x3Row.setValue("ETL_RESULT",etlResult)
                    x3Row.setValue("ETL_RESULT_DESCRIPTION",etlResultDescription)
                x3Set.save()
            finally:
                try:
                    if not x3Set.isClosed():
                        x3Set.close()
                finally:
                    if not AssetSet.isClosed():
                        AssetSet.close()
        i=i+1
    GenAssetSet.close()

except SQLException as e:
    etlResult = 'ERROR SQL'
    etlResultDescription = e.message[0:99]
    responseBody = "Process ended with SQL ERROR for "+str(classstructureid)+" - "+str(param)+" with CHUNK_ID "+str(chunkid)+", total processed: "+str(i)+", total created: "+str(totalOK)+". Error message: "+etlResultDescription                                                                                                                                                                                                                                
except Exception as e:
    etlResult = 'ERROR JYTHON'
    etlResultDescription = e.message[0:99]
    log("ERROR","JYTHON Error on script","",etlResultDescription,"")
    responseBody = "Process ended with JYTHON ERROR for "+str(classstructureid)+" - "+str(param)+" with CHUNK_ID "+str(chunkid)+", total processed: "+str(i)+", total created: "+str(totalOK)+". Error message: "+etlResultDescription
except MXException as e:
    etlResult = 'ERROR MX'
    etlResultDescription = e.message[0:99]
    log("ERROR","MX Error on script","",etlResultDescription,"")
    responseBody = "Process ended with MX ERROR for "+str(classstructureid)+" - "+str(param)+" with CHUNK_ID "+str(chunkid)+", total processed: "+str(i)+", total created: "+str(totalOK)+". Error message: "+etlResultDescription
except JException as e:
    etlResult = 'ERROR JAVA'
    etlResultDescription = e.message[0:99]
    log("ERROR","JAVA Error on script","",etlResultDescription,"")
    responseBody = "Process ended with JAVA ERROR for "+str(classstructureid)+" - "+str(param)+" with CHUNK_ID "+str(chunkid)+", total processed: "+str(i)+", total created: "+str(totalOK)+". Error message: "+etlResultDescription
else:
    responseBody = "Process ended SUCCESSFULLY for "+str(classstructureid)+" - "+param+" with CHUNK_ID "+str(chunkid)+", total processed: "+str(i)+", total created: "+str(totalOK)+"."
finally:
    dbShortcut.close()