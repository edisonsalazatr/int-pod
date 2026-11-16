#X3ETLPROCRESTSAPASSETMDFEXTRAN
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
    NewLog.setValue("X3SCRIPT","X3ETLPROCRESTSAPASSETMDFEXTRAN")
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
dbShortcutParent=DBShortcut()
dbShortcutParent.connect(mxServer.getSystemUserInfo().getConnectionKey())
dbShortcutSource=DBShortcut()
dbShortcutSource.connect(mxServer.getSystemUserInfo().getConnectionKey())
dbShortcutX3=DBShortcut()
dbShortcutX3.connect(mxServer.getSystemUserInfo().getConnectionKey())

classstructureid = 0
assettype = 'NONE'
assetlevel = 0

i=0
totalOK=0

try:
    if param == 'PMTSTLIL':
        classstructureid = 1004
        assettype = 'CAMPATA AEREA'
        assetlevel = 2
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'CAMPATA')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT = 1
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")        

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'1')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'CAMPATA')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.DUPLICATE = 1
        AND asst.X3SAPASSETID IS NULL
        AND x3sst.X3FICT IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'0')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


    elif param == 'PMTSTLIS':
        classstructureid = 1036
        assettype = 'SOSTEGNO AEREO'
        assetlevel = 2
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'SOSTEGNO')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.DUPLICATE = 1
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'0')

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
          
                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


    elif param == 'PMTSTLIZ':
        classstructureid = 1050
        assettype = 'TRATTA CAVO'
        assetlevel = 2
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'TRATTA CAVO')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT = 1
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'1')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


    elif param == 'PMTSTLIC':
        classstructureid = 1005
        assettype = 'CONDUTTORI'
        assetlevel = 3
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'CONDUTTORE')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT = 1
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")
                STRNOTRUNC1=STRNO[:7]

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'0')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


    elif param == 'PMTSTLIG':
        classstructureid = 1016
        assettype = 'FUNI DI GUARDIA'
        assetlevel = 3
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'CORDA DI GUARDIA')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT = 1
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")
                STRNOTRUNC1=STRNO[:7]

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'0')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'CORDA DI GUARDIA')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.DUPLICATE = 1
        AND asst.X3SAPASSETID IS NULL
        AND x3sst.X3FICT IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")
                STRNOTRUNC1=STRNO[:7]

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'0')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


    elif param == 'PMTSTLIV':
        classstructureid = 1051
        assettype = 'CAVI'
        assetlevel = 3
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'CAVO')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND x3sst.X3FICT = 1
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
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
                NewX3SAPAsset.setValue("DUMMY",'0')

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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
            i=i+1
        GenAssetSet.close()


    elif param == 'PMTSTLIM':
        classstructureid = 1004
        assettype = 'MORSETTERIA'
        assetlevel = 3
        i=0
        totalOK=0

        sql = """
        SELECT
            x3sst.X3SEDITECHSAPID,
            x3sst.STRNO,
            x3sst.TPLMA,
            x3sst.TPLNR
        FROM X3SEDITECHSAP x3sst
        LEFT JOIN X3SAPASSET asst
            ON (asst.STRNO = x3sst.STRNO
                AND asst.SAPCLASS = 'MORSETTERIA')
        WHERE x3sst.RBNR = '"""+ str(param) +"""'
        AND x3sst.CHUNK_ID = """+ str(chunkid) +"""
        AND asst.X3SAPASSETID IS NULL
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

                X3SAPAssetSet = MXServer.getMXServer().getMboSet("X3SAPASSET", MXServer.getMXServer().getSystemUserInfo());
                X3SAPAssetSet.setWhere("1=2")
                NewX3SAPAsset = X3SAPAssetSet.add()

                TPLNR=GenAssetSet.getString("TPLNR")	
                STRNO=GenAssetSet.getString("STRNO")
                TPLMA=GenAssetSet.getString("TPLMA")
                STRNOTRUNCLIN=STRNO[:7]

                x3sapassetid=NewX3SAPAsset.getString("X3SAPASSETID")
                NewX3SAPAsset.setValue("SAPUID",TPLNR)
                NewX3SAPAsset.setValue("STRNO",STRNO)
                NewX3SAPAsset.setValue("SAPOBJECT",'S')
                NewX3SAPAsset.setValue("ROOTASSETTYPE",'L')
                NewX3SAPAsset.setValue("SYNCHACTION",'')
                NewX3SAPAsset.setValue("MAPPINGERROR",'0')
                NewX3SAPAsset.setValue("SAPCLASS",'MORSETTERIA')
                NewX3SAPAsset.setValue("SAPLEVEL",'3')
                NewX3SAPAsset.setValue("ROOTSTRNO",STRNOTRUNCLIN)
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

                enddate = mxServer.getDate()

                X3SAPAssetSet.save()

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
                        if not X3SAPAssetSet.isClosed():
                            X3SAPAssetSet.close()									
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
    dbShortcutParent.close()
    dbShortcutSource.close()