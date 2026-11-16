#X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN
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
    NewLog.setValue("X3SCRIPT","X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN")
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
    if param == 'PMTSTLIN':
        classstructureid = 1003
        assettype = 'LINEA'
        assetlevel = 1
        
        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """    
        
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
        
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
                AssetLocRelationSet=NewAsset.getMboSet("ASSETLOCRELATION")
                AssetLocRelationSet.setWhere("1=2")   
                
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
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                NewAsset.setValue("X3KOSTL",KOSTL)
                NewAsset.setValue("X3MSGRP",MSGRP)
                NewAsset.setValue("CLASSSTRUCTUREID",'1003')
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
                NewX3SAPAsset.setValue("SAPUID",TPLNR)
                NewX3SAPAsset.setValue("STRNO",STRNO)
                NewX3SAPAsset.setValue("SAPOBJECT",'S')
                NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
                NewX3SAPAsset.setValue("SYNCHACTION",'')
                NewX3SAPAsset.setValue("MAPPINGERROR",'0')
                NewX3SAPAsset.setValue("ROOTSTRNO",STRNO)
                NewX3SAPAsset.setValue("SAPCLASS",'LINEA')
                NewX3SAPAsset.setValue("SAPLEVEL",'1')
                
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
                            if not AssetLocRelationSet.isClosed():
                                AssetLocRelationSet.close()
                        finally:
                            try:
                                if not X3SAPAssetSet.isClosed():
                                    X3SAPAssetSet.close()									
                            finally:
                                if not AssetSet.isClosed():
                                    AssetSet.close()
            i=i+1
        GenAssetSet.close()
        
    
    elif param == 'PMTSTLIL':
        classstructureid = 1004
        assettype = 'CAMPATA AEREA'
        assetlevel = 2
        
        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sst.POSNR,
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS SOURCEASSETNUM
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = x3sst.TPLMA
                AND parent.CLASSSTRUCTUREID = '1003'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
                AssetLocRelationSet=NewAsset.getMboSet("ASSETLOCRELATION")
                AssetLocRelationSet.setWhere("1=2")
                NewAssetLocRelation = AssetLocRelationSet.add()
                
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
        
                NewAsset.setValue("STATUS",STTXU,11L)		
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR)
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                NewAsset.setValue("CLASSSTRUCTUREID",'1004')
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
                SourceAssetnum=GenAssetSet.getString("SOURCEASSETNUM")
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
                            if not AssetLocRelationSet.isClosed():
                                AssetLocRelationSet.close()
                        finally:
                            try:
                                if not X3SAPAssetSet.isClosed():
                                    X3SAPAssetSet.close()									
                            finally:
                                if not AssetSet.isClosed():
                                    AssetSet.close()
            i=i+1
        GenAssetSet.close()

    
    elif param == 'PMTSTLIS':
        classstructureid = 1036
        assettype = 'SOSTEGNO AEREO'
        assetlevel = 2

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS SOURCEASSETNUM
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = SUBSTR(x3sst.STRNO,1,7)
                AND parent.CLASSSTRUCTUREID = '1003'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
                AssetLocRelationSet=NewAsset.getMboSet("ASSETLOCRELATION")
                AssetLocRelationSet.setWhere("1=2")
                NewAssetLocRelation = AssetLocRelationSet.add()
                
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
        
                NewAsset.setValue("STATUS",STTXU,11L)
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR)
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                NewAsset.setValue("CLASSSTRUCTUREID",'1036')
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                   
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT)
                
                SourceAssetnum=GenAssetSet.getString("SOURCEASSETNUM")
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
                            if not AssetLocRelationSet.isClosed():
                                AssetLocRelationSet.close()
                        finally:
                            try:
                                if not X3SAPAssetSet.isClosed():
                                    X3SAPAssetSet.close()									
                            finally:
                                if not AssetSet.isClosed():
                                    AssetSet.close()
            i=i+1
        GenAssetSet.close()

        
    elif param == 'PMTSTLIZ':
        classstructureid = 1050
        assettype = 'TRATTA CAVO'
        assetlevel = 2

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sst.POSNR,
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS SOURCEASSETNUM
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = x3sst.TPLMA
                AND parent.CLASSSTRUCTUREID = '1003'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
                AssetLocRelationSet=NewAsset.getMboSet("ASSETLOCRELATION")
                AssetLocRelationSet.setWhere("1=2")
                NewAssetLocRelation = AssetLocRelationSet.add()
                
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
                
                NewAsset.setValue("STATUS",STTXU,11L)
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR)
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                NewAsset.setValue("CLASSSTRUCTUREID",'1050')
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                   
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
                SourceAssetnum=GenAssetSet.getString("SOURCEASSETNUM")
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
                            if not AssetLocRelationSet.isClosed():
                                AssetLocRelationSet.close()
                        finally:
                            try:
                                if not X3SAPAssetSet.isClosed():
                                    X3SAPAssetSet.close()									
                            finally:
                                if not AssetSet.isClosed():
                                    AssetSet.close()
            i=i+1
        GenAssetSet.close()
   

    elif param == 'PMTSTLIC':
        classstructureid = 1005
        assettype = 'CONDUTTORI'
        assetlevel = 3

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sa.SAPUID AS TPLNRPARENT,
            CASE 
            	WHEN parentCampata.DUPLICATE = 0 THEN parent.ASSETNUM
            	WHEN parentCampata.DUPLICATE = 1 THEN parentdoppia.ASSETNUM
            	ELSE NULL
            END AS ASSETNUMPARENT,
            CASE 
            	WHEN parentCampata.DUPLICATE = 0 THEN parent.STATUS
            	WHEN parentCampata.DUPLICATE = 1 THEN parentdoppia.STATUS
            	ELSE NULL
            END AS STATUSPARENT,
            asstLinea.ASSETNUM AS ASSETNUMLINEA
        FROM X3SEDITECHSAP x3sst
        INNER JOIN X3SEDITECHSAP parentCampata
        	ON (parentCampata.STRNO = x3sst.TPLMA 
            AND parentCampata.LOADED = x3sst.LOADED)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = SUBSTR(x3sst.STRNO,1,16)
                AND parent.CLASSSTRUCTUREID = '1004'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
		LEFT JOIN X3CARATSAP x3cs
			ON (x3sst.TPLMA = x3cs.OBJECT_ID
				AND x3cs.ATNAM = 'PMT_CAMP_TER_ASS'
				AND x3cs.CHAR_TYP IS NOT NULL
                AND x3cs.LOADED = x3sst.LOADED)
		LEFT JOIN ASSET parentdoppia
		      ON (parentdoppia.X3SAPIDETL = x3cs.CHAR_TYP
		            AND parentdoppia.CLASSSTRUCTUREID = '1004'
		            AND parentdoppia.LOADED = 1
		            AND parentdoppia.SITEID = 'SIT01')
        LEFT JOIN ASSET asstLinea
            ON (asstLinea.X3SAPIDETL = SUBSTR(x3sst.STRNO,1,7)
                AND asstLinea.CLASSSTRUCTUREID IN ('1003') 
                AND asstLinea.LOADED = 1
                AND asstLinea.SITEID = 'SIT01')
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        --AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
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
                ASSETNUMLINEA=GenAssetSet.getString("ASSETNUMLINEA")
                STATUSPARENT=GenAssetSet.getString("STATUSPARENT")
                
                NewAsset.setValue("STATUS",'DATT',11L)
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR) 
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                STRNOTRUNC1=STRNO[:7]
                STRNOTRUNC2=STRNO[:16]
                NewAsset.setValue("CLASSSTRUCTUREID",'1005')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                assetnumparent=GenAssetSet.getString("ASSETNUMPARENT")
                
                if STATUSPARENT == 'DEMO':
                    NewAsset.setValue("PARENT",assetnumparent,11L)
                else:
                    NewAsset.setValue("PARENT",assetnumparent)
                
                NewAsset.setValue("X3ROOTASSETNUM",ASSETNUMLINEA)
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
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


    elif param == 'PMTSTLIG':
        classstructureid = 1016
        assettype = 'FUNI DI GUARDIA'
        assetlevel = 3

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS ASSETNUMPARENT,
            parent.STATUS AS STATUSPARENT
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = SUBSTR(x3sst.STRNO,1,16)
                AND parent.CLASSSTRUCTUREID = '1004'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
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
                assetnumparent=GenAssetSet.getString("ASSETNUMPARENT")
                STATUSPARENT=GenAssetSet.getString("STATUSPARENT")
                
                NewAsset.setValue("STATUS",'DATT',11L)
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR) 
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                NewAsset.setValue("CLASSSTRUCTUREID",'1016')
                STRNOTRUNC1=STRNO[:7]
                STRNOTRUNC2=STRNO[:16]
                
                if STATUSPARENT == 'DEMO':
                    NewAsset.setValue("PARENT",assetnumparent,11L)
                else:
                    NewAsset.setValue("PARENT",assetnumparent)
                    
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
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


    elif param == 'PMTSTLIV':
        classstructureid = 1051
        assettype = 'CAVI'
        assetlevel = 3

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS ASSETNUMPARENT,
            parent.STATUS AS STATUSPARENT
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = x3sst.TPLMA
                AND parent.CLASSSTRUCTUREID = '1050'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
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
                assetnumparent=GenAssetSet.getString("ASSETNUMPARENT")
                STATUSPARENT=GenAssetSet.getString("STATUSPARENT")
                
                NewAsset.setValue("STATUS",'DATT',11L)
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR) 
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("CLASSSTRUCTUREID",'1051')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                STRNOTRUNC1=STRNO[:7]
                STRNOTRUNC2=STRNO[:16]
                
                if STATUSPARENT == 'DEMO':
                    NewAsset.setValue("PARENT",assetnumparent,11L)
                else:
                    NewAsset.setValue("PARENT",assetnumparent)
                
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
                NewX3SAPAsset.setValue("SAPUID",TPLNR)
                NewX3SAPAsset.setValue("STRNO",STRNO)
                NewX3SAPAsset.setValue("SAPOBJECT",'S')
                NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
                NewX3SAPAsset.setValue("SYNCHACTION",'')
                NewX3SAPAsset.setValue("MAPPINGERROR",'0')
                NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC1)
                NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
                NewX3SAPAsset.setValue("SAPCLASS",'CAVO')
                NewX3SAPAsset.setValue("SAPLEVEL",'3')
                STRNOTRUNC=STRNO[:7]
                NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNC)
                NewX3SAPAsset.setValue("PARENTSTRNO",TPLMA)
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
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


    elif param == 'PMTSTLICG':
        classstructureid = 1027
        assettype = 'GIUNTI'
        assetlevel = 3

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS ASSETNUMPARENT,
            parent.STATUS AS STATUSPARENT
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = x3sst.STRNO
                AND parent.CLASSSTRUCTUREID = '1026'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
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
                assetnumparent=GenAssetSet.getString("ASSETNUMPARENT")
                STATUSPARENT=GenAssetSet.getString("STATUSPARENT")
                
                NewAsset.setValue("STATUS",'DATT',11L)
                NewAsset.setValue("INSTALLDATE",INBDT)
                NewAsset.setValue("X3SWERK",SWERK,11L)
                NewAsset.setValue("X3INGRP",INGRP,11L)
                NewAsset.setValue("X3ZZSOCP",ZZSOCP)
                NewAsset.setValue("DESCRIPTION",PLTXT)
                NewAsset.setValue("X3EQFNR",EQFNR) 
                NewAsset.setValue("X3BUKRS",BUKRS)
                NewAsset.setValue("X3KOKRS",KOKRS)
                NewAsset.setValue("X3GEWRK",GEWRK,11L)
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("CLASSSTRUCTUREID",'1027')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                STRNOTRUNC7=STRNO[:7]
                STRNOTRUNC17=STRNO[:17]
                
                if STATUSPARENT == 'DEMO':
                    NewAsset.setValue("PARENT",assetnumparent,11L)
                else:
                    NewAsset.setValue("PARENT",assetnumparent)
                
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
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


    elif param == 'PMTSTLIT':
        classstructureid = 1032
        assettype = 'TERMINALI'
        assetlevel = 3

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
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
            x3sa.SAPUID AS TPLNRPARENT,
            parent.ASSETNUM AS ASSETNUMPARENT,
            parent.STATUS AS STATUSPARENT
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET x3sa
            ON (x3sa.STRNO = x3sst.TPLMA)
        LEFT JOIN ASSET parent
            ON (parent.X3SAPIDETL = x3sst.STRNO
                AND parent.CLASSSTRUCTUREID = '1026'
                AND parent.LOADED = 1
                AND parent.SITEID = 'SIT01')
        LEFT JOIN ASSET asst
            ON (asst.X3SAPIDETL = x3sst.STRNO
                AND asst.CLASSSTRUCTUREID = '"""+ str(classstructureid) +"""'
                AND asst.LOADED = 1
                AND asst.SITEID = 'SIT01')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.FLTYP = 'L' 
        AND x3sst.DUPLICATE = '0'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT IS NULL
        AND asst.ASSETNUM IS NULL
        ORDER BY x3sst.X3SEDITECHSAPID ASC
        """
    
        sqfQuery=SqlFormat(sql)
        GenAssetSet=dbShortcut.executeQuery(sqfQuery.format())
    
        while GenAssetSet.next():
            try:
                startdate = mxServer.getDate()
                etlResult = 'SUCCESS'
                etlResultDescription = 'SUCCESS'
                
                X3SEDITECHSAPID=GenAssetSet.getString("X3SEDITECHSAPID")
                x3Set=MXServer.getMXServer().getMboSet("X3SEDITECHSAP", MXServer.getMXServer().getSystemUserInfo());
                x3Set.setWhere("X3SEDITECHSAPID='"+ X3SEDITECHSAPID + "'")
                x3Row=x3Set.getMbo(0)
                
                AssetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
                AssetSet.setWhere("1=2")
                NewAsset = AssetSet.add()
                
                X3SAPAssetSet=NewAsset.getMboSet("X3SAPASSET")
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()
                
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
                assetnumparent=GenAssetSet.getString("ASSETNUMPARENT")
                STATUSPARENT=GenAssetSet.getString("STATUSPARENT")
                
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
                NewAsset.setValue("LOADED",'1')
                NewAsset.setValue("CLASSSTRUCTUREID",'1032')
                NewAsset.setValue("X3SAPID",STRNO)
                NewAsset.setValue("X3SAPIDETL",STRNO)
                STRNOTRUNC7=STRNO[:7]
                STRNOTRUNC17=STRNO[:17]
                
                if STATUSPARENT == 'DEMO':
                    NewAsset.setValue("PARENT",assetnumparent,11L)
                else:
                    NewAsset.setValue("PARENT",assetnumparent)
                
                assnum=NewAsset.getString("ASSETNUM")
                integrationService.setScenario(2)
                
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
                TPLNRPARENT=GenAssetSet.getString("TPLNRPARENT")
                NewX3SAPAsset.setValue("SAPPARENTUID",TPLNRPARENT) 
                
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
        
    else:
        raise Exception("Invalid ASSETTYPE")
        
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