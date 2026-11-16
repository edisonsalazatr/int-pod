from java.io import BufferedReader, InputStreamReader, OutputStreamWriter, StringReader
from java.net import HttpURLConnection, URL
from psdi.mbo import MboConstants
from psdi.server import MXServer
from javax.xml.parsers import DocumentBuilderFactory
from org.xml.sax import InputSource
from java.util import Base64
from java.text import SimpleDateFormat

if not mbo.isNull("X4ODM") or not mbo.isNull("X4NTK"):
    service.error("x3terna","ErrorMsg", ["Impossibile procedere con la creazione."] )
mapping = {
    ("AM-LGP","IMPIEGAT"): "AMPAM_I",
    ("AM-LGP","QUADRO"): "AMPAM_Q",
    ("AM-PC","IMPIEGAT"): "AMPC_I",
    ("AM-PRO","IMPIEGAT"): "AMPRO_I",
    ("AM-PRO","QUADRO"): "AMPRO_Q",
    ("AM-SPM","IMPIEGAT"): "AMSPM_I",
    ("AM-SPM","QUADRO"): "AMSPM_Q",
    ("AM-SPM","IMPIEGAT"): "SPMPRV_I",
    ("ATCEN-COA","IMPIEGAT"): "CENCOA_I",
    ("ATCEN-COA","QUADRO"): "CENCOA_Q",
    ("ATCEN-MPL","IMPIEGAT"): "CENMPL_I",
    ("ATCEN-MPL","QUADRO"): "CENMPL_Q",
    ("ATCEN-RL","IMPIEGAT"): "ATCERL_I",
    ("ATCEN-RL","QUADRO"): "ATCERL_Q",
    ("ATCNR-COA","IMPIEGAT"): "CNRCOA_I",
    ("ATCNR-COA","QUADRO"): "CNRCOA_Q",
    ("ATCNR-MPL","IMPIEGAT"): "CNRMPL_I",
    ("ATCNR-MPL","QUADRO"): "CNRMPL_Q",
    ("ATCNR-RL","IMPIEGAT"): "ATCNRL_I",
    ("ATCNR-RL","QUADRO"): "ATCNRL_Q",
    ("ATNES-COA","IMPIEGAT"): "NESCOA_I",
    ("ATNES-COA","QUADRO"): "NESCOA_Q",
    ("ATNES-MPL","IMPIEGAT"): "NESMPL_I",
    ("ATNES-MPL","QUADRO"): "NESMPL_Q",
    ("ATNES-RL","QUADRO"): "DTNEP_Q",
    ("ATNOR-COA","IMPIEGAT"): "DPNDCO_I",
    ("ATNOR-COA","QUADRO"): "ATNOCO_Q",
    ("ATNOR-MPL","IMPIEGAT"): "NORMPL_I",
    ("ATNOR-MPL","QUADRO"): "NORMPL_Q",
    ("ATNOR-RL","IMPIEGAT"): "ATNRRL_I",
    ("ATNOR-RL","QUADRO"): "ATNRRL_Q",
    ("ATNOV-COA","QUADRO"): "ATNCOA_Q",
    ("ATNOV-MPL","IMPIEGAT"): "NOVMPL_I",
    ("ATNOV-MPL","QUADRO"): "NOVMPL_Q",
    ("ATNOV-RL","IMPIEGAT"): "ATNVRL_I",
    ("ATNOV-RL","QUADRO"): "ATNVRL_Q",
    ("ATSUD-CESS","IMPIEGAT"): "SUDCOA_I",
    ("ATSUD-CESS","QUADRO"): "SUDCOA_Q",
    ("ATSUD-MPL","IMPIEGAT"): "SUDMPL_I",
    ("ATSUD-MPL","QUADRO"): "SUDMPL_Q",
    ("ATSUD-RL","IMPIEGAT"): "DPSURL_I",
    ("ATSUD-RL","QUADRO"): "DTCSP_Q",
    ("DSSA-MPL","IMPIEGAT"): "SARMPL_I",
    ("DSSA-MPL","QUADRO"): "SARMPL_Q",
    ("DTCEN-AT","IMPIEGAT"): "DTCENA_I",
    ("DTCEN-AT","QUADRO"): "DTCENA_Q",
    ("DTCEN-PSA","IMPIEGAT"): "CENPSA_I",
    ("DTCEN-PSA","QUADRO"): "CENPSA_Q",
    ("DTCEN-SG","IMPIEGAT"): "DTCESG_I",
    ("DTCEN-SG","QUADRO"): "DTCESG_Q",
    ("DTCEN-UAM","IMPIEGAT"): "UAMTT_I",
    ("DTCEN-UAM","OPERAIO"): "UAMTT_O",
    ("DTCEN-UAM","QUADRO"): "UAMTT_Q",
    ("DTCEN-ULN","IMPIEGAT"): "ULNTT_I",
    ("DTCEN-ULN","OPERAIO"): "ULNTT_O",
    ("DTCEN-ULN","QUADRO"): "ULNTT_Q",
    ("DTCEN-ULS","IMPIEGAT"): "ULSTT_I",
    ("DTCEN-ULS","OPERAIO"): "ULSTT_O",
    ("DTCEN-ULS","QUADRO"): "ULSTT_Q",
    ("DTCEN-UMA","IMPIEGAT"): "UMATT_I",
    ("DTCEN-UMA","OPERAIO"): "UMATT_O",
    ("DTCEN-UMA","QUADRO"): "UMATT_Q",
    ("DTCEN-UMB","IMPIEGAT"): "UMBTT_I",
    ("DTCEN-UMB","OPERAIO"): "UMBTT_O",
    ("DTCEN-UMB","QUADRO"): "UMBTT_Q",
    ("DTCNR-AT","IMPIEGAT"): "DTCNRA_I",
    ("DTCNR-AT","QUADRO"): "DTCNRA_Q",
    ("DTCNR-PSA","IMPIEGAT"): "CNRPSA_I",
    ("DTCNR-PSA","QUADRO"): "CNRPSA_Q",
    ("DTCNR-SG","IMPIEGAT"): "DTCNSG_I",
    ("DTCNR-SG","QUADRO"): "DTCNSG_Q",
    ("DTCNR-UBO","IMPIEGAT"): "UBOTT_I",
    ("DTCNR-UBO","OPERAIO"): "UBOTT_O",
    ("DTCNR-UBO","QUADRO"): "UBOTT_Q",
    ("DTCNR-UFI","IMPIEGAT"): "UFITT_I",
    ("DTCNR-UFI","OPERAIO"): "UFITT_O",
    ("DTCNR-UFI","QUADRO"): "UFITT_Q",
    ("DTCNR-UPR","IMPIEGAT"): "UPRTT_I",
    ("DTCNR-UPR","OPERAIO"): "UPRTT_O",
    ("DTCNR-UPR","QUADRO"): "UPRTT_Q",
    ("DTCNR-USU","IMPIEGAT"): "USUTT_I",
    ("DTCNR-USU","OPERAIO"): "USUTT_O",
    ("DTCNR-USU","QUADRO"): "USUTT_Q",
    ("DTCS-UFE","IMPIEGAT"): "UFETT_I",
    ("DTCS-UFE","OPERAIO"): "UFETT_O",
    ("DTCS-UFE","QUADRO"): "UFETT_Q",
    ("DTNES-AT","IMPIEGAT"): "DTNESA_I",
    ("DTNES-AT","QUADRO"): "DTNESA_Q",
    ("DTNES-PSA","IMPIEGAT"): "NESPSA_I",
    ("DTNES-PSA","QUADRO"): "NESPSA_Q",
    ("DTNES-PSA","IMPIEGAT"): "NESTSA_I",
    ("DTNES-SG","IMPIEGAT"): "DTNESSG_I",
    ("DTNES-UCA","IMPIEGAT"): "UCATT_I",
    ("DTNES-UCA","OPERAIO"): "UCATT_O",
    ("DTNES-UCA","QUADRO"): "UCATT_Q",
    ("DTNES-UDU","IMPIEGAT"): "UDUTT_I",
    ("DTNES-UDU","OPERAIO"): "UDUTT_O",
    ("DTNES-UDU","QUADRO"): "UDUTT_Q",
    ("DTNES-UFVG","IMPIEGAT"): "UFVTT_I",
    ("DTNES-UFVG","OPERAIO"): "UFVTT_O",
    ("DTNES-UFVG","QUADRO"): "UFVTT_Q",
    ("DTNES-UTA","IMPIEGAT"): "UTATT_I",
    ("DTNES-UTA","OPERAIO"): "UTATT_O",
    ("DTNES-UTA","QUADRO"): "UTATT_Q",
    ("DTNES-UVV","IMPIEGAT"): "UVVTT_I",
    ("DTNES-UVV","OPERAIO"): "UVVTT_O",
    ("DTNES-UVV","QUADRO"): "UVVTT_Q",
    ("DTNOR-AT","IMPIEGAT"): "DTNRAT_I",
    ("DTNOR-AT","QUADRO"): "DTNRAT_Q",
    ("DTNOR-PSA","IMPIEGAT"): "NORPSA_I",
    ("DTNOR-PSA","QUADRO"): "NORPSA_Q",
    ("DTNOR-SG","IMPIEGAT"): "DTNRSG_I",
    ("DTNOR-SG","QUADRO"): "DTNRSG_Q",
    ("DTNOR-UBG","OPERAIO"): "UBGTT_O",
    ("DTNOR-UBG","QUADRO"): "UBGTT_Q",
    ("DTNOR-UBG","IMPIEGAT"): "UBGTP_I",
    ("DTNOR-UBS","IMPIEGAT"): "UBSTT_I",
    ("DTNOR-UBS","OPERAIO"): "UBSTT_O",
    ("DTNOR-UBS","QUADRO"): "UBSTT_Q",
    ("DTNOR-UCH","IMPIEGAT"): "UCHTT_I",
    ("DTNOR-UCH","OPERAIO"): "UCHTT_O",
    ("DTNOR-UCH","QUADRO"): "UCHTT_Q",
    ("DTNOR-UCS","IMPIEGAT"): "UCSTT_I",
    ("DTNOR-UCS","OPERAIO"): "UCSTT_O",
    ("DTNOR-UCS","QUADRO"): "UCSTT_Q",
    ("DTNOR-UGO","IMPIEGAT"): "UGOTT_I",
    ("DTNOR-UGO","OPERAIO"): "UGOTT_O",
    ("DTNOR-UGO","QUADRO"): "UGOTT_Q",
    ("DTNOV-AT","IMPIEGAT"): "DTNVAT_I",
    ("DTNOV-AT","QUADRO"): "DTNVAT_Q",
    ("DTNOV-PSA","IMPIEGAT"): "AVTS_I",
    ("DTNOV-PSA","QUADRO"): "AVTS_Q",
    ("DTNOV-SG","IMPIEGAT"): "DTNVSG_I",
    ("DTNOV-SG","QUADRO"): "DTNVSG_Q",
    ("DTNOV-UGE","IMPIEGAT"): "UGETT_I",
    ("DTNOV-UGE","OPERAIO"): "UGETT_O",
    ("DTNOV-UGE","QUADRO"): "UGETT_Q",
    ("DTNOV-UNO","IMPIEGAT"): "UNOTT_I",
    ("DTNOV-UNO","OPERAIO"): "UNOTT_O",
    ("DTNOV-UNO","QUADRO"): "UNOTT_Q",
    ("DTNOV-UPM","IMPIEGAT"): "UPMTT_I",
    ("DTNOV-UPM","OPERAIO"): "UPMTT_O",
    ("DTNOV-UPM","QUADRO"): "UPMTT_Q",
    ("DTNOV-UTO","IMPIEGAT"): "UTOTT_I",
    ("DTNOV-UTO","OPERAIO"): "UTOTT_O",
    ("DTNOV-UTO","QUADRO"): "UTOTT_Q",
    ("DTSAR-COA","IMPIEGAT"): "SARCOA_I",
    ("DTSAR-COA","QUADRO"): "SARCOA_Q",
    ("DTSAR-PSA","IMPIEGAT"): "SARPSA_I",
    ("DTSAR-PSA","QUADRO"): "SARPSA_Q",
    ("DTSAR-RL","IMPIEGAT"): "DTSARL_I",
    ("DTSAR-RL","QUADRO"): "DTSARL_Q",
    ("DTSAR-SG","IMPIEGAT"): "DTSASG_I",
    ("DTSAR-SG","QUADRO"): "DTSASG_Q",
    ("DTSAR-UCO","IMPIEGAT"): "UCOTT_I",
    ("DTSAR-UCO","OPERAIO"): "UCOTT_O",
    ("DTSAR-UCO","QUADRO"): "UCOTT_Q",
    ("DTSAR-UVI","IMPIEGAT"): "UVITT_I",
    ("DTSAR-UVI","OPERAIO"): "UVITT_O",
    ("DTSAR-UVI","QUADRO"): "UVITT_Q",
    ("DTSIC-COA","IMPIEGAT"): "SICCOA_I",
    ("DTSIC-COA","QUADRO"): "SICCOA_Q",
    ("DTSIC-MPL","IMPIEGAT"): "SICMPL_I",
    ("DTSIC-MPL","QUADRO"): "SICMPL_Q",
    ("DTSIC-PSA","IMPIEGAT"): "SICPSA_I",
    ("DTSIC-PSA","QUADRO"): "SICPSA_Q",
    ("DTSIC-RL","IMPIEGAT"): "DTSIRL_I",
    ("DTSIC-RL","QUADRO"): "DTSIRL_Q",
    ("DTSIC-SG","IMPIEGAT"): "DTSISG_I",
    ("DTSIC-SG","QUADRO"): "DTSISG_Q",
    ("DTSIC-UCT","IMPIEGAT"): "UCTTT_I",
    ("DTSIC-UCT","OPERAIO"): "UCTTT_O",
    ("DTSIC-UCT","QUADRO"): "UCTTT_Q",
    ("DTSIC-UPA","IMPIEGAT"): "UPATT_I",
    ("DTSIC-UPA","OPERAIO"): "UPATT_O",
    ("DTSIC-UPA","QUADRO"): "UPATT_Q",
    ("DTSUD-AT","IMPIEGAT"): "DTSUAT_I",
    ("DTSUD-AT","QUADRO"): "DTSUAT_Q",
    ("DTSUD-PSA","IMPIEGAT"): "SUDPSA_I",
    ("DTSUD-PSA","QUADRO"): "SUDPSA_Q",
    ("DTSUD-SG","IMPIEGAT"): "DPSUSG_I",
    ("DTSUD-UBA","IMPIEGAT"): "UBATT_I",
    ("DTSUD-UBA","OPERAIO"): "UBATT_O",
    ("DTSUD-UBA","QUADRO"): "UBATT_Q",
    ("DTSUD-UBR","IMPIEGAT"): "UBRTT_I",
    ("DTSUD-UBR","OPERAIO"): "UBRTT_O",
    ("DTSUD-UBR","QUADRO"): "UBRTT_Q",
    ("DTSUD-UFM","IMPIEGAT"): "UFMTT_I",
    ("DTSUD-UFM","OPERAIO"): "UFMTT_O",
    ("DTSUD-UFM","QUADRO"): "UFMTT_Q",
    ("DTSUD-UMD","IMPIEGAT"): "UMDTT_I",
    ("DTSUD-UMD","OPERAIO"): "UMDTT_O",
    ("DTSUD-UMD","QUADRO"): "UMDTT_Q",
    ("DTSUD-URO","IMPIEGAT"): "UROTT_I",
    ("DTSUD-URO","OPERAIO"): "UROTT_O",
    ("DTSUD-URO","QUADRO"): "UROTT_Q"
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

# URL dell'endpoint
url_odm = "http://servicebus-col.servizi.prv/ESB/SAP/ODM_Manage/1.0" 
url_ntk = "http://servicebus-col.servizi.prv/ESB/SAP/NTW_Manage/1.0"

username = "ESB_APP_EAM"
password = "U768O[4l"
if tipospesa=="CAPEX":
    url=url_ntk
else:
    url=url_odm
    
# Formattazione delle date
date_format = SimpleDateFormat("yyyy-MM-dd")
TARGSTARTDATE = date_format.format(mbo.getDate("TARGSTARTDATE"))
TARGCOMPDATE = date_format.format(mbo.getDate("TARGCOMPDATE"))
DURATA= mbo.getDouble("ESTDUR")
# Variabili messaggio
WONUM = mbo.getString("WONUM")
X4ODM = mbo.getString("X4ODM")
DESCRIPTION = mbo.getString("DESCRIPTION")
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
        service.error("x3terna","ErrorMsg", ["Necessario popolare i campi WBS e WBE per poter procedere con la creazione."] )
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
if not equipmentMboSet.isEmpty():
    X4SAPIDASSET=X4SAPIDASSET[:-11]
equipmentMboSet.close()
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
            dauno = (wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")) + (wplaborNewMbo.getDouble("LABORHRS") * wplaborNewMbo.getDouble("QUANTITY"))
            ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
            hrwrk = wplaborNewMbo.getDouble("LABORHRS") * wplaborNewMbo.getDouble("QUANTITY")
            hrtra = wplaborNewMbo.getDouble("X4ORETRASF") * wplaborNewMbo.getDouble("QUANTITY")

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
            operazione_tag = """<item>
                <Dauno>{}</Dauno>
                <Ntend>{}</Ntend>
                <Personale>
                  <item>
                    <Arbpl>{}</Arbpl>
                    <Hrwrk>{}</Hrwrk>
                    <Hrtra>{}</Hrtra>
                  </item>
                </Personale>
              </item>""".format(
                valori['dauno'],
                valori['ntend'],
                cdltitris,
                valori['hrwrk'],
                valori['hrtra']
            )
            operazioni.append(operazione_tag)
        # Unisco tutte le operazioni in un unico blocco XML
        operation_xml = "<Operation>{}</Operation>".format("\n".join(operazioni))
        #service.error("TEST",operation_xml)
    elif tipospesa=='CAPEX':
        wplaborNewMbo = wplaborSet.moveFirst()
        first_cdl=mbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
        aggregati_capex = {}
        while wplaborNewMbo:
            cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
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
                    """.format(cdltitris, valori['hrwrk'], valori['hrtra'])
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
              <Trtyp>H</Trtyp>
            </tns:ZrichiestaCreazioneNtw>
        </soap:Body>
    </soap:Envelope>""".format(TIPONTK,X4NTK,X4WBSCONT,JPNUMDESC[:40],first_cdl,WONUM, NOPERAZIONE, IDSTANDARD, X4SAPIDASSET,TARGSTARTDATE,TARGCOMPDATE,TARGCOMPDATE,DURATA,operation_xml,X4SAPIDASSET)
    
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
            {}
          </IOdmMaintain>
          <Trtyp>H</Trtyp>
        </tns:Zfmpms4EamOdmMaintain>
      </soap:Body>
    </soap:Envelope>""".format(WONUM,IDSTANDARD,X4ODM,DESCRIPTION[:40], TARGSTARTDATE, TARGCOMPDATE, X4TIPOATTIVITA, X4TIPOORDINE, X4WBS, X4SAPIDASSET,X4SAPIDASSET,first_cdl,X4NORDINEINTERNO,X4WBSCONT,operation_xml)

#service.error("TEST",xml_data)
# Funzione per eseguire la richiesta POST e restituire la risposta
def send_post_request(url, xml_data):
    url_object = URL(url)
    connection = url_object.openConnection()
    connection.setDoOutput(True)
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Content-Type", "application/xml")
    # Creazione manuale dell'header di autenticazione
    auth_string = str(username)+":"+str(password)
    #service.error("TEST",str(auth_string))
    #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"))
    auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"));
    connection.setRequestProperty("Authorization", "Basic {}".format(auth_encoded))

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
service.error("TEST",str(xml_data))
response = send_post_request(url, xml_data)

#service.error("TEST",str(response))
if tipospesa=='CAPEX':
    #service.error("Errore nella creazione della Network  ",(response))
    ntk,noperazione,errore=extract_ntk_from_response(response)
    
    if errore==True:
        service.error("x3terna","ErrorMsg", ["Errore nella creazione: "+str(ntk)] )
        #service.error("Errore nella creazione della Network  ",(ntk))
        #service.error("Errore nella creazione della Network  ",(response))
    if mbo:
        update_mbo_with_response_ntk(mbo, ntk, noperazione)
        
else:       
    #service.error("TEST",xml_data)
    odm,errore = extract_odm_from_response(response)
    # Ottenere il MBO corrente. Questo potrebbe variare a seconda del contesto in cui lo script viene eseguito.
    if errore==True:
        service.error("x3terna","ErrorMsg", ["Errore nella creazione: "+str(odm)] )
        #service.error("Errore nella creazione: ",(odm))
    if mbo:
        update_mbo_with_response_odm(mbo, odm)
        #service.error("x3terna", "Odm ricevuto e aggiornato.")
'''
except Exception as e:
    # Gestione degli errori
    service.log("Error occurred: " + str(e))
 '''