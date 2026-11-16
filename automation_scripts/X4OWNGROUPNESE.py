if mbo.getString("STATUS")=="NON ESEGUITO" and mbo.getBoolean("HISTORYFLAG")==False and (mbo.isNull("OWNERGROUP") or mbo.isNull("WOEQ9")):
    jpSet=mbo.getMboSet("JOBPLAN")
    if not jpSet.isEmpty():
        jpMbo=jpSet.getMbo(0)
        tipoUDB=jpMbo.getString("X4TIPUDB")
        if tipoUDB=="UI":
            #service.error("TEST","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum ='"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
            personSet=mbo.getMboSet("$UDBUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum = '"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
        else:
            personSet=mbo.getMboSet("$PERSONGROUPNONUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum =:JPNUM AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))) OR X3PARENTPERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))))))")
        if not personSet.isEmpty():
            persongroup=personSet.getMbo(0).getString("PERSONGROUP")
            ui=personSet.getMbo(0).getString("X3PARENTPERSONGROUP")
            mbo.setValue("OWNERGROUP",persongroup,11L)
            #mbo.getThisMboSet().save()
        personSet.close()
    jpSet.close()
    
    # Popola l'attributo WOEQ9 con STRNO della sede tecnica/equipment corrispondente.
    # La relazione X4SAPIDALL3 e' strutturata per arrivare alla X3SAPASSET tramite mapping (X3MAXIMOSAPMAP)
    # e binding (X3SAPTOMAXOBJECT) del connettore.
    if mbo.isNull("woeq9"):
        # recupero l'asset del work order
        assetMbo = mbo.getMboSet("ASSET").getMbo(0)
        if assetMbo != None:
            # recupero l'asset SAP tramite dati del connettore EAM/SAP corrispondente all'asset
            sapAssetMbo = assetMbo.getMboSet("X4SAPIDALL3").getMbo(0)
            if sapAssetMbo != None:
                if sapAssetMbo.getString("sapobject") == "E":
                    # nel caso di equipment occorre sostituire il quinto segmento con il reale EQNR (presente in SAPUID se disponibile)
                    if not sapAssetMbo.isNull("sapuid"):
                        strNo = sapAssetMbo.getString("strno")
                        result = "-".join(strNo.split("-")[:-1])
                        result = result + "-" + sapAssetMbo.getString("sapuid")
                        mbo.setValue("woeq9", result, 11L)
                else:
                    # nel caso di sede tecnica si utilizza STRNO
                    mbo.setValue("woeq9", sapAssetMbo.getString("strno"), 11L)
            
            if mbo.isNull("woeq9"):
                # se non e' stato possibile recuperare dalla SAPASSET il STRNO utilizzo quello del padre se disponibile
                parentSet = assetMbo.getMboSet("PARENT")
                if not parentSet.isEmpty():
                    parentMbo = parentSet.getMbo(0)
                    parentSapid = parentMbo.getString("x3sapid")
                    if parentSapid:
                        mbo.setValue("woeq9", parentSapid, 11L)