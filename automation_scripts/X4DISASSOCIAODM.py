from java.io import BufferedReader, InputStreamReader, OutputStreamWriter, StringReader
from java.net import HttpURLConnection, URL
from psdi.mbo import MboConstants
from psdi.server import MXServer
from javax.xml.parsers import DocumentBuilderFactory
from org.xml.sax import InputSource
from java.util import Base64
from com.ibm.json.java import JSONObject, JSONArray, JSON
from java.util import HashMap
from psdi.iface.router import HTTPHandler
from org.apache.http.impl.client import HttpClients
from java.lang import String


tipospesa=mbo.getString("X4TIPOSPESA")
ntk=False
odm=True


# URL dell'endpoint
configUri = MXServer.getMXServer().getConfig()
# URL dell'endpoint
url_odm = configUri.getProperty("mxe.int.sap.disassociazione.odm")
url_ntk = configUri.getProperty("mxe.int.sap.disassociazione.ntk")
 
# Definizione degli URL
configUri = MXServer.getMXServer().getConfig()
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
body += "&client_secret=" + str(secret)
properties.put("HEADERS", "content-type:application/x-www-form-urlencoded,content-length:" + str(len(body)))

respBytes = HTTPHandler().invoke(properties, body)
obj = JSON.parse(String(respBytes))

token=str(obj["access_token"]) 

if tipospesa=="CAPEX":
    url=url_ntk
else:
    url=url_odm
    
# Variabili messaggio
X4ODM = mbo.getString("X4ODM")
X4NTK = mbo.getString("X4NTK")
WONUM = mbo.getString("WONUM").replace(u"Ì", u"i")
if WONUM and len(WONUM) > 20:
    WONUM = WONUM[-20:]
#X4OPNTK =mbo.getString("X4OPNTK")

if tipospesa=="CAPEX":
    # XML da inviare nella richiesta POST per NTW
    xml_data ="""<?xml version="1.0" encoding="utf-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
  <soap:Body>
    <tns:ZdisassociazioneNtw xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
      <DisassociazioneNtwRequest>
        <DisassociazioneNtw>
          <item>
            <Istanza>{}</Istanza>
            <Network>{}</Network>
          </item>
        </DisassociazioneNtw>
      </DisassociazioneNtwRequest>
    </tns:ZdisassociazioneNtw>
  </soap:Body>
</soap:Envelope>""".format(WONUM, X4NTK)
else:
    # XML da inviare nella richiesta POST per OdM

    xml_data = """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sap-com:document:sap:soap:functions:mc-style">
	<soapenv:Header/>
	<soapenv:Body>
		<urn:Zfmpms4EamDisassociazione>
			<IAufnr>{}</IAufnr>
		</urn:Zfmpms4EamDisassociazione>
	</soapenv:Body>
</soapenv:Envelope>
""".format(X4ODM)


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
        esitoMbo.setValue("IDINTERFACCIA", "UNLINK ODM/NTK", 11L)
        esitoMbo.setValue("END_ELAB", MXServer.getMXServer().getDate(), 11L)
        esitoMbo.setValue("START_ELAB", MXServer.getMXServer().getDate(), 11L)
        esitoMbo.setValue("SOURCE", "EAM", 11L)
        
        esitoSet.save()
        esitoSet.close()
        
    except Exception as e:
        service.log("Errore nel logging X4ESITOINTERF: " + str(e))

# Funzione per eseguire la richiesta POST e restituire la risposta
def send_post_request(url, xml_data):
    url_object = URL(url)
    connection = url_object.openConnection()
    connection.setDoOutput(True)
    connection.setRequestMethod("POST")
    if tipospesa=="CAPEX":
        connection.setRequestProperty("Content-Type", "application/xml")
        # Creazione manuale dell'header di autenticazione
        #auth_string = str(username)+":"+str(password)
        #service.error("TEST",str(auth_string))
        #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"))
        connection.setRequestProperty('Authorization', 'Bearer ' + token)
    else:
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8")
        connection.setRequestProperty("SOAPAction","urn:sap-com:sprx:ep:cust:ZWS_CL_PM_EAM_DISASSOCIAZIONE_ODM:ZWS_CL_PM_EAM_DISASSOCIAZIONE_ODM:Zfmpms4EamDisassociazioneRequest")
    #connection.setRequestProperty("Content-Type", "application/xml")
    #connection.setRequestProperty("proxyHost", "10.178.117.170")
    #connection.setRequestProperty("proxyPort", "8080")

    # Creazione manuale dell'header di autenticazione
    #auth_string = "{username}:{password}"
    #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"))
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
        
#Prendo valore di eMessage        
def extract_from_response(response):
    # Parse XML response
    factory = DocumentBuilderFactory.newInstance()
    builder = factory.newDocumentBuilder()
    isr = InputSource(StringReader(response))
    doc = builder.parse(isr)
    
    if tipospesa=='CAPEX':
        #Find <Type> tag
        elements = doc.getElementsByTagName("Type")
        if elements.getLength() > 0:
            return elements.item(0).getTextContent()
        else:
            raise Exception("Tag <Type> not found in the response")
    else:
        #Find <eMessage> tag
        elements = doc.getElementsByTagName("EMessage")
        if elements.getLength() > 0:
            return elements.item(0).getTextContent()
        else:
            raise Exception("Tag <EMessage> not found in the response")

# Invocazione delle funzioni
try:
    labTransSet=mbo.getMboSet("$LABTRANSOKSAP$","LABTRANS","refwo=:wonum and siteid=:siteid and X4CHECKSAP='OK'")
    if not labTransSet.isEmpty():
        service.error("x3terna", "ErrorMsg", ["Impossibile procedere con la disassociazione, consuntivazioni presenti."])
    response = send_post_request(url, xml_data)
    #service.error("TEST",response.encode("utf-8"))
    # Ottenere il MBO corrente. Questo potrebbe variare a seconda del contesto in cui lo script viene eseguito
    if tipospesa=='CAPEX':
        type = extract_from_response(response)
        if type == "":
            mbo.setValueNull("X4NTK")
            mbo.setValueNull("X4NOPERAZIONE")
            mbo.getThisMboSet().save()
            log_esito_interfaccia(mbo, "OK", "Disassociazione NTK avvenuta", response) #+++++++++++++++++AGGIUNTO X4ESITOINT
        else:
            log_esito_interfaccia(mbo, "KO", str(ntk), response)    #+++++++++++++++++AGGIUNTO X4ESITOINT
            service.error("x3terna", "ErrorMsg", ["Impossibile procedere con la disassociazione."])
    else:
        eMessage = extract_from_response(response)
        if eMessage == "Aggiornamento effettuato con successo":
            mbo.setValueNull("X4ODM")
            if mbo.getString("X4TIPOSPESA")=="ANR":
                mbo.setValueNull("X4TIPOORDINE")
            mbo.getThisMboSet().save()
            log_esito_interfaccia(mbo, "OK", "Disassociazione OdM avvenuta", response) #+++++++++++++++++AGGIUNTO X4ESITOINT
        else:
            log_esito_interfaccia(mbo, "KO", str(odm), response)  #+++++++++++++++++AGGIUNTO X4ESITOINT
            service.error("x3terna", "ErrorMsg", ["Impossibile procedere con la disassociazione."])
except Exception as e:
    # Gestione degli errori
    service.error("TEST",str("errore: "+str(e)))
    service.log("Error occurred: " + str(e))