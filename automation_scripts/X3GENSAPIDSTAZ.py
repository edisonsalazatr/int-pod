'''
Nello script di creazione si deve aggiungere la validazione del SAPID.
In questo script si deve mettere una parte per mettere in read-only il campo del SAPID o in required sulla base del mapping.
'''
from psdi.server import MXServer
from psdi.mbo import MboConstants

def evaluateLevel(classID):
	countAncestorMboSet=MXServer.getMXServer().getMboSet("CLASSANCESTOR",MXServer.getMXServer().getSystemUserInfo())
	countAncestorMboSet.setWhere("CLASSSTRUCTUREID='"+classID+"'")
	countAncestorMboSet.reset()
	countAncestor=countAncestorMboSet.count()
	equipmentMboSet=MXServer.getMXServer().getMboSet("CLASSANCESTOR",MXServer.getMXServer().getSystemUserInfo())
	equipmentMboSet.setWhere("CLASSSTRUCTUREID='"+classID+"' and ancestorclassid='DISPOSITIVI E COMPONENTI'")
	equipmentMboSet.reset()
	fakeEquipment=0
	if countAncestor==4 and equipmentMboSet.isEmpty():
	    countAncestor=3
	    fakeEquipment=1
	return countAncestor,fakeEquipment

def dettagliNecessari(classID):
	MappingStazioniMboSet=ClassstructureMbo.getMboSet("X3MAPSTAZIONISAPTIPO")
	if MappingStazioniMboSet.isEmpty() and classID is not None:
		dettagliNecessari=0
		return dettagliNecessari
	elif not MappingStazioniMboSet.isEmpty() and classID is not None:
		TipoAssetMboSet=ClassstructureMbo.getMboSet("X3MAPSTAZIONISAPTIPO")
		#dettagliAssetMboSet=ClassstructureMbo.getMboSet("X3MAPSTAZIONISAPDET")
		if not TipoAssetMboSet.isEmpty() and not mbo.isNull("TIPOASSET"): #and dettagliAssetMboSet.isEmpty():
			dettagliNecessari=0
			return dettagliNecessari
		#elif not TipoAssetMboSet.isEmpty() and not mbo.isNull("TIPOASSET") and not dettagliAssetMboSet.isEmpty() and not mbo.isNull("DETTAGLIOLIVELLO"):
			#dettagliNecessari=0
			#return dettagliNecessari
		#elif not dettagliAssetMboSet.isEmpty() and not mbo.isNull("DETTAGLIOLIVELLO") and TipoAssetMboSet.isEmpty():
			#dettagliNecessari=0
			#return dettagliNecessari
		else:
			dettagliNecessari=1
			return dettagliNecessari
			
def newSapID(sapIDowner,livello,classification):
	dettagliAssetMboSet=ClassstructureMbo.getMboSet("X3MAPSTAZIONISAPDET")
	newsapid=None
	if livello==2:
		if TipoAssetMboSet.isEmpty():
			secondLevelSapid = secondLevel.get(classification)
			newsapid=str(sapIDowner)+secondLevelSapid
		else:
			secondLevelSapidSet = mbo.getMboSet("$TIPOCLASS$","X3MAPSTAZIONISAP","ELEMENTOSAP='"+mbo.getString("TIPOASSET")+"'")
			secondLevelSapidMbo=secondLevelSapidSet.getMbo(0)
			secondLevelSapid=secondLevelSapidMbo.getString("CODIFICASAP")
			newsapid=str(sapIDowner)+secondLevelSapid[-3:]
	if livello==3:
		#if TipoAssetMboSet.isEmpty()and not dettagliAssetMboSet.isEmpty():
			#thirdLevelSapidSet = mbo.getMboSet("$DETTAGLICLASS$","X3MAPSTAZIONISAP","ELEMENTOSAP='"+mbo.getString("DETTAGLIOLIVELLO")+"'")
			#thirdLevelSapidMbo=thirdLevelSapidSet.getMbo(0)
			#thirdLevelSapid=thirdLevelSapidMbo.getString("CODIFICASAP")
			#newsapid=str(sapIDowner)+thirdLevelSapid[9:]
		if TipoAssetMboSet.isEmpty(): #and dettagliAssetMboSet.isEmpty():
			thirdLevelSapid = thirdlevel.get(classification)
			newsapid=str(sapIDowner)+thirdLevelSapid
		elif not TipoAssetMboSet.isEmpty(): #and not dettagliAssetMboSet.isEmpty():
			thirdLevelSapidSet = mbo.getMboSet("$TIPOCLASS$","X3MAPSTAZIONISAP","ELEMENTOSAP='"+mbo.getString("TIPOASSET")+"'")
			thirdLevelSapidMbo=thirdLevelSapidSet.getMbo(0)
			thirdLevelSapid=thirdLevelSapidMbo.getString("CODIFICASAP")
			newsapid=str(sapIDowner)+thirdLevelSapid[9:]
			#dettLevelSapidSet = mbo.getMboSet("$DETTAGLICLASS$","X3MAPSTAZIONISAP","ELEMENTOSAP='"+mbo.getString("DETTAGLIOLIVELLO")+"'")
			#dettLevelSapidMbo=dettLevelSapidSet.getMbo(0)
			#dettLevelSapid=dettLevelSapidMbo.getString("CODIFICASAP")
			#lensapid=len(dettLevelSapid) - len(newsapid)
			#service.error("TEST",str(lensapid))
			#newsapid=str(newsapid)+dettLevelSapid[-lensapid:]
			
	return newsapid
	
owner=mbo.getOwner()
sapIDowner=owner.getString("X3SAPID")
classID=mbo.getString("CLASSSTRUCTUREID")
ClassstructureMboSet=mbo.getMboSet("CLASSSTRUCTURE")
ClassstructureMbo=ClassstructureMboSet.getMbo(0)
classification=ClassstructureMbo.getString("CLASSIFICATIONID")
tipo=mbo.getString("TIPOASSET")
dettAsset=mbo.getString("DETTAGLIOLIVELLO")
mbo.setFieldFlag("X3SAPID", MboConstants.READONLY, False)
#livello=evaluateLevel(classID)
TipoAssetMboSet=ClassstructureMbo.getMboSet("X3MAPSTAZIONISAPTIPO")
secondLevel={
	"CONTROLLO GENERALE DI IMPIANTO": "-91",
	"CONVERSIONI": "-79",
	"RIFASAMENTI": "-78",
	"SERVIZI AUSILIARI": "-92",
	"SEZIONE": "-5n",
	"STRUTTURE E IMPIANTI TECNOLOGICI": "-93",
	"TRASFORMAZIONI": "-77"
	}
thirdlevel={
	"APPARATI DI ACQUIS. EVENTI E PERTURBAZ.": "-14XX",
	"APPARATI DI TELETRASMISSIONI": "-13XX",
	"AUTOMATISMO DI STAZIONE": "-12XX",
	"CIRCUITI GENERALI DI IMPIANTO": "-10XX",
	"QUADRO COMANDO E CONTROLLO": "-11XX",
	"SISTEMA DI CONTROLLO E AUTOMAZIONE": "-15XX",
	"COMPENSATORE SINCRONO": "-12XX",
	"IMPIANTI ARIA COMPRESSA": "-13XX",
	"BATTERIA CONDENSATORI": "-10XX",
	"REATTORE": "-11XX",
	"SERVIZI AUSILIARI BT CORRENTE ALTERNATA" : "-11XX",
	"SERVIZI AUSILIARI BT CORRENTE CONTINUA" : "-12XX",
	"SERVIZI AUSILIARI MT" : "-10XX",
	"SBARRA" : "-17XX",
	"EDIFICI LOGISTICI" : "-13XX",
	"FABBRICATI INDUSTRIALI" : "-12XX",
	"IMPIANTI TECNOLOGICI" : "-14XX",
	"MAGLIA DI TERRA" : "-17XX",
	"TERRENI E INFRASTRUTTURE" : "-11XX",
	"PHASE SHIFTING TRANSFORMER" : "-10XX",
	"BOOSTER" : "-15XX"
}
if not mbo.isNull("X3SAPID"):
	mbo.setValue("X3SAPID","")
dettagliNecessari= dettagliNecessari(classID)

if dettagliNecessari==0:
	livello,fakeEquipment=evaluateLevel(classID)
	#service.error("TEST",str(livello))
	if livello==2:
		if classification in secondLevel:
			newsapID=newSapID(sapIDowner,livello,classification)
			mbo.setValue("X3SAPID",newsapID)
		else:
			mbo.setFieldFlag("X3SAPID", MboConstants.READONLY, True)
	elif livello==3:
	    if classification in thirdlevel or not TipoAssetMboSet.isEmpty():
	        if fakeEquipment:
	            owner2=owner.getMboSet("PARENT")
	            sapIDowner2=owner2.getMbo(0).getString("X3SAPID")
	            if fakeEquipment and classification=="MAGLIA DI TERRA":
	                newsapID=newSapID(sapIDowner2,livello,classification)
	                newsapidsplit=newsapID.split('-')
	                newsapID=newsapID.split('-')[0]+"-91-"+newsapID.split('-')[2]
	            else:
	                newsapID=newSapID(sapIDowner2,livello,classification)
	        else:
    		    newsapID=newSapID(sapIDowner,livello,classification)
    		mbo.setValue("X3SAPID",newsapID)
    	else:
    	    mbo.setFieldFlag("X3SAPID", MboConstants.READONLY, True)