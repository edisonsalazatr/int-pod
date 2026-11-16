from psdi.mbo import MboConstants
from psdi.util.logging import MXLoggerFactory

logger = MXLoggerFactory.getLogger("maximo")

try:

    UDB = mbo.getString("UDB")
    ANNO = mbo.getInt("ANNO")
    CRAFT = mbo.getString("CRAFT")
    NEW_RATE = mbo.getDouble("PAYRATE")
    '''    
    logger.info("ESECUZIONE SCRIPT - UDB: {}, ANNO: {}, PAYRATE: {} ".format(UDB, ANNO, NEW_RATE))
    
    wp_labor_set = mbo.getMboSet("$wplab$","WPLABOR","CRAFT =:CRAFT AND X4TITRIS =:UDB AND EXTRACT(YEAR FROM X4DATAFINERISORSA)=:ANNO AND WONUM IN (SELECT WONUM FROM WORKORDER WHERE status not IN ('CHIUSO','NON VALIDATO','NON ESEGUITO') and not exists (select 1 from labtrans where refwo=workorder.wonum and (x4checksap='OK' or GENAPPRSERVRECEIPT=1)))")
    wp_labor_set.reset()
    
    for i in range(0,wp_labor_set.count()):
        wp_labor = wp_labor_set.getMbo(i)
        wp_labor.setValue("RATE",NEW_RATE, MboConstants.NOACCESSCHECK)
        
        
    wp_labor_set.save()	
    wp_labor_set.close()
    '''
    conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
    con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
    s = con.createStatement()
    val = "update WPLABOR set x4changedate=sysdate,RATE = "+str(NEW_RATE)+" where CRAFT ='"+str(CRAFT)+"' AND X4TITRIS ='"+str(UDB)+"' AND EXTRACT(YEAR FROM X4DATAFINERISORSA)="+str(ANNO)+" AND WONUM IN (SELECT WONUM FROM WORKORDER WHERE status not IN ('CHIUSO','NON VALIDATO','NON ESEGUITO') and not exists (select 1 from labtrans where refwo=workorder.wonum and (x4checksap='OK' or GENAPPRSERVRECEIPT=1)))"
    rs = s.execute(val)
    con.commit()
    
    if s != "":
        s.close()
    if conKey != "" and con != "":
        mbo.getThisMboSet().getMboServer().freeDBConnection(conKey)
except Exception as e:
    logger.error("ERRORE NELL'ESECUZIONE DELLO SCRIPT: {} ".format(e))