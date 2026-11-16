from psdi.server import MXServer
from psdi.mbo import MboSetRemote, MboRemote
from psdi.mbo import MboConstants

def isControlWo(wo):
    jobPlan = wo.getMboSet("JOBPLAN").getMbo(0)
    if jobPlan is None:
        return False

    classification = jobPlan.getMboSet("CLASSSTRUCTURE").getMbo(0)
    if classification is None:
        return False

    jpClassification = classification.getString("CLASSIFICATIONID")

    return jpClassification not in (
        "DEMOLIZIONE",
        "MANUTENZIONE SU CONDIZIONE",
        "NUOVA COSTRUZIONE/INSTALLAZIONE",
        "SOSTITUZIONE"
    )


if isControlWo(mbo):
    evalresult = not mbo.getMboSet("X4DIFFORMITAODL").isEmpty()
else:
    evalresult = True