from psdi.server import MXServer
from com.terna.integration.service import IntegrationService
from com.terna.integration.service import Scenario
from com.terna.integration.sapconnector import SapConnectorScenario
from com.terna.integration.sapconnector import SapConnectorScenarioOperation
from com.terna.integration.sapconnector.step1.handler.rename import RenameArg
from java.util.regex import Pattern
appName = mboset.getMbo(0).getThisMboSet().getParentApp()

if appName!="X3STAZIONI":
    REGEX_2_LVL = "^[0-9]{3}[A-Z0-9-]{0,5}(-S)?$"
    def isCorrectLvl2(stringa):
        if not Pattern.matches(REGEX_2_LVL, stringa):
            service.error("x3err_format_liv2", "x3err_format_liv2", None)

    thismbo= mboset.getMbo(0)
    targetasset=thismbo.getString("ASSETNUM")
    ownerasset=thismbo.getString("OWNERASSET")
    siteid=thismbo.getString("SITEID")
    newsapid=thismbo.getString("NEWX3SAPID")
    sostegno=False
    assetMboSet = MXServer.getMXServer().getMboSet("ASSET", thismbo.getUserInfo())
    assetMboSet.setWhere("assetnum='"+str(targetasset)+"' and siteid='"+str(siteid)+"'")
    assetMboSet.reset()
    if not assetMboSet.isEmpty():
        assetMbo=assetMboSet.getMbo(0)
        statoAsset=assetMbo.getString("STATUS")
        if statoAsset in ('DEMO','CEAL','DELE'):
            service.error("x3terna","ErrorMsg",["Asset in stato DEMO, impossibile procedere"])
        classification=assetMbo.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")
        if classification=="SOSTEGNO AEREO":
            sostegno=True
    if len(newsapid)>8 and sostegno==False:
        service.error("x3err_lenght_liv2","x3err_lenght_liv2",None)
    if sostegno==True and not len(newsapid)==8:
        service.error("x3err_lenght_liv2","x3err_lenght_liv2",None)
    newsapid=newsapid.ljust(8, "-")
    
    def split_stringa(oldsapid):
        comp=len(oldsapid)
        if comp==7:
            parte1 = oldsapid[0:7]
            parte2 = parte3 = None
        elif comp==16:
            parte1 = oldsapid[0:7]
            parte2 = oldsapid[8:16]
            parte3 = None
        elif comp==19 or comp==18:
            parte1 = oldsapid[0:7]
            parte2 = oldsapid[8:16]
            parte3 = oldsapid[17:20]
        return parte1, parte2, parte3
    
    def split_stringatronco(oldtroncosapid):
        parte1 = oldtroncosapid[0:7]
        parte2 = oldtroncosapid[8:]
        return parte1, parte2
    def renamesapid(parte1,parte2,parte3):
        if parte1:
            newid=parte1
            if  parte2:
                newid=newid+"-"+parte2
                if  parte3:
                    newid=newid+"-"+parte3
        return newid
        
    def renamesapidtronco(parte1,parte2):
        if parte1:
            newid=parte1
            if  parte2:
                newid=newid+"-"+parte2
        return newid
    #se si cambia x3sapid della linea
    
    #Cambio SAPID Linea- non più previsto. aggiunto service error
    if not targetasset:
        service.error("x3terna", "ErrorMsg", ["Operazione non consentita"])
        #check se esiste già un asset con questo sapid
        if not thismbo.getMboSet("$CHKASSET$","ASSET","x3sapid='"+str(newsapid)+"'").isEmpty():
            service.error("x3renamesapid","x3renamesapid_exists",None)
        assetlineaMbo=thismbo.getMboSet("$ASSETLINEA$","ASSET","assetnum='"+str(ownerasset)+"' and siteid='"+str(siteid)+"'")
        if not assetlineaMbo.isEmpty():
            assetlinea=assetlineaMbo.getMbo(0)
            assetlinea.setValue("X3SAPID",newsapid)
        assetlocrellineaMbo=thismbo.getMboSet("$ALOCRELLINEA$","ASSETLOCRELATION","sourceassetnum='"+str(ownerasset)+"' and siteid='"+str(siteid)+"'")
        if not assetlocrellineaMbo.isEmpty():
            for i in range(0,assetlocrellineaMbo.count()):
                assetlocrellinea=assetlocrellineaMbo.getMbo(i)
                oldsapid=assetlocrellinea.getString("X3SAPID")
                if not assetlocrellinea.isNull("X3SAPID"):
                    parte1,parte2,parte3=split_stringa(oldsapid)
                    new2ndlvlsapid=renamesapid(newsapid,parte2,parte3)
                    assetlinea.setValue("SERIALNUM",new2ndlvlsapid)
                    assetlocrellinea.setValue("X3SAPID",new2ndlvlsapid)
            #cambio sapid tronchi
            tronchiMbo=assetlinea.getMboSet("X3TRONCHI")
            if not tronchiMbo.isEmpty():
                for j in range(0,tronchiMbo.count()):
                    tronco=tronchiMbo.getMbo(j)
                    oldtroncosapid=tronco.getString("SAPID")
                    if not tronco.isNull("SAPID"):
                        parte1,parte2=split_stringatronco(oldtroncosapid)
                        newsapidtronco=renamesapidtronco(newsapid,parte2)
                        tronco.setValue("SAPID",newsapidtronco)
        IntegrationService.getService().saveNoSync(assetlineaMbo)
        assetlineaMbo.close()
        service.closeDialog()
    #nel caso di cambio sapid per asset di secondo livello              
    else:
        assetlocrellineaMbo=thismbo.getMboSet("$ALOCRELLINEA$","ASSETLOCRELATION","sourceassetnum='"+str(ownerasset)+"' and targetassetnum='"+str(targetasset)+"' and siteid='"+str(siteid)+"'")
        if not assetlocrellineaMbo.isEmpty():
            assetlocrellinea=assetlocrellineaMbo.getMbo(0)
            oldsapid=assetlocrellinea.getString("X3SAPID")
            assetlineaMbo=thismbo.getMboSet("$ASSETLINEA$","ASSET","assetnum='"+str(ownerasset)+"' and siteid='"+str(siteid)+"'")
            if not assetlineaMbo.isEmpty():
                assetlinea=assetlineaMbo.getMbo(0)
                sapidlinea=assetlinea.getString("X3SAPID")
            if assetlocrellinea.isNull("X3SAPID"):
                renamesapid(sapidlinea,newsapid,parte3)
            else:
                if not thismbo.getMboSet("$CHKASSET$","X3SAPASSET","(STRNO LIKE '"+str(sapidlinea)+"-"+str(newsapid)+"%' or OLDSTRNO='"+str(sapidlinea)+"-"+str(newsapid)+"%')").isEmpty():
                    service.error("x3renamesapid","x3renamesapid_exists",None)
                
                parte1,parte2,parte3=split_stringa(oldsapid)
                newsapid2nd=renamesapid(parte1,newsapid,parte3)
                if len(newsapid)<8:
                    caratteri_da_aggiungere = 8 -  len(newsapid)
                    stringa_con_trattini = newsapid + '-' * caratteri_da_aggiungere
                    isCorrectLvl2(stringa_con_trattini)
                else:
                    isCorrectLvl2(newsapid)
                scenario = Scenario(SapConnectorScenario.RENAME_LINE_SECOND_LEVEL)
                
                existingAsset = MXServer.getMXServer().getMboSet("ASSETLOCRELATION", thismbo.getUserInfo())
                existingAsset.setWhere("x3sapid='"+str(newsapid2nd)+"'")
                existingAsset.reset()
                if not existingAsset.isEmpty():
                    service.error("x3renamesapid", "x3renamesapid_exists", None)
                assetlocrellinea.setValue("X3SAPID",newsapid2nd)
                #OP88
                if assetlocrellinea.getBoolean("X3SAPIDRENAME")==True:
                    assetlocrellinea.setValue("X3SAPIDRENAME","0")
                renameArgs = RenameArg()
                renameArgs.relation = assetlocrellineaMbo.getMbo(0)
                renameArgs.prevSapId = str(oldsapid)
                renameArgs.newSapId = str(newsapid2nd)
                scenario.callOperation(SapConnectorScenarioOperation.RENAME_LINE_SECOND_LEVEL,renameArgs)
                scenario.save()
                assetTerzolvllocrellineaMbo=assetlinea.getMboSet("$ALOCRELLINEA$","ASSETLOCRELATION","sourceassetnum='"+str(ownerasset)+"' and targetassetnum!='"+str(targetasset)+"' and x3sapid like '"+str(oldsapid)+"%' and siteid='"+str(siteid)+"'")
                if not assetTerzolvllocrellineaMbo.isEmpty():
                    oldsapidterzolvl=assetTerzolvllocrellineaMbo.getMbo(0).getString("X3SAPID")
                    parte1,parte2,parte3=split_stringa(oldsapidterzolvl)
                    new3rdlvlsapid=renamesapid(sapidlinea,newsapid,parte3)
                    assetTerzolvllocrellineaMbo.getMbo(0).setValue("X3SAPID",new3rdlvlsapid)
                    #OP88
                    if assetTerzolvllocrellineaMbo.getMbo(0).getBoolean("X3SAPIDRENAME")==True:
                        assetTerzolvllocrellineaMbo.getMbo(0).setValue("X3SAPIDRENAME","0")
            IntegrationService.getService().saveNoSync(assetlineaMbo)
            assetlineaMbo.close()