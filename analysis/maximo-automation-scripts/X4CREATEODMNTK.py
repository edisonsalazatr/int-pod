from java.io import BufferedReader, InputStreamReader, OutputStreamWriter, StringReader
from java.net import HttpURLConnection, URL
from psdi.mbo import MboConstants
from psdi.server import MXServer
from javax.xml.parsers import DocumentBuilderFactory
from org.xml.sax import InputSource
from java.util import Base64
from java.text import SimpleDateFormat
from com.ibm.json.java import JSONObject, JSONArray, JSON
from java.util import HashMap
from psdi.iface.router import HTTPHandler
from org.apache.http.impl.client import HttpClients
from java.lang import String

if not mbo.isNull("X4ODM") or not mbo.isNull("X4NTK"):
    service.error("x3terna","ErrorMsg", ["Impossibile procedere con la creazione."] )
mapping = {
    ("AM-AI","IMPIEGAT"): "AMPC_I",
    ("AM-AI","QUADRO"): "AMPC_Q",
    ("AM-AI-AMIS","IMPIEGAT"): "AIAMIS_I",
    ("AM-AI-AMIS","QUADRO"): "AIAMIS_Q",
    ("AM-AMOC","IMPIEGAT"): "LGPEES_I",
    ("AM-AMOC","QUADRO"): "LGPEES_Q",
    ("AM-AMOC-AAC","IMPIEGAT"): "AIAAC_I",
    ("AM-AMOC-AAC","QUADRO"): "AIAAC_Q",
    ("AM-AMOC-AMCC","IMPIEGAT"): "AMCC_I",
    ("AM-AMOC-AMCC","QUADRO"): "AMCC_Q",
    ("AM-AMOC-PV","IMPIEGAT"): "SPMPRV_I",
    ("AM-AMOC-PV","QUADRO"): "SPMPRV_Q",
    ("AM-GP-PMI","IMPIEGAT"): "GPPMI_I",
    ("AM-GP-PMI","QUADRO"): "GPPMI_Q",
    ("AM-GP-PRO","IMPIEGAT"): "AMPRO_I",
    ("AM-GP-PRO","QUADRO"): "AMPRO_Q",
    ("AM-PMI","IMPIEGAT"): "LGPCMR_I",
    ("AM-PMI","QUADRO"): "LGPCMR_Q",
    ("AM-SPM","IMPIEGAT"): "AMSPM_I",
    ("AM-SPM","QUADRO"): "AMSPM_Q",
    ("AM-SUP","IMPIEGAT"): "AMSUP_I",
    ("AM-SUP","QUADRO"): "AMSUP_Q",
    ("DPCE-AT","IMPIEGAT"): "DTCENA_I",
    ("DPCE-AT","QUADRO"): "DTCENA_Q",
    ("DPCE-AT-COAM","IMPIEGAT"): "CENCOA_I",
    ("DPCE-AT-COAM","QUADRO"): "CENCOA_Q",
    ("DPCE-AT-MPL","IMPIEGAT"): "CENMPL_I",
    ("DPCE-AT-MPL","QUADRO"): "CENMPL_Q",
    ("DPCE-AT-RL","IMPIEGAT"): "ATCERL_I",
    ("DPCE-AT-RL","QUADRO"): "ATCERL_Q",
    ("DPCE-SG","IMPIEGAT"): "DTCESG_I",
    ("DPCE-SG","QUADRO"): "DTCESG_Q",
    ("DPCE-SG-TCG","IMPIEGAT"): "CENTCG_I",
    ("DPCE-SG-TCG","QUADRO"): "CENTCG_Q",
    ("DPCE-SG-TCP","IMPIEGAT"): "CENTCP_I",
    ("DPCE-SG-TCP","QUADRO"): "CENTCP_Q",
    ("DPCE-SG-TS","IMPIEGAT"): "SCENTS_I",
    ("DPCE-SG-TS","QUADRO"): "SCENTS_Q",
    ("DPCE-UAM","IMPIEGAT"): "UAMTT_I",
    ("DPCE-UAM","OPERAIO"): "UAMTT_O",
    ("DPCE-UAM","QUADRO"): "UAMTT_Q",
    ("DPCE-ULN","IMPIEGAT"): "ULNTT_I",
    ("DPCE-ULN","OPERAIO"): "ULNTT_O",
    ("DPCE-ULN","QUADRO"): "ULNTT_Q",
    ("DPCE-ULS","IMPIEGAT"): "ULSTT_I",
    ("DPCE-ULS","OPERAIO"): "ULSTT_O",
    ("DPCE-ULS","QUADRO"): "ULSTT_Q",
    ("DPCE-UMA","IMPIEGAT"): "UMATT_I",
    ("DPCE-UMA","OPERAIO"): "UMATT_O",
    ("DPCE-UMA","QUADRO"): "UMATT_Q",
    ("DPCE-UMB","IMPIEGAT"): "UMBTT_I",
    ("DPCE-UMB","OPERAIO"): "UMBTT_O",
    ("DPCE-UMB","QUADRO"): "UMBTT_Q",
    ("DPCN-AT","IMPIEGAT"): "DTCNRA_I",
    ("DPCN-AT","QUADRO"): "DTCNRA_Q",
    ("DPCN-AT-COAM","IMPIEGAT"): "CNRCOA_I",
    ("DPCN-AT-COAM","QUADRO"): "CNRCOA_Q",
    ("DPCN-AT-MPL","IMPIEGAT"): "CNRMPL_I",
    ("DPCN-AT-MPL","QUADRO"): "CNRMPL_Q",
    ("DPCN-AT-RL","IMPIEGAT"): "ATCNRL_I",
    ("DPCN-AT-RL","QUADRO"): "ATCNRL_Q",
    ("DPCN-SG","IMPIEGAT"): "DTCNSG_I",
    ("DPCN-SG","QUADRO"): "DTCNSG_Q",
    ("DPCN-SG-TCG","IMPIEGAT"): "CNRTCG_I",
    ("DPCN-SG-TCG","QUADRO"): "CNRTCG_Q",
    ("DPCN-SG-TCP","IMPIEGAT"): "CNRTCP_I",
    ("DPCN-SG-TCP","QUADRO"): "CNRTCP_Q",
    ("DPCN-SG-TS","IMPIEGAT"): "SGCNTS_I",
    ("DPCN-SG-TS","QUADRO"): "SGCNTS_Q",
    ("DPCN-UBO","IMPIEGAT"): "UBOTT_I",
    ("DPCN-UBO","OPERAIO"): "UBOTT_O",
    ("DPCN-UBO","QUADRO"): "UBOTT_Q",
    ("DPCN-UFI","IMPIEGAT"): "UFITT_I",
    ("DPCN-UFI","OPERAIO"): "UFITT_O",
    ("DPCN-UFI","QUADRO"): "UFITT_Q",
    ("DPCN-UPR","IMPIEGAT"): "UPRTT_I",
    ("DPCN-UPR","OPERAIO"): "UPRTT_O",
    ("DPCN-UPR","QUADRO"): "UPRTT_Q",
    ("DPCN-USU","IMPIEGAT"): "USUTT_I",
    ("DPCN-USU","OPERAIO"): "USUTT_O",
    ("DPCN-USU","QUADRO"): "USUTT_Q",
    ("DPND-AT","IMPIEGAT"): "DTNRAT_I",
    ("DPND-AT","QUADRO"): "DTNRAT_Q",
    ("DPND-AT-COAM","IMPIEGAT"): "DPNDCO_I",
    ("DPND-AT-MPL","IMPIEGAT"): "NORMPL_I",
    ("DPND-AT-MPL","QUADRO"): "NORMPL_Q",
    ("DPND-AT-RL","IMPIEGAT"): "ATNRRL_I",
    ("DPND-AT-RL","QUADRO"): "ATNRRL_Q",
    ("DPND-SG","IMPIEGAT"): "DTNRSG_I",
    ("DPND-SG","QUADRO"): "DTNRSG_Q",
    ("DPND-SG-TCG","IMPIEGAT"): "NORTCG_I",
    ("DPND-SG-TCG","QUADRO"): "NORTCG_Q",
    ("DPND-SG-TCP","IMPIEGAT"): "NORTCP_I",
    ("DPND-SG-TCP","QUADRO"): "NORTCP_Q",
    ("DPND-SG-TS","IMPIEGAT"): "SGNRTS_I",
    ("DPND-SG-TS","QUADRO"): "SGNRTS_Q",
    ("DPND-UBG","IMPIEGAT"): "UBGTT_I",
    ("DPND-UBG","OPERAIO"): "UBGTT_O",
    ("DPND-UBG","QUADRO"): "UBGTT_Q",
    ("DPND-UBS","IMPIEGAT"): "UBSTT_I",
    ("DPND-UBS","OPERAIO"): "UBSTT_O",
    ("DPND-UBS","QUADRO"): "UBSTT_Q",
    ("DPND-UCH","IMPIEGAT"): "UCHTT_I",
    ("DPND-UCH","OPERAIO"): "UCHTT_O",
    ("DPND-UCH","QUADRO"): "UCHTT_Q",
    ("DPND-UCS","IMPIEGAT"): "UCSTT_I",
    ("DPND-UCS","OPERAIO"): "UCSTT_O",
    ("DPND-UCS","QUADRO"): "UCSTT_Q",
    ("DPND-UGO","IMPIEGAT"): "UGOTT_I",
    ("DPND-UGO","OPERAIO"): "UGOTT_O",
    ("DPND-UGO","QUADRO"): "UGOTT_Q",
    ("DPNE-AT","IMPIEGAT"): "DTNESA_I",
    ("DPNE-AT","QUADRO"): "DTNESA_Q",
    ("DPNE-AT-COAM","IMPIEGAT"): "NESCOA_I",
    ("DPNE-AT-COAM","QUADRO"): "NESCOA_Q",
    ("DPNE-AT-MPL","IMPIEGAT"): "NESMPL_I",
    ("DPNE-AT-MPL","QUADRO"): "NESMPL_Q",
    ("DPNE-AT-RL","IMPIEGAT"): "DTNEPA_I",
    ("DPNE-AT-RL","QUADRO"): "DTNEPA_Q",
    ("DPNE-SG","IMPIEGAT"): "DPNESG_I",
    ("DPNE-SG-TCG","IMPIEGAT"): "DTNECG_I",
    ("DPNE-SG-TCG","QUADRO"): "DTNECG_Q",
    ("DPNE-SG-TCP","IMPIEGAT"): "DTNECP_I",
    ("DPNE-SG-TCP","QUADRO"): "DTNECP_Q",
    ("DPNE-SG-TS","IMPIEGAT"): "DTNETS_I",
    ("DPNE-SG-TS","QUADRO"): "DTNETS_Q",
    ("DPNE-UCA","QUADRO"): "UCATT_Q",
    ("DPNE-UCA","IMPIEGAT"): "UCATT_I",
    ("DPNE-UCA","OPERAIO"): "UCATT_O",
    ("DPNE-UDU","IMPIEGAT"): "UDUTT_I",
    ("DPNE-UDU","OPERAIO"): "UDUTT_O",
    ("DPNE-UDU","QUADRO"): "UDUTT_Q",
    ("DPNE-UFV","IMPIEGAT"): "UFVTT_I",
    ("DPNE-UFV","OPERAIO"): "UFVTT_O",
    ("DPNE-UFV","QUADRO"): "UFVTT_Q",
    ("DPNE-UTA","IMPIEGAT"): "UTATT_I",
    ("DPNE-UTA","OPERAIO"): "UTATT_O",
    ("DPNE-UTA","QUADRO"): "UTATT_Q",
    ("DPNE-UVV","IMPIEGAT"): "UVVTT_I",
    ("DPNE-UVV","OPERAIO"): "UVVTT_O",
    ("DPNE-UVV","QUADRO"): "UVVTT_Q",
    ("DPNO-AT","IMPIEGAT"): "DTNVAT_I",
    ("DPNO-AT","QUADRO"): "DTNVAT_Q",
    ("DPNO-AT-COAM","QUADRO"): "ATNCOA_Q",
    ("DPNO-AT-COAM","IMPIEGAT"): "DPNOCO_I",
    ("DPNO-AT-MPL","IMPIEGAT"): "NOVMPL_I",
    ("DPNO-AT-MPL","QUADRO"): "NOVMPL_Q",
    ("DPNO-AT-RL","IMPIEGAT"): "ATNVRL_I",
    ("DPNO-AT-RL","QUADRO"): "ATNVRL_Q",
    ("DPNO-SG","IMPIEGAT"): "DTNVSG_I",
    ("DPNO-SG","QUADRO"): "DTNVSG_Q",
    ("DPNO-SG-TCG","IMPIEGAT"): "NOVTCG_I",
    ("DPNO-SG-TCG","QUADRO"): "NOVTCG_Q",
    ("DPNO-SG-TCP","IMPIEGAT"): "NOVTCP_I",
    ("DPNO-SG-TCP","QUADRO"): "NOVTCP_Q",
    ("DPNO-SG-TS","IMPIEGAT"): "SGNVTS_I",
    ("DPNO-SG-TS","QUADRO"): "SGNVTS_Q",
    ("DPNO-UGE","IMPIEGAT"): "UGETT_I",
    ("DPNO-UGE","OPERAIO"): "UGETT_O",
    ("DPNO-UGE","QUADRO"): "UGETT_Q",
    ("DPNO-UNO","IMPIEGAT"): "UNOTT_I",
    ("DPNO-UNO","OPERAIO"): "UNOTT_O",
    ("DPNO-UNO","QUADRO"): "UNOTT_Q",
    ("DPNO-UPM","IMPIEGAT"): "UPMTT_I",
    ("DPNO-UPM","OPERAIO"): "UPMTT_O",
    ("DPNO-UPM","QUADRO"): "UPMTT_Q",
    ("DPNO-UTO","IMPIEGAT"): "UTOTT_I",
    ("DPNO-UTO","OPERAIO"): "UTOTT_O",
    ("DPNO-UTO","QUADRO"): "UTOTT_Q",
    ("DPSU-AT","IMPIEGAT"): "DTSUAT_I",
    ("DPSU-AT","QUADRO"): "DTSUAT_Q",
    ("DPSU-AT-COAM","IMPIEGAT"): "SUDCOA_I",
    ("DPSU-AT-COAM","QUADRO"): "SUDCOA_Q",
    ("DPSU-AT-MPL","IMPIEGAT"): "SUDMPL_I",
    ("DPSU-AT-MPL","QUADRO"): "SUDMPL_Q",
    ("DPSU-AT-RL","IMPIEGAT"): "DTCSPA_I",
    ("DPSU-AT-RL","QUADRO"): "DTCSPA_Q",
    ("DPSU-SG","IMPIEGAT"): "DPSUSG_I",
    ("DPSU-SG-TCG","IMPIEGAT"): "DTCSCG_I",
    ("DPSU-SG-TCG","QUADRO"): "DTCSCG_Q",
    ("DPSU-SG-TCP","IMPIEGAT"): "DTCSCP_I",
    ("DPSU-SG-TCP","QUADRO"): "DTCSCP_Q",
    ("DPSU-SG-TS","IMPIEGAT"): "DTCSTS_I",
    ("DPSU-SG-TS","QUADRO"): "DTCSTS_Q",
    ("DPSU-UBA","IMPIEGAT"): "UBATT_I",
    ("DPSU-UBA","OPERAIO"): "UBATT_O",
    ("DPSU-UBA","QUADRO"): "UBATT_Q",
    ("DPSU-UBR","IMPIEGAT"): "UBRTT_I",
    ("DPSU-UBR","OPERAIO"): "UBRTT_O",
    ("DPSU-UBR","QUADRO"): "UBRTT_Q",
    ("DPSU-UFE","IMPIEGAT"): "UFETT_I",
    ("DPSU-UFE","OPERAIO"): "UFETT_O",
    ("DPSU-UFE","QUADRO"): "UFETT_Q",
    ("DPSU-UFM","IMPIEGAT"): "UFMTT_I",
    ("DPSU-UFM","OPERAIO"): "UFMTT_O",
    ("DPSU-UFM","QUADRO"): "UFMTT_Q",
    ("DPSU-UMD","IMPIEGAT"): "UMDTT_I",
    ("DPSU-UMD","OPERAIO"): "UMDTT_O",
    ("DPSU-UMD","QUADRO"): "UMDTT_Q",
    ("DPSU-URO","IMPIEGAT"): "UROTT_I",
    ("DPSU-URO","OPERAIO"): "UROTT_O",
    ("DPSU-URO","QUADRO"): "UROTT_Q",
    ("DSSA-COAM","IMPIEGAT"): "SARCOA_I",
    ("DSSA-COAM","QUADRO"): "SARCOA_Q",
    ("DSSA-MPL","IMPIEGAT"): "SARMPL_I",
    ("DSSA-MPL","QUADRO"): "SARMPL_Q",
    ("DSSA-RL","IMPIEGAT"): "DTSARL_I",
    ("DSSA-RL","QUADRO"): "DTSARL_Q",
    ("DSSA-SG","IMPIEGAT"): "DTSASG_I",
    ("DSSA-SG","QUADRO"): "DTSASG_Q",
    ("DSSA-UCO","IMPIEGAT"): "UCOTT_I",
    ("DSSA-UCO","OPERAIO"): "UCOTT_O",
    ("DSSA-UCO","QUADRO"): "UCOTT_Q",
    ("DSSA-UVI","IMPIEGAT"): "UVITT_I",
    ("DSSA-UVI","OPERAIO"): "UVITT_O",
    ("DSSA-UVI","QUADRO"): "UVITT_Q",
    ("DSSI-COAM","IMPIEGAT"): "SICCOA_I",
    ("DSSI-COAM","QUADRO"): "SICCOA_Q",
    ("DSSI-MPL","IMPIEGAT"): "SICMPL_I",
    ("DSSI-MPL","QUADRO"): "SICMPL_Q",
    ("DSSI-RL","IMPIEGAT"): "DTSIRL_I",
    ("DSSI-RL","QUADRO"): "DTSIRL_Q",
    ("DSSI-SG","IMPIEGAT"): "DTSISG_I",
    ("DSSI-SG","QUADRO"): "DTSISG_Q",
    ("DSSI-UCT","IMPIEGAT"): "UCTTT_I",
    ("DSSI-UCT","OPERAIO"): "UCTTT_O",
    ("DSSI-UCT","QUADRO"): "UCTTT_Q",
    ("DSSI-UPA","IMPIEGAT"): "UPATT_I",
    ("DSSI-UPA","OPERAIO"): "UPATT_O",
    ("DSSI-UPA","QUADRO"): "UPATT_Q",
    ("RTN-PMO","IMPIEGAT"): "RICGE_I",
    ("RTN-PMO","QUADRO"): "RICGE_Q",
    ("UI_RT2","OPERAIO"): "RT2TT_O",
    ("UI_GTG","IMPIEGAT"): "TCG_I",
    ("UI_GTG","OPERAIO"): "TCG_O"
}
def evalCdl(titris,craft):
     return mapping.get((titris, craft), None)
mboKey = mbo.getLong("WORKORDERID")  # Salva la chiave primaria (ad esempio, ASSETNUM)
mbo.getThisMboSet().save()  # Salvataggio
mbo.getThisMboSet().reset()  # Resetta e ricarica il set di MBO
mbo = mbo.getThisMboSet().getMboForUniqueId(mboKey)

tipospesa=mbo.getString("X4TIPOSPESA")
ntk=False
odm=True

configUri = MXServer.getMXServer().getConfig()
# URL dell'endpoint
url_odm = configUri.getProperty("mxe.int.sap.manage.odm")
url_ntk = configUri.getProperty("mxe.int.sap.manage.ntk")

url_string_azure = configUri.getProperty("mxe.int.sap.azureuri") 
# Ottenere l'istanza di MXServer
mxserver = MXServer.getMXServer()
userInfo = mxserver.getSystemUserInfo()

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

if tipospesa=="CAPEX":
    url=url_ntk
else:
    url=url_odm
    

if mbo.isNull("TARGSTARTDATE") or mbo.isNull("TARGCOMPDATE"):
    service.error("x3terna","ErrorMsg", ["Verificare che le date di pianificazione siano state popolate correttamente."] )
# Formattazione delle date
date_format = SimpleDateFormat("yyyy-MM-dd")
TARGSTARTDATE = date_format.format(mbo.getDate("TARGSTARTDATE"))
TARGCOMPDATE = date_format.format(mbo.getDate("TARGCOMPDATE"))
DURATA= mbo.getDouble("ESTDUR")
# Variabili messaggio
WONUM = mbo.getString("WONUM").replace(u"Ì", u"i")
if WONUM and len(WONUM) > 20:
    WONUM = WONUM[-20:]
X4ODM = mbo.getString("X4ODM")
DESCRIPTION = str(mbo.getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "").replace(">", "").replace("=", "").replace("&", "").replace("à", "a").replace("è", "e").replace("é", "e").replace("ì", "i").replace("ò", "o").replace("ù", "u").replace("À", "A").replace("È", "E").replace("É", "E").replace("Ì", "I").replace("Ò", "O").replace("Ù", "U")
X4NTK = mbo.getString("X4NTK")
OWNERGROUP= mbo.getString("OWNERGROUP")
JPNUM= mbo.getString("JPNUM")
X4TITRIS= mbo.getString("X4TITRIS")

NTKSET = mbo.getMboSet("$X4NTK$", "X4NTK", "IDNTK=:X4NTK")
if NTKSET.isEmpty():
    TIPONTK="Z_PM"
    NOPERAZIONE=""
    
else:
    TIPONTK = NTKSET.getMbo(0).getString("TIPO")
    NOPERAZIONE = NTKSET.getMbo(0).getString("NOPERAZIONE")

JPNUMDESC = str(mbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "").replace(">", "").replace("=", "").replace("à", "a").replace("è", "e").replace("é", "e") \
    .replace("ì", "i").replace("ò", "o").replace("ù", "u") \
    .replace("À", "A").replace("È", "E").replace("É", "E") \
    .replace("Ì", "I").replace("Ò", "O").replace("Ù", "U").replace("&", "e")
IDSTANDARD = mbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("X4ATTALL")

#TARGSTARTDATE = mbo.getDate("TARGSTARTDATE")
#TARGCOMPDATE = mbo.getDate("TARGCOMPDATE")
WOPRIORITY = mbo.getString("WOPRIORITY")
X4TIPOATTIVITA = mbo.getString("X4TIPOATTIVITA")
if mbo.isNull("X4TIPOORDINE") and mbo.getString("X4TIPOOI")=="T09":
    #X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
    X4TIPOORDINE = "PMTG"
elif mbo.isNull("X4TIPOORDINE") and mbo.getString("X4TIPOOI")=="T06":
    #X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
    X4TIPOORDINE = "PMT9"
elif mbo.isNull("X4TIPOORDINE"):
    X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
else:
    X4TIPOORDINE = mbo.getString("X4TIPOORDINE")
if tipospesa=="CAPEX":
    X4WBSCONT = mbo.getString("X4WBE")
    if mbo.isNull("X4WBE"):
        service.error("x3terna","ErrorMsg", ["Necessario popolare i campi WBS e WBE per poter procedere con la creazione."] )
else:
    if mbo.getString("X4TIPOORDINE")=="PMT0":
        X4WBS=""
    else:
        X4WBS = mbo.getString("X4WBEPIAN")
      
    if mbo.getString("X4TIPOORDINE") in ["PMT0","PML9","PMTG"] or (mbo.getString("X4TIPOATTIVITA")=="EPC" and mbo.getString("X4TIPOSPESA")=="ANR"):
        X4WBSCONT = mbo.getString("X4WBE")
    else:
        X4WBSCONT=""
        
X4SAPIDASSET = mbo.getString("X4SAPIDASSET")
classID=mbo.getMboSet("ASSET").getMbo(0).getString("CLASSSTRUCTUREID")
equipmentMboSet=MXServer.getMXServer().getMboSet("CLASSANCESTOR",MXServer.getMXServer().getSystemUserInfo())
equipmentMboSet.setWhere("CLASSSTRUCTUREID='"+classID+"' and ancestorclassid='DISPOSITIVI E COMPONENTI'")
equipmentMboSet.reset()
if not equipmentMboSet.isEmpty() and len(X4SAPIDASSET)>14:
    X4SAPIDASSET=X4SAPIDASSET[:-11]
equipmentMboSet.close()
if len(X4SAPIDASSET)<6:
    X4SAPIDASSET=""
X4NORDINEINTERNO = mbo.getString("X4NORDINEINTERNO")
ESTDUR = mbo.getDouble("ESTDUR")

if mbo.getBoolean("X4AGGREGATI")==True:
    wplaborSet=mbo.getMboSet("$WPLABFIGLI$","WPLABOR","wonum in(select wonum from workorder where parent=:wonum and siteid=:siteid) and siteid=:siteid")
else:
    wplaborSet=mbo.getMboSet("WPLABOR")
operazioni = []
if not wplaborSet.isEmpty():
    if tipospesa == 'OPEX' or tipospesa == 'ANR':
        wplaborNewMbo = wplaborSet.moveFirst()
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
        aggregati = {}

        while wplaborNewMbo:
            cdltitris = evalCdl(wplaborNewMbo.getString("X4TITRIS"), wplaborNewMbo.getString("CRAFT"))
            if cdltitris==None:
                wplaborNewMbo = wplaborSet.moveNext()
                continue
            dauno = (wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")) + (wplaborNewMbo.getDouble("LABORHRS") * wplaborNewMbo.getDouble("QUANTITY"))
            ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
            hrwrk = wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")
            hrtra = wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")

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
            operazione_tag = """
                  <item>
                    <Arbpl>{}</Arbpl>
                    <Hrwrk>{}</Hrwrk>
                    <Hrtra>{}</Hrtra>
                  </item>
                """.format(
                cdltitris,
                str(("{:.2f}".format(float(valori['hrwrk']))).rstrip('0').rstrip('.')),
                str(("{:.2f}".format(float(valori['hrtra']))).rstrip('0').rstrip('.'))
            )
            operazioni.append(operazione_tag)
        # Unisco tutte le operazioni in un unico blocco XML
        operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
        #service.error("TEST",operation_xml)
    elif tipospesa=='CAPEX':
        wplaborNewMbo = wplaborSet.moveFirst()
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
        aggregati_capex = {}
        while wplaborNewMbo:
            cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
            if cdltitris==None:
                wplaborNewMbo = wplaborSet.moveNext()
                continue
            dauno = (wplaborNewMbo.getDouble("QUANTITY")*wplaborNewMbo.getDouble("LABORHRS"))+(wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))
            ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
            ntstart = date_format.format(wplaborNewMbo.getDate("X4DATAINIZIORISORSA"))
            arbpl = cdltitris
            hrwrk = wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")
            hrtra = wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")
            
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
            operazione_tag = """
                      <item>
                        <Cdl>{}</Cdl>
                        <Orelavoro>{}</Orelavoro>
                        <Oretrasferimento>{}</Oretrasferimento>
                      </item>
                    """.format(cdltitris, str(("{:.2f}".format(float(valori['hrwrk']))).rstrip('0').rstrip('.')), str(("{:.2f}".format(float(valori['hrtra']))).rstrip('0').rstrip('.')))
            operazioni.append(operazione_tag)
        # Unisco tutte le operazioni in un unico blocco XML
        operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
elif wplaborSet.isEmpty():
    if tipospesa == 'OPEX' or tipospesa == 'ANR':
        wplaborNewMbo = wplaborSet.moveFirst()
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
        dauno = mbo.getDouble("ESTDUR")
        ntend = date_format.format(mbo.getDate("TARGCOMPDATE"))
        hrwrk = 0
        hrtra = 0
        operazione_tag = """
                  <item>
                    <Arbpl>{}</Arbpl>
                    <Hrwrk>{}</Hrwrk>
                    <Hrtra>{}</Hrtra>
                  </item>
                """.format(
                first_cdl,
                str(hrwrk)[:5],
                str(hrtra)[:5]
            )
        operazioni.append(operazione_tag)
        operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
    elif tipospesa=='CAPEX':
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        dauno = mbo.getDouble("ESTDUR")
        ntend = date_format.format(mbo.getDate("TARGCOMPDATE"))
        ntstart = date_format.format(mbo.getDate("TARGSTARTDATE"))
        arbpl = first_cdl
        hrwrk = 0
        hrtra = 0
        operazione_tag = """
                  <item>
                    <Cdl>{}</Cdl>
                    <Orelavoro>{}</Orelavoro>
                    <Oretrasferimento>{}</Oretrasferimento>
                  </item>
                """.format(arbpl, str(("{:.2f}".format(float(hrwrk))).rstrip('0').rstrip('.')), str(("{:.2f}".format(float(hrtra))).rstrip('0').rstrip('.')))
        operazioni.append(operazione_tag)
        # Unisco tutte le operazioni in un unico blocco XML
        operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
if not operazioni:
    if tipospesa == 'OPEX' or tipospesa == 'ANR':
        wplaborNewMbo = wplaborSet.moveFirst()
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
        dauno = mbo.getDouble("ESTDUR")
        ntend = date_format.format(mbo.getDate("TARGCOMPDATE"))
        hrwrk = 0
        hrtra = 0
        operazione_tag = """
                  <item>
                    <Arbpl>{}</Arbpl>
                    <Hrwrk>{}</Hrwrk>
                    <Hrtra>{}</Hrtra>
                  </item>
                """.format(
                first_cdl,
                str(hrwrk)[:5],
                str(hrtra)[:5]
            )
        operazioni.append(operazione_tag)
        operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
    elif tipospesa=='CAPEX':
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        dauno = mbo.getDouble("ESTDUR")
        ntend = date_format.format(mbo.getDate("TARGCOMPDATE"))
        ntstart = date_format.format(mbo.getDate("TARGSTARTDATE"))
        arbpl = first_cdl
        hrwrk = 0
        hrtra = 0
        operazione_tag = """
                  <item>
                    <Cdl>{}</Cdl>
                    <Orelavoro>{}</Orelavoro>
                    <Oretrasferimento>{}</Oretrasferimento>
                  </item>
                """.format(arbpl, str(("{:.2f}".format(float(hrwrk))).rstrip('0').rstrip('.')), str(("{:.2f}".format(float(hrtra))).rstrip('0').rstrip('.')))
        operazioni.append(operazione_tag)
        # Unisco tutte le operazioni in un unico blocco XML
        operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
if wplaborSet.isEmpty():
    ORELAVORO=0
    ORETRASFERIMENTO=0
    DURATA=0
else:
    ORELAVORO=0
    ORETRASFERIMENTO=0
    DURATA=0
    for i in range(0,wplaborSet.count()):
        wplaborMbo=wplaborSet.getMbo(i)
        ORELAVORO=ORELAVORO+(wplaborMbo.getDouble("LABORHRS")*wplaborMbo.getDouble("QUANTITY"))
        ORETRASFERIMENTO=ORETRASFERIMENTO+wplaborMbo.getDouble("X4ORETRASF")
        DURATA = ORELAVORO + ORETRASFERIMENTO
        if DURATA>9999:
            DURATA=9999
wplaborSet.close()
#ORELAVORO = ORELAVORO
#ORETRASFERIMENTO = ORETRASFERIMENTO

'''if ORELAVORO==0 and mbo.getBoolean("X4TECMIG")==False:
    service.error("x3terna","ErrorMsg", ["Si prega di compilare almeno una riga di pianificazione con delle ore di esecuzione."] )
'''
CDL = mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
if tipospesa=="CAPEX":
    #XML da inviare nella richiesta POST per NTW
    xml_data ="""<?xml version="1.0" encoding="utf-8"?>
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
              <Trtyp>H</Trtyp>
            </tns:ZrichiestaCreazioneNtw>
        </soap:Body>
    </soap:Envelope>""".format(TIPONTK,X4NTK,X4WBSCONT,JPNUMDESC[:40],first_cdl,WONUM, NOPERAZIONE, JPNUM, X4SAPIDASSET,TARGSTARTDATE,TARGCOMPDATE,TARGCOMPDATE,str(("{:.1f}".format(float(DURATA))).rstrip('0').rstrip('.')),operation_xml,X4SAPIDASSET)
    
else:
    # XML da inviare nella richiesta POST per OdM
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

def log_esito_interfaccia(mbo, esito, descrizione, risposta):   #+++++++++++++++++AGGIUNTO X4ESITOINT
    try:
        mxserver = MXServer.getMXServer()
        userInfo = mxserver.getSystemUserInfo()
        
        esitoSet = mxserver.getMboSet("X4ESITOINT", userInfo)
        esitoMbo = esitoSet.add()
        
        esitoMbo.setValue("WONUM", mbo.getString("WONUM"), 11L)
        esitoMbo.setValue("ESITO", esito, 11L)  # OK / KO
        esitoMbo.setValue("DESC_ESITO", descrizione[:254], 11L)
        esitoMbo.setValue("MESSAGE", risposta[:2000], 11L)
        esitoMbo.setValue("DEST", "SAP", 11L)
        esitoMbo.setValue("IDINTERFACCIA", "MANAGE ODM/NTK", 11L)
        esitoMbo.setValue("END_ELAB", MXServer.getMXServer().getDate(), 11L)
        esitoMbo.setValue("START_ELAB", MXServer.getMXServer().getDate(), 11L)
        esitoMbo.setValue("SOURCE", "EAM", 11L)
        
        esitoSet.save()
        esitoSet.close()
        
    except Exception as e:
        service.log("Errore nel logging X4ESITOINTERF: " + str(e))
        
#service.error("TEST",xml_data)
# Funzione per eseguire la richiesta POST e restituire la risposta
def send_post_request(url, xml_data):
    url_object = URL(url)
    connection = url_object.openConnection()
    connection.setDoOutput(True)
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Content-Type", "application/xml")
    # Creazione manuale dell'header di autenticazione
    #auth_string = str(username)+":"+str(password)
    #service.error("TEST",str(auth_string))
    #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"))
    #connection.setRequestProperty("Authorization", "Basic {}".format(auth_encoded))
    connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8")
    connection.setRequestProperty('Authorization', 'Bearer ' + token)
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
    else:
        #raise Exception("HTTP request failed with code: " + str(response_code))
        #service.error("x3terna", "ErrorMsg", ["Chiamata non riuscita."])
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
    odm_elements = doc.getElementsByTagName("Aufnr")
    odm_text = odm_elements.item(0).getTextContent().strip()
    if odm_text:
        odm_text = odm_elements.item(0).getTextContent().strip()
        errore=False
        return odm_elements.item(0).getTextContent(), errore
    else:
        odm_elements = doc.getElementsByTagName("Message")
        resperrore= odm_elements.item(0).getTextContent().strip().encode('ascii', 'replace').decode('ascii')
        if resperrore:
            errore=True
            return resperrore, errore
            
# Funzione per aggiornare il workorder (set del campo ODM)
def update_mbo_with_response_odm(mbo, odm):
    mbo.setValue("X4ODM", odm, 11L)
    mbo.getThisMboSet().save()
    #service.error("x3terna", "Odm ricevuto e aggiornato.")
    
#Prendo valore di NTK      
def extract_ntkBack_from_response(response):
    # Parse XML response
    factory = DocumentBuilderFactory.newInstance()
    builder = factory.newDocumentBuilder()
    isr = InputSource(StringReader(response))
    doc = builder.parse(isr)
    # Find <Network> tag
    ntk_elements = doc.getElementsByTagName("Network")
    ntk_text = ntk_elements.item(0).getTextContent().strip()
    if ntk_text:
        ntk_text = ntk_elements.item(0).getTextContent().strip()
        errore=False
        return ntk_elements.item(0).getTextContent(), errore
    else:
        ntk_elements = doc.getElementsByTagName("Message")
        resperrore= ntk_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
        if resperrore:
            errore=True
            return resperrore, errore
            
def extract_ntk_from_response(response):
    # Parse XML response
    factory = DocumentBuilderFactory.newInstance()
    builder = factory.newDocumentBuilder()
    isr = InputSource(StringReader(response))
    doc = builder.parse(isr)
    
    ntk_elements = doc.getElementsByTagName("Network")
    ntk_text = ntk_elements.item(0).getTextContent().strip()
    if not ntk_text:
        message_elements = doc.getElementsByTagName("Message")
        resperrore = message_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
        if resperrore:
            errore = True
            return resperrore, None, errore
    
    op_elements = doc.getElementsByTagName("Operazione")
    op_text = op_elements.item(1).getTextContent().strip()
    
    if ntk_text:
        ntk_text = ntk_elements.item(0).getTextContent().strip()
        errore = False
        return ntk_elements.item(0).getTextContent(), op_text, errore
    
    else:
        #service.error("Errore nella creazione della Network  ",(resperrore))
        message_elements = doc.getElementsByTagName("Message")
        resperrore = message_elements.item(2).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
       
        errore = True
        return resperrore, None, errore
            
# Funzione per aggiornare il workorder (set del campo NTK)
def update_mbo_with_response_ntk(mbo, ntk, noperazione):
    mbo.setValue("X4NTK", ntk, 11L)
    #service.error("operazione: ", str(noperazione))
    mbo.setValue("X4NOPERAZIONE", noperazione, 11L)
    mbo.getThisMboSet().save()
    #service.error("x3terna", "Ntk ricevuto e aggiornato.")
    
# Invocazione delle funzioni
#service.error("TEST",str(xml_data))
response = send_post_request(url, xml_data)

#service.error("TEST", unicode(response))
if tipospesa=='CAPEX':
    #service.error("Errore nella creazione della Network  ",(response))
    ntk,noperazione,errore=extract_ntk_from_response(response)
    
    if errore==True:
        log_esito_interfaccia(mbo, "KO", str(ntk), response)    #+++++++++++++++++AGGIUNTO X4ESITOINT
        service.error("x3terna","ErrorMsg", ["Errore nella creazione: "+str(ntk)] )
        #service.error("Errore nella creazione della Network  ",(ntk))
        #service.error("Errore nella creazione della Network  ",(response))
    if mbo:
        update_mbo_with_response_ntk(mbo, ntk, noperazione)
        log_esito_interfaccia(mbo, "OK", "Creazione NTK avvenuta", response) #+++++++++++++++++AGGIUNTO X4ESITOINT
        
else:       
    #service.error("TEST",xml_data)
    odm,errore = extract_odm_from_response(response)
    # Ottenere il MBO corrente. Questo potrebbe variare a seconda del contesto in cui lo script viene eseguito.
    if errore==True:
        log_esito_interfaccia(mbo, "KO", str(odm), response)  #+++++++++++++++++AGGIUNTO X4ESITOINT
        service.error("x3terna","ErrorMsg", ["Errore nella creazione: "+str(odm)] )
        #service.error("Errore nella creazione: ",(odm))
    if mbo:
        update_mbo_with_response_odm(mbo, odm)
        log_esito_interfaccia(mbo, "OK", "Creazione OdM avvenuta", response) #+++++++++++++++++AGGIUNTO X4ESITOINT
        #service.error("x3terna", "Odm ricevuto e aggiornato.")
'''
except Exception as e:
    # Gestione degli errori
    service.log("Error occurred: " + str(e))
 '''