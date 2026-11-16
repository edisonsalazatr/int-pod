#X3ETLPROCRESTSAPLEV4ISOLATOREEXTRAN
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
    NewLog.setValue("X3SCRIPT","X3ETLPROCRESTSAPLEV4ISOLATOREEXTRAN")
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
    if param == 'PMTSTLII':
        classstructureid = 1039
        assettype = 'ISOLATORE CONDUTTORE'
        assetlevel = 4
    else:
        raise Exception("Invalid ASSETTYPE, the correct param is PMTSTLII and the actual param is " +param)
    
    sql = """
    SELECT
        x3sst.X3GENLVL4SAPISOLATOREID,
        x3sst.DESCRIPTION,
        x3sst.BUKRS,
        x3sst.EQFNR,
        x3sst.GEWRK,
        TO_CHAR(x3sst.INBDT,'YYYY-MM-DD') As INBDT,
        x3sst.INGRP,
        x3sst.KOKRS,
        x3sst.STRNO,
        x3sst.SWERK,
        x3sst.TPLMA,
        x3sst.TPLNR,
        x3sst.ZZSOCP,
        x3sst.ASSETNUM1037,
        x3sst.SAPPARENTUID,
        x3sst.LOADEDASSET,
        x3sst.STATUS,
        x3sst.LOCATION,
        x3sst.CLASSSTRUCTUREID,
        x3sst.SAPOBJECT,
        x3sst.ROOTASSETTYPE,
        x3sst.MAPPINGERROR,
        x3sst.SAPCLASS,
        x3sst.SAPLEVEL,
        x3sst.LOADED
    FROM X3GENLVL4SAPISOLATORE x3sst
    WHERE x3sst.CHUNK_ID = """+ str(chunkid) +"""
    AND x3sst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
    AND x3sst.EAM_ASSET_ID is NULL
    """
    
    sqfQuery=SqlFormat(sql)
    GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
    while GenAssetSet.next():
        try:
            startdate = mxServer.getDate()
            etlResult = 'SUCCESS'
            etlResultDescription = 'SUCCESS'
    
            X3GENLVL4SAPISOLATOREID=GenAssetSet.getString("X3GENLVL4SAPISOLATOREID")
            x3Set=MXServer.getMXServer().getMboSet("X3GENLVL4SAPISOLATORE", MXServer.getMXServer().getSystemUserInfo());
            x3Set.setWhere("X3GENLVL4SAPISOLATOREID='"+ X3GENLVL4SAPISOLATOREID + "'")
            x3Row=x3Set.getMbo(0)
    
            AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
            AssetSet.setWhere("1=2")
            NewAsset = AssetSet.add()
            
            X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
            X3SAPAssetSet.setWhere("1=2")
    
            DESCRIPTION=GenAssetSet.getString("DESCRIPTION")  
            BUKRS=GenAssetSet.getString("BUKRS")
            EQFNR=GenAssetSet.getString("EQFNR")
            GEWRK=GenAssetSet.getString("GEWRK")                
            INBDT=GenAssetSet.getString("INBDT")	
            INGRP=GenAssetSet.getString("INGRP")
            KOKRS=GenAssetSet.getString("KOKRS")               
            STRNO=GenAssetSet.getString("STRNO")
            SWERK=GenAssetSet.getString("SWERK")
            TPLMA=GenAssetSet.getString("TPLMA")
            TPLNR=GenAssetSet.getString("TPLNR")                
            ZZSOCP=GenAssetSet.getString("ZZSOCP")
            ASSETNUM1037=GenAssetSet.getString("ASSETNUM1037")
            SAPPARENTUID=GenAssetSet.getString("SAPPARENTUID")
            LOADEDASSET=GenAssetSet.getString("LOADEDASSET")
            STATUS=GenAssetSet.getString("STATUS")
            CLASSSTRUCTUREID=GenAssetSet.getString("CLASSSTRUCTUREID")
            SAPOBJECT=GenAssetSet.getString("SAPOBJECT")
            ROOTASSETTYPE=GenAssetSet.getString("ROOTASSETTYPE")
            MAPPINGERROR=GenAssetSet.getString("MAPPINGERROR")
            SAPCLASS=GenAssetSet.getString("SAPCLASS")
            SAPLEVEL=GenAssetSet.getString("SAPLEVEL")
            STRNOTRUNCLIN=STRNO[:7]
            
            NewAsset.setValue("STATUS",STATUS,11L)	
            NewAsset.setValue("INSTALLDATE",INBDT)
            NewAsset.setValue("X3SWERK",SWERK,11L)
            NewAsset.setValue("X3INGRP",INGRP,11L)
            NewAsset.setValue("X3ZZSOCP",ZZSOCP)
            NewAsset.setValue("X3EQFNR",EQFNR) 
            NewAsset.setValue("X3BUKRS",BUKRS)
            NewAsset.setValue("X3KOKRS",KOKRS)
            NewAsset.setValue("X3GEWRK",GEWRK,11L)
            NewAsset.setValue("LOADED",LOADEDASSET)
            NewAsset.setValue("X3SAPID",STRNO)
            NewAsset.setValue("X3SAPIDETL",STRNO)
            NewAsset.setValue("PARENT",ASSETNUM1037)
            NewAsset.setValue("CLASSSTRUCTUREID",CLASSSTRUCTUREID)
            NewAsset.setValue("DESCRIPTION",DESCRIPTION)
            assnum=NewAsset.getString("ASSETNUM")
            integrationService.setScenario(2)
            
            if (DESCRIPTION == 'ISOLATORE CONDUTTORE 1' or DESCRIPTION == 'ISOLATORE CONDUTTORE'):
                NewX3SAPAsset = X3SAPAssetSet.add()
                #NewX3SAPAsset.setValue("SAPUID",TPLNR,11L)
                #NewX3SAPAsset.setValue("STRNO",STRNO,11L)
                #NewX3SAPAsset.setValue("SAPOBJECT",SAPOBJECT,11L)
                #NewX3SAPAsset.setValue("ROOTASSETTYPE",ROOTASSETTYPE,11L)
                #NewX3SAPAsset.setValue("MAPPINGERROR",MAPPINGERROR,11L)
                #NewX3SAPAsset.setValue("SAPCLASS",SAPCLASS,11L)
                #NewX3SAPAsset.setValue("SAPLEVEL",SAPLEVEL,11L)
                #NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNCLIN,11L)
                #NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA,11L)
                #NewX3SAPAsset.setValue("SAPPARENTUID",SAPPARENTUID,11L)
                NewX3SAPAsset.setValue("SAPUID",TPLNR)
                NewX3SAPAsset.setValue("STRNO",STRNO)
                NewX3SAPAsset.setValue("SAPOBJECT",SAPOBJECT)
                NewX3SAPAsset.setValue("ROOTASSETTYPE",ROOTASSETTYPE)
                NewX3SAPAsset.setValue("MAPPINGERROR",MAPPINGERROR)
                NewX3SAPAsset.setValue("SAPCLASS",SAPCLASS)
                NewX3SAPAsset.setValue("SAPLEVEL",SAPLEVEL)
                NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNCLIN)
                NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
                NewX3SAPAsset.setValue("SAPPARENTUID",SAPPARENTUID)
                                                
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
                    try:
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
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