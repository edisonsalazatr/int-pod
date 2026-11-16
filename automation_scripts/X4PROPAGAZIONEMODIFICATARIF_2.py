from psdi.mbo import MboConstants
from psdi.util.logging import MXLoggerFactory
from psdi.server import MXServer

if launchPoint == 'X4PROPAGAZIONEMODIFICATARIF_2':
    
    logger = MXLoggerFactory.getLogger("maximo")
    #try:
    if onadd or (onupdate and mbo.isModified("X4TITRIS")):
        #wplabor = mbo
        X4TITRIS = mbo.getString("X4TITRIS") # UDB
        # ANNO = int(wplabor.getString("X3DATAFINERISORSA").split(" ")[0].split(".")[-1])
        CRAFT = mbo.getString("CRAFT")
        # workorder_set = wplabor.getMboSet("$WORKORDER$", "WORKORDER", "STATUS NOT IN ('CONSUTIVATO PARZ', 'CHIUSO TECNICAMENTE', 'CHIUSO')")
        workorder_set = mbo.getMboSet("WORKORDER")
        # workorder_set.reset()

        has_invalid_workorders = True
       
        workorder_mbo = workorder_set.getMbo(0)
        if workorder_mbo:
            if mbo.isNull("X4TITRIS") or mbo.isNull("X4DATAINIZIORISORSA") or mbo.isNull("X4DATAFINERISORSA"):
                if mbo.isNull("X4TITRIS"):
                    mbo.setValue("X4TITRIS",workorder_mbo.getString("OWNERGROUP"))
                if mbo.isNull("X4DATAINIZIORISORSA"):
                    mbo.setValue("X4DATAINIZIORISORSA",workorder_mbo.getDate("TARGSTARTDATE"))
                if mbo.isNull("X4DATAFINERISORSA"):
                    mbo.setValue("X4DATAFINERISORSA",workorder_mbo.getDate("TARGCOMPDATE"))
        if workorder_mbo:
            status = workorder_mbo.getString("STATUS")
            if status in ['CONSUTIVATO PARZ', 'CHIUSO TECNICAMENTE', 'CHIUSO']:
                has_invalid_workorders = False
    
        if has_invalid_workorders:
            x4craftperyear_set = mbo.getMboSet("$X4CRAFTPERYEAR$", "X4CRAFTPERYEAR", "CRAFT =:CRAFT AND UDB =:X4TITRIS AND ANNO = EXTRACT(YEAR FROM :X4DATAFINERISORSA)");
            #x4craftperyear_set.reset()
            
        if not x4craftperyear_set.isEmpty():
            x4craftperyear_mbo = x4craftperyear_set.getMbo(0)
            NEW_RATE = x4craftperyear_mbo.getDouble("PAYRATE")
            
            mbo.setValue("RATE", NEW_RATE, 11L)
            #mbo.getThisMboSet().save() 
            
            #x4craftperyear_set.save()
    
        x4craftperyear_set.close();
        workorder_set.close();
    if onadd and not mbo.isNull("WONUM"):
        wonum=mbo.getString("WONUM")
        wo_parent = mbo.getOwner()
        if wo_parent:
            #wo_parent = wo_parent_set.getMbo(0)
            ownergroup = mbo.getString("X4TITRIS")
            assetnum = wo_parent.getString("ASSETNUM")
            x3sapid=wo_parent.getString("X4SAPIDASSET")
            if ownergroup and x3sapid and mbo.isNull("X4ORETRASF"):
                matrice_set = MXServer.getMXServer().getMboSet("X4MATRICEDEST", mbo.getUserInfo())
                matrice_set.setWhere("DESTINAZIONE='{}' AND ORIGINE = '{}'".format(x3sapid[:7], ownergroup))
                matrice_set.reset()
                if not matrice_set.isEmpty():
                    matrice = matrice_set.getMbo(0)
                    ttrasf = matrice.getDouble("TTRASF")
                       
                    if ttrasf:
                        trasf_field = "X4ORETRASF"
                        mbo.setValue(trasf_field, ttrasf, 11L)
    '''    
    except Exception as e:
        logger.error("ERRORE NELL'ESECUZIONE DELLO SCRIPT: {} ".format(e))