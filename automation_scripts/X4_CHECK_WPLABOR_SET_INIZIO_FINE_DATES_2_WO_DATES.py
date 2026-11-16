from psdi.mbo import MboConstants
from psdi.util import MXFormat
from psdi.server import MXServer

wplabor = mbo
wonum = wplabor.getString("WONUM")
workorder = wplabor.getMboSet("WORKORDER")
workorder = workorder.getMbo(0)
wo_inizio_previsto = workorder.getDate("TARGSTARTDATE")
wo_fine_previsto = workorder.getDate("TARGCOMPDATE")
where_clause = """
    WONUM='%s' AND (
        X4DATAINIZIORISORSA < TO_TIMESTAMP('%s', 'YYYY-MM-DD HH24:MI:SS.FF')
            OR
        X4DATAFINERISORSA > TO_TIMESTAMP('%s', 'YYYY-MM-DD HH24:MI:SS.FF')
    )
""" % (wonum, wo_inizio_previsto, MXFormat.dateTimeToSQLString(wo_fine_previsto))
wplabor_set = MXServer.getMXServer().getMboSet("WPLABOR", mbo.getUserInfo())
wplabor_set.setWhere(where_clause)
if not wplabor_set.isEmpty():
    service.error(
        "x3terna",
        "ErrorMsg",
        ["Controllare le date dei Labor assegnati al OdL. Le date non possono essere minori/maggiori delle date di inizio o di fine previsti."]
    )