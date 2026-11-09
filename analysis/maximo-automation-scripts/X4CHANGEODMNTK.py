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

checkodm=mbo.getMboSet("$CHECKODMCH$","X4ODM",":x4odm=IDODM")
checkntk=mbo.getMboSet("$CHECKNTKCH$","X4NTK",":x4ntk=IDNTK")

if (not checkodm.isEmpty() or not checkntk.isEmpty()) and onupdate and (mbo.isModified("X4ODM") or mbo.isModified("X4NTK")) and (not mbo.isNull("X4ODM") or not mbo.isNull("X4NTK")) and mbo.isNull("PARENT"):
    
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
        
    # Formattazione delle date
    date_format = SimpleDateFormat("yyyy-MM-dd")
    TARGSTARTDATE = date_format.format(mbo.getDate("TARGSTARTDATE"))
    TARGCOMPDATE = date_format.format(mbo.getDate("TARGCOMPDATE"))
    DURATA = mbo.getDouble("ESTDUR")
        
    # Variabili messaggio
    WONUM = mbo.getString("WONUM").replace(u"Ì", u"i")
    if WONUM and len(WONUM) > 20:
        WONUM = WONUM[-20:]
    X4ODM = mbo.getString("X4ODM")
    DESCRIPTION = DESCRIPTION = str(mbo.getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "").replace(">", "").replace("=", "").replace("à", "a").replace("è", "e").replace("é", "e").replace("ì", "i").replace("ò", "o").replace("ù", "u").replace("À", "A").replace("È", "E").replace("É", "E").replace("Ì", "I").replace("Ò", "O").replace("Ù", "U")
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
    
    JPNUMDESC = mbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("DESCRIPTION")
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
            NTKSet=mbo.getMboSet("$XtNTK$","X4NTK","idntk=:x4ntk")
            if not NTKSet.isEmpty():
                X4WBSCONT=NTKSet.getMbo(0).getString("WBE")
                if NTKSet.getMbo(0).isNull("WBE"):
                    mbo.setValueNull("X4NTK")
                    service.error("x3terna","ErrorMsg", ["WBE non definita per l'operazione, necessario selezionarla per poter procedere."] )
                    
    else:
        if mbo.getString("X4TIPOORDINE")=="PMT0" or mbo.getString("X4TIPOORDINE")=="PMAC":
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
    if len(X4SAPIDASSET)<6:
        X4SAPIDASSET=""
    equipmentMboSet.close()
    X4NORDINEINTERNO = mbo.getString("X4NORDINEINTERNO")
    ESTDUR = mbo.getDouble("ESTDUR")
    isParent=mbo.getBoolean("X4AGGREGATI")
    if isParent==True:
        wplaborSet=mbo.getMboSet("$WPFIGLI$","WPLABOR","wonum in (select wonum from workorder where parent=:wonum and siteid=:siteid)")
    else:
        wplaborSet=mbo.getMboSet("WPLABOR")
    operazioni = []
    if not wplaborSet.isEmpty():
        if tipospesa=='OPEX' or tipospesa=='ANR':
            first_cdl=mbo.getMboSet("$X4ODMID$","X4ODM",":x4odm=idodm").getMbo(0).getString("CDLRESP")
            wplaborNewMbo = wplaborSet.moveFirst()
            #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
            aggregati = {}
            while wplaborNewMbo:
                cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                if cdltitris==None:
                    wplaborNewMbo = wplaborSet.moveNext()
                    continue
                dauno = (wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))+(wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY"))
                ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
                arbpl = cdltitris
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
            aggregati_capex = {}
            while wplaborNewMbo:
                cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                if cdltitris==None:
                    wplaborNewMbo = wplaborSet.moveNext()
                    continue
                dauno = (wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY"))+(wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))
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
                        """.format(cdltitris, str(("{:.2f}".format(float(str(valori['hrwrk'])))).rstrip('0').rstrip('.')), str(("{:.2f}".format(float(str(valori['hrtra'])))).rstrip('0').rstrip('.')))
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
                    str(valori['hrwrk'])[:5],
                    str(valori['hrtra'])[:5]
                )
            operazioni.append(operazione_tag)
            operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
        elif tipospesa=='CAPEX':
            first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
            dauno = mbo.getDouble("ESTDUR")
            ntend = date_format.format(mbo.getDate("TARGCOMPDATE"))
            wplaborNewMbo = wplaborSet.moveFirst()
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
                    """.format(arbpl, str(("{:.2f}".format(float(str(hrwrk)))).rstrip('0').rstrip('.')), str(("{:.2f}".format(float(str(hrtra)))).rstrip('0').rstrip('.')))
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
    wplaborSet.close()
    #ORELAVORO = ORELAVORO
    #ORETRASFERIMENTO = ORETRASFERIMENTO
    
    if ORELAVORO==0:
        service.error("x3terna","ErrorMsg", ["Si prega di compilare almeno una riga di pianificazione con delle ore di esecuzione."] )
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
                  <Trtyp>V</Trtyp>
                </tns:ZrichiestaCreazioneNtw>
            </soap:Body>
        </soap:Envelope>""".format(TIPONTK,X4NTK,X4WBSCONT,JPNUMDESC[:40],first_cdl,WONUM, NOPERAZIONE, JPNUM, X4SAPIDASSET,TARGSTARTDATE,TARGCOMPDATE,TARGCOMPDATE,str(("{:.1f}".format(float(ESTDUR))).rstrip('0').rstrip('.')),operation_xml,X4SAPIDASSET)
        
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
              <Trtyp>V</Trtyp>
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
            raise Exception("HTTP request failed with code: " + str(response_code))
            
            
    #Prendo valore di OdM        
    def extract_odm_from_response(response):
        # Parse XML response
        factory = DocumentBuilderFactory.newInstance()
        builder = factory.newDocumentBuilder()
        isr = InputSource(StringReader(response))
        doc = builder.parse(isr)
        # Find <OdM> tag
        odm_elements = doc.getElementsByTagName("Type")
        odm_text = odm_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii') #aggiunto
        if odm_text:
            odm_text = odm_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii') #aggiunto
            if odm_text=="E":
                errore=True
                odm_elements = doc.getElementsByTagName("Message")
                resperrore= odm_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
            else:
                resperrore=None
                errore=False
            return resperrore, errore
                
    # Funzione per aggiornare il workorder (set del campo ODM)
    def update_mbo_with_response_odm(mbo, odm):
        mbo.setValue("X4ODM", odm, 11L)
        mbo.getThisMboSet().save()
        #service.error("x3terna", "Odm ricevuto e aggiornato.")
        
    #Prendo valore di NTK      
    def extract_ntk_from_response(response):
        # Parse XML response
        factory = DocumentBuilderFactory.newInstance()
        builder = factory.newDocumentBuilder()
        isr = InputSource(StringReader(response))
        doc = builder.parse(isr)
        
        ntk_elements = doc.getElementsByTagName("Network")
        ntk_text_item = ntk_elements.item(0)
        ntk_text = ntk_text_item.getTextContent().strip() if ntk_text_item else None
        if not ntk_text:
            message_elements = doc.getElementsByTagName("Message")
            resperrore = message_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
            #service.error("TEST",str(resperrore))
            if resperrore:
                errore = True
                return resperrore, None, errore
        
        op_elements = doc.getElementsByTagName("Operazione")
        op_text = op_elements.item(1).getTextContent().strip()
        #service.error("TEST",str(op_text))
        if not op_text:
            errore = True
            resperrore = "Operazione non generata."
            return resperrore, None, errore
        
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
        mbo.setValue("X4NOPERAZIONE", noperazione, 11L)
        mbo.getThisMboSet().save()
    #service.error("x3terna", "Ntk ricevuto e aggiornato.")
        
        
    # Invocazione delle funzioni
    #service.error("TEST",str(xml_data))
    response = send_post_request(url, xml_data)
    #service.error("test",str(response.encode('utf-8')))
    #service.error("test",str(xml_data))
    
    if tipospesa=='CAPEX':
        ntk,noperazione,errore=extract_ntk_from_response(response)
        if errore==True:
            log_esito_interfaccia(mbo, "KO", str(ntk), response)    #+++++++++++++++++AGGIUNTO X4ESITOINT
            service.error("x3terna","ErrorMsg", ["Errore nella creazione: "+str(ntk)])
        else:
            mbo.setValue("X4NOPERAZIONE",noperazione)
            log_esito_interfaccia(mbo, "OK", "Creazione NTK avvenuta", response) #+++++++++++++++++AGGIUNTO X4ESITOINT
        #if mbo:
            #update_mbo_with_response_ntk(mbo, ntk)
            #service.error("x3terna", "Odm ricevuto e aggiornato.")
    else:       
    #service.error("TEST",response)
        odm,errore = extract_odm_from_response(response)
        # Ottenere il MBO corrente. Questo potrebbe variare a seconda del contesto in cui lo script viene eseguito.
        if errore==True:
            log_esito_interfaccia(mbo, "KO", str(odm), response)  #+++++++++++++++++AGGIUNTO X4ESITOINT
            service.error("Errore nella creazione: ",(odm))
        else:
            log_esito_interfaccia(mbo, "OK", "Creazione OdM avvenuta", response) #+++++++++++++++++AGGIUNTO X4ESITOINT
        #if mbo:
            #update_mbo_with_response_odm(mbo, odm)
            #service.error("x3terna", "Odm ricevuto e aggiornato.")
    '''
    except Exception as e:
        # Gestione degli errori
        service.log("Error occurred: " + str(e))"
X4WOESEGUITO_TASK,Cambio stato per task da mobile,Draft,"from psdi.server import MXServer
if mbo.getString("STATUS")=="COMP" or mbo.getString("STATUS")=="ESEGUITO":
    microSet=mbo.getMboSet("X4MICROCONTROLLO")
    if not microSet.isEmpty():
        microMbo=microSet.getMbo(0)
        if microMbo.getBoolean("MISUREOBB")==True and not microMbo.getMboSet("X4ASSOCMISURA").isEmpty():
            checkMisObb=mbo.getMboSet("$MISMICRO$","X4MICROCONTROLLI",":x4microcontrollo=idmicrocontrollo and misureobb=1 and exists (SELECT 1 FROM x4rilevazioniodl WHERE tipo='MIS' AND workorderid=:workorderid AND (id,assetnum) NOT IN (SELECT idmisura,assetnum FROM x4misurewo WHERE wonuM=:wonum))")
            if not checkMisObb.isEmpty():
                service.error("x3terna", "ErrorMsg", ["Si prega di inserire tutte le misure obbligatorie per il microcontrollo."])
    if mbo.getString("STATUS")=="COMP":
        mbo.setValue("STATUS","ESEGUITO",11L)
        mbo.getThisMboSet().save()
    
if mbo.isNull("X4CLASSASSET") and launchPoint=="X4WOESEGUITO_TASK2":
    assetSet=mbo.getMboSet("ASSET")
    assetMbo=assetSet.getMbo(0)
    classificationID=assetMbo.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    mbo.setValue("X4CLASSASSET",classificationID,11L)
    if mbo.isNull("X4SAPIDASSET"):
        parenAssetSet=assetMbo.getMboSet("PARENT")
        if not parenAssetSet.isEmpty():
            parenAssetMbo=parenAssetSet.getMbo(0)
            mbo.setValue("X4SAPIDASSET",parenAssetMbo.getString("X3SAPID"))
    assetSet.close()
    

    
if mbo.isModified("X4COMPMOBILE") and mbo.getBoolean("X4COMPMOBILE")==True:
    if mbo.isNull("X4MATRICOLAESITO"):
        mbo.setValue("X4MATRICOLAESITO",user)
    checkTipoPdL=mbo.getMboSet("$JOBPLANCONTROLLO$","JOBPLAN","JPNUM=:JPNUM and :PLUSCJPREVNUM=PLUSCREVNUM and classstructureid in (select classstructureid from classstructure where classificationid like 'CONTROLLO%')")
    if not checkTipoPdL.isEmpty():
        wonum=mbo.getString("WONUM")
        difforConfermaSet=mbo.getMboSet("$DIFFORCONFERMA$","X4DIFFORSTAT","(IDDIFFORMITA_EXT IN (SELECT difformita FROM X4DIFFATT WHERE jpnum=:jpnum) OR IDDIFFORMITA_EXT IN (SELECT IDDIFFORMITA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4difforstatid not in (select x4difforstatid from x4diffor_wo where wonum=:wonum)")
        if not difforConfermaSet.isEmpty():
            for i in range (0,difforConfermaSet.count()):
                difforConfermaMbo=difforConfermaSet.getMbo(i)
                iddiff=difforConfermaMbo.getString("IDDIFFORMITA_EXT")
                asset=difforConfermaMbo.getString("ASSETNUM")
                gravita=difforConfermaMbo.getString("GRAVITA")
                difforstatid=difforConfermaMbo.getLong("X4DIFFORSTATID")
                fase=difforConfermaMbo.getString("FASE")
                difforConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                difforConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                difforWO=mbo.getMboSet("X4DIFFORMITAWO")
                newDifforWo=difforWO.add()
                newDifforWo.setValue("IDDIFFORMITA",iddiff,11L)
                newDifforWo.setValue("ASSETNUM",asset,11L)
                newDifforWo.setValue("GRAVITA",gravita,11L)
                newDifforWo.setValue("X4DIFFORSTATID",difforstatid)
                newDifforWo.setValue("WONUM",wonum,11L)
                newDifforWo.setValue("SITEID","SIT01",11L)
                newDifforWo.setValue("FASE","fase",11L)
            difforWO.save()
            difforConfermaSet.save()
        interfConfermaSet=mbo.getMboSet("$INTERFCONFERMA$","X4INTERFSTAT","(IDINTERFERENZA_EXT IN (SELECT interferenza FROM x4interfatt WHERE attivita=:jpnum) OR IDINTERFERENZA_EXT IN (SELECT IDINTERFERENZA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4interfstatid not in (select x4interfstatid from x4interf_wo where wonum=:wonum)")
        if not interfConfermaSet.isEmpty():
            for j in range (0,interfConfermaSet.count()):
                interfConfermaMbo=interfConfermaSet.getMbo(j)
                idint=interfConfermaMbo.getString("IDINTERFERENZA_EXT")
                asset=interfConfermaMbo.getString("ASSETNUM")
                gravita=interfConfermaMbo.getString("GRAVITA")
                interfstatid=interfConfermaMbo.getLong("X4INTERFSTATID")
                fase=interfConfermaMbo.getString("FASE")
                interfConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                interfConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                interfWO=mbo.getMboSet("X4INTERFERENZAWO")
                newinterfWO=interfWO.add()
                newinterfWO.setValue("IDINTERFERENZA",idint,11L)
                newinterfWO.setValue("ASSETNUM",asset,11L)
                newinterfWO.setValue("GRAVITA",gravita,11L)
                newinterfWO.setValue("X4INTERFSTATID",interfstatid)
                newinterfWO.setValue("WONUM",wonum,11L)
                newinterfWO.setValue("SITEID","SIT01",11L)
                newinterfWO.setValue("FASE",fase,11L)
            interfWO.save()
            interfConfermaSet.save()
            
if mbo.isModified("STATUS") and mbo.getString("STATUS")=="ESEGUITO" and mbo.getBoolean("X4COMPMOBILE")==False:
    checkTipoPdL=mbo.getMboSet("$JOBPLANCONTROLLO$","JOBPLAN","JPNUM=:JPNUM and :PLUSCJPREVNUM=PLUSCREVNUM and classstructureid in (select classstructureid from classstructure where classificationid like 'CONTROLLO%')")
    if not checkTipoPdL.isEmpty():
        wonum=mbo.getString("WONUM")
        difforConfermaSet=mbo.getMboSet("$DIFFORCONFERMA$","X4DIFFORSTAT","(IDDIFFORMITA_EXT IN (SELECT difformita FROM X4DIFFATT WHERE jpnum=:jpnum) OR IDDIFFORMITA_EXT IN (SELECT IDDIFFORMITA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4difforstatid not in (select x4difforstatid from x4diffor_wo where wonum=:wonum)")
        if not difforConfermaSet.isEmpty():
            for i in range (0,difforConfermaSet.count()):
                difforConfermaMbo=difforConfermaSet.getMbo(i)
                iddiff=difforConfermaMbo.getString("IDDIFFORMITA_EXT")
                asset=difforConfermaMbo.getString("ASSETNUM")
                gravita=difforConfermaMbo.getString("GRAVITA")
                difforstatid=difforConfermaMbo.getLong("X4DIFFORSTATID")
                fase=difforConfermaMbo.getString("FASE")
                difforConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                difforConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                #difforWO=mbo.getMboSet("X4DIFFORMITAWO")
                difforWO=MXServer.getMXServer().getMboSet("X4DIFFOR_WO",MXServer.getMXServer().getSystemUserInfo())
                difforWO.reset()
                newDifforWo=difforWO.add()
                newDifforWo.setValue("IDDIFFORMITA",iddiff,11L)
                newDifforWo.setValue("ASSETNUM",asset,11L)
                newDifforWo.setValue("GRAVITA",gravita,11L)
                newDifforWo.setValue("X4DIFFORSTATID",difforstatid)
                newDifforWo.setValue("WONUM",wonum,11L)
                newDifforWo.setValue("SITEID","SIT01",11L)
                newDifforWo.setValue("FASE","fase",11L)
                difforWO.save()
            #difforConfermaSet.save()
        interfConfermaSet=mbo.getMboSet("$INTERFCONFERMA$","X4INTERFSTAT","(IDINTERFERENZA_EXT IN (SELECT interferenza FROM x4interfatt WHERE attivita=:jpnum) OR IDINTERFERENZA_EXT IN (SELECT IDINTERFERENZA FROM X3CATASSOCIAZIONE WHERE IDORIGINE=:JPNUM OR IDORIGINE=:X4MICROCONTROLLO)) AND(assetnum in (select assetnum from assetancestor where ancestor=:assetnum) or assetnum in (select assetnum from asset where ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum in ((select targetassetnum from assetlocrelation where substr(x3sapid,1,16) in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)))OR assetnum=:assetnum) and status in('VALIDATA', 'DA VALIDARE') and x4interfstatid not in (select x4interfstatid from x4interf_wo where wonum=:wonum)")
        if not interfConfermaSet.isEmpty():
            for j in range (0,interfConfermaSet.count()):
                interfConfermaMbo=interfConfermaSet.getMbo(j)
                idint=interfConfermaMbo.getString("IDINTERFERENZA_EXT")
                asset=interfConfermaMbo.getString("ASSETNUM")
                gravita=interfConfermaMbo.getString("GRAVITA")
                interfstatid=interfConfermaMbo.getLong("X4INTERFSTATID")
                fase=interfConfermaMbo.getString("FASE")
                interfConfermaMbo.setValue("MATRICOLARILEVATORE",user,11L)
                interfConfermaMbo.setValue("ULTIMARILEVAZIONE",MXServer.getMXServer().getDate(),11L)
                #interfWO=mbo.getMboSet("X4INTERFERENZAWO")
                interfWO=MXServer.getMXServer().getMboSet("X4INTERF_WO",MXServer.getMXServer().getSystemUserInfo())
                interfWO.reset()
                newinterfWO=interfWO.add()
                newinterfWO.setValue("IDINTERFERENZA",idint,11L)
                newinterfWO.setValue("ASSETNUM",asset,11L)
                newinterfWO.setValue("GRAVITA",gravita,11L)
                newinterfWO.setValue("X4INTERFSTATID",interfstatid)
                newinterfWO.setValue("WONUM",wonum,11L)
                newinterfWO.setValue("SITEID","SIT01",11L)
                newinterfWO.setValue("FASE",fase,11L)
                interfWO.save()
            #interfConfermaSet.save()"
X4RILEVAZIONIODL,Popolo tabella Rilevazioni OdL,Draft,"from psdi.server import MXServer


lib = service.invokeScript("X4EVALCOND").get("X4EvalCondLib")(service)

stato = mbo.getString("STATUS")
workorderid=mbo.getLong("WORKORDERID")
#rilevazioniSet = MXServer.getMXServer().getMboSet("X4RILEVAZIONIODL", mbo.getUserInfo())
#_query = "workorderid="+str(workorderid)
#rilevazioniSet.setWhere(_query)
#rilevazioniSet.reset()
rilevazioniSet = mbo.getMboSet("$RILODL$","X4RILEVAZIONIODL","workorderid=:workorderid")

def sqlValue(value):
    if value is None:
        return ""
    return str(value).replace("'", "''")


def getMisureWhere(classificationid, jpnum, revnum,micro):
    classid = sqlValue(classificationid)
    jp = sqlValue(jpnum)
    mc = sqlValue(micro)
    if not revnum:
        revnum=''
    origineWhere = "((idorigine='" + jp + "' and PLUSCREVNUM='"+ revnum +"') or idorigine='" + mc + "')"
    return "x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid='" + classid + "') and tipoparametro='Misura' and (not exists (select 1 from x4catassociazionemis where " + origineWhere + ") or idmisura in (select idmisura from x4catassociazionemis where " + origineWhere + "))"


def evalRiferimento(assetMbo):
    classificazioneAsset=assetMbo.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    #riferimento=classificazioneAsset
    riferimento=""
    if classificazioneAsset:
        if classificazioneAsset=="CONDUTTORE" or classificazioneAsset=="ARMAMENTO CONDUTTORE":
            #service.error("TEST",str(classificazioneAsset))
            specSet=assetMbo.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_RIF_POSIZIONE'")
            if not specSet.isEmpty():
                riferimento=specSet.getMbo(0).getString("NUMVALUE")
                
    
        elif classificazioneAsset=="FUNE DI GUARDIA" or classificazioneAsset=="ARMAMENTO FUNE DI GUARDIA":
            specSet=assetMbo.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_RIF_FUNE'")
            if not specSet.isEmpty():
                riferimento=specSet.getMbo(0).getString("ALNVALUE")
        else:
            check1Set=assetMbo.getMboSet("$SP1$","ASSETSPEC","assetnum=:assetnum and assetattrid in ('LIN_RIF_ASSET_ARM_COND','LIN_RIF_ASSET_ARM_FDG','LIN_RIF_ASSET_COND','LIN_RIF_ASSET_FDG')")
            check2Set=assetMbo.getMboSet("$SP2$","CLASSANCESTOR","classstructureid=:classstructureid and ancestorclassid='DISPOSITIVI E COMPONENTI'")
            if not check1Set.isEmpty():
                specSet=check1Set.getMbo(0).getMboSet("$SP3$","ASSETSPEC","assetnum=:tablevalue and assetattrid in('LIN_RIF_FUNE','LIN_RIF_POSIZIONE')")
                if not specSet.isEmpty():
                    if specSet.getMbo(0).getString("ASSETATTRID")=="LIN_RIF_FUNE":
                        riferimento=specSet.getMbo(0).getString("ALNVALUE")
                    else:
                        riferimento=specSet.getMbo(0).getString("NUMVALUE")
            elif not check2Set.isEmpty():
                riferimento=assetMbo.getString("X3RIFERIMENTOFASE")
    return riferimento


if rilevazioniSet.isEmpty():
    assetNum = mbo.getString("ASSETNUM")
    asset_set = mbo.getMboSet("ASSET")
    jpnum=mbo.getString("JPNUM")
    revSet=mbo.getMboSet("$JPNUMREV$","JOBPLAN",":jpnum=jpnum and status='ACTIVE'")
    if not revSet.isEmpty():
        revnum=revSet.getMbo(0).getString("PLUSCREVNUM")
    else:
        revnum=''
    micro=mbo.getString("X4MICROCONTROLLO")
    classIdAsset = asset_set.getMbo(0).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
    if classIdAsset=="CAMPATA AEREA" or classIdAsset=="SOSTEGNO AEREO" or classIdAsset=="ESTREMO CAVO" or classIdAsset=="TRATTA CAVO":
        asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
        asset_ancestorSostSet=asset_set.getMbo(0).getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
        for i in range(0,asset_ancestorSet.count()):
            classNew=True
            assetI=asset_ancestorSet.getMbo(i)
            assetNum=assetI.getString("ASSETNUM")
            assetDesc=assetI.getString("DESCRIPTION")
            classIdFiglio = assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            recordMisExt=assetI.getMboSet("$X4MIS$","X4CATMISURE",getMisureWhere(classIdFiglio,jpnum,revnum,micro))
            if not recordMisExt.isEmpty():
                for m in range (0,recordMisExt.count()):
                    misuraMbo=recordMisExt.getMbo(m)
                    idMis=misuraMbo.getString("IDMISURA")
                    if lib.check(mbo.getUserInfo(),assetNum, "MISURA", idMis):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("ID", idMis)
                        rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("TIPO", "MIS")
                        rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM")[:25])
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        
            queryI = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and GENMISURA=0 and status='ATTIVO' and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diffI$", "X4CATDIFFOR", queryI)
            classNew=True
            if not diffAppl.isEmpty():
                for j in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(j)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    if lib.check(mbo.getUserInfo(),assetNum, "DIFFOR", idDiff):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("ID", idDiff)
                        rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "DIFF")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                    
            queryIntI = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryIntI)
            classNew=True
            if not intAppl.isEmpty():
                for k in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(k)
                    riferimento=None
                    classificazioneAsset=assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
                    idInt = intMbo.getString("IDINTERFERENZA")
                    if lib.check(mbo.getUserInfo(),assetNum, "INTERF", idInt):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("ID", idInt)
                        rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "INT")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
        for iS in range(0,asset_ancestorSostSet.count()):
            assetI=asset_ancestorSostSet.getMbo(iS)
            assetNum=assetI.getString("ASSETNUM")
            assetDesc=assetI.getString("DESCRIPTION")
            classIdFiglio = assetI.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            classNew=True
            recordMisExt=assetI.getMboSet("$X4MIS$","X4CATMISURE",getMisureWhere(classIdFiglio,jpnum,revnum,micro))
            if not recordMisExt.isEmpty():
                for m2 in range (0,recordMisExt.count()):
                    misuraMbo=recordMisExt.getMbo(m2)
                    idMis=misuraMbo.getString("IDMISURA")
                    if lib.check(mbo.getUserInfo(),assetNum, "MISURA", idMis):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                        rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "MIS")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM")[:25])
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
            queryI = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and GENMISURA=0 and  iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diffI$", "X4CATDIFFOR", queryI)
            classNew=True
            if not diffAppl.isEmpty():
                for jS in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(jS)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    if lib.check(mbo.getUserInfo(),assetNum, "DIFFOR", idDiff):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("ID", idDiff)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "DIFF")
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            queryIntI = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdFiglio)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryIntI)
            classNew=True
            if not intAppl.isEmpty():
                for kS in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(kS)
                    idInt = intMbo.getString("IDINTERFERENZA")
                    if lib.check(mbo.getUserInfo(),assetNum, "INTERF", idInt):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetNum)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(assetI)))
                        rilevazioniNew.setValue("ID", idInt)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "INT")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
        rilevazioniSet.save()
    else:
        if mbo.isNull("X4MICROCONTROLLO"):
            asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTOR$","ASSET","(assetnum=:assetnum or ((parent=:parent or parent=:assetnum) and :classstructureid in (select classstructureid from classancestor where ancestorclassid='LINEA' or ancestorclassid='DISPOSITIVI E COMPONENTI'))) and siteid=:siteid")
        else:
            asset_ancestorSet=asset_set.getMbo(0).getMboSet("$AANCESTORTOT$","ASSET","assetnum=:assetnum and siteid=:siteid")
        for b in range (0,asset_ancestorSet.count()):
            classIdAsset=asset_ancestorSet.getMbo(b).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
            assetnumRil=asset_ancestorSet.getMbo(b).getString("ASSETNUM")
            assetDesc=asset_ancestorSet.getMbo(b).getString("DESCRIPTION")
            recordMisExt=asset_ancestorSet.getMbo(b).getMboSet("$X4MIS$","X4CATMISURE",getMisureWhere(classIdAsset,jpnum,revnum,micro))
            classNew=True
            if not recordMisExt.isEmpty():
                for m3 in range (0,recordMisExt.count()):
                    misuraMbo=recordMisExt.getMbo(m3)
                    idMis=misuraMbo.getString("IDMISURA")
                    if lib.check(mbo.getUserInfo(),assetnumRil, "MISURA", idMis):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                        rilevazioniNew.setValue("ID", misuraMbo.getString("IDMISURA"))
                        rilevazioniNew.setValue("DESCRIPTION", misuraMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "MIS")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
                        rilevazioniNew.setValue("WONUM", misuraMbo.getString("UM")[:25])
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
            query = "x4catdifforid in (select x4catdifforid from X4DIFFORLASSIFICATIONS where classificationid='"+str(classIdAsset)+"') and status='ATTIVO' and GENMISURA=0 and iddifformita in (select iddifformita from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            diffAppl = mbo.getMboSet("$diff$", "X4CATDIFFOR", query)
            classNew=True
            if not diffAppl.isEmpty():
                for nm in range (0,diffAppl.count()):
                    diffMbo = diffAppl.getMbo(nm)
                    idDiff = diffMbo.getString("IDDIFFORMITA")
                    if lib.check(mbo.getUserInfo(),assetnumRil, "DIFFOR", idDiff):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("ID", idDiff)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", diffMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "DIFF")
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            queryInt = "x4catinterfid in (select x4catinterfid from X4INTERFCLASSIFICATIONS where classificationid='"+str(classIdAsset)+"') and status='ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idorigine='"+str(jpnum)+"' or idorigine='"+str(micro)+"')"
            intAppl = mbo.getMboSet("$IntI$", "X4CATINTERF", queryInt)
            classNew=True
            if not intAppl.isEmpty():
                for nk in range (0,intAppl.count()):
                    intMbo = intAppl.getMbo(nk)
                    idInt = intMbo.getString("IDINTERFERENZA")
                    if lib.check(mbo.getUserInfo(),assetnumRil, "INTERF", idInt):
                        rilevazioniNew = rilevazioniSet.add()
                        rilevazioniNew.setValue("ASSETNUM", assetnumRil)
                        rilevazioniNew.setValue("ASSETDESC", assetDesc)
                        rilevazioniNew.setValue("RIFERIMENTO", str(evalRiferimento(asset_ancestorSet.getMbo(b))))
                        rilevazioniNew.setValue("ID", idInt)
                        if classNew==True:
                            rilevazioniNew.setValue("CLASSIFICATIONID", "1")
                            classNew=False
                        rilevazioniNew.setValue("DESCRIPTION", intMbo.getString("DESCRIPTION"))
                        rilevazioniNew.setValue("TIPO", "INT")
                        rilevazioniNew.setValue("WORKORDERID", mbo.getLong("WORKORDERID"))
            #rilevazioniSet.save()"
COUNTOPERINTERF,COUNTOPERINTERF,Draft,"from psdi.server import MXServer
from com.terna.common.maximo import MXUtils
from com.terna.common.db import DbUtils
from psdi.util.logging import MXLoggerFactory
from java.lang import System, Exception as javaEx

prefix = "[ COUNTOPERINTERF ] "
logger = MXLoggerFactory.getLogger("maximo.terna")

# Libreria per verifica applicabilita misura
lib = service.invokeScript("X4EVALCOND").get("X4EvalCondLib")(service)

def is_misura_applicable(mbo, assetnum, idmisura_ext):
    try:
        isApplicable = lib.check(mbo.getUserInfo(), assetnum, "MISURA", idmisura_ext)
        logger.debug(prefix + "X4EVALCOND check - ASSETNUM: %s, IDMISURA_EXT: %s, isApplicable: %s"
                     % (assetnum, idmisura_ext, str(isApplicable)))
        return isApplicable
    except javaEx, e:
        logger.error(prefix + "Errore durante verifica X4EVALCOND per asset %s e misura %s"
                     % (assetnum, idmisura_ext))
        logger.error(str(e))
        return False
    except Exception, e:
        logger.error(prefix + "Errore durante verifica X4EVALCOND per asset %s e misura %s"
                     % (assetnum, idmisura_ext))
        logger.error(str(e))
        return False

def getVariabileErrore(mbo, assetnum, misura_id, errorcode, e):
    start = System.currentTimeMillis()
    variabileErroreSet = MXServer.getMXServer().getMboSet("X4VARIABILERRORE", mbo.getUserInfo())
    where_clause = "ASSETNUM = '" + assetnum + "' AND X4CATMISUREID = " + str(misura_id)
    variabileErroreSet.setWhere(where_clause)
    variabileErroreSet.reset()

    if variabileErroreSet.isEmpty():
        variabileErrore = variabileErroreSet.add()
        variabileErrore.setValue("SITEID", "SIT01")
        variabileErrore.setValue("ASSETNUM", assetnum)
        variabileErrore.setValue("X4CATMISUREID", str(misura_id))
        variabileErrore.setValue("RETRY", 0)
    else:
        variabileErrore = variabileErroreSet.getMbo(0)

    variabileErrore.setValue("ERRORCODE", errorcode)
    variabileErrore.setValue("ERROR", str(e))
    variabileErrore.setValue("CREATION_DATE", MXServer.getMXServer().getDate())

    # SALVA il MboSet, NON il singolo Mbo
    variabileErroreSet.save()
    variabileErroreSet.close()  # chiudi per sicurezza
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "getVariabileErrore time taken: " + str(elapsed) + " ms")




# Funzione per recuperare la misura da X4CATMISURE
def get_misura(mbo, asset_type):
    start = System.currentTimeMillis()
    misura_set = MXUtils.getMboSet("X4CATMISURE")
    try:
        where_clause = "CALCOLO = 'COUNTOPERINTERF' AND TIPOLOGIAASSET = '" + asset_type + "'"
        misura_set.setWhere(where_clause)
        misura_set.reset()
        
        if misura_set.isEmpty():
            return None, None, None
            
        misura = misura_set.getMbo(0)
        misura_id = misura.getInt("X4CATMISUREID")
        misura_ext_id = misura.getString("IDMISURA")
        return misura, misura_id, misura_ext_id
    finally:
        misura_set.close()
        elapsed = System.currentTimeMillis() - start
        logger.debug(prefix + "get_misura time taken: " + str(elapsed) + " ms")



# Funzione per eseguire la query dei parametri con DbUtils
def get_parameters(misura_id):
    start = System.currentTimeMillis()
    query = "SELECT * FROM X4CATMISUREPARAMETRI WHERE X4CATMISUREID = " + str(misura_id) + " AND PARAMETER = 'Alta'"
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_parameters time taken: " + str(elapsed) + " ms")
    return DbUtils.runSelect(query)

def get_op_classstructure():
    start = System.currentTimeMillis()
    query = (
        "SELECT classificationid, classstructureid "
        "FROM classstructure "
        "WHERE classificationid IN ('OPERE INTERFERENTI AEREE', 'OPERA INTERFERENTE AEREA')"
    )
    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_op_classstructure time taken: " + str(elapsed) + " ms")

    op_map = {row[0]: row[1] for row in result if row}
    logger.debug(prefix + "get_op_classstructure result: " + str(op_map))

    return op_map



# Funzione per eseguire la query per ottenere il target assetnum
def get_target(service, assetnum, mode, livello=None):
    start = System.currentTimeMillis()
    if mode == 'SOSTEGNO':
        query = (
            "SELECT DISTINCT alr2.targetassetnum "
            "FROM asset a "
            "JOIN asset parent ON a.parent = parent.assetnum "
            "JOIN assetlocrelation alr ON alr.sourceassetnum = parent.x3rootassetnum "
            "  AND alr.targetassetnum = a.ancestor "
            "JOIN assetlocrelation alr2 ON alr2.x3sapid = SUBSTR(alr.x3sapid, 1, 16) "
            "WHERE a.classstructureid = '1038' "
            "  AND parent.x3rootassetnum IS NOT NULL "
            "  AND a.ancestor IS NOT NULL "
            "  AND a.assetnum = '" + assetnum + "'"
        )
    elif mode == 'CAMPATA':
        if livello is None:
            livello = "3" 
        query = (
            "SELECT assetnum "
            "FROM ( "
            "  SELECT assetnum, LEVEL AS livello "
            "  FROM asset "
            "  START WITH assetnum = '" + assetnum + "' "
            "  CONNECT BY PRIOR parent = assetnum "
            ") "
            "WHERE livello = " + str(livello)
        )
    else: 
        return None

    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_target time taken: " + str(elapsed) + " ms")
    if result and result[0][0]:
        return result[0][0]
    return None


# Funzione per ottenere il conteggio delle opere interferenti
def get_interfering_assets_count(service, target_asset_num, use_high_query, cs_id_aerea_lvl4):
    start = System.currentTimeMillis()
    
    if use_high_query:
        logger.info(prefix + "Using high priority query for interfering assets")
        query = (
            "SELECT COUNT(a1.assetnum) "
            "FROM asset a1 "
            "JOIN assetspec ac1 ON ac1.assetnum = a1.assetnum "
            "WHERE ac1.assetattrid = 'LIN_CRITICITA_LIV4_INT' "
            "  AND ac1.numvalue = '1' "
            "  AND a1.ancestor = '" + target_asset_num + "' "
            "  AND a1.classstructureid = '" + cs_id_aerea_lvl4 + "'"
        )
    else:
        logger.info(prefix + "Using standard query for OP")
        query = (
            "SELECT COUNT(a1.assetnum) "
            "FROM asset a1 "
            "WHERE a1.ancestor = '" + target_asset_num + "' "
            "  AND a1.classstructureid = '" + cs_id_aerea_lvl4 + "'"
        )

    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_interfering_assets_count time taken: " + str(elapsed) + " ms")
    logger.debug(prefix + "DEBUG result: " + str(result))
    
    if result and result[0][0]:
        return result[0][0]

    return None



# Funzione per aggiornare il record X4MISUREASSET
def update_or_create_x4_misure_asset(mbo, assetnum, misura_id, misura_ext_id, value):
    # Crea un nuovo MboSet ogni volta (senza alias)
    start = System.currentTimeMillis()
    misura_asset_set = MXServer.getMXServer().getMboSet("X4MISUREASSET", mbo.getUserInfo())
    try:
        misura_asset_set.setWhere("ASSETNUM='%s' AND SITEID='SIT01' AND IDMISURA='%s'" % (assetnum, misura_id))
        misura_asset_set.reset()

        if misura_asset_set.isEmpty():
            x4misureasset = misura_asset_set.add()
            x4misureasset.setValue("PRIMARILEVAZIONE", MXServer.getMXServer().getDate())
            x4misureasset.setValue("ULTIMARILEVAZIONE", MXServer.getMXServer().getDate())
        else:
            x4misureasset = misura_asset_set.getMbo(0)
            x4misureasset.setValue("ULTIMARILEVAZIONE", MXServer.getMXServer().getDate())

        x4misureasset.setValue("SITEID", "SIT01")
        x4misureasset.setValue("ASSETNUM", assetnum)
        x4misureasset.setValue("IDMISURA", str(misura_id))
        x4misureasset.setValue("IDMISURA_EXT", str(misura_ext_id))
        logger.debug(prefix + "update_or_create_x4_misure_asset - ASSETNUM: %s, VALORE: %s" % (assetnum, str(value)))
        if value is not None:
            x4misureasset.setValue("VALORE", value)
        # Salva il MboSet per confermare i cambiamenti
        misura_asset_set.save()
    except javaEx, e:
        logger.error(prefix + "Error durante l'inserimento nella tabella X4MISUREASSET")
        # Inserisce le righe di errore
        variabileErrore = getVariabileErrore(mbo, assetnum, misura_id, "ERRCOI001", e)
    except Exception, e:
        logger.error(prefix + "Error durante l'inserimento nella tabella X4MISUREASSET")
        # Inserisce le righe di errore
        variabileErrore = getVariabileErrore(mbo, assetnum, misura_id, "ERRCOI001", e)
    finally:
        # Rilascio risorse
        misura_asset_set.close()
        elapsed = System.currentTimeMillis() - start
        logger.debug(prefix + "update_or_create_x4_misure_asset time taken: " + str(elapsed) + " ms")



# Funzione principale di elaborazione per ASSET
def process_asset(service, mbo, asset_type, classstructureid, cs_id_aerea_lvl4, cs_id_aerea_lvl3):
    assetnum = mbo.getString("ASSETNUM")
    logger.info(prefix + "Processing asset: " + assetnum + " (ClassStructureID: " + classstructureid + ")")

    misura, misura_id, misura_ext_id = get_misura(mbo, asset_type)

    if not misura:
        logger.info(prefix + "No misura found for type: " + asset_type)
        return

    if not is_misura_applicable(mbo, assetnum, misura_ext_id):
        logger.info(prefix + "Misura %s non applicabile per asset %s" % (misura_ext_id, assetnum))
        return

    try:
        logger.debug(prefix +  "Found misura: IDMISURA = " + misura_ext_id + ", X4CATMISUREID = " + str(misura_id))
        parameters = get_parameters(misura_id)
        use_high_query = parameters and len(parameters) > 0
        logger.debug(prefix +  "use_high_query:  " + str(use_high_query))


        if asset_type == 'ARMAMENTO CONDUTTORE':
            target_asset_num = get_target(service, assetnum, "SOSTEGNO")
        else:
            target_asset_num = get_target(service, assetnum, "CAMPATA")
        
        if not target_asset_num:
            logger.info(prefix + "No target asset found for asset: " + assetnum)
            valore = 0
            update_or_create_x4_misure_asset(mbo, assetnum, misura_id, misura_ext_id, valore)
            variabileErrore = getVariabileErrore(mbo, assetnum, misura_id, "ERRCOI999", "No campata found for this asset")
            return

        logger.debug(prefix + "Target asset found: " + target_asset_num)

        interfering_assets_count = get_interfering_assets_count(service, target_asset_num, use_high_query, cs_id_aerea_lvl4)
        if interfering_assets_count is not None:
            logger.debug(prefix + "OP count: " + str(interfering_assets_count))
            valore = interfering_assets_count.doubleValue()
        else:
            valore = 0
            logger.debug(prefix + "OP count is null, setting valore to None")
        update_or_create_x4_misure_asset(mbo, assetnum, misura_id, misura_ext_id, valore)
    except javaEx, e:
        logger.error(prefix + "Error during the run of the script")
        # Inserisce le righe di errore
        variabileErrore = getVariabileErrore(mbo, assetnum, misura_id, "ERRCOI999", e)
    except Exception, e:
        logger.error(prefix + "Error during the run of the script")
        # Inserisce le righe di errore
        variabileErrore = getVariabileErrore(mbo, assetnum, misura_id, "ERRCOI999", e)


# Funzione per eseguire la query per ottenere figli della campata
def get_figli_livello3(service, assetnum_list):
    if not assetnum_list:
        return []
    start = System.currentTimeMillis()
    # Costruisce la clausola IN: '123','456','789'
    assetnum_in_clause = ""
    for i, num in enumerate(assetnum_list):
        if i > 0:
            assetnum_in_clause += ","
        assetnum_in_clause += "'" + num + "'"

    query = (
        "SELECT assetnum, classstructureid "
        "FROM ( "
        "    SELECT LEVEL AS livello_gerarchia, assetnum, parent, classstructureid "
        "    FROM asset "
        "    START WITH assetnum IN (" + assetnum_in_clause + ") "
        "    CONNECT BY PRIOR assetnum = parent "
        ") "
        "WHERE livello_gerarchia = 3 "
        "  AND classstructureid IN ('1022', '1009', '1014', '1038')"
    )

    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_figli_livello3 time taken: " + str(elapsed) + " ms")
    logger.debug(prefix + "result get_figli_livello3 (concat): " + str(result))
    return result if result else []


def process_op_for_class(service, mbo, target_asset_num, asset_type, asset_list, valCountOp, countOPUrgenti):
    # Recupera la misura per il tipo di asset
    misura, misura_id, misura_ext_id = get_misura(mbo, asset_type)
    if not misura:
        logger.info(prefix + "No misura found for type: " + asset_type)
        return  # Esce dalla funzione se la misura non viene trovata

    logger.info(prefix + "Found misura: IDMISURA = " + misura_ext_id + ", X4CATMISUREID = " + str(misura_id))
    try:
        # Recupera i parametri
        parameters = get_parameters(misura_id)
        use_high_query = parameters and len(parameters) > 0
        logger.info(prefix + "use_high_query:  " + str(use_high_query))
        # Se la query ad alta priorità è abilitata
        if use_high_query:
            if countOPUrgenti is not None:
                logger.debug(prefix + "count OP urgenti: " + str(countOPUrgenti))
                valore = countOPUrgenti.doubleValue()
            else:
                valore = 0
        else:
            valore = valCountOp
        # Per ogni asset con lo specifico classstructureid
        for asset in asset_list:
            if not is_misura_applicable(mbo, asset, misura_ext_id):
                logger.info(prefix + "Misura %s non applicabile per asset %s" % (misura_ext_id, asset))
                continue

            update_or_create_x4_misure_asset(mbo, asset, misura_id, misura_ext_id, valore)
    except javaEx, e:
        logger.error(prefix + "Error during the run of the script")
        # Inserisce le righe di errore
        for asset in asset_list:
            variabileErrore = getVariabileErrore(mbo, asset, misura_id, "ERRCOI999", e)
    except Exception, e:
        logger.error(prefix + "Error during the run of the script")
        # Inserisce le righe di errore
        for asset in asset_list:
            variabileErrore = getVariabileErrore(mbo, asset, misura_id, "ERRCOI999", e)




# Funzione principale di elaborazione per OP
def process_op(service, mbo, classstructureid, parent, cs_id_aerea_lvl4 , cs_id_aerea_lvl3):
    assetnum = mbo.getString("ASSETNUM")
    logger.info(prefix + "Processing asset: " + assetnum + " (ClassstructureID: " + classstructureid + ")")
    if classstructureid == cs_id_aerea_lvl4:
        logger.info(prefix + "Getting target for classstructureid with level 3")
        target_campata = get_target(service, assetnum, "CAMPATA", 3)
    else: 
        logger.info(prefix + "Getting target for classstructureid with level 2")
        target_campata = get_target(service, parent, "CAMPATA", 2)
        
    if not target_campata:
        logger.info(prefix + "No campata found for asset: " + assetnum)
        return
    logger.info(prefix +  "Target asset found: " + target_campata)
    target_sostegno = get_sostegno_by_campata_sap_suffix(service, target_campata)
    target_asset_list = [target_campata]
    # Aggiunge target_sostegno solo se non è None (cioè non nullo)
    if target_sostegno:
        target_asset_list.extend(target_sostegno)
    result = get_figli_livello3(service, target_asset_list)
    if not result:
        logger.info(prefix + "No figli found!")
        return
    
    logger.info(prefix + "figli founds!")
    countOP = get_interfering_assets_count(service, target_campata, False, cs_id_aerea_lvl4)
    logger.debug(prefix + "OP count totali: " + str(countOP))
    valCountOp = countOP.doubleValue()
    countOPUrgenti = get_interfering_assets_count(service, target_campata, True, cs_id_aerea_lvl4)
    
    tipologie_per_class_id = {
    '1014': 'GIUNTO CONDUTTORE',
    '1009': 'CONDUTTORE',
    '1022': 'FUNE DI GUARDIA',
    '1038': 'ARMAMENTO CONDUTTORE'
    }
    # Dizionario per raccogliere assetnum raggruppati per classstructureid
    assets_by_classstructure = {}
    
    for assetnum_res, classstructureid in result:
        if classstructureid not in assets_by_classstructure:
            assets_by_classstructure[classstructureid] = []
        assets_by_classstructure[classstructureid].append(assetnum_res)
    
    for class_id, asset_type in tipologie_per_class_id.items():
        if class_id in assets_by_classstructure:
            logger.info(prefix + "Creating variables for assets with classstructureid = " + str(class_id))
            process_op_for_class(service, mbo, target_campata, asset_type, assets_by_classstructure[class_id], valCountOp, countOPUrgenti)

# Funzione per ottenere il sostegno 
def get_sostegno_by_campata_sap_suffix(service, assetnum):
    query = (
        "SELECT DISTINCT targetassetnum "
        "FROM assetlocrelation "
        "WHERE x3sapid IN ("
        "    SELECT x3sapid || '-S' "
        "    FROM assetlocrelation "
        "    WHERE targetassetnum = '" + assetnum + "'"
        ")"
    )
    result = DbUtils.runSelect(query)
    logger.debug(prefix + "DEBUG result get_target_asset_by_sap_suffix: " + str(result))
    
    if result and len(result) > 0:
        return [row[0] for row in result]  # restituisce tutti i targetassetnum trovati
    else:
        return []



# Esegui la logica per diversi asset type
logger.info(prefix + "START COUNTOPERINTERF")
start = System.currentTimeMillis()
classstructureid_corrente = mbo.getString("CLASSSTRUCTUREID")
op_class_map = get_op_classstructure()
cs_id_aerea_lvl4 = op_class_map.get("OPERA INTERFERENTE AEREA")
cs_id_aerea_lvl3 = op_class_map.get("OPERE INTERFERENTI AEREE")
logger.debug(prefix + "cs_id_aerea_lvl4: " + str(cs_id_aerea_lvl4) + " cs_id_aerea_lvl3: " + str(cs_id_aerea_lvl3))
logger.debug(prefix + "ASSET ClassstructureID: " + classstructureid_corrente)
if classstructureid_corrente == "1038" and mbo.isNew():
    process_asset(service, mbo, "ARMAMENTO CONDUTTORE", classstructureid_corrente, cs_id_aerea_lvl4, cs_id_aerea_lvl3)
elif classstructureid_corrente == "1022" and mbo.isNew():
    process_asset(service, mbo, "FUNE DI GUARDIA", classstructureid_corrente, cs_id_aerea_lvl4, cs_id_aerea_lvl3)
elif classstructureid_corrente == "1009" and mbo.isNew():
    process_asset(service, mbo, "CONDUTTORE", classstructureid_corrente, cs_id_aerea_lvl4, cs_id_aerea_lvl3)
elif classstructureid_corrente == "1014" and mbo.isNew():
    process_asset(service, mbo, "GIUNTO CONDUTTORE", classstructureid_corrente, cs_id_aerea_lvl4, cs_id_aerea_lvl3)
elif (classstructureid_corrente == cs_id_aerea_lvl4 or classstructureid_corrente == cs_id_aerea_lvl3):
    parent = mbo.getString("PARENT")
    process_op(service, mbo, classstructureid_corrente, parent, cs_id_aerea_lvl4, cs_id_aerea_lvl3)
else:
    logger.debug(prefix + "Unhandled ClassStructureID or asset that is not a modified OPERA INTERFERENTE")
elapsed = System.currentTimeMillis() - start
logger.info(prefix + "time taken for COUNTOPERINTERF: " + str(elapsed) + " ms")
logger.info(prefix + "END COUNTOPERINTERF")"
X3FIXSTATUS,RITM0225727: Script per bonifica stati di quarto livello per una linea,Draft,"#---------------------------------------------------------------------------------------------------
#
# X3FIXSTATUS
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------


from psdi.util.logging import MXLoggerFactory
from psdi.mbo import SqlFormat
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from com.terna.common.date import DateUtils
from com.terna.integration.common.asset import AssetKey
from java.util import HashMap
from java.util import Date
from java.lang import StringBuffer
from java.lang import Throwable 
from java.lang import System 

#
# logs an INFO verbose message 
#
def verbose(message, *args):
	if logVerbose:
		info(message, *args)

#
# logs an INFO message 
#
def info(message, *args):
	if rootAssetMbo != None:
		log.info(StringUtils.format("[X3FIXSTATUS(" + rootAssetMbo.getString("x3sapid") + ")] " + message, args))
	else:
		log.info(StringUtils.format("[X3FIXSTATUS] " + message, args))

# 
# logs an ERROR message 
#
def error(message, *args):
	if rootAssetMbo != None:
		log.error(StringUtils.format("[X3FIXSTATUS(" + rootAssetMbo.getString("x3sapid") + ")] " + message, args))
	else:
		log.error(StringUtils.format("[X3FIXSTATUS] " + message, args))

def evalRootAsset(rootAssetMbo):
	start = System.currentTimeMillis()
	changed = 0
	failed = 0
	failures = StringBuffer()
	ancestorAssetSet = MXUtils.getMboSet("ASSET")
	assetSet = MXUtils.getMboSet("ASSET")
	assetChangeStatusSet = MXUtils.getMboSet("ASSET")

	try:
		# build the query to select assets
		query =  '''
		(siteid, assetnum) in
			(
			select a.siteid, a.assetnum
			from asset a, classstructure c, asset l2, classstructure c2, assetlocrelation alr, asset l1, assetancestor aa
			where
			l1.siteid = alr.siteid and l1.assetnum = alr.sourceassetnum and
			alr.siteid = l2.siteid and alr.targetassetnum = l2.assetnum and
			a.classstructureid = c.classstructureid and
			l2.classstructureid = c2.classstructureid and
			aa.assetnum = a.assetnum and aa.hierarchylevels = 2 AND l2.assetnum = aa.ancestor AND
			l2.status <> a.status and
			c2.classificationid in ('CAMPATA AEREA', 'SOSTEGNO AEREO', 'TRATTA CAVO', 'ESTREMO CAVO') and
			c.classificationid not in ('CONDUTTORI', 'FUNI DI GUARDIA', 'GIUNTI', 'TERMINALI', 'ATTACCHI CONDUTTORE', 'TELAIO', 'CAVI', 'OPERA INTERFERENTE AEREA', 'OPERA INTERFERENTE CAVO', 'OPERE INTERFERENTI AEREE', 'OPERE INTERFERENTI CAVI')
			and (l2.status, a.STATUS) in (
			('ATTV', 'DATT'), ('CEAL', 'DATT'), ('DEMO', 'DATT'), ('DEMO', 'ESER'), ('NADI', 'DATT'), ('NAND', 'DATT'))
			and l1.x3sapid = :x3sapid
			)'''
		sqlFormat =  SqlFormat(rootAssetMbo, query)
		where = sqlFormat.format()
		
		# get the assets		
		info("selecting assets to fix")
		assetSet.setWhere(where)
		verbose("getting number of assets to be fixed")
		n = assetSet.count()
		info("{0} assets need to be fixed", n)
		# go thru assets
		i = 0
		while i < n:
			asset = assetSet.getMbo(i)
			assetKey = AssetKey(asset)
			verbose("fixing asset {0}", assetKey) 
			# get the ancestor
			verbose("look-up for asset ancestor for asset {0}", assetKey)
			query = "(siteid, assetnum) in ( select siteid, ancestor from assetancestor where siteid = :siteid and assetnum = :assetnum and hierarchylevels = 2)"
			sqlFormat =  SqlFormat(asset, query)
			where = sqlFormat.format()
			ancestorAssetSet.setWhere(where)
			ancestorAssetSet.reset()
			ancestor = ancestorAssetSet.getMbo(0)
			if ancestor == None:
				error("unable to find ancestor for asset {0}", assetKey)
				
			if ancestor != None:
				# get status
				ancestorKey = AssetKey(ancestor)
				ancestorStatus = ancestor.getString("status")
				assetStatus = asset.getString("status")
				verbose("getting new status for asset {0}.{1}, ancestor {2}.{3}", assetKey, assetStatus, ancestorKey, ancestorStatus)
				newAssetStatus = None
				if (ancestorStatus, assetStatus) == ("ATTV", "DATT"):
					newAssetStatus = "ESER"
				elif (ancestorStatus, assetStatus) == ("CEAL", "DATT"):
					newAssetStatus = "DIST"				
				elif (ancestorStatus, assetStatus) == ("DEMO", "DATT"):
					newAssetStatus = "DIST"				
				elif (ancestorStatus, assetStatus) == ("DEMO", "ESER"):
					newAssetStatus = "DIST"				
				elif (ancestorStatus, assetStatus) == ("NADI", "DATT"):
					newAssetStatus = "ESER"	
				elif (ancestorStatus, assetStatus) == ("NAND", "DATT"):
					newAssetStatus = "ESER"						
				else:
					error("unable to map new status for asset {0}.{1}, ancestor {2}.{3}", assetKey, assetStatus, ancestorKey, ancestorStatus)
					failed = failed + 1
				if newAssetStatus != None:
					# change status
					try:
						info("[{0}/{1}] changing status for asset {2}. {3} -> {4}", (i+1), n, assetKey, assetStatus, newAssetStatus)
						# get the asset in a dedicated set
						sqlFormat =  SqlFormat(asset, "siteid = :siteid and assetnum = :assetnum")
						assetChangeStatusSet.setWhere(sqlFormat.format())
						assetChangeStatusSet.reset()
						assetChangeStatus = assetChangeStatusSet.getMbo(0)
						# if not found can be discared
						if assetChangeStatus != None:
							# reload status
							assetStatus = assetChangeStatus.getString("status")
							if assetStatus != newAssetStatus:
								assetChangeStatus.changeStatus(newAssetStatus, DateUtils.now(), "Stato aggiornato per bonifica RITM0225727", MboConstants.NOACCESSCHECK)
								assetChangeStatusSet.save()
							else:
								failures.append(StringUtils.format("{0}: same status {1}, skipped\n", assetKey, assetStatus, newAssetStatus))
								info("asset {0} has status {1} and is required to change to the same status {2}", assetKey, assetStatus, newAssetStatus)
							changed = changed + 1	
					except Throwable as t:
						error("unable to fix asset {0}. {1} -> {2}", assetKey, assetStatus, newAssetStatus)					
						verbose("cause {0}", t.getMessage())
						failures.append(StringUtils.format("{0}: {1}\n", assetKey, StringUtils.safe(t.getMessage())))
						failed = failed + 1
			
			# move to next
			i = i + 1
	finally:
		finish = System.currentTimeMillis()
		verbose("cleaning sets")
		assetSet.close()
		ancestorAssetSet.close()
		assetChangeStatusSet.close()
		# updating statistics
		try:
			info("updating statistics")
			fixSet = MXUtils.getMboSet("X3ASSETSTATUSFIX")
			sqlFormat = SqlFormat(rootAssetMbo, "siteid = :siteid and assetnum = :assetnum")
			fixSet.setWhere(sqlFormat.format())
			fix = fixSet.getMbo(0)
			if fix == None:
				fix = fixSet.add()
				fix.setValue("orgid", rootAssetMbo.getString("orgid"))
				fix.setValue("siteid", rootAssetMbo.getString("siteid"))
				fix.setValue("assetnum", rootAssetMbo.getString("assetnum"))
				fix.setValue("sapid", rootAssetMbo.getString("x3sapid"))
				fix.setValue("description", rootAssetMbo.getString("description"))
			fix.setValue("elapsed", DateUtils.format(finish-start))
			fix.setValue("startdate", Date(start))
			fix.setValue("enddate", Date(finish))
			fix.setValue("total", n)
			fix.setValue("fixed", changed)
			fix.setValue("failed", failed)
			fix.setValue("errors", failures.toString())
			fixSet.save()			
			fixSet.close()
			info("statistics updated")
		except Throwable as t:
			error("unable to update statistics cause {0}", StringUtils.safe(t.getMessage()))


# get the logger
log = MXLoggerFactory.getLogger("maximo.fix")
st = System.currentTimeMillis()		
info("evaluating first level {0}", rootAssetMbo.getString("x3sapid"))
result = evalRootAsset(rootAssetMbo)
info("changed {0}, failed {1}, failures {2}", result["changed"], result["failed"], result["failures"])"
X3FIXSTATUSMAIN,RITM0225727: Script per l'avvio della bonifica degli stati degi asset di quarto livello,Draft,"#---------------------------------------------------------------------------------------------------
#
# X3FIXSTATUSMAIN
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------


from psdi.util.logging import MXLoggerFactory
from psdi.mbo import SqlFormat
from psdi.server import MXServer
from psdi.mbo import MboConstants
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from com.terna.common.date import DateUtils
from com.terna.integration.common.asset import AssetKey
from java.util import HashMap
from java.lang import Throwable 
from java.lang import System 
from java.util import Date

#
# logs an INFO verbose message 
#
def verbose(message, *args):
	if logVerbose:
		info(message, *args)

#
# logs an INFO message 
#
def info(message, *args):
	log.info(StringUtils.format("[X3FIXSTATUSMAIN] " + message, args))

# 
# logs an ERROR message 
#
def error(message, *args):
	log.error(StringUtils.format("[X3FIXSTATUSMAIN] " + message, args))

	
st = System.currentTimeMillis()
# if True the processed lines will be excluded
excludeProcessedLines = False
# number of max threads for the pool executor
maxThreads = 10
# number of first levels to evaluate
maxRun = 6000
# number of max assets to be fixed
maxFixed = 25000
# if True verbose logging is enabled		
logVerbose = False
# store the asset MBO which is being fixed
rootAssetMbo = None
# get the logger
log = MXLoggerFactory.getLogger("maximo.fix")

info("starting asset status fix procedure")
# get pool executor
scriptPoolExecutor = service.invokeScript("X4SCRIPTPOOLEXECUTOR").get("X4SCRIPTPOOLEXECUTOR")(service, "X3FIXSTATUS", maxThreads, log, "[X4SCRIPTPOOLEXECUTOR]")	
# prepare set for fix counting
startDate = DateUtils.now()
fixSet = MXUtils.getMboSet("X3ASSETSTATUSFIX")
su = MXServer.getMXServer().getSystemUserInfo()
sqlFormat = SqlFormat(su.getLocale(), su.getTimeZone(), "enddate >= :0")
sqlFormat.setTimestamp(0, startDate)
fixSet.setWhere(sqlFormat.format())
info("fixed lines will start from {0} query {1}", DateUtils.format(startDate), sqlFormat.format())

# get root assets
rootAssetSet = MXUtils.getMboSet("ASSET")
query = "classstructureid in (select classstructureid from classstructure where classificationid = 'LINEA')"
if excludeProcessedLines:
	query = query + " and x3sapid not in (select sapid from x3assetstatusfix)"
rootAssetSet.setWhere(query)
i = 0
rootAssetMbo = rootAssetSet.getMbo(0)
while rootAssetMbo != None and i < maxRun:
	# before starting evaluation, check how much fixes have been done so far
	fixSet.reset()
	fixed = fixSet.sum("fixed")
	info("[{0}] fixed {1} assets so far from {2}", (i+1), int(fixed), DateUtils.format(startDate))
	if fixed > maxFixed:
		info("[{0}] {1} fixed assets so far, required to fix {2}, give up...", (i+1), int(fixed), maxFixed)
		break
	sapId = rootAssetMbo.getString("x3sapid")
	info("[{0}] starting evaluation for first level {1}", (i+1), sapId)
	ctx = HashMap()
	ctx.put("rootAssetMbo", rootAssetMbo)
	ctx.put("logVerbose", logVerbose)
	scriptPoolExecutor.startThread(ctx)
	# move on
	i = i + 1
	rootAssetMbo = rootAssetSet.getMbo(i)

# wait all threads
rootAssetMbo = None
info("waiting for pool completion")
scriptPoolExecutor.waitAllThreads()
info("pool execution completed")

# dump statistics
fixSet.reset()
fixed = fixSet.sum("fixed")
elapsed = System.currentTimeMillis() - st
info("procedure complete in {0}", DateUtils.format(elapsed))
info("fixed {0} assets for {1} first levels", int(fixed), fixSet.count())
if fixed != 0:
	info("average per asset {0} ms", int(elapsed /fixed))

if logVerbose:
	info("")
	fixMbo = fixSet.getMbo(0)
	i = 0
	while fixMbo != None:
		verbose("[{0}]\t {1} {2} {3}\t{4}\t{5}", (i+1), fixMbo.getString("sapid"), fixMbo.getString("elapsed"), fixMbo.getInt("total"), fixMbo.getInt("fixed"), fixMbo.getInt("failed"))
		i = i + 1
		fixMbo = fixSet.getMbo(i)

# clean up
rootAssetSet.close()
fixSet.close()"
X4CHECKSPECOPINT,Check obbligatorietà campi op. int,Draft,"from psdi.mbo import MboConstants
from psdi.server import MXServer

'''
if interactive and mbo.getString("ASSETATTRID")=="LIN_ENTE_GESTORE_INT" or mbo.getString("ASSETATTRID")=="LIN_SAPID_LINEA_INT":
    if mbo.getString("ASSETATTRID")=="LIN_ENTE_GESTORE_INT":
        mboValue = mbo.getMboValue("ALNVALUE")
        prevValue = mboValue.getPreviousValue().asString()
        if not mbo.isNull("ALNVALUE") and mbo.getString("ALNVALUE")=="TERNA" and prevValue:
            checSet = mbo.getMboSet("$ASPEOPINT$","ASSETSPEC","assetnum=:assetnum and assetattrid='LIN_SAPID_LINEA_INT'")
            if not checSet.isEmpty():
                checkMbo=checSet.getMbo(0)
                if checkMbo.isNull("TABLEVALUE"):
                    checkMbo.setFieldFlag("TABLEVALUE", MboConstants.READONLY, False);
                    service.error("x3terna","ErrorMsg",["Necessario compilare l'attributo della linea interferente."])
'''
if interactive and mbo.getString("ASSETATTRID")=="LIN_ENTE_GESTORE_INT" and mbo.isModified("ALNVALUE") and mbo.getString("ALNVALUE")=="RETE":
    assetnum=mbo.getString("ASSETNUM")
    sapIDintSet = MXServer.getMXServer().getMboSet("ASSETSPEC", MXServer.getMXServer().getSystemUserInfo())
    sapIDintSet.setWhere("assetnum="+str(assetnum)+" and assetattrid='LIN_SAPID_LINEA_INT' and TABLEVALUE is not null")
    sapIDintSet.reset()
    if not sapIDintSet.isEmpty():
        checkMbo=sapIDintSet.getMbo(0)
        checkMbo.setValueNull("TABLEVALUE")
        sapIDintSet.save()"
X4WONONESE,Gestione difformità per odl non eseguito,Draft,"from psdi.server import MXServer
if mbo.isModified("STATUS") and mbo.getString("STATUS")=="NON VALIDATO":
    jobplan_set = mbo.getMboSet("JOBPLAN")
    if jobplan_set.count():
        jobplan = jobplan_set.getMbo(0)
        
        #Difformita
        x4diffatt_set = jobplan.getMboSet("X4DIFFATT")
        for i in range(0, x4diffatt_set.count()):
            x4diffatt = x4diffatt_set.getMbo(i)
            assetSet=mbo.getMboSet("ASSET")
            assetMbo=assetSet.getMbo(0)
            asset_ancestorSet=assetMbo.getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or parent=:parent or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
            #asset_ancestorSostSet=mbo.getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
            for i2 in range(0, asset_ancestorSet.count()):
                assetChildMbo=asset_ancestorSet.getMbo(i2)
                query_raw_string = "IDDIFFORMITA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                x4difforstat_set = x4diffatt.getMboSet("$X4DIFFATT2X4DIFFORSTAT", "X4DIFFORSTAT", query_raw_string % (
                    x4diffatt.getString("DIFFORMITA"),
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA')",
                ))
                for j in range(0, x4difforstat_set.count()):
                    x4difforstat = x4difforstat_set.getMbo(j)
                    x4difforstat.setValue("STATUS", "SOSPESA",11L)
                    #x4difforstat.setValue("ODLRISOLUTORE", mbo.getString("WONUM"),11L)
                x4difforstat_set.save()
                x4difforstat_set.close()        x4diffatt_set.save()
        x4diffatt_set.close()
    
        # Interferenze
        x4interfatt_set = jobplan.getMboSet("X4INTERFATT")
        for l in range(0, x4interfatt_set.count()):
            x4interf_mbo = x4interfatt_set.getMbo(l)
            assetSet=mbo.getMboSet("ASSET")
            assetMbo=assetSet.getMbo(0)
            asset_ancestorSet=assetMbo.getMboSet("$AANCESTOR$","ASSET","assetnum IN ( SELECT q.assetnum FROM asset q WHERE (q.ancestor= :assetnum or parent=:parent or q.assetnum= :assetnum) and q.siteid= :siteid union select k.ASSETNUM from asset  k where ( k.assetnum in (select targetassetnum from assetlocrelation p where  p.targetassetnum = k.assetnum AND substr(x3sapid,1, length(x3sapid)-2) in (select x3sapid from assetlocrelation where targetassetnum= :assetnum ))) and siteid= :siteid)")
            #asset_ancestorSostSet=mbo.getMboSet("$ASOSTANCESTOR$","ASSET","(ancestor in (select targetassetnum from assetlocrelation where substr(x3sapid,1, LENGTH(X3SAPID)-2) in (select x3sapid from assetlocrelation where targetassetnum=:assetnum))) and siteid=:siteid")
            for l2 in range(0, asset_ancestorSet.count()):
                assetChildMbo=asset_ancestorSet.getMbo(l2)
                query_raw_string = "IDINTERFERENZA_EXT='%s' AND ASSETNUM='%s' AND STATUS IN %s"
                x4interfstat_set = MXServer.getMXServer().getMboSet("X4INTERFSTAT", mbo.getUserInfo())
                x4interfstat_set.setWhere(query_raw_string % (
                    x4interf_mbo.getString("INTERFERENZA"), 
                    assetChildMbo.getString("ASSETNUM"), 
                    "('DA VALIDARE', 'VALIDATA')",
                ))
                for m in range(0, x4interfstat_set.count()):
                    x4interfstat = x4interfstat_set.getMbo(m)
                    x4interfstat.setValue("STATUS", "SOSPESA",11L)
                    #x4interfstat.setValue("ODLRISOLUTORE", mbo.getString("WONUM"),11L)
                x4interfstat_set.save()
                x4interfstat_set.close()
        x4interfatt_set.save()
        x4interfatt_set.close()
    jobplan_set.close()"
COUNTOPERINTERF_TRANS,COUNTOPERINTERF_TRANS,Draft,"# COUNTOPERINTERF_TRANS v. 14/10/2026 - salta gli asset non applicabili (X4EVALCOND.check), gestione errori negli except
from psdi.server import MXServer
from com.terna.common.exception import MXLiteralException
from java.util import HashMap
from psdi.util.logging import MXLoggerFactory
from java.lang import System, Exception as javaEx
from com.terna.common.db import DbUtils
from com.terna.common.maximo import MXUtils
from java.lang import Throwable

prefix = "[ COUNTOPERINTERF_TRANS ] "
logger = MXLoggerFactory.getLogger("maximo.terna")

def get_op_classstructure():
    start = System.currentTimeMillis()
    query = (
        "SELECT classificationid, classstructureid "
        "FROM classstructure "
        "WHERE classificationid IN ('OPERE INTERFERENTI AEREE', 'OPERA INTERFERENTE AEREA')"
    )
    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_op_classstructure time taken: " + str(elapsed) + " ms")

    op_map = {row[0]: row[1] for row in result if row}
    logger.debug(prefix + "get_op_classstructure result: " + str(op_map))

    return op_map

# Funzione per eseguire la query dei parametri con DbUtils
def get_parameters(misura_id):
    start = System.currentTimeMillis()
    query = "SELECT * FROM X4CATMISUREPARAMETRI WHERE X4CATMISUREID = " + str(misura_id) + " AND PARAMETER = 'Alta'"
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_parameters time taken: " + str(elapsed) + " ms")
    return DbUtils.runSelect(query)

# Funzione per eseguire la query per ottenere il target assetnum
def get_target(assetnum, mode, livello=None):
    start = System.currentTimeMillis()
    if mode == 'SOSTEGNO':
        query = (
            "SELECT DISTINCT alr2.targetassetnum "
            "FROM asset a "
            "JOIN asset parent ON a.parent = parent.assetnum "
            "JOIN assetlocrelation alr ON alr.sourceassetnum = parent.x3rootassetnum "
            "  AND alr.targetassetnum = a.ancestor "
            "JOIN assetlocrelation alr2 ON alr2.x3sapid = SUBSTR(alr.x3sapid, 1, 16) "
            "WHERE a.classstructureid = '1038' "
            "  AND parent.x3rootassetnum IS NOT NULL "
            "  AND a.ancestor IS NOT NULL "
            "  AND a.assetnum = '" + assetnum + "'"
        )
    elif mode == 'CAMPATA':
        if livello is None:
            livello = "3" 
        query = (
            "SELECT assetnum "
            "FROM ( "
            "  SELECT assetnum, LEVEL AS livello "
            "  FROM asset "
            "  START WITH assetnum = '" + assetnum + "' "
            "  CONNECT BY PRIOR parent = assetnum "
            ") "
            "WHERE livello = " + str(livello)
        )
    else: 
        return None

    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_target time taken: " + str(elapsed) + " ms")
    if result and result[0][0]:
        return result[0][0]
    return None

def get_interfering_assets_count(target_asset_num, use_high_query, cs_id_aerea_lvl4):
    start = System.currentTimeMillis()
    
    if use_high_query:
        logger.info(prefix + "Using high priority query for interfering assets")
        query = (
            "SELECT COUNT(a1.assetnum) "
            "FROM asset a1 "
            "JOIN assetspec ac1 ON ac1.assetnum = a1.assetnum "
            "WHERE ac1.assetattrid = 'LIN_CRITICITA_LIV4_INT' "
            "  AND ac1.numvalue = '1' "
            "  AND a1.ancestor = '" + target_asset_num + "' "
            "  AND a1.classstructureid = '" + cs_id_aerea_lvl4 + "'"
        )
    else:
        logger.info(prefix + "Using standard query for OP")
        query = (
            "SELECT COUNT(a1.assetnum) "
            "FROM asset a1 "
            "WHERE a1.ancestor = '" + target_asset_num + "' "
            "  AND a1.classstructureid = '" + cs_id_aerea_lvl4 + "'"
        )

    result = DbUtils.runSelect(query)
    elapsed = System.currentTimeMillis() - start
    logger.debug(prefix + "get_interfering_assets_count time taken: " + str(elapsed) + " ms")
    logger.debug(prefix + "DEBUG result: " + str(result))
    
    if result and result[0][0]:
        return result[0][0]

    return None

# Funzione per aggiornare il record X4MISUREASSET
def update_or_create_x4_misure_asset(assetnum, misura_id, misura_ext_id, value):
    # Crea un nuovo MboSet ogni volta (senza alias)
    start = System.currentTimeMillis()
    mxServer = MXServer.getMXServer()
    systemUserInfo = mxServer.getSystemUserInfo()
    misura_asset_set = mxServer.getMboSet("X4MISUREASSET", systemUserInfo)
    misura_asset_set.setWhere("ASSETNUM='%s' AND SITEID='SIT01' AND IDMISURA='%s'" % (assetnum, misura_id))
    misura_asset_set.reset()

    if misura_asset_set.isEmpty():
        x4misureasset = misura_asset_set.add()
        x4misureasset.setValue("PRIMARILEVAZIONE", MXServer.getMXServer().getDate())
        x4misureasset.setValue("ULTIMARILEVAZIONE", MXServer.getMXServer().getDate())
    else:
        x4misureasset = misura_asset_set.getMbo(0)
        x4misureasset.setValue("ULTIMARILEVAZIONE", MXServer.getMXServer().getDate())

    x4misureasset.setValue("SITEID", "SIT01")
    x4misureasset.setValue("ASSETNUM", assetnum)
    x4misureasset.setValue("IDMISURA", str(misura_id))
    x4misureasset.setValue("IDMISURA_EXT", str(misura_ext_id))
    logger.info(prefix + "update_or_create_x4_misure_asset - ASSETNUM: %s, VALORE: %s" % (assetnum, str(value)))
    if value is not None:
        x4misureasset.setValue("VALORE", value)
    # Salva il MboSet per confermare i cambiamenti
    misura_asset_set.save()
    misura_asset_set.close()

def process_asset(ctx, cs_id_aerea_lvl4, cs_id_aerea_lvl3):
    assetnum = ctx.get("assetnum")
    asset_type = ctx.get("TIPOLOGIAASSET")
    misura_id = ctx.get("CATMISUREID")
    misura_ext_id = ctx.get("VAR_NAME")
    parameters = get_parameters(misura_id)
    use_high_query = parameters and len(parameters) > 0
    logger.debug(prefix +  "use_high_query:  " + str(use_high_query))


    if asset_type == 'ARMAMENTO CONDUTTORE':
        target_asset_num = get_target(assetnum, "SOSTEGNO")
    else:
        target_asset_num = get_target(assetnum, "CAMPATA")
    
    if not target_asset_num:
        logger.info(prefix + "No target asset found for asset: " + assetnum)
        valore = 0
        update_or_create_x4_misure_asset(assetnum, misura_id, misura_ext_id, valore)
        return {
            "status": "ERROR",
            "description": "No target asset found for asset: {}".format(assetnum)
        }

    logger.debug(prefix + "Target asset found: " + target_asset_num)

    interfering_assets_count = get_interfering_assets_count(target_asset_num, use_high_query, cs_id_aerea_lvl4)
    if interfering_assets_count is not None:
        logger.info(prefix + "OP count: " + str(interfering_assets_count))
        valore = interfering_assets_count.doubleValue()
    else:
        valore = 0
        logger.info(prefix + "OP count is null, setting valore to None")
    update_or_create_x4_misure_asset(assetnum, misura_id, misura_ext_id, valore)
    return {
        "status": "SUCCESS"
    }

# Applicabilita': si valuta solo se la misura ha condizioni in X4CARDMISURE.
# Gli asset esclusi chiudono con SUCCESS / NON APPLICABILE; se la verifica
# fallisce l'asset viene calcolato.
def has_conditions(misura_id):
    cardSet = MXServer.getMXServer().getMboSet("X4CARDMISURE", MXServer.getMXServer().getSystemUserInfo())
    try:
        cardSet.setWhere("X4CATMISUREID = %d" % misura_id)
        cardSet.reset()
        return not cardSet.isEmpty()
    finally:
        cardSet.close()

# Recupera i parametri
idmisura = request.getQueryParam("PARAMETER1")
chunkid = request.getQueryParam("CHUNKID")
loaded = request.getQueryParam("LOADED")
# Coda da leggere e su cui riscrivere l'esito: la manda eam-etl come query
# param. Se non arriva si usa quella del notturno, quindi il comportamento
# dell'elaborazione periodica resta identico.
#
# L'elenco e' chiuso di proposito: il valore arriva da un query param e
# finisce in un nome di oggetto e dentro una where clause. Qualunque altro
# valore viene rifiutato.
STAGING_DEFAULT = "X4MISUREASSETSTAGING"
STAGING_AMMESSE = (STAGING_DEFAULT, "X4MISUREASSETSTAGING_OD")
staging_obj = request.getQueryParam("STAGING")
if not staging_obj:
	staging_obj = STAGING_DEFAULT
elif staging_obj not in STAGING_AMMESSE:
	service.error("x3terna", "ErrorMsg", ["Valore STAGING non ammesso: %s" % staging_obj])
if idmisura == None or chunkid == None:
	raise MXLiteralException("idmisura, chunkid sono parametri obbligatori")
mxServer = MXServer.getMXServer()
systemUserInfo = mxServer.getSystemUserInfo()
lib = service.invokeScript("X4EVALCOND").get("X4EvalCondLib")(service)
	
# Recupera righe da X4MISUREASSETSTAGING
stagingSet = mxServer.getMboSet(staging_obj, systemUserInfo)
whereClause = "X4CATMISUREID = '%s' and chunk_id = '%s'" % (idmisura, chunkid)
if loaded:
    whereClause += " and loaded = '%s'" % loaded
stagingSet.setWhere(whereClause)

# Recupera il CATMISUREID
catMisureSet = mxServer.getMboSet("X4CATMISURE", systemUserInfo)
catMisureSet.setWhere("X4CATMISUREID = '%s'" % idmisura)

catMisure = catMisureSet.moveFirst()
if catMisure is None:
    catMisureSet.close()
    raise MXLiteralException("ERRHMC005", "Misura %s non trovata in X4CATMISURE" % idmisura)

idmisura_ext = catMisure.getString("IDMISURA")

CATMISUREID = catMisure.getLong("X4CATMISUREID")
assettype = catMisure.getString("TIPOLOGIAASSET")
conCondizioni = has_conditions(CATMISUREID)
catMisureSet.close()

op_class_map = get_op_classstructure()
cs_id_aerea_lvl4 = op_class_map.get("OPERA INTERFERENTE AEREA")
cs_id_aerea_lvl3 = op_class_map.get("OPERE INTERFERENTI AEREE")

# Itera sulle righe di staging
stagingMbo = stagingSet.moveFirst()
while stagingMbo:
    assetnum = stagingMbo.getString("ASSETNUM")

    assetSet = stagingMbo.getMboSet("$ASSETREL123$","ASSET",":ASSETNUM=ASSETNUM")
    assetSet.reset()
    asset = assetSet.moveFirst()

    isApplicable = True
    if asset and conCondizioni:
        try:
            isApplicable = lib.check(systemUserInfo, assetnum, "MISURA", idmisura_ext)
        except (javaEx, Exception) as ce:
            logger.error(prefix + "Errore verifica applicabilita' per %s asset %s: %s (procedo come applicabile)" % (idmisura_ext, assetnum, ce))
            isApplicable = True

    if asset and not isApplicable:
        stagingMbo.setValue("RESULT", "SUCCESS")
        stagingMbo.setValue("RESULT_DESCRIPTION", "NON APPLICABILE")
    elif asset:
        ctx = HashMap()
        ctx.put("assetnum", assetnum)
        ctx.put("VAR_NAME", idmisura_ext)
        ctx.put("CATMISUREID", CATMISUREID)
        ctx.put("TIPOLOGIAASSET", assettype)
        try:
            result = process_asset(ctx, cs_id_aerea_lvl4, cs_id_aerea_lvl3)
            if result.get("status") == "SUCCESS":
                stagingMbo.setValue("RESULT", "SUCCESS")
            else:
                stagingMbo.setValue("RESULT", "ERROR")
                stagingMbo.setValue("RESULT_DESCRIPTION", str(result.get("description", "")[:99]))
        except javaEx as e:
            errorMessage = str(e.getMessage())
            logger.error(prefix + errorMessage)
            stagingMbo.setValue("RESULT", "ERROR")
            stagingMbo.setValue("RESULT_DESCRIPTION", str(errorMessage[:99]))
        except Exception as e:
            errorMessage = str(e)
            logger.error(prefix + errorMessage)
            stagingMbo.setValue("RESULT", "ERROR")
            stagingMbo.setValue("RESULT_DESCRIPTION", str(errorMessage[:99]))
    else:
        stagingMbo.setValue("RESULT", "ERROR")
        stagingMbo.setValue("RESULT_DESCRIPTION", "Asset '%s' non trovato" % assetnum)

    assetSet.close()
    stagingMbo = stagingSet.moveNext()

stagingSet.save()
stagingSet.close()