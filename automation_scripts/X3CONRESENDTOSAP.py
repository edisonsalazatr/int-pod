# X3CONRESENDTOSAP
# Richiamato come azione dall'applicazione X3CONERRMGMT
# Mette in coda nella tabella X3CONNECTORQUEUE il primo livello
# per essere rimandato a SAP nella prima finestra disponibile
# v1.0 - 06/11/2023
from com.terna.integration.service import IntegrationService
from com.terna.common.log import Logger
from com.terna.common.string import StringUtils

log = Logger.getLogger("APPLICATION")

def yes():
	assetnum = mbo.getString("assetnum")
	sapid = mbo.getString("x3sapid")
	IntegrationService.getService().queueRootAsset(mbo.getString("siteid"), assetnum, "SYNCH")
	log.debug("Queued Line/Station {0} ({1})", assetnum, sapid)
	wcs = service.webclientsession()
	wcs.showMessageBox("x3terna","InfoMsg",[StringUtils.format("Linea/Stazione {0} ({1}) aggiunta alla coda del connettore SAP", assetnum, sapid)])
	
def no():
	pass
	
def dflt():
	service.yncerror("x3","GenericYesNo",["Vuoi mettere in coda l'invio a SAP della linea/stazione corrente?"])
	
cases = {service.YNC_NULL: dflt,service.YNC_YES: yes,service.YNC_NO: no}
if interactive:
	x = service.yncuserinput()
	cases[x]()