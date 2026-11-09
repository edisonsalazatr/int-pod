if mbo.getString("STATUS")=="NON ESEGUITO" and mbo.getBoolean("HISTORYFLAG")==False and (mbo.isNull("OWNERGROUP") or mbo.isNull("WOEQ9")):
    jpSet=mbo.getMboSet("JOBPLAN")
    if not jpSet.isEmpty():
        jpMbo=jpSet.getMbo(0)
        tipoUDB=jpMbo.getString("X4TIPUDB")
        if tipoUDB=="UI":
            #service.error("TEST","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum ='"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
            personSet=mbo.getMboSet("$UDBUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum = '"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
        else:
            personSet=mbo.getMboSet("$PERSONGROUPNONUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum =:JPNUM AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))) OR X3PARENTPERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))))))")
        if not personSet.isEmpty():
            persongroup=personSet.getMbo(0).getString("PERSONGROUP")
            ui=personSet.getMbo(0).getString("X3PARENTPERSONGROUP")
            mbo.setValue("OWNERGROUP",persongroup,11L)
            #mbo.getThisMboSet().save()
        personSet.close()
    jpSet.close()
    
    # Popola l'attributo WOEQ9 con STRNO della sede tecnica/equipment corrispondente.
    # La relazione X4SAPIDALL3 e' strutturata per arrivare alla X3SAPASSET tramite mapping (X3MAXIMOSAPMAP)
    # e binding (X3SAPTOMAXOBJECT) del connettore.
    if mbo.isNull("woeq9"):
        # recupero l'asset del work order
        assetMbo = mbo.getMboSet("ASSET").getMbo(0)
        if assetMbo != None:
            # recupero l'asset SAP tramite dati del connettore EAM/SAP corrispondente all'asset
            sapAssetMbo = assetMbo.getMboSet("X4SAPIDALL3").getMbo(0)
            if sapAssetMbo != None:
                if sapAssetMbo.getString("sapobject") == "E":
                    # nel caso di equipment occorre sostituire il quinto segmento con il reale EQNR (presente in SAPUID se disponibile)
                    if not sapAssetMbo.isNull("sapuid"):
                        strNo = sapAssetMbo.getString("strno")
                        result = "-".join(strNo.split("-")[:-1])
                        result = result + "-" + sapAssetMbo.getString("sapuid")
                        mbo.setValue("woeq9", result, 11L)
                else:
                    # nel caso di sede tecnica si utilizza STRNO
                    mbo.setValue("woeq9", sapAssetMbo.getString("strno"), 11L)
            
            if mbo.isNull("woeq9"):
                # se non e' stato possibile recuperare dalla SAPASSET il STRNO utilizzo quello del padre se disponibile
                parentSet = assetMbo.getMboSet("PARENT")
                if not parentSet.isEmpty():
                    parentMbo = parentSet.getMbo(0)
                    parentSapid = parentMbo.getString("x3sapid")
                    if parentSapid:
                        mbo.setValue("woeq9", parentSapid, 11L)"
X4SENDSAP,Invio vs SAP,Draft,"from java.io import BufferedReader, InputStreamReader, OutputStreamWriter, StringReader
from java.net import HttpURLConnection, URL
from psdi.mbo import MboConstants
from psdi.server import MXServer
from javax.xml.parsers import DocumentBuilderFactory
from org.xml.sax import InputSource
from java.util import Base64
from java.text import SimpleDateFormat
from psdi.iface.mic import PublishChannelCache
from com.ibm.json.java import JSONObject, JSONArray, JSON
from java.util import HashMap
from psdi.iface.router import HTTPHandler
from org.apache.http.impl.client import HttpClients
from java.lang import String, System
from psdi.util.logging import MXLoggerFactory
logger = MXLoggerFactory.getLogger("maximo.terna")
logger.debug("[CONSUNTIVI SAP] Inizio esecuzione script")
from psdi.server import MXServer
from psdi.mbo import MboConstants
from psdi.util import MXException
from psdi.mbo import DBShortcut
def getSingleValue(sql):
    dbs = None
    rs = None
    try:
        dbs = DBShortcut()
        dbs.connect(mbo.getUserInfo().getConnectionKey())

        rs = dbs.executeQuery(sql)

        if rs.next():
            value = rs.getLong(1)
            if rs.wasNull():
                return None
            return value

        return None

    finally:
        try:
            if rs:
                rs.close()
        except:
            pass

        try:
            if dbs:
                dbs.close()
        except:
            pass


def getNewCorrelationId():
    sql = "SELECT X4CORRELATIONID_SEQ.NEXTVAL FROM DUAL"
    return getSingleValue(sql)
    
app = service.webclientsession().getCurrentApp()

if app is None:
    service.error("AppError: ", "L'oggetto 'app' non è inizializzato")

consuntSet = app.getDataBean()

if consuntSet is None:
    service.error("DialogError: ", "Dialog non esistente")

selectedCons = consuntSet.getSelection()

configUri = MXServer.getMXServer().getConfig()
url_string_azure = configUri.getProperty("mxe.int.sap.azureuri") 
#Recuperare valori di scope, client_id e secret
scope = configUri.getProperty("mxe.int.sap.scope") 
client_id = configUri.getProperty("mxe.int.sap.clientid") 
secret = configUri.getProperty("mxe.int.sap.secret") 
if not client_id or not secret or not scope:
	raise ValueError("Parametri di autenticazione mancanti o non configurati correttamente.")
	
#Generazione token vs Azure
attrObj = JSONObject()
attrObj.put("grant_type", "client_credentials")
attrObj.put("scope", scope)
attrObj.put("client_id", client_id)
attrObj.put("client_secret", secret)
bodyAzure=attrObj.toString()
properties = HashMap()
properties.put("HTTPMETHOD", "POST")
properties.put("URL", url_string_azure)

#Creazione del corpo della richiesta in modo dinamico
body = "grant_type=client_credentials"
body += "&scope=" + str(scope)
body += "&client_id=" + str(client_id)
body += "&client_secret="[REDACTED]"HEADERS", "content-type:application/x-www-form-urlencoded,content-length:" + str(len(body)))

respBytes = HTTPHandler().invoke(properties, body)
obj = JSON.parse(String(respBytes))

token=str(obj["access_token"])

for i in range(0,selectedCons.size()):
    singleConsSelez = selectedCons.get(i)
    singleConsSet = MXServer.getMXServer().getMboSet("LABTRANS", MXServer.getMXServer().getSystemUserInfo())
    singleConsSet.setWhere("labtransid={}".format(singleConsSelez.getLong("LABTRANSID")))
    singleConsSet.reset()
    singleCons=singleConsSet.getMbo(0)
    if singleCons.getBoolean("GENAPPRSERVRECEIPT")==True or singleCons.isModified() or singleCons.toBeUpdated():
        continue
    singleCons.setValue("GENAPPRSERVRECEIPT",1,11L)
    singleCons.setValue("APPROVER",user)
    
    #Verifico consuntivi già inviati con stesso correlationid
    if not singleCons.isNull("X4CORRELATIONID"):
        sameCorrSentSet=singleCons.getMboSet("$SAMECORRSET$","LABTRANS","x4correlationid=:x4correlationid and GENAPPRSERVRECEIPT=1")
        if not sameCorrSentSet.isEmpty():
            continue
    
    #Verifico se si deve aggregare, in caso positivo verifico se non esiste già un consuntivo aggregato mandato, poi stacco nuovo codice ed aggregato tutti i consuntivi
    capexAggrSet=singleCons.getMboSet("$WOAGGCAPEX$","WORKORDER","wonum=:refwo and siteid=:siteid and x4aggregati=1 and istask=0 and x4tipospesa in ('CAPEX','ANR')")
    reg = singleCons.getDouble("REGULARHRS")
    uff = singleCons.getDouble("X4OREUFF")
    if uff > 0 and not (reg > 0):
        naturaWhere = " and x4oreuff > 0 and (regularhrs is null or regularhrs = 0)"
    else:
        naturaWhere = " and regularhrs > 0 and (x4oreuff is null or x4oreuff = 0)"
    if not capexAggrSet.isEmpty():
        correlationSet=singleCons.getMboSet("$LABTRANSCORR$","LABTRANS","(GENAPPRSERVRECEIPT=0 and (X4CORRELATIONID is null or X4CORRELATIONID not in (select X4CORRELATIONID from labtrans where genapprservreceipt=1 and X4CORRELATIONID is not null))) and refwo=:refwo and laborcode=:laborcode and trunc(startdatetime)=trunc(:startdatetime) and refwo in (select wonum from workorder where x4aggregati=1 and istask=0 and x4tipospesa in ('CAPEX','ANR'))" +naturaWhere)
        if not correlationSet.isEmpty():
            newId = getNewCorrelationId()
            if newId is not None:
                singleCons.setValue("X4CORRELATIONID", newId, 11L)
                correlationUpdateSet=singleCons.getMboSet("$LABTRANSCORR$","LABTRANS","(X4CORRELATIONID is null or X4CORRELATIONID not in (select X4CORRELATIONID from labtrans where genapprservreceipt=1 and X4CORRELATIONID is not null)) and labtransid!=:labtransid and GENAPPRSERVRECEIPT=0 and refwo=:refwo and laborcode=:laborcode and trunc(startdatetime)=trunc(:startdatetime) and refwo in (select wonum from workorder where x4aggregati=1 and istask=0 and x4tipospesa in ('CAPEX','ANR'))")
                for jk in range (0,correlationUpdateSet.count()):
                    correlationMbo=correlationUpdateSet.getMbo(jk)
                    correlationMbo.setValue("X4CORRELATIONID", newId, 11L)
                correlationUpdateSet.save()
    capexAggrSet.close()
    odm=None
    checkOdL=singleCons.getMboSet("WORKORDER")
    create=False
    if not checkOdL.isEmpty():
        checkOdLMbo=checkOdL.getMbo(0)
        if checkOdLMbo.getString("X4TIPOSPESA")=="OPEX" and checkOdLMbo.isNull("X4ODM"):
            create=True

    singleCons.setValue("APPROVER",user,11L)
    if checkOdL.getMbo(0).getBoolean("X4AGGREGATI")==True:
        wpSet=singleCons.getMboSet("$WPLABORODL$","WPLABOR","wonum in (select wonum from workorder where parent=:refwo and siteid=:siteid) and siteid=:siteid")
    else:
        wpSet=singleCons.getMboSet("$WPLABORODL$","WPLABOR","wonum=:refwo and siteid=:siteid and (quantity!=0 and (laborhrs!=0 or X4ORETRASF!=0))")
    cdl={}
    checkOdL.close()
    mapping = {
        ("AM-AI","IMPIEGAT",None): "AMPC_I",
        ("AM-AI","QUADRO",None): "AMPC_Q",
        ("AM-AI-AMIS","IMPIEGAT",None): "AIAMIS_I",
        ("AM-AI-AMIS","QUADRO",None): "AIAMIS_Q",
        ("AM-AMOC","IMPIEGAT","ES"): "LGPEES_I",
        ("AM-AMOC","QUADRO","ES"): "LGPEES_Q",
        ("AM-AMOC","IMPIEGAT","OC"): "AMAMOC_I",
        ("AM-AMOC","QUADRO","OC"): "AMAMOC_Q",
        ("AM-AMOC-AMCC","IMPIEGAT",None): "AMCC_I",
        ("AM-AMOC-AMCC","QUADRO",None): "AMCC_Q",
        ("AM-AMOC-AAC","IMPIEGAT",None): "AIAAC_I",
        ("AM-AMOC-AAC","QUADRO",None): "AIAAC_Q",
        ("AM-AMOC-PV","IMPIEGAT",None): "SPMPRV_I",
        ("AM-AMOC-PV","QUADRO",None): "SPMPRV_Q",
        ("DPCE-AT","IMPIEGAT",None): "DTCENA_I",
        ("DPCE-AT","QUADRO",None): "DTCENA_Q",
        ("DPCN-AT","IMPIEGAT",None): "DTCNRA_I",
        ("DPCN-AT","QUADRO",None): "DTCNRA_Q",
        ("DPNE-AT","IMPIEGAT",None): "DTNESA_I",
        ("DPNE-AT","QUADRO",None): "DTNESA_Q",
        ("DPND-AT","IMPIEGAT",None): "DTNRAT_I",
        ("DPND-AT","QUADRO",None): "DTNRAT_Q",
        ("DPNO-AT","IMPIEGAT",None): "DTNVAT_I",
        ("DPNO-AT","QUADRO",None): "DTNVAT_Q",
        ("DPSU-AT","IMPIEGAT",None): "DTSUAT_I",
        ("DPSU-AT","QUADRO",None): "DTSUAT_Q",
        ("DSSA-COAM","IMPIEGAT",None): "SARCOA_I",
        ("DSSA-COAM","QUADRO",None): "SARCOA_Q",
        ("DSSI-COAM","IMPIEGAT",None): "SICCOA_I",
        ("DSSI-COAM","QUADRO",None): "SICCOA_Q",
        ("DPSU-AT-COAM","IMPIEGAT",None): "SUDCOA_I",
        ("DPSU-AT-COAM","QUADRO",None): "SUDCOA_Q",
        ("DPNE-AT-COAM","IMPIEGAT",None): "NESCOA_I",
        ("DPNE-AT-COAM","QUADRO",None): "NESCOA_Q",
        ("DPNO-AT-COAM","QUADRO",None): "ATNCOA_Q",
        ("DPNO-AT-COAM","IMPIEGAT",None): "DPNOCO_I",
        ("DPND-AT-COAM","IMPIEGAT",None): "DPNDCO_I",
        ("DPCE-AT-COAM","IMPIEGAT",None): "CENCOA_I",
        ("DPCE-AT-COAM","QUADRO",None): "CENCOA_Q",
        ("DPCN-AT-COAM","IMPIEGAT",None): "CNRCOA_I",
        ("DPCN-AT-COAM","QUADRO",None): "CNRCOA_Q",
        ("DPCE-SG-TCP","IMPIEGAT",None): "CENTCP_I",
        ("DPCE-SG-TCP","QUADRO",None): "CENTCP_Q",
        ("DPCN-SG-TS","IMPIEGAT",None): "SGCNTS_I",
        ("DPCN-SG-TS","QUADRO",None): "SGCNTS_Q",
        ("DPCN-SG-TCP","IMPIEGAT",None): "CNRTCP_I",
        ("DPCN-SG-TCP","QUADRO",None): "CNRTCP_Q",
        ("DPND-SG-TCP","IMPIEGAT",None): "NORTCP_I",
        ("DPND-SG-TCP","QUADRO",None): "NORTCP_Q",
        ("DPND-SG-TS","IMPIEGAT",None): "SGNRTS_I",
        ("DPND-SG-TS","QUADRO",None): "SGNRTS_Q",
        ("DPNE-SG-TCP","IMPIEGAT",None): "DTNECP_I",
        ("DPNE-SG-TCP","QUADRO",None): "DTNECP_Q",
        ("DPNE-SG-TS","IMPIEGAT",None): "DTNETS_I",
        ("DPNE-SG-TS","QUADRO",None): "DTNETS_Q",
        ("DPNO-SG-TS","IMPIEGAT",None): "SGNVTS_I",
        ("DPNO-SG-TS","QUADRO",None): "SGNVTS_Q",
        ("DPNO-SG-TCP","IMPIEGAT",None): "NOVTCP_I",
        ("DPNO-SG-TCP","QUADRO",None): "NOVTCP_Q",
        ("DPSU-SG-TCP","IMPIEGAT",None): "DTCSCP_I",
        ("DPSU-SG-TCP","QUADRO",None): "DTCSCP_Q",
        ("DPSU-SG-TS","IMPIEGAT",None): "DTCSTS_I",
        ("DPSU-SG-TS","QUADRO",None): "DTCSTS_Q",
        ("AM-PMI","IMPIEGAT","MR"): "LGPCMR_I",
        ("AM-PMI","QUADRO","MR"): "LGPCMR_Q",
        ("AM-PMI","IMPIEGAT","AM"): "AMPAM_I",
        ("AM-PMI","QUADRO","AM"): "AMPAM_Q",
        ("AM-PMI","IMPIEGAT","MI"): "PMIMI_I",
        ("AM-PMI","QUADRO","MI"): "PMIMI_Q",
        ("AM-PMI","IMPIEGAT","OL"): "PMIPOL_I",
        ("AM-PMI","QUADRO","OL"): "PMIPOL_Q",
        ("DSSA-MPL","IMPIEGAT",None): "SARMPL_I",
        ("DSSA-MPL","QUADRO",None): "SARMPL_Q",
        ("DSSI-MPL","IMPIEGAT",None): "SICMPL_I",
        ("DSSI-MPL","QUADRO",None): "SICMPL_Q",
        ("DPSU-AT-MPL","IMPIEGAT",None): "SUDMPL_I",
        ("DPSU-AT-MPL","QUADRO",None): "SUDMPL_Q",
        ("DPNE-AT-MPL","IMPIEGAT",None): "NESMPL_I",
        ("DPNE-AT-MPL","QUADRO",None): "NESMPL_Q",
        ("DPND-AT-MPL","IMPIEGAT",None): "NORMPL_I",
        ("DPND-AT-MPL","QUADRO",None): "NORMPL_Q",
        ("DPNO-AT-MPL","IMPIEGAT",None): "NOVMPL_I",
        ("DPNO-AT-MPL","QUADRO",None): "NOVMPL_Q",
        ("DPCE-AT-MPL","IMPIEGAT",None): "CENMPL_I",
        ("DPCE-AT-MPL","QUADRO",None): "CENMPL_Q",
        ("DPCN-AT-MPL","IMPIEGAT",None): "CNRMPL_I",
        ("DPCN-AT-MPL","QUADRO",None): "CNRMPL_Q",
        ("AM-GP-PMI","IMPIEGAT",None): "GPPMI_I",
        ("AM-GP-PMI","QUADRO",None): "GPPMI_Q",
        ("RTN-PMO","IMPIEGAT",None): "RICGE_I",
        ("RTN-PMO","QUADRO",None): "RICGE_Q",
        ("AM-GP-PRO","IMPIEGAT","RL"): "PROPRL_I",
        ("AM-GP-PRO","QUADRO","RL"): "PROPRL_Q",
        ("AM-GP-PRO","IMPIEGAT","RS"): "PROPRS_I",
        ("AM-GP-PRO","QUADRO","RS"): "PROPRS_Q",
        ("AM-GP-PRO","IMPIEGAT","RO"): "AMPRO_I",
        ("AM-GP-PRO","QUADRO","RO"): "AMPRO_Q",
        ("DPNO-AT-RL","QUADRO","TL"): "RLNVTL_Q",
        ("DPNO-AT-RL","IMPIEGAT","TS"): "RLNVTS_I",
        ("DPNO-AT-RL","QUADRO","TS"): "RLNVTS_Q",
        ("DPND-AT-RL","IMPIEGAT","AS"): "NORTAS_I",
        ("DPND-AT-RL","QUADRO","AS"): "NORTAS_Q",
        ("DPNO-AT-RL","IMPIEGAT","AS"): "NOVTAS_I",
        ("DPNO-AT-RL","QUADRO","AS"): "NOVTAS_Q",
        ("DPCE-AT-RL","IMPIEGAT","TL"): "RLCETL_I",
        ("DPCE-AT-RL","QUADRO","TL"): "RLCETL_Q",
        ("DPCE-AT-RL","IMPIEGAT","TS"): "RLCETS_I",
        ("DPCE-AT-RL","QUADRO","TS"): "RLCETS_Q",
        ("DPCN-AT-RL","IMPIEGAT","TL"): "RLCNTL_I",
        ("DPCN-AT-RL","QUADRO","TL"): "RLCNTL_Q",
        ("DPCN-AT-RL","IMPIEGAT","TS"): "RLCNTS_I",
        ("DPCN-AT-RL","QUADRO","TS"): "RLCNTS_Q",
        ("DPND-AT-RL","IMPIEGAT","TL"): "RLNRTL_I",
        ("DPND-AT-RL","QUADRO","TL"): "RLNRTL_Q",
        ("DPND-AT-RL","IMPIEGAT","TS"): "RLNRTS_I",
        ("DPND-AT-RL","QUADRO","TS"): "RLNRTS_Q",
        ("DPNO-AT-RL","IMPIEGAT","TL"): "RLNVTL_I",
        ("DPCE-AT-RL","IMPIEGAT","RL"): "ATCERL_I",
        ("DPCE-AT-RL","QUADRO","RL"): "ATCERL_Q",
        ("DPCN-AT-RL","IMPIEGAT","RL"): "ATCNRL_I",
        ("DPCN-AT-RL","QUADRO","RL"): "ATCNRL_Q",
        ("DPND-AT-RL","IMPIEGAT","RL"): "ATNRRL_I",
        ("DPND-AT-RL","QUADRO","RL"): "ATNRRL_Q",
        ("DPNO-AT-RL","IMPIEGAT","RL"): "ATNVRL_I",
        ("DPNO-AT-RL","QUADRO","RL"): "ATNVRL_Q",
        ("DPCE-AT-RL","IMPIEGAT","AS"): "CENTAS_I",
        ("DPCE-AT-RL","QUADRO","AS"): "CENTAS_Q",
        ("DPCN-AT-RL","IMPIEGAT","AS"): "CNRTAS_I",
        ("DPCN-AT-RL","QUADRO","AS"): "CNRTAS_Q",
        ("DPSU-AT-RL","IMPIEGAT","RL"): "DPSURL_I",
        ("DPSU-AT-RL","IMPIEGAT","PA"): "DTCSPA_I",
        ("DPSU-AT-RL","QUADRO","PA"): "DTCSPA_Q",
        ("DPSU-AT-RL","IMPIEGAT","PL"): "DTCSPL_I",
        ("DPSU-AT-RL","QUADRO","PL"): "DTCSPL_Q",
        ("DPSU-AT-RL","IMPIEGAT","PS"): "DTCSPS_I",
        ("DPSU-AT-RL","QUADRO","PS"): "DTCSPS_Q",
        ("DPSU-AT-RL","QUADRO","SP"): "DTCSP_Q",
        ("DPNE-AT-RL","IMPIEGAT","PA"): "DTNEPA_I",
        ("DPNE-AT-RL","QUADRO","PA"): "DTNEPA_Q",
        ("DPNE-AT-RL","IMPIEGAT","PL"): "DTNEPL_I",
        ("DPNE-AT-RL","QUADRO","PL"): "DTNEPL_Q",
        ("DPNE-AT-RL","IMPIEGAT","PS"): "DTNEPS_I",
        ("DPNE-AT-RL","QUADRO","PS"): "DTNEPS_Q",
        ("DPNE-AT-RL","QUADRO","EP"): "DTNEP_Q",
        ("DSSA-RL","IMPIEGAT",None): "DTSARL_I",
        ("DSSA-RL","QUADRO",None): "DTSARL_Q",
        ("DSSI-RL","IMPIEGAT",None): "DTSIRL_I",
        ("DSSI-RL","QUADRO",None): "DTSIRL_Q",
        ("DPNE-SG","IMPIEGAT",None): "DPNESG_I",
        ("DPSU-SG","IMPIEGAT",None): "DPSUSG_I",
        ("DPCE-SG","IMPIEGAT",None): "DTCESG_I",
        ("DPCE-SG","QUADRO",None): "DTCESG_Q",
        ("DPCN-SG","IMPIEGAT",None): "DTCNSG_I",
        ("DPCN-SG","QUADRO",None): "DTCNSG_Q",
        ("DPND-SG","IMPIEGAT",None): "DTNRSG_I",
        ("DPND-SG","QUADRO",None): "DTNRSG_Q",
        ("DPNO-SG","IMPIEGAT",None): "DTNVSG_I",
        ("DPNO-SG","QUADRO",None): "DTNVSG_Q",
        ("DSSA-SG","IMPIEGAT",None): "DTSASG_I",
        ("DSSA-SG","QUADRO",None): "DTSASG_Q",
        ("DSSI-SG","IMPIEGAT",None): "DTSISG_I",
        ("DSSI-SG","QUADRO",None): "DTSISG_Q",
        ("DPCE-SG-TS","IMPIEGAT",None): "SCENTS_I",
        ("DPCE-SG-TS","QUADRO",None): "SCENTS_Q",
        ("AM-SPM","IMPIEGAT","AC"): "SPMMAC_I",
        ("AM-SPM","QUADRO","AC"): "SPMMAC_Q",
        ("AM-SPM","IMPIEGAT","AS"): "SPMMAS_I",
        ("AM-SPM","QUADRO","AS"): "SPMMAS_Q",
        ("AM-SPM","OPERAIO","ET"): "INGMET_O",
        ("AM-SPM","IMPIEGAT","PM"): "AMSPM_I",
        ("AM-SPM","QUADRO","PM"): "AMSPM_Q",
        ("AM-SUP","IMPIEGAT","UP"): "AMSUP_I",
        ("AM-SUP","QUADRO","UP"): "AMSUP_Q",
        ("AM-SUP","IMPIEGAT","AM"): "PSAM_I",
        ("AM-SUP","QUADRO","AM"): "PSAM_Q",
        ("DPND-SG-TCG","IMPIEGAT",None): "NORTCG_I",
        ("DPND-SG-TCG","QUADRO",None): "NORTCG_Q",
        ("DPNO-SG-TCG","IMPIEGAT",None): "NOVTCG_I",
        ("DPNO-SG-TCG","QUADRO",None): "NOVTCG_Q",
        ("DPCE-SG-TCG","IMPIEGAT",None): "CENTCG_I",
        ("DPCE-SG-TCG","QUADRO",None): "CENTCG_Q",
        ("DPCN-SG-TCG","IMPIEGAT",None): "CNRTCG_I",
        ("DPCN-SG-TCG","QUADRO",None): "CNRTCG_Q",
        ("DPSU-SG-TCG","IMPIEGAT",None): "DTCSCG_I",
        ("DPSU-SG-TCG","QUADRO",None): "DTCSCG_Q",
        ("DPNE-SG-TCG","IMPIEGAT",None): "DTNECG_I",
        ("DPNE-SG-TCG","QUADRO",None): "DTNECG_Q",
        ("DPCE-UAM","QUADRO","RE"): "UAMRE_Q",
        ("DPCE-UAM","IMPIEGAT","TP"): "UAMTP_I",
        ("DPCE-UAM","IMPIEGAT","TT"): "UAMTT_I",
        ("DPCE-UAM","OPERAIO","TT"): "UAMTT_O",
        ("DPCE-UAM","QUADRO","TT"): "UAMTT_Q",
        ("DPCE-UAM","IMPIEGAT","RE"): "UAMRE_I",
        ("DPSU-UBA","QUADRO","RE"): "UBARE_Q",
        ("DPSU-UBA","IMPIEGAT","TP"): "UBATP_I",
        ("DPSU-UBA","QUADRO","TP"): "UBATP_Q",
        ("DPSU-UBA","IMPIEGAT","TT"): "UBATT_I",
        ("DPSU-UBA","OPERAIO","TT"): "UBATT_O",
        ("DPSU-UBA","QUADRO","TT"): "UBATT_Q",
        ("DPSU-UBA","IMPIEGAT","RE"): "UBARE_I",
        ("DPND-UBG","QUADRO","RE"): "UBGRE_Q",
        ("DPND-UBG","IMPIEGAT","TP"): "UBGTP_I",
        ("DPND-UBG","IMPIEGAT","TT"): "UBGTT_I",
        ("DPND-UBG","OPERAIO","TT"): "UBGTT_O",
        ("DPND-UBG","QUADRO","TT"): "UBGTT_Q",
        ("DPND-UBG","IMPIEGAT","RE"): "UBGRE_I",
        ("DPCN-UBO","QUADRO","RE"): "UBORE_Q",
        ("DPCN-UBO","IMPIEGAT","TP"): "UBOTP_I",
        ("DPCN-UBO","IMPIEGAT","TT"): "UBOTT_I",
        ("DPCN-UBO","OPERAIO","TT"): "UBOTT_O",
        ("DPCN-UBO","QUADRO","TT"): "UBOTT_Q",
        ("DPCN-UBO","IMPIEGAT","RE"): "UBORE_I",
        ("DPSU-UBR","QUADRO","RE"): "UBRRE_Q",
        ("DPSU-UBR","IMPIEGAT","TP"): "UBRTP_I",
        ("DPSU-UBR","IMPIEGAT","TT"): "UBRTT_I",
        ("DPSU-UBR","OPERAIO","TT"): "UBRTT_O",
        ("DPSU-UBR","QUADRO","TT"): "UBRTT_Q",
        ("DPSU-UBR","IMPIEGAT","RE"): "UBRRE_I",
        ("DPND-UBS","QUADRO","RE"): "UBSRE_Q",
        ("DPND-UBS","IMPIEGAT","TP"): "UBSTP_I",
        ("DPND-UBS","IMPIEGAT","TT"): "UBSTT_I",
        ("DPND-UBS","OPERAIO","TT"): "UBSTT_O",
        ("DPND-UBS","QUADRO","TT"): "UBSTT_Q",
        ("DPND-UBS","IMPIEGAT","RE"): "UBSRE_I",
        ("DPNE-UCA","QUADRO","TT"): "UCATT_Q",
        ("DPNE-UCA","QUADRO","RE"): "UCARE_Q",
        ("DPNE-UCA","IMPIEGAT","TP"): "UCATP_I",
        ("DPNE-UCA","IMPIEGAT","TT"): "UCATT_I",
        ("DPNE-UCA","OPERAIO","TT"): "UCATT_O",
        ("DPNE-UCA","IMPIEGAT","RE"): "UCARE_I",
        ("DPND-UCH","QUADRO","RE"): "UCHRE_Q",
        ("DPND-UCH","IMPIEGAT","TP"): "UCHTP_I",
        ("DPND-UCH","QUADRO","TP"): "UCHTP_Q",
        ("DPND-UCH","IMPIEGAT","TT"): "UCHTT_I",
        ("DPND-UCH","OPERAIO","TT"): "UCHTT_O",
        ("DPND-UCH","QUADRO","TT"): "UCHTT_Q",
        ("DPND-UCH","IMPIEGAT","RE"): "UCHRE_I",
        ("DSSA-UCO","QUADRO","RE"): "UCORE_Q",
        ("DSSA-UCO","IMPIEGAT","TP"): "UCOTP_I",
        ("DSSA-UCO","IMPIEGAT","TT"): "UCOTT_I",
        ("DSSA-UCO","OPERAIO","TT"): "UCOTT_O",
        ("DSSA-UCO","QUADRO","TT"): "UCOTT_Q",
        ("DSSA-UCO","IMPIEGAT","RE"): "UCORE_I",
        ("DPND-UCS","QUADRO","RE"): "UCSRE_Q",
        ("DPND-UCS","IMPIEGAT","TP"): "UCSTP_I",
        ("DPND-UCS","IMPIEGAT","TT"): "UCSTT_I",
        ("DPND-UCS","OPERAIO","TT"): "UCSTT_O",
        ("DPND-UCS","QUADRO","TT"): "UCSTT_Q",
        ("DPND-UCS","IMPIEGAT","RE"): "UCSRE_I",
        ("DSSI-UCT","QUADRO","RE"): "UCTRE_Q",
        ("DSSI-UCT","IMPIEGAT","TP"): "UCTTP_I",
        ("DSSI-UCT","IMPIEGAT","TT"): "UCTTT_I",
        ("DSSI-UCT","OPERAIO","TT"): "UCTTT_O",
        ("DSSI-UCT","QUADRO","TT"): "UCTTT_Q",
        ("DSSI-UCT","IMPIEGAT","RE"): "UCTRE_I",
        ("DPNE-UDU","QUADRO","RE"): "UDURE_Q",
        ("DPNE-UDU","IMPIEGAT","TP"): "UDUTP_I",
        ("DPNE-UDU","IMPIEGAT","TT"): "UDUTT_I",
        ("DPNE-UDU","OPERAIO","TT"): "UDUTT_O",
        ("DPNE-UDU","QUADRO","TT"): "UDUTT_Q",
        ("DPNE-UDU","IMPIEGAT","RE"): "UDURE_I",
        ("DPSU-UFE","QUADRO","RE"): "UFERE_Q",
        ("DPSU-UFE","IMPIEGAT","TP"): "UFETP_I",
        ("DPSU-UFE","IMPIEGAT","TT"): "UFETT_I",
        ("DPSU-UFE","OPERAIO","TT"): "UFETT_O",
        ("DPSU-UFE","QUADRO","TT"): "UFETT_Q",
        ("DPSU-UFE","IMPIEGAT","RE"): "UFERE_I",
        ("DPCN-UFI","QUADRO","RE"): "UFIRE_Q",
        ("DPCN-UFI","IMPIEGAT","TP"): "UFITP_I",
        ("DPCN-UFI","IMPIEGAT","TT"): "UFITT_I",
        ("DPCN-UFI","OPERAIO","TT"): "UFITT_O",
        ("DPCN-UFI","QUADRO","TT"): "UFITT_Q",
        ("DPCN-UFI","IMPIEGAT","RE"): "UFIRE_I",
        ("DPSU-UFM","QUADRO","RE"): "UFMRE_Q",
        ("DPSU-UFM","IMPIEGAT","TP"): "UFMTP_I",
        ("DPSU-UFM","QUADRO","TP"): "UFMTP_Q",
        ("DPSU-UFM","IMPIEGAT","TT"): "UFMTT_I",
        ("DPSU-UFM","OPERAIO","TT"): "UFMTT_O",
        ("DPSU-UFM","QUADRO","TT"): "UFMTT_Q",
        ("DPSU-UFM","IMPIEGAT","RE"): "UFMRE_I",
        ("DPNE-UFV","QUADRO","RE"): "UFVRE_Q",
        ("DPNE-UFV","IMPIEGAT","TP"): "UFVTP_I",
        ("DPNE-UFV","IMPIEGAT","TT"): "UFVTT_I",
        ("DPNE-UFV","OPERAIO","TT"): "UFVTT_O",
        ("DPNE-UFV","QUADRO","TT"): "UFVTT_Q",
        ("DPNE-UFV","IMPIEGAT","RE"): "UFVRE_I",
        ("DPNO-UGE","QUADRO","RE"): "UGERE_Q",
        ("DPNO-UGE","IMPIEGAT","TP"): "UGETP_I",
        ("DPNO-UGE","IMPIEGAT","TT"): "UGETT_I",
        ("DPNO-UGE","OPERAIO","TT"): "UGETT_O",
        ("DPNO-UGE","QUADRO","TT"): "UGETT_Q",
        ("DPNO-UGE","IMPIEGAT","RE"): "UGERE_I",
        ("DPND-UGO","QUADRO","RE"): "UGORE_Q",
        ("DPND-UGO","IMPIEGAT","TP"): "UGOTP_I",
        ("DPND-UGO","IMPIEGAT","TT"): "UGOTT_I",
        ("DPND-UGO","OPERAIO","TT"): "UGOTT_O",
        ("DPND-UGO","QUADRO","TT"): "UGOTT_Q",
        ("DPND-UGO","IMPIEGAT","RE"): "UGORE_I",
        ("DPCE-ULN","QUADRO","RE"): "ULNRE_Q",
        ("DPCE-ULN","IMPIEGAT","TP"): "ULNTP_I",
        ("DPCE-ULN","QUADRO","TP"): "ULNTP_Q",
        ("DPCE-ULN","IMPIEGAT","TT"): "ULNTT_I",
        ("DPCE-ULN","OPERAIO","TT"): "ULNTT_O",
        ("DPCE-ULN","QUADRO","TT"): "ULNTT_Q",
        ("DPCE-ULN","IMPIEGAT","RE"): "ULNRE_I",
        ("DPCE-ULS","QUADRO","RE"): "ULSRE_Q",
        ("DPCE-ULS","IMPIEGAT","TP"): "ULSTP_I",
        ("DPCE-ULS","IMPIEGAT","TT"): "ULSTT_I",
        ("DPCE-ULS","OPERAIO","TT"): "ULSTT_O",
        ("DPCE-ULS","QUADRO","TT"): "ULSTT_Q",
        ("DPCE-ULS","IMPIEGAT","RE"): "ULSRE_I",
        ("DPCE-UMA","QUADRO","RE"): "UMARE_Q",
        ("DPCE-UMA","IMPIEGAT","TP"): "UMATP_I",
        ("DPCE-UMA","IMPIEGAT","TT"): "UMATT_I",
        ("DPCE-UMA","OPERAIO","TT"): "UMATT_O",
        ("DPCE-UMA","QUADRO","TT"): "UMATT_Q",
        ("DPCE-UMA","IMPIEGAT","RE"): "UMARE_I",
        ("DPCE-UMB","QUADRO","RE"): "UMBRE_Q",
        ("DPCE-UMB","IMPIEGAT","TP"): "UMBTP_I",
        ("DPCE-UMB","IMPIEGAT","TT"): "UMBTT_I",
        ("DPCE-UMB","OPERAIO","TT"): "UMBTT_O",
        ("DPCE-UMB","QUADRO","TT"): "UMBTT_Q",
        ("DPCE-UMB","IMPIEGAT","RE"): "UMBRE_I",
        ("DPSU-UMD","QUADRO","RE"): "UMDRE_Q",
        ("DPSU-UMD","IMPIEGAT","TP"): "UMDTP_I",
        ("DPSU-UMD","IMPIEGAT","TT"): "UMDTT_I",
        ("DPSU-UMD","OPERAIO","TT"): "UMDTT_O",
        ("DPSU-UMD","QUADRO","TT"): "UMDTT_Q",
        ("DPSU-UMD","IMPIEGAT","RE"): "UMDRE_I",
        ("DPNO-UNO","QUADRO","RE"): "UNORE_Q",
        ("DPNO-UNO","IMPIEGAT","TP"): "UNOTP_I",
        ("DPNO-UNO","IMPIEGAT","TT"): "UNOTT_I",
        ("DPNO-UNO","OPERAIO","TT"): "UNOTT_O",
        ("DPNO-UNO","QUADRO","TT"): "UNOTT_Q",
        ("DPNO-UNO","IMPIEGAT","RE"): "UNORE_I",
        ("DSSI-UPA","QUADRO","RE"): "UPARE_Q",
        ("DSSI-UPA","IMPIEGAT","TP"): "UPATP_I",
        ("DSSI-UPA","IMPIEGAT","TT"): "UPATT_I",
        ("DSSI-UPA","OPERAIO","TT"): "UPATT_O",
        ("DSSI-UPA","QUADRO","TT"): "UPATT_Q",
        ("DSSI-UPA","IMPIEGAT","RE"): "UPARE_I",
        ("DPNO-UPM","QUADRO","RE"): "UPMRE_Q",
        ("DPNO-UPM","IMPIEGAT","TP"): "UPMTP_I",
        ("DPNO-UPM","QUADRO","TP"): "UPMTP_Q",
        ("DPNO-UPM","IMPIEGAT","TT"): "UPMTT_I",
        ("DPNO-UPM","OPERAIO","TT"): "UPMTT_O",
        ("DPNO-UPM","QUADRO","TT"): "UPMTT_Q",
        ("DPNO-UPM","IMPIEGAT","RE"): "UPMRE_I",
        ("DPCN-UPR","QUADRO","RE"): "UPRRE_Q",
        ("DPCN-UPR","IMPIEGAT","TP"): "UPRTP_I",
        ("DPCN-UPR","IMPIEGAT","TT"): "UPRTT_I",
        ("DPCN-UPR","OPERAIO","TT"): "UPRTT_O",
        ("DPCN-UPR","QUADRO","TT"): "UPRTT_Q",
        ("DPCN-UPR","IMPIEGAT","RE"): "UPRRE_I",
        ("DPSU-URO","QUADRO","RE"): "URORE_Q",
        ("DPSU-URO","IMPIEGAT","TP"): "UROTP_I",
        ("DPSU-URO","IMPIEGAT","TT"): "UROTT_I",
        ("DPSU-URO","OPERAIO","TT"): "UROTT_O",
        ("DPSU-URO","QUADRO","TT"): "UROTT_Q",
        ("DPSU-URO","IMPIEGAT","RE"): "URORE_I",
        ("DPCN-USU","QUADRO","RE"): "USURE_Q",
        ("DPCN-USU","IMPIEGAT","TP"): "USUTP_I",
        ("DPCN-USU","IMPIEGAT","TT"): "USUTT_I",
        ("DPCN-USU","OPERAIO","TT"): "USUTT_O",
        ("DPCN-USU","QUADRO","TT"): "USUTT_Q",
        ("DPCN-USU","IMPIEGAT","RE"): "USURE_I",
        ("DPNE-UTA","QUADRO","RE"): "UTARE_Q",
        ("DPNE-UTA","IMPIEGAT","TP"): "UTATP_I",
        ("DPNE-UTA","IMPIEGAT","TT"): "UTATT_I",
        ("DPNE-UTA","OPERAIO","TT"): "UTATT_O",
        ("DPNE-UTA","QUADRO","TT"): "UTATT_Q",
        ("DPNE-UTA","IMPIEGAT","RE"): "UTARE_I",
        ("DPNE-UTA","QUADRO","TP"): "UTATP_Q",
        ("DPNO-UTO","QUADRO","RE"): "UTORE_Q",
        ("DPNO-UTO","IMPIEGAT","TP"): "UTOTP_I",
        ("DPNO-UTO","IMPIEGAT","TT"): "UTOTT_I",
        ("DPNO-UTO","OPERAIO","TT"): "UTOTT_O",
        ("DPNO-UTO","QUADRO","TT"): "UTOTT_Q",
        ("DPNO-UTO","IMPIEGAT","RE"): "UTORE_I",
        ("DSSA-UVI","QUADRO","RE"): "UVIRE_Q",
        ("DSSA-UVI","IMPIEGAT","TP"): "UVITP_I",
        ("DSSA-UVI","IMPIEGAT","TT"): "UVITT_I",
        ("DSSA-UVI","OPERAIO","TT"): "UVITT_O",
        ("DSSA-UVI","QUADRO","TT"): "UVITT_Q",
        ("DSSA-UVI","IMPIEGAT","RE"): "UVIRE_I",
        ("DPNE-UVV","QUADRO","RE"): "UVVRE_Q",
        ("DPNE-UVV","IMPIEGAT","TP"): "UVVTP_I",
        ("DPNE-UVV","IMPIEGAT","TT"): "UVVTT_I",
        ("DPNE-UVV","OPERAIO","TT"): "UVVTT_O",
        ("DPNE-UVV","QUADRO","TT"): "UVVTT_Q",
        ("DPNE-UVV","IMPIEGAT","RE"): "UVVRE_I",
        ("DPCE-UAM","IMPIEGAT",None): "UAMTT_I",
        ("DPCE-UAM","OPERAIO",None): "UAMTT_O",
        ("DPCE-UAM","QUADRO",None): "UAMTT_Q",
        ("DPSU-UBA","IMPIEGAT",None): "UBATT_I",
        ("DPSU-UBA","OPERAIO",None): "UBATT_O",
        ("DPSU-UBA","QUADRO",None): "UBATT_Q",
        ("DPND-UBG","IMPIEGAT",None): "UBGTT_I",
        ("DPND-UBG","OPERAIO",None): "UBGTT_O",
        ("DPND-UBG","QUADRO",None): "UBGTT_Q",
        ("DPCN-UBO","IMPIEGAT",None): "UBOTT_I",
        ("DPCN-UBO","OPERAIO",None): "UBOTT_O",
        ("DPCN-UBO","QUADRO",None): "UBOTT_Q",
        ("DPSU-UBR","IMPIEGAT",None): "UBRTT_I",
        ("DPSU-UBR","OPERAIO",None): "UBRTT_O",
        ("DPSU-UBR","QUADRO",None): "UBRTT_Q",
        ("DPND-UBS","IMPIEGAT",None): "UBSTT_I",
        ("DPND-UBS","OPERAIO",None): "UBSTT_O",
        ("DPND-UBS","QUADRO",None): "UBSTT_Q",
        ("DPNE-UCA","QUADRO",None): "UCATT_Q",
        ("DPNE-UCA","IMPIEGAT",None): "UCATT_I",
        ("DPNE-UCA","OPERAIO",None): "UCATT_O",
        ("DPND-UCH","IMPIEGAT",None): "UCHTT_I",
        ("DPND-UCH","OPERAIO",None): "UCHTT_O",
        ("DPND-UCH","QUADRO",None): "UCHTT_Q",
        ("DSSA-UCO","IMPIEGAT",None): "UCOTT_I",
        ("DSSA-UCO","OPERAIO",None): "UCOTT_O",
        ("DSSA-UCO","QUADRO",None): "UCOTT_Q",
        ("DPND-UCS","IMPIEGAT",None): "UCSTT_I",
        ("DPND-UCS","OPERAIO",None): "UCSTT_O",
        ("DPND-UCS","QUADRO",None): "UCSTT_Q",
        ("DSSI-UCT","IMPIEGAT",None): "UCTTT_I",
        ("DSSI-UCT","OPERAIO",None): "UCTTT_O",
        ("DSSI-UCT","QUADRO",None): "UCTTT_Q",
        ("DPNE-UDU","IMPIEGAT",None): "UDUTT_I",
        ("DPNE-UDU","OPERAIO",None): "UDUTT_O",
        ("DPNE-UDU","QUADRO",None): "UDUTT_Q",
        ("DPSU-UFE","IMPIEGAT",None): "UFETT_I",
        ("DPSU-UFE","OPERAIO",None): "UFETT_O",
        ("DPSU-UFE","QUADRO",None): "UFETT_Q",
        ("DPCN-UFI","IMPIEGAT",None): "UFITT_I",
        ("DPCN-UFI","OPERAIO",None): "UFITT_O",
        ("DPCN-UFI","QUADRO",None): "UFITT_Q",
        ("DPSU-UFM","IMPIEGAT",None): "UFMTT_I",
        ("DPSU-UFM","OPERAIO",None): "UFMTT_O",
        ("DPSU-UFM","QUADRO",None): "UFMTT_Q",
        ("DPNE-UFV","IMPIEGAT",None): "UFVTT_I",
        ("DPNE-UFV","OPERAIO",None): "UFVTT_O",
        ("DPNE-UFV","QUADRO",None): "UFVTT_Q",
        ("DPNO-UGE","IMPIEGAT",None): "UGETT_I",
        ("DPNO-UGE","OPERAIO",None): "UGETT_O",
        ("DPNO-UGE","QUADRO",None): "UGETT_Q",
        ("DPND-UGO","IMPIEGAT",None): "UGOTT_I",
        ("DPND-UGO","OPERAIO",None): "UGOTT_O",
        ("DPND-UGO","QUADRO",None): "UGOTT_Q",
        ("DPCE-ULN","IMPIEGAT",None): "ULNTT_I",
        ("DPCE-ULN","OPERAIO",None): "ULNTT_O",
        ("DPCE-ULN","QUADRO",None): "ULNTT_Q",
        ("DPCE-ULS","IMPIEGAT",None): "ULSTT_I",
        ("DPCE-ULS","OPERAIO",None): "ULSTT_O",
        ("DPCE-ULS","QUADRO",None): "ULSTT_Q",
        ("DPCE-UMA","IMPIEGAT",None): "UMATT_I",
        ("DPCE-UMA","OPERAIO",None): "UMATT_O",
        ("DPCE-UMA","QUADRO",None): "UMATT_Q",
        ("DPCE-UMB","IMPIEGAT",None): "UMBTT_I",
        ("DPCE-UMB","OPERAIO",None): "UMBTT_O",
        ("DPCE-UMB","QUADRO",None): "UMBTT_Q",
        ("DPSU-UMD","IMPIEGAT",None): "UMDTT_I",
        ("DPSU-UMD","OPERAIO",None): "UMDTT_O",
        ("DPSU-UMD","QUADRO",None): "UMDTT_Q",
        ("DPNO-UNO","IMPIEGAT",None): "UNOTT_I",
        ("DPNO-UNO","OPERAIO",None): "UNOTT_O",
        ("DPNO-UNO","QUADRO",None): "UNOTT_Q",
        ("DSSI-UPA","IMPIEGAT",None): "UPATT_I",
        ("DSSI-UPA","OPERAIO",None): "UPATT_O",
        ("DSSI-UPA","QUADRO",None): "UPATT_Q",
        ("DPNO-UPM","IMPIEGAT",None): "UPMTT_I",
        ("DPNO-UPM","OPERAIO",None): "UPMTT_O",
        ("DPNO-UPM","QUADRO",None): "UPMTT_Q",
        ("DPCN-UPR","IMPIEGAT",None): "UPRTT_I",
        ("DPCN-UPR","OPERAIO",None): "UPRTT_O",
        ("DPCN-UPR","QUADRO",None): "UPRTT_Q",
        ("DPSU-URO","IMPIEGAT",None): "UROTT_I",
        ("DPSU-URO","OPERAIO",None): "UROTT_O",
        ("DPSU-URO","QUADRO",None): "UROTT_Q",
        ("DPCN-USU","IMPIEGAT",None): "USUTT_I",
        ("DPCN-USU","OPERAIO",None): "USUTT_O",
        ("DPCN-USU","QUADRO",None): "USUTT_Q",
        ("DPNE-UTA","IMPIEGAT",None): "UTATT_I",
        ("DPNE-UTA","OPERAIO",None): "UTATT_O",
        ("DPNE-UTA","QUADRO",None): "UTATT_Q",
        ("DPNO-UTO","IMPIEGAT",None): "UTOTT_I",
        ("DPNO-UTO","OPERAIO",None): "UTOTT_O",
        ("DPNO-UTO","QUADRO",None): "UTOTT_Q",
        ("DSSA-UVI","IMPIEGAT",None): "UVITT_I",
        ("DSSA-UVI","OPERAIO",None): "UVITT_O",
        ("DSSA-UVI","QUADRO",None): "UVITT_Q",
        ("DPNE-UVV","IMPIEGAT",None): "UVVTT_I",
        ("DPNE-UVV","OPERAIO",None): "UVVTT_O",
        ("DPNE-UVV","QUADRO",None): "UVVTT_Q",
        ("UI_RT2","OPERAIO",None): "RT2TT_O",
        ("UI_RT2","OPERAIO","TT"): "RT2TT_O",
        ("UI_GTG","IMPIEGAT",None): "TCG_I",
        ("UI_GTG","IMPIEGAT","TT"): "TCG_I",
        ("UI_GTG","OPERAIO",None): "TCG_O",
        ("UI_GTG","OPERAIO","TT"): "TCG_O"
    }
    #revisione performance
    wpMbo = wpSet.moveFirst()
    while wpMbo:
        x4titr = mapping.get(
            (wpMbo.getString("X4TITRIS"), wpMbo.getString("CRAFT"), None),
            None
        )
        if x4titr and x4titr not in cdl:
            cdl[x4titr] = True
        wpMbo = wpSet.moveNext()
    craftCons=singleCons.getString("CRAFT")
    pgroupConsSet=singleCons.getMboSet("$PERSONGROUPTEAM$","PERSONGROUPTEAM","respparty=:laborcode and persongroup in (select persongroup from persongroup where x4udb=1)")
    '''if pgroupConsSet.isEmpty():
        service.error("x3terna","ErrorMsg", ["Risorsa non censita in EAM"])
        #service.error("x3terna", "Risorsa non censita in EAM.")'''
    if not pgroupConsSet.isEmpty():
        pgroupCons=pgroupConsSet.getMbo(0).getString("PERSONGROUP")
        tipo=pgroupConsSet.getMbo(0).getString("X4TIPO")
        if pgroupConsSet.getMbo(0).isNull("X4TIPO"):
            tipo=None
    else:
        pgroupCons=None
        tipo=None
    udbCons=mapping.get((pgroupCons, craftCons,tipo), None)
    #service.error("TEST",str(udbCons))
    def evalCdl(titris,craft):
     return mapping.get((titris, craft,None), None)
     
    try:
        if (not udbCons in cdl) or create==True:
            
            workorder=singleCons.getMboSet("WORKORDER")
            workorderMbo=workorder.getMbo(0)
            tipospesa=workorderMbo.getString("X4TIPOSPESA")
            ntk=False
            odm=True
            
            
            # URL dell'endpoint
            url_odm = configUri.getProperty("mxe.int.sap.manage.odm")
            url_ntk = configUri.getProperty("mxe.int.sap.manage.ntk")
    
            # Ottenere l'istanza di MXServer
            mxserver = MXServer.getMXServer()
            userInfo = mxserver.getSystemUserInfo()
            
            
            
            if tipospesa=="CAPEX":
                url=url_ntk
            else:
                url=url_odm
                
            # Formattazione delle date
            date_format = SimpleDateFormat("yyyy-MM-dd")
            TARGSTARTDATE = date_format.format(workorderMbo.getDate("TARGSTARTDATE"))
            TARGCOMPDATE = date_format.format(workorderMbo.getDate("TARGCOMPDATE"))
            ESTDUR = workorderMbo.getDouble("ESTDUR")
                
            # Variabili messaggio
            WONUM = workorderMbo.getString("WONUM")
            if WONUM and len(WONUM) > 20:
                WONUM = WONUM[-20:]
            SITEID = workorderMbo.getString("SITEID")
            X4ODM = workorderMbo.getString("X4ODM")
            DESCRIPTION = str(workorderMbo.getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "").replace(">", "").replace("=", "").replace("à", "a").replace("è", "e").replace("é", "e").replace("ì", "i").replace("ò", "o").replace("ù", "u").replace("À", "A").replace("È", "E").replace("É", "E").replace("Ì", "I").replace("Ò", "O").replace("Ù", "U")
            X4NTK = workorderMbo.getString("X4NTK")
            OWNERGROUP= workorderMbo.getString("OWNERGROUP")
            JPNUM= workorderMbo.getString("JPNUM")
            X4TITRIS= workorderMbo.getString("X4TITRIS")
            
            NTKSET = workorderMbo.getMboSet("$X4NTK$", "X4NTK", "IDNTK=:X4NTK")
            if NTKSET.isEmpty():
                TIPONTK="Z_PM"
            else:
                TIPONTK = NTKSET.getMbo(0).getString("TIPO")
            NOPERAZIONE = workorderMbo.getString("X4NOPERAZIONE")
            
            JPNUMDESC = str(workorderMbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "").replace(">", "").replace("=", "").replace("à", "a").replace("è", "e").replace("é", "e") \
            .replace("ì", "i").replace("ò", "o").replace("ù", "u") \
            .replace("À", "A").replace("È", "E").replace("É", "E") \
            .replace("Ì", "I").replace("Ò", "O").replace("Ù", "U")
            IDSTANDARD = workorderMbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("X4ATTALL")
            
            #TARGSTARTDATE = mbo.getDate("TARGSTARTDATE")
            #TARGCOMPDATE = mbo.getDate("TARGCOMPDATE")
            WOPRIORITY = workorderMbo.getString("WOPRIORITY")
            X4TIPOATTIVITA = workorderMbo.getString("X4TIPOATTIVITA")
            if workorderMbo.isNull("X4TIPOORDINE") and workorderMbo.getString("X4TIPOOI")=="T09":
                #X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
                X4TIPOORDINE = "PMTG"
            elif workorderMbo.isNull("X4TIPOORDINE") and workorderMbo.getString("X4TIPOOI")=="T06":
                #X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
                X4TIPOORDINE = "PMT9"
            elif workorderMbo.isNull("X4TIPOORDINE"):
                X4TIPOORDINE = workorderMbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
            else:
                X4TIPOORDINE = workorderMbo.getString("X4TIPOORDINE")
            if tipospesa=="CAPEX":
                X4WBSCONT = workorderMbo.getString("X4WBE")
            else:
                if workorderMbo.getString("X4TIPOORDINE")=="PMT0" or workorderMbo.getString("X4TIPOORDINE")=="PMAC":
                    X4WBS=""
                else:
                    X4WBS = workorderMbo.getString("X4WBEPIAN")
                  
                if workorderMbo.getString("X4TIPOORDINE") in ["PMT0","PML9","PMTG"] or (workorderMbo.getString("X4TIPOATTIVITA")=="EPC" and workorderMbo.getString("X4TIPOSPESA")=="ANR"):
                    X4WBSCONT = workorderMbo.getString("X4WBE")
                else:
                    X4WBSCONT=""
                    
            X4SAPIDASSET = workorderMbo.getString("X4SAPIDASSET")
            classID=workorderMbo.getMboSet("ASSET").getMbo(0).getString("CLASSSTRUCTUREID")
            equipmentMboSet=MXServer.getMXServer().getMboSet("CLASSANCESTOR",MXServer.getMXServer().getSystemUserInfo())
            equipmentMboSet.setWhere("CLASSSTRUCTUREID='"+classID+"' and ancestorclassid='DISPOSITIVI E COMPONENTI'")
            equipmentMboSet.reset()
            if not equipmentMboSet.isEmpty():
                X4SAPIDASSET=X4SAPIDASSET[:-11]
            equipmentMboSet.close()
            X4NORDINEINTERNO = workorderMbo.getString("X4NORDINEINTERNO")
            ESTDUR = workorderMbo.getDouble("ESTDUR")
            isParent=workorderMbo.getBoolean("X4AGGREGATI")
            if isParent==True:
                wplaborSet=workorderMbo.getMboSet("$WPFIGLI$","WPLABOR","wonum in (select wonum from workorder where parent=:wonum and siteid=:siteid) ")
            else:
                wplaborSet=workorderMbo.getMboSet("WPLABOR")
            operazioni = []
            if not wplaborSet.isEmpty():
                if tipospesa=='OPEX' or tipospesa=='ANR':
                    first_cdl_set=workorderMbo.getMboSet("$X4ODMID$","X4ODM",":x4odm=idodm")
                    if not first_cdl_set.isEmpty():
                        first_cdl=first_cdl_set.getMbo(0).getString("CDLRESP")
                        '''if first_cdl_set.getMbo(0).getString("STATOSISTEMA")=="CHIU":
                            service.error("x3terna","ErrorMsg", ["L'OdM si trova in stato CHIUSO, impossibile procedere."] )'''
                    wplaborNewMbo = wplaborSet.moveFirst()
                    if first_cdl_set.isEmpty():
                        first_cdl=singleCons.getMboSet("WORKORDER").getMbo(0).getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
                    #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                    aggregati = {}
                    aggregati[udbCons] = {
                        'dauno': (singleCons.getDouble("X4ORETOT")),
                        'ntend': date_format.format(singleCons.getDate("FINISHDATE")),
                        'hrwrk': singleCons.getDouble("REGULARHRS")+singleCons.getDouble("X4OREUFF"),
                        'hrtra': singleCons.getDouble("X4ORETRASF")
                    }       
                    while wplaborNewMbo:
                        cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                        if cdltitris==None:
                            wplaborNewMbo = wplaborSet.moveNext()
                            continue
                        dauno = (wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))+(wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")+(singleCons.getDouble("X4ORETOT")))
                        ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
                        arbpl = cdltitris
                        hrwrk = wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")
                        hrtra = wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")
                        if hrwrk==0 or dauno==0:
                            wplaborNewMbo = wplaborSet.moveNext()
                            continue
                        # Se il CDL è già nel dizionario, somma i valori
                        if cdltitris in aggregati:
                            aggregati[cdltitris]['dauno'] += dauno
                            aggregati[cdltitris]['hrwrk'] += hrwrk
                            aggregati[cdltitris]['hrtra'] += hrtra
                        else:
                            aggregati[cdltitris] = {
                                'dauno': dauno,
                                'ntend': ntend,
                                'hrwrk': hrwrk,
                                'hrtra': hrtra
                            }
                        wplaborNewMbo = wplaborSet.moveNext()  # Passo al prossimo Mbo
        
                    for cdltitris, valori in aggregati.items():
                        hrwrk_str = ("{:.2f}".format(float(valori['hrwrk']))).rstrip('0').rstrip('.')
                        hrtra_str = ("{:.2f}".format(float(valori['hrtra']))).rstrip('0').rstrip('.')
                        operazione_tag = """<item>
                                <Arbpl>{}</Arbpl>
                                <Hrwrk>{}</Hrwrk>
                                <Hrtra>{}</Hrtra>
                              </item>""".format(
                            cdltitris,
                            hrwrk_str,
                            hrtra_str
                        )
                        operazioni.append(operazione_tag)
                        
        
                    # Unisco tutte le operazioni in un unico blocco XML
                    operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
                    #service.error("TEST",operation_xml)
                elif tipospesa=='CAPEX':
                    wplaborNewMbo = wplaborSet.moveFirst()
                    first_cdl=singleCons.getMboSet("WORKORDER").getMbo(0).getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
                    aggregati_capex = {}
                    aggregati_capex[udbCons] = {
                        'dauno': (singleCons.getDouble("X4ORETOT")),
                        'hrwrk': singleCons.getDouble("REGULARHRS")+singleCons.getDouble("X4OREUFF"),
                        'hrtra': singleCons.getDouble("X4ORETRASF")
                    }
                    while wplaborNewMbo:
                        cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                        if cdltitris==None:
                            wplaborNewMbo = wplaborSet.moveNext()
                            continue
                        dauno = (wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY"))+(wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))+(singleCons.getDouble("X4ORETOT"))
                        ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
                        ntstart = date_format.format(wplaborNewMbo.getDate("X4DATAINIZIORISORSA"))
                        arbpl = cdltitris
                        hrwrk = wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")
                        hrtra = wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")
                        if hrwrk==0 or dauno==0:
                            wplaborNewMbo = wplaborSet.moveNext()
                            continue
        
                        # Se il CDL è già nel dizionario, somma i valori
                        if cdltitris in aggregati_capex:
                            aggregati_capex[cdltitris]['dauno'] += dauno
                            aggregati_capex[cdltitris]['hrwrk'] += hrwrk
                            aggregati_capex[cdltitris]['hrtra'] += hrtra
                        else:
                            aggregati_capex[cdltitris] = {
                                'dauno': dauno,
                                'hrwrk': hrwrk,
                                'hrtra': hrtra
                            }
                        wplaborNewMbo = wplaborSet.moveNext()  # Passo al prossimo Mbo
                    for cdltitris, valori in aggregati_capex.items():
                        hrwrk_str = ("{:.2f}".format(float(valori['hrwrk']))).rstrip('0').rstrip('.')
                        hrtra_str = ("{:.2f}".format(float(valori['hrtra']))).rstrip('0').rstrip('.')
                        operazione_tag = """
                                  <item>
                                    <Cdl>{}</Cdl>
                                    <Orelavoro>{}</Orelavoro>
                                    <Oretrasferimento>{}</Oretrasferimento>
                                  </item>
                                """.format(cdltitris, hrwrk_str, hrtra_str)
                        operazioni.append(operazione_tag)
                # Unisco tutte le operazioni in un unico blocco XML
                    operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
            elif wplaborSet.isEmpty():
                if tipospesa == 'OPEX' or tipospesa == 'ANR':
                    #wplaborNewMbo = wplaborSet.moveFirst()
                    first_cdl=singleCons.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
                    dauno = workorderMbo.getDouble("ESTDUR")
                    ntend = date_format.format(workorderMbo.getDate("TARGCOMPDATE"))
                    hrwrk = singleCons.getDouble("REGULARHRS")+singleCons.getDouble("X4OREUFF")
                    hrtra = singleCons.getDouble("X4ORETRASF")
                    
                    hrwrk_str = ("{:.2f}".format(float(hrwrk))).rstrip('0').rstrip('.')
                    hrtra_str = ("{:.2f}".format(float(hrtra))).rstrip('0').rstrip('.')
                    
                    operazione_tag = """
                              <item>
                                <Arbpl>{}</Arbpl>
                                <Hrwrk>{}</Hrwrk>
                                <Hrtra>{}</Hrtra>
                              </item>
                            """.format(
                            udbCons,
                            hrwrk_str,
                            hrtra_str
                        )
                    operazioni.append(operazione_tag)
                    operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
                elif tipospesa=='CAPEX':
                    first_cdl=singleCons.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
                    dauno = workorderMbo.getDouble("ESTDUR")
                    ntend = date_format.format(workorderMbo.getDate("TARGCOMPDATE"))
                    ntstart = date_format.format(workorderMbo.getDate("TARGSTARTDATE"))
                    arbpl = first_cdl
                    hrwrk = singleCons.getDouble("REGULARHRS")+singleCons.getDouble("X4OREUFF")
                    hrtra = singleCons.getDouble("X4ORETRASF")
                    hrwrk_str = ("{:.2f}".format(float(hrwrk))).rstrip('0').rstrip('.')
                    hrtra_str = ("{:.2f}".format(float(hrtra))).rstrip('0').rstrip('.')
                    operazione_tag = """
                              <item>
                                <Cdl>{}</Cdl>
                                <Orelavoro>{}</Orelavoro>
                                <Oretrasferimento>{}</Oretrasferimento>
                              </item>
                            """.format(udbCons, hrwrk_str, hrtra_str)
                    operazioni.append(operazione_tag)
                    # Unisco tutte le operazioni in un unico blocco XML
                    operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
            ORELAVORO = 0
            ORETRASFERIMENTO = 0
            DURATA = 0
            
            wplaborMbo = wplaborSet.moveFirst()
            
            while wplaborMbo:
                ORELAVORO += wplaborMbo.getDouble("LABORHRS") * wplaborMbo.getDouble("QUANTITY")
                ORETRASFERIMENTO += wplaborMbo.getDouble("X4ORETRASF")
                wplaborMbo = wplaborSet.moveNext()
            
            DURATA = ORELAVORO + ORETRASFERIMENTO
            wplaborSet.close()

            '''if ORELAVORO==0 and ORETRASFERIMENTO==0:
                service.error("x3terna","ErrorMsg", ["Si prega di compilare almeno una riga di pianificazione con delle ore di esecuzione."] )'''
            CDL = workorderMbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
            if tipospesa=="CAPEX":
                #XML da inviare nella richiesta POST per NTW                xml_data ="""<?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                    <soap:Body>
                        <tns:ZrichiestaCreazioneNtw xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
                          <NtwMaintain>
                            <Tipontw>{}</Tipontw>
                            <Network>{}</Network>
                            <Wbs>{}</Wbs>
                            <Descrizione>{}</Descrizione>
                            <Centrolavoro>{}</Centrolavoro>
                            <Operazioni>
                            <item>
                                <Istanza>{}</Istanza>
                                <Operazione>{}</Operazione>
                                <Idattivita>{}</Idattivita>
                                <Sede>{}</Sede>
                                <Iniziocardine>{}</Iniziocardine>
                                <Finecardine>{}</Finecardine>
                                <Datatarget>{}</Datatarget>
                                <Durata>{}</Durata>
                                {}
                                <Sedi>
                                  <item>
                                    <Tplnr>{}</Tplnr>
                                  </item>
                                </Sedi>
                              </item>
                          </Operazioni>
                          </NtwMaintain>
                          <Trtyp>V</Trtyp>
                        </tns:ZrichiestaCreazioneNtw>
                    </soap:Body>
                </soap:Envelope>""".format(TIPONTK,X4NTK,X4WBSCONT,JPNUMDESC[:40],first_cdl,WONUM, NOPERAZIONE, JPNUM, X4SAPIDASSET,TARGSTARTDATE,TARGCOMPDATE,TARGCOMPDATE,str(("{:.1f}".format(float(ESTDUR))).rstrip('0').rstrip('.')),operation_xml,X4SAPIDASSET)
                
            else:
                # XML da inviare nella richiesta POST per OdM
                if create==False:
                    xml_data = """<?xml version="1.0" encoding="utf-8"?>
                    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                      <soap:Header>
                        <wsse:Security soap:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"/>
                      </soap:Header>
                      <soap:Body>
                        <tns:Zfmpms4EamOdmMaintain xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
                          <IOdmMaintain>
                            <Instance>{}</Instance>
                            <IdGeocall>{}</IdGeocall>
                            <Aufnr>{}</Aufnr>
                            <Auftext>{}</Auftext>
                            <Gstrp>{}</Gstrp>
                            <Gltrp>{}</Gltrp>
                            <Priok>2</Priok>
                            <Ilart>{}</Ilart>
                            <Auart>{}</Auart>
                            <Wbs>{}</Wbs>
                            <Functloc>
                              <item>
                                <Tplnr>{}</Tplnr>
                              </item>
                            </Functloc>
                            <Tplnr>{}</Tplnr>
                            <CdlResp>{}</CdlResp>
                            <Costs>
                              <Aufnr>{}</Aufnr>
                              <Wbs>{}</Wbs>
                            </Costs>
                            <Operation>
                                <item>
                                <Dauno>{}</Dauno>
                                    <Ntend>{}</Ntend>
                                    {}
                                </item>
                                </Operation>
                          </IOdmMaintain>
                          <Trtyp>V</Trtyp>
                        </tns:Zfmpms4EamOdmMaintain>
                      </soap:Body>
                    </soap:Envelope>""".format(WONUM,IDSTANDARD,X4ODM,DESCRIPTION[:40], TARGSTARTDATE, TARGCOMPDATE, X4TIPOATTIVITA, X4TIPOORDINE, X4WBS, X4SAPIDASSET,X4SAPIDASSET,first_cdl,X4NORDINEINTERNO,X4WBSCONT,("{:.2f}".format(float(dauno))).rstrip('0').rstrip('.'),ntend,operation_xml)
                else:
                    xml_data = """<?xml version="1.0" encoding="utf-8"?>
                        <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                          <soap:Header>
                            <wsse:Security soap:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"/>
                          </soap:Header>
                          <soap:Body>
                            <tns:Zfmpms4EamOdmMaintain xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
                              <IOdmMaintain>
                                <Instance>{}</Instance>
                                <IdGeocall>{}</IdGeocall>
                                <Aufnr>{}</Aufnr>
                                <Auftext>{}</Auftext>
                                <Gstrp>{}</Gstrp>
                                <Gltrp>{}</Gltrp>
                                <Priok>2</Priok>
                                <Ilart>{}</Ilart>
                                <Auart>{}</Auart>
                                <Wbs>{}</Wbs>
                                <Functloc>
                                  <item>
                                    <Tplnr>{}</Tplnr>
                                  </item>
                                </Functloc>
                                <Tplnr>{}</Tplnr>
                                <CdlResp>{}</CdlResp>
                                <Costs>
                                  <Aufnr>{}</Aufnr>
                                  <Wbs>{}</Wbs>
                                </Costs>
                                <Operation>
                                <item>
                                <Dauno>{}</Dauno>
                                    <Ntend>{}</Ntend>
                                    {}
                                </item>
                                </Operation>
                              </IOdmMaintain>
                              <Trtyp>H</Trtyp>
                            </tns:Zfmpms4EamOdmMaintain>
                          </soap:Body>
                        </soap:Envelope>""".format(WONUM,IDSTANDARD,X4ODM,DESCRIPTION[:40], TARGSTARTDATE, TARGCOMPDATE, X4TIPOATTIVITA, X4TIPOORDINE, X4WBS, X4SAPIDASSET,X4SAPIDASSET,first_cdl,X4NORDINEINTERNO,X4WBSCONT,("{:.2f}".format(float(dauno))).rstrip('0').rstrip('.'),ntend,operation_xml)
    
            #service.error("TEST",xml_data)
            # Funzione per eseguire la richiesta POST e restituire la risposta
            def send_post_request(url, xml_data):
                url_object = URL(url)
                connection = url_object.openConnection()
                connection.setDoOutput(True)
                connection.setRequestMethod("POST")
                connection.setRequestProperty("Content-Type", "application/xml")
                connection.setRequestProperty('Authorization', 'Bearer ' + token)
                # Creazione manuale dell'header di autenticazione
                #auth_string = str(username)+":"+str(password)
                #service.error("TEST",str(auth_string))
                #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"))
                #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"));
                #connection.setRequestProperty("Authorization", "Basic {}".format(auth_encoded))
            
                # Scrivere il body della richiesta
                writer = OutputStreamWriter(connection.getOutputStream())
                writer.write(xml_data)
                writer.flush()
                writer.close()
            
                # Leggere la risposta
                response_code = connection.getResponseCode()
                if response_code == HttpURLConnection.HTTP_OK:
                    reader = BufferedReader(InputStreamReader(connection.getInputStream()))
                    response = ""
                    line = reader.readLine()
                    while line:
                        response += line
                        line = reader.readLine()
                    reader.close()
                    return response
    
                    
            #Prendo valore di OdM        
            def extract_odm_from_response(response):
                # Parse XML response
                factory = DocumentBuilderFactory.newInstance()
                builder = factory.newDocumentBuilder()
                isr = InputSource(StringReader(response))
                doc = builder.parse(isr)
                # Find <OdM> tag
                errore=True
                resperrore=None
                odm_elements = doc.getElementsByTagName("Type")
                if odm_elements:
                    odm_text = odm_elements.item(0).getTextContent().strip()
                    if odm_text:
                        odm_text = odm_elements.item(0).getTextContent().strip()
                        if odm_text=="E":
                            errore=True
                            odm_elements = doc.getElementsByTagName("Message")
                            if odm_elements:
                                resperrore= odm_elements.item(0).getTextContent().strip()
                        else:
                            resperrore= doc.getElementsByTagName("Aufnr").item(0).getTextContent()
                            errore=False
                return resperrore, errore
                        
            # Funzione per aggiornare il workorder (set del campo ODM)
            def update_mbo_with_response_odm(mbo, odm):
                workorderMbo.setValue("X4ODM", odm, 11L)
                checkOdL.save()
                '''
                woSet = MXServer.getMXServer().getMboSet("WORKORDER", MXServer.getMXServer().getSystemUserInfo())
                woSet.setWhere("wonum='{}' and siteid='{}'".format(WONUM, SITEID))
                woSet.reset()
                if not woSet.isEmpty():
                    woMbo = woSet.getMbo(0)
                    woMbo.setValue("X4ODM", odm, 11L)
                    woSet.save()   # salva solo sul set separato
                woSet.close()'''
                #service.error("x3terna", "Odm ricevuto e aggiornato.")
                
            #Prendo valore di NTK      
            def extract_ntk_from_response(response):
                # Parse XML response
                factory = DocumentBuilderFactory.newInstance()
                builder = factory.newDocumentBuilder()
                isr = InputSource(StringReader(response))
                doc = builder.parse(isr)
                errore=True
                resperrore=None
                ntk_elements = doc.getElementsByTagName("Network")
                if ntk_elements:
                    ntk_text = ntk_elements.item(0).getTextContent()
                    if not ntk_text:
                        #ntk_text = ntk_elements.item(0).getTextContent().strip()
                        message_elements = doc.getElementsByTagName("Message")
                        if message_elements:
                            resperrore = message_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
                            if resperrore:
                                errore = True
                                return resperrore, None, errore
                    
                    #op_elements = doc.getElementsByTagName("Operazione")
                    #op_text = op_elements.item(1).getTextContent().strip()
                    
                    if ntk_text:
                        ntk_text = ntk_elements.item(0).getTextContent().strip()
                        errore = False
                        return ntk_elements.item(0).getTextContent(), "NA", errore
                    
                    else:
                        #service.error("Errore nella creazione della Network  ",(resperrore))
                        message_elements = doc.getElementsByTagName("Message")
                        resperrore = message_elements.item(2).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
                       
                        errore = True
                        return resperrore, None, errore
                            
            # Invocazione delle funzioni
            #service.error("TEST",str(xml_data))
            response = send_post_request(url, xml_data)
            #service.error("TEST",str(response.encode("utf-8")))
            if response:
                if tipospesa=='CAPEX':
                    ntk,noperazione,errore=extract_ntk_from_response(response)

                else:       
                    odm,errore = extract_odm_from_response(response)
                    #service.error("TEST",odm)
                    woSet = MXServer.getMXServer().getMboSet("WORKORDER", MXServer.getMXServer().getSystemUserInfo())
                    woSet.setWhere("wonum='{}' and siteid='{}'".format(singleCons.getString("REFWO"), singleCons.getString("SITEID")))
                    woSet.reset()
                    if create==True:
                        if len(odm)<20 and not woSet.isEmpty():
                            woMbo = woSet.getMbo(0)
                            woMbo.setValue("X4ODM", odm, 11L)
                            woSet.save()
                        elif len(odm)>20 and odm.startswith("Impossibile procedere, IDF Istanza") and woSet.getMbo(0).isNull("X4ODM"):
                            singleCons.setValue("GENAPPRSERVRECEIPT",0,11L)
                            singleCons.setValue("X4CHECKSAP","KO",11L)
                            singleCons.setValue("X4ESITOSAPDESC",odm,11L)

        else:
            if singleCons.getMboSet("WORKORDER").getMbo(0).getString("X4TIPOSPESA")=='OPEX' or singleCons.getMboSet("WORKORDER").getMbo(0).getString("X4TIPOSPESA")=='ANR':
                    first_cdl_set=singleCons.getMboSet("WORKORDER").getMbo(0).getMboSet("$X4ODMID$","X4ODM",":x4odm=idodm")
                    if not first_cdl_set.isEmpty():
                        first_cdl=first_cdl_set.getMbo(0).getString("CDLRESP")
                        '''if first_cdl_set.getMbo(0).getString("STATOSISTEMA")=="CHIU":
                            service.error("x3terna","ErrorMsg", ["L'OdM si trova in stato CHIUSO, impossibile procedere."] )'''
    except Exception as e:
        logger.info("Errore invio SAP (non bloccante): " + str(e))
            
    finally:
        if odm:
            if isinstance(odm, str):
                if (not odm.startswith("Impossibile procedere, IDF Istanza")) or not woSet.getMbo(0).isNull("X4ODM"):
                    singleCons.setValue("GENAPPRSERVRECEIPT",1,11L)
                else:
                    singleCons.setValue("GENAPPRSERVRECEIPT",0,11L)
        else:
            singleCons.setValue("GENAPPRSERVRECEIPT",1,11L)
        #if singleCons.getMboValue("GENAPPRSERVRECEIPT").getInitialValue().asBoolean()==False:
        #PublishChannelCache.getInstance().getPublishChannel("X4CONSSAP").publish(singleCons, False);
        singleConsSet.save()
    
mbo.getThisMboSet().save()"
OSIN.X4LOADER,,Draft,"from psdi.server import MXServer
from com.ibm.json.java import JSONObject, JSONArray
from psdi.mbo import SqlFormat

# =============================================================================
# CONFIGURAZIONE
# =============================================================================

IGNORED_FIELDS = set([
    "USERID",
    "BODY",
    "ACTION",
    "_ACTION",
    "CHANGEBY",
    "CHANGEDATE",
    "ROWSTAMP",
    "_ROWSTAMP",
    "LANGCODE",
    "ORGID"
])


# Oggetto principale.
# Nel JSON flat di MxLoader normalmente i campi arrivano senza prefisso.
MAIN_OBJECTS = set([
    "WORKORDER"
])


# Child object.
# Se nel JSON i child arrivano come oggetti/array annidati, vengono prefissati.
CHILD_OBJECTS = set([
    "WPLABOR",
    "WPSERVICE"
])


COMMON_OPTIONAL_FIELDS = set([
    # Optional validi per tutte le combinazioni, se servono.
    # Meglio usarli poco.
])

COMBOS_SKIP_WONUM_EXISTENCE_CHECK = set([
    "MXLOADER_CREAZIONE_BASE",
    "MXLOADER_CREAZIONE_CON_POLICY",
    "MXLOADER_CREAZIONE_CON_POLICY_MOD",
    "COLWO_NEW_SG_ACN_V2"
])
REQUIRED_COMBINATIONS = [
    {
        "name": "MXLOADER_COIN",
        "required": [
            "WONUM",
            "SITEID",
            "X4TIPOSPESA",
            "X4COIN",
        ],
        "optional": [
            "X4WBS",
            "X4WBE",
            "IDCLUSTER",
            "OWNERGROUP"
        ]
    },
    {
        "name": "MXLOADER_PMI",
        "required": [
            "WONUM",
            "SITEID",
            "X4PMI"
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_DATIODL",
        "required": [
            "WONUM",
            "SITEID",
            "ESTDUR",
            "X4BURKS",
            "X4OUTSOURCING"
        ],
        "optional": [
            "X4NATURALAVORO",
            "X4NOTE",
            "IDCLUSTER",
            "X4TTRASF",
            "JPNUM"
        ]
    },
    {
        "name": "MXLOADER_RISEST",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "WPSERVICE.ITEMNUM",
            "WPSERVICE.ITEMQTY",
            "WPSERVICE.LINECOST",
            "WPSERVICE.X4DATAINIZIORISORSA",
            "WPSERVICE.X4DATAFINERISORSA"
        ],
        "optional": [
            "WPSERVICE.WPITEMID",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_RISINT",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "ESTDUR",
            "WPLABOR.CRAFT",
            "WPLABOR.QUANTITY",
            "WPLABOR.X4TITRIS",
            "WPLABOR.LABORHRS",
            "WPLABOR.X4DATAINIZIORISORSA",
            "WPLABOR.X4DATAFINERISORSA"
        ],
        "optional": [
            "WPLABOR.WPLABORUID",
            "WPLABOR.X4TITRIS",
            "WPLABOR.X4ORETRASF",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_ODL_CHIUSI_V2",
        "required": [
            "WONUM",
            "SITEID",
            "DESCRIPTION",
            "X4AGGREGATI",
            "STATUS",
            "ASSETNUM",
            "JPNUM",
            "X4FISICOPLAN",
            "X4FISICO",
            "OWNERGROUP",
            "X4TIPOSPESA",
            "X4COIN",
            "X4BURKS",
            "X4PERIMETROINT",
            "X4TIPOODL",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "ACTFINISH",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "X4POLICY",
            "X4SAPIDASSET",
            "PARENT",
            "X4POLICYID",
            "X4WBE",
            "WOEQ9",
            "X4WBS",
            "X4COIN",
            "X4PRIMAELEGGIBILITA",
            "IDCLUSTER",
            "REPORTDATE"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_BASE",
        "required": [
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "X4TIPOSPESA",
            "X4COIN",
            "X4PMI",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT",
            "X4BURKS",
            "X4OUTSOURCING"
        ],
        "optional": [
            "WONUM",
            "X4SOTTOPROGRAMMA",
            "X4NATURALAVORO",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_CON_POLICY",
        "required": [
            "WONUM",
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "X4TIPOSPESA",
            "X4COIN",
            "X4PMI",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT",
            "X4BURKS",
            "X4OUTSOURCING"
        ],
        "optional": [
            "PARENT",
            "X4POLICY",
            "X4POLICYID",
            "X4DATAMODELLO",
            "X4TRONCONUM",
            "X4AGGREGATI",
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_CREAZIONE_CON_POLICY_MOD",
        "required": [
            "WONUM",
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "PARENT",
            "X4COIN",
            "X4POLICY",
            "X4POLICYID",
            "X4SAPIDASSET",
            "X4DATAMODELLO",
            "X4TRONCONUM",
            "X4AGGREGATI",
            "IDCLUSTER",
            "X4PRIMAELEGGIBILITA",
            "X4WBS",
            "X4WBE"
        ]
    },
    {
        "name": "MXLOADER_DATE",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "IDCLUSTER",
            "REPORTDATE"
        ]
    },
    {
        "name": "MXLOADER_DATEPADRE",
        "required": [
            "WONUM",
            "SITEID",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_SERB",
        "required": [
            "WONUM",
            "SITEID",
            "X4FISICOPLAN"
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "COLWO_NEW_SG_ACN_V2",
        "required": [
            "WONUM",
            "ASSETNUM",
            "SITEID",
            "JPNUM",
            "TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "X4POLICYID",
            "PARENT",
            "X4SAPIDASSET",
            "X4AGGREGATI",
            "X4TRONCONUM",
            "X4DATAMODELLO",
            "X4POLICY",
            "X4POLICYID",
            "IDCLUSTER",
            "X4PRIMAELEGGIBILITA",
            "REPORTDATE"
        ]
    },
    {
        "name": "MXLOADER_IMPORT_MISURE",
        "required": [
            "ASSETNUM",
            "IDMISURA_EXT",
            "IDMISURA",
            "ULTIMARILEVAZIONE",
            "PRIMARILEVAZIONE",
            "VALORE",
            "SITEID"
        ],
        "optional": [
            # Popola qui
        ]
    },
    {
        "name": "MXLOADER_DIFFOR_INTERF",
        "required": [
            "IDDIFFORMITA_EXT",
            "ASSETNUM",
            "STATUS",
            "PRIMARILEVAZIONE",
            "ULTIMARILEVAZIONE",
            "SITEID"
        ],
        "optional": [
            "DESCRIPTION",
            "FASE",
            "GRAVITA",
            "IDMISURA",
            "VALMISURA"
        ]
    },
    {
        "name": "GIN",
        "required": [
            "WONUM",
            "SITEID",
            "REASONFORCHANGE"
        ],
        "optional": [
            "IDCLUSTER"
        ]
    },
    {
        "name": "MXLOADER_4in1",
        "required": [
            "WONUM",
            "SITEID",
            "X4TIPOSPESA",
            "X4COIN",
			"X4PMI",
			"ESTDUR",
            "X4BURKS",
            "X4OUTSOURCING",
			"TARGSTARTDATE",
            "TARGCOMPDATE",
            "SNECONSTRAINT",
            "FNLCONSTRAINT"
        ],
        "optional": [
            "X4WBS",
            "X4WBE",
            "IDCLUSTER",
            "OWNERGROUP",
			"X4NATURALAVORO",
            "X4NOTE",
            "IDCLUSTER",
            "X4TTRASF",
            "JPNUM",
			"REPORTDATE"
        ]
    }
]


# =============================================================================
# UTILITY
# =============================================================================

def get_json_value_case_insensitive(json_obj, field_name):
    if json_obj is None:
        return None

    target = normalize(field_name)

    keys = json_obj.keySet().iterator()

    while keys.hasNext():
        key = keys.next()

        if normalize(key) == target:
            return json_obj.get(key)

    return None


def get_json_string_value(json_obj, field_name):
    value = get_json_value_case_insensitive(json_obj, field_name)

    if is_blank(value):
        return ""

    return str(value).strip()



def normalize(value):
    if value is None:
        return ""

    return str(value).strip().upper()


def is_blank(value):
    if value is None:
        return True

    s = str(value).strip()

    if s == "":
        return True

    # Se vuoi considerare null string come vuoto
    if s.upper() == "NULL":
        return True

    return False


def safe_preview(value, max_len):
    if value is None:
        return "None"

    try:
        s = unicode(value)
    except:
        s = str(value)

    s = s.replace("\r", "\\r")
    s = s.replace("\n", "\\n")
    s = s.replace("\t", "\\t")

    if len(s) > max_len:
        return s[:max_len] + "..."

    return s


def clean_body_for_json(value):
    if value is None:
        return ""

    try:
        s = unicode(value)
    except:
        s = str(value)

    if s.startswith(u"\ufeff"):
        s = s[1:]

    s = s.strip()

    # Se per qualche motivo c'è testo prima del JSON, taglia dal primo {
    first_json_char = s.find("{")

    if first_json_char > 0:
        s = s[first_json_char:]

    return s


def normalize_field_list(values):
    result = set()

    for value in values:
        norm = normalize(value)

        if norm:
            result.add(norm)

    return result


def get_java_class_name(value):
    if value is None:
        return ""

    try:
        return value.getClass().getName()
    except:
        return ""


def is_json_object(value):
    if value is None:
        return False

    try:
        if isinstance(value, JSONObject):
            return True
    except:
        pass

    return get_java_class_name(value) == "com.ibm.json.java.JSONObject"


def is_json_array(value):
    if value is None:
        return False

    try:
        if isinstance(value, JSONArray):
            return True
    except:
        pass

    return get_java_class_name(value) == "com.ibm.json.java.JSONArray"

def build_field_name(current_object, attr_name):
    attr_name = normalize(attr_name)

    if "." in attr_name:
        return attr_name

    if current_object is None:
        return attr_name

    current_object = normalize(current_object)

    if current_object in MAIN_OBJECTS:
        return attr_name

    if current_object in CHILD_OBJECTS:
        return current_object + "." + attr_name

    return attr_name


# =============================================================================
# PARSING JSON
# =============================================================================

def parse_json(json_string):
    return JSONObject.parse(json_string)


def extract_populated_fields_from_json(json_string):
    populated_fields = set()

    root = parse_json(json_string)

    def walk_json_object(json_obj, current_object):
        keys = json_obj.keySet().iterator()

        while keys.hasNext():
            key = keys.next()
            raw_key = str(key)
            norm_key = normalize(raw_key)

            value = json_obj.get(key)

            if norm_key in IGNORED_FIELDS:
                continue

            # Caso child object annidato:
            # "wplabor": { ... }
            # oppure:
            # "wplabor": [ { ... }, { ... } ]
            if norm_key in CHILD_OBJECTS or norm_key in MAIN_OBJECTS:
                if is_json_object(value):
                    walk_json_object(value, norm_key)
                    continue

                if is_json_array(value):
                    for i in range(value.size()):
                        item = value.get(i)

                        if is_json_object(item):
                            walk_json_object(item, norm_key)

                    continue

            # Caso oggetto JSON generico annidato
            if is_json_object(value):
                walk_json_object(value, current_object)
                continue

            # Caso array generico
            if is_json_array(value):
                for i in range(value.size()):
                    item = value.get(i)

                    if is_json_object(item):
                        walk_json_object(item, current_object)
                    else:
                        if not is_blank(item):
                            field_name = build_field_name(current_object, norm_key)

                            if field_name not in IGNORED_FIELDS:
                                populated_fields.add(field_name)

                continue

            # Caso campo semplice
            if not is_blank(value):
                field_name = build_field_name(current_object, norm_key)

                if field_name not in IGNORED_FIELDS:
                    populated_fields.add(field_name)

    walk_json_object(root, None)

    return populated_fields





# =============================================================================
# SCRIPT OSIN
# =============================================================================

def beforeMboData(ctx):
    # =============================================================================
    # VALIDAZIONE
    # =============================================================================
    def validate_aln_domain_value(json_body, field_name, domain_id, userInfo):
        root = parse_json(json_body)
    
        value = get_json_value_case_insensitive(root, field_name)
    
        # Se il campo non è presente o è vuoto, lascio gestire il required template.
        if is_blank(value):
            return
    
        value_str = str(value).strip()
    
        alnSet = None
    
        try:
            alnSet = MXServer.getMXServer().getMboSet("ALNDOMAIN", userInfo)
    
            sqlf = SqlFormat(userInfo, "domainid = :1 and value = :2")
            sqlf.setObject(1, "ALNDOMAIN", "DOMAINID", domain_id)
            sqlf.setObject(2, "ALNDOMAIN", "VALUE", value_str)
    
            alnSet.setWhere(sqlf.format())
            alnSet.reset()
    
            if alnSet.getMbo(0) is None:
                ctx.error("x3terna", "ErrorMsg", [
                    field_name + " non valido: '" + value_str + "'. Il valore non risulta ammesso dal dominio."
                ])
    
        finally:
            if alnSet is not None:
                alnSet.close()
    
    def is_true_outsourcing_value(value):
        if is_blank(value):
            return False
    
        v = normalize(value)
    
        return v in ("1", "TRUE", "YES", "Y")
    
    def validate_naturalavoro_required_if_outsourcing(json_body, combo_name):
        combo_name_norm = normalize(combo_name)
    
        combos_with_outsourcing = set([
            "MXLOADER_DATIODL",
            "MXLOADER_CREAZIONE_BASE",
            "MXLOADER_CREAZIONE_CON_POLICY"
        ])
    
        if combo_name_norm not in combos_with_outsourcing:
            return
    
        root = parse_json(json_body)
    
        outsourcing = get_json_value_case_insensitive(root, "X4OUTSOURCING")
    
        if not is_true_outsourcing_value(outsourcing):
            return
    
        natura_lavoro = get_json_value_case_insensitive(root, "X4NATURALAVORO")
    
        if is_blank(natura_lavoro):
            ctx.error("x3terna", "ErrorMsg", [
                "X4NATURALAVORO e obbligatorio quando X4OUTSOURCING e valorizzato a 1, true, Yes o Y."
            ])
    def workorder_exists(wonum, siteid, userInfo):
        woSet = None
    
        try:
            woSet = MXServer.getMXServer().getMboSet("WORKORDER", userInfo)
    
            if not is_blank(siteid):
                sqlf = SqlFormat(userInfo, "wonum = :1 and siteid = :2")
                sqlf.setObject(1, "WORKORDER", "WONUM", wonum)
                sqlf.setObject(2, "WORKORDER", "SITEID", siteid)
            else:
                sqlf = SqlFormat(userInfo, "wonum = :1")
                sqlf.setObject(1, "WORKORDER", "WONUM", wonum)
    
            woSet.setWhere(sqlf.format())
            woSet.reset()    
            return woSet.getMbo(0) is not None
    
        finally:
            if woSet is not None:
                woSet.close()
    
    def validate_serbatoio_if_x4fisicoplan(json_body, combo_name, userInfo):
        if normalize(combo_name) != "MXLOADER_SERB":
            return
    
        root = parse_json(json_body)
    
        wonum = get_json_string_value(root, "WONUM")
        siteid = get_json_string_value(root, "SITEID")
        x4fisicoplan = get_json_value_case_insensitive(root, "X4FISICOPLAN")
        
        try:
            fisicoplan_value = float(str(x4fisicoplan).strip().replace(",", "."))
        except:
            ctx.error("x3terna", "ErrorMsg", [
                "X4FISICOPLAN deve essere un valore numerico maggiore di 0."
            ])
            return
    
        if fisicoplan_value < 0:
            ctx.error("x3terna", "ErrorMsg", [
                "X4FISICOPLAN deve essere maggiore di 0."
            ])
            return
    
        woSet = None
    
        try:
            woSet = MXServer.getMXServer().getMboSet("WORKORDER", userInfo)
    
            sqlf = SqlFormat(userInfo, "wonum = :1 and siteid = :2")
            sqlf.setObject(1, "WORKORDER", "WONUM", wonum)
            sqlf.setObject(2, "WORKORDER", "SITEID", siteid)
    
            woSet.setWhere(sqlf.format())
            woSet.reset()
    
            wo = woSet.getMbo(0)
    
            if wo is not None and not wo.getBoolean("X4SERBATOIO"):
                ctx.error("x3terna", "ErrorMsg", [
                    "X4FISICOPLAN modificabile solo per ODL con X4SERBATOIO = 1."
                ])
    
        finally:
            if woSet is not None:
                woSet.close()
    
    def validate_wonum_exists_if_needed(json_body, combo_name, userInfo):
        combo_name_norm = normalize(combo_name)
    
        # Per queste combinazioni il WONUM può non esistere ancora,
        # perché sono template di creazione.
        if combo_name_norm in COMBOS_SKIP_WONUM_EXISTENCE_CHECK:
            return
    
        root = parse_json(json_body)
    
        wonum = get_json_string_value(root, "WONUM")
        siteid = get_json_string_value(root, "SITEID")
    
        # Se la combo non prevede WONUM, non controllo.
        # Se invece WONUM è required, la validazione combinazione lo ha già controllato prima.
        if is_blank(wonum):
            return
    
        if not workorder_exists(wonum, siteid, userInfo):
            if not is_blank(siteid):
                msg = "L'ODL " + wonum + " non esiste per il sito " + siteid + "."
            else:
                msg = "L'ODL " + wonum + " non esiste."
    
            ctx.error("x3terna", "ErrorMsg", [msg])
    def get_allowed_fields_for_combo(combo):
        required = normalize_field_list(combo["required"])
        optional = normalize_field_list(combo.get("optional", []))
    
        allowed = set()
        allowed.update(required)
        allowed.update(optional)
        allowed.update(COMMON_OPTIONAL_FIELDS)
        allowed.update(IGNORED_FIELDS)
    
        return allowed
    
    
    def validate_loader_body(json_body):
        sent_fields = extract_populated_fields_from_json(json_body)
    
        valid_combinations = []
        errors = []
    
        for combo in REQUIRED_COMBINATIONS:
            combo_name = combo["name"]

            required = normalize_field_list(combo["required"])
            allowed = get_allowed_fields_for_combo(combo)
    
            missing = required.difference(sent_fields)
            extra = sent_fields.difference(allowed)
    
            if len(missing) == 0 and len(extra) == 0:
                valid_combinations.append(combo_name)
            else:
                errors.append({
                    "combo": combo_name,
                    "missing": missing,
                    "extra": extra
                })
    
        if len(valid_combinations) > 0:
            return valid_combinations[0]
    
        best_error = None
        best_score = None
    
        for err in errors:
            score = len(err["missing"]) + len(err["extra"])
    
            if best_error is None or score < best_score:
                best_error = err
                best_score = score
    
        msg = "Template MxLoader non valido."
    
        if best_error is not None:

            if len(best_error["missing"]) > 0:
                msg += "\n\nCampi obbligatori mancanti"
    
            if len(best_error["extra"]) > 0:
                msg += "\n\nCampi valorizzati non ammessi per questa combinazione:"
                msg += "\n" + ", ".join(sorted(list(best_error["extra"])))
    
        ctx.error("x3terna", "ErrorMsg", [msg])
    
    data = ctx.getData()
    mbo = ctx.getMbo()

    mxServer = MXServer.getMXServer()

    currentUI = mbo.getUserInfo()
    userId = currentUI.getUserName()

    raw_body = data.getDataAsString()
    body = clean_body_for_json(raw_body)

    data.setCurrentData("USERID", userId)
    data.setCurrentData("BODY", raw_body)

    if body is None or body.strip() == "":
        ctx.error("x3terna", "ErrorMsg", [
            "Body MxLoader vuoto. Impossibile validare il template."
        ])

    if not body.startswith("{"):
        ctx.error("x3terna", "ErrorMsg", [
            "Body MxLoader non JSON. Primo contenuto ricevuto: " + safe_preview(raw_body, 500)
        ])

    try:
        combo_name = validate_loader_body(body)
        validate_aln_domain_value(body, "X4BURKS", "X3ZZSOCP", currentUI)
        validate_naturalavoro_required_if_outsourcing(body, combo_name)
        validate_wonum_exists_if_needed(body, combo_name, currentUI)
        validate_serbatoio_if_x4fisicoplan(body, combo_name, currentUI)
    except Exception, e:
        ctx.error("x3terna", "ErrorMsg", [
            "Errore durante la validazione del template MxLoader. "
            + "Body ricevuto: "
            + safe_preview(raw_body, 500)
            + " - Errore: "
            + str(e)
        ])