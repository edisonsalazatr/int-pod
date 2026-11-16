from psdi.server import MXServer
from com.terna.integration.service import IntegrationService
from com.terna.integration.service import Scenario
from com.terna.integration.sapconnector import SapConnectorScenario
from com.terna.integration.sapconnector import SapConnectorScenarioOperation
from com.terna.integration.sapconnector.step1.handler.stazione.techsite.rename import RenameArg

def split_stringa(oldsapid):
    comp = len(oldsapid)
    if comp == 7:
        parte1 = oldsapid[0:7]
        parte2 = parte3 = None
    elif comp == 10:
        parte1 = oldsapid[0:7]
        parte2 = oldsapid[7:10]
        parte3 = None
    elif comp == 14:
        parte1 = oldsapid[0:7]
        parte2 = oldsapid[7:10]
        parte3 = oldsapid[10:14]
    else:
        parte1 = parte2 = parte3 = None  # Default case for unexpected lengths
    return parte1, parte2, parte3

# Main logic
thismbo = mboset.getMbo(0)
appName = thismbo.getThisMboSet().getParentApp()
asset = thismbo.getString("OWNERASSET")
siteid = thismbo.getString("SITEID")
newsapid = thismbo.getString("NEWX3SAPID")
owner = mboset.getOwner()

if appName == "X3STAZIONI":
    # Check if new SAP ID already exists
    if not owner.getMboSet("$CHKASSET$", "X3SAPASSET", "STRNO='" + str(newsapid) + "' or OLDSTRNO='" + str(newsapid) + "'").isEmpty():
        service.error("x3renamesapid", "x3renamesapid_exists", None)
    
    # Get old SAP ID and split it
    old_sapid = owner.getString("X3SAPID")
    parte1_old, parte2_old, parte3_old = split_stringa(old_sapid)
    
    if parte1_old is None:
        service.error("x3renamesapid", "Invalid old SAP ID format", None)
    else:
        # Construct the fixed part of the old SAP ID
        if parte2_old is None and parte3_old is None:
            fixed_part_old = parte1_old
        elif parte3_old is None:
            fixed_part_old = parte1_old + parte2_old
        else:
            fixed_part_old = parte1_old + parte2_old +  parte3_old[:2]  # Include first two digits of parte3

        parte1_new, parte2_new, parte3_new = split_stringa(newsapid)

        if parte1_old != parte1_new or parte2_old != parte2_new or (parte3_old and parte3_old[:2] != parte3_new[:2]):
            service.error("x3renamesapidstz", "x3renamesapidstz_invalid", None)
        else:
            variable_part = newsapid[-2:]
            new_full_sapid = fixed_part_old + variable_part

            scenario = Scenario(SapConnectorScenario.RENAME_STATION_THIRD_LEVEL)
            renameArgs = RenameArg()
            renameArgs.asset = owner
            renameArgs.prevSapId = old_sapid
            renameArgs.newSapId = new_full_sapid
            scenario.callOperation(SapConnectorScenarioOperation.RENAME_STATION_THIRD_LEVEL, renameArgs)
            scenario.save()
            owner.setValue("X3SAPID", new_full_sapid)
            IntegrationService.getService().saveNoSync(owner.getThisMboSet())