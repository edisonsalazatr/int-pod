'''
Created on 11 lug 2023

- 4.6.1    ID SAP per le linee

La maschera di editing che SAP applica per le linee e che dovrà essere implementata in IBM MAS Manage dovrà adottare le seguenti regole:
•    Livello 1 SAP – NNXXXCN (7 digit)
    o    Digit 1: indica la tipologia di asset, per le linee l’unico valore ammesso “2”;
    o    Digit 2: indica il livello di tensione;
    o    Digit 3÷5: usati per l’identificativo di esercizio della terna (la linea), in caso di lettere solo maiuscole;
    o    Digit 6: carattere alfabetico solo maiuscolo, utilizzato per differenziare quei casi in cui esiste già una linea con i precedenti 5 caratteri uguali a quella che deve essere creata;
    o    Digit 7: carattere numerico utilizzato per indicare un differente titolare o DT (distretto o Dipartimento territoriale) per ciascuna linea. In pratica, per ogni DT o titolare viene creato un nuovo primo livello, con i precedenti 6 caratteri identici;
•    Livello 2 SAP - NNNXXXXX (8 digit)
    o    Digit 1÷3: caratteri numerici;
    o    Digit 4÷8: caratteri alfanumerici, facoltativi;
    o    In ogni caso ciascuno degli 8 digit del 2° livello, se non imputati dall’utente, verranno popolati con il trattino (-);
•    Livello 3 SAP - XN (2 digit)
    o    Digit 1: carattere alfabetico solo maiuscolo, indica il tipo di oggetto. In creazione da IBM MAS Manage dovranno essere gestiti solo i seguenti: 
            S (sostegno);
            E (estremo cavo);
            T (tratta cavo);
    o    Digit 2: possibilità facoltativa, mai impiegata in SAP

@version: v1 - initial coding


'''

from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from java.util.regex import Pattern

logger = MXLoggerFactory.getLogger("maximo.script")

REGEX_VALIDATIONS = { 
                        "LINEA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]$", "ERRMSG": "x3err_format_liv1"},
                        "X3TRONCHI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-T[0-9]{2}$", "ERRMSG": "x3error_tronchi"},#NON HA CLASSIFICAZIONE VIENE GESTITA CON TABELLA X3TRONCHI
                        #"X3TRONCHI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-}$", "ERRMSG": "x3error_tronchi_fr"},
                        "CAMPATA AEREA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}$", "ERRMSG": "x3err_format_liv2"},
                        "CONDUTTORI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-C$", "ERRMSG": "x3err_format_liv3"},
                        "FUNI DI GUARDIA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-G$", "ERRMSG": "x3err_format_liv3"},
                        "ISOLATORE CONDUTTORE": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-I$", "ERRMSG": "x3err_format_liv3"},
                        "ISOLATORE FUNE DI GUARDIA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-I$", "ERRMSG": "x3err_format_liv3"},
                        "????MORSETTERIA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-M$", "ERRMSG": "x3err_format_liv3"},
                        "SOSTEGNO AEREO": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-S$", "ERRMSG": "x3err_format_liv3"},
                        "TRATTA CAVO": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}$", "ERRMSG": "x3err_format_liv2"},
                        "TERMINALE": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-[HK]$", "ERRMSG": "x3err_format_liv3"},
                        "CAMERA ISPEZIONABILE": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-R$", "ERRMSG": "x3err_format_liv3"},
                        "CAVI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-V$", "ERRMSG": "x3err_format_liv3"}
                        
                    }


def getMboSet(name):
    return MXServer.getMXServer().getMboSet(name, MXServer.getMXServer().getSystemUserInfo())


if launchPoint == "X3TRONCHI":
    #X3TRONCHI
    if not Pattern.matches(REGEX_VALIDATIONS.get("X3TRONCHI").get("REGEX"), mbo.getString("SAPID")):
        if interactive:
            service.error(REGEX_VALIDATIONS.get("X3TRONCHI").get("ERRMSG"), REGEX_VALIDATIONS.get("X3TRONCHI").get("ERRMSG"), None)
        else:
            raise ValueError('Field X3TRONCHI.SAPID has bad format')
    lineaMboSet=mbo.getMboSet("PARENT")
    if not lineaMboSet.isEmpty() and onadd:
        sapidLinea=lineaMboSet.getMbo(0).getString("X3SAPID")
        sapidTronco=mbo.getString("SAPID")
        if sapidTronco[:7]!=sapidLinea:
            service.error("x3terna","ErrorMsg",["Incongruenza SAP ID livello 1."])
        if not mbo.getMboSet("$TRONCOESISTENTE$","X3TRONCHI","SAPID='"+str(sapidTronco)+"'").isEmpty():
            service.error("x3renamesapid", "x3renamesapid_exists", None)
###verificare classificationid utilizzate nelle regex
elif launchPoint == "X3ASSET" or launchPoint == "X3ASSETLOCRELATION":
    if launchPoint == "X3ASSETLOCRELATION":
        #ASSETLOCRELATION
        assetMboSet = mbo.getMboSet("TARGETASSET")
        assetMbo = assetMboSet.getMbo(0)
        classStructMboSet = assetMbo.getMboSet("CLASSSTRUCTURE")
    else: 
        #ASSET 
        classStructMboSet = mbo.getMboSet("CLASSSTRUCTURE")
        
    classStructMbo = classStructMboSet.getMbo(0)
    classId = classStructMbo.getString("CLASSIFICATIONID")
    rule = REGEX_VALIDATIONS.get(classId)
#####verificared da integrazione    
    if rule is not None:
        if not Pattern.matches(rule.get("REGEX"), mbo.getString("X3SAPID")):
            if not interactive: 
                raise ValueError('Field ' + 'ASSET.X3SAPID' if  launchPoint == "X3ASSET" else 'ASSETLOCRELATION.X3SAPID' + ' has bad format')
            #if interactive: 
               # service.error(rule.get("ERRMSG"), rule.get("ERRMSG"), None)
            #else:
                #raise ValueError('Field ' + 'ASSET.X3SAPID' if  launchPoint == "X3ASSET" else 'ASSETLOCRELATION.X3SAPID' + ' has bad format')
    else:
        logger.error(" - x3sapidvalidation - IMPOSSIBLE TO VALIDATE STRNO FIELD, no rules found")