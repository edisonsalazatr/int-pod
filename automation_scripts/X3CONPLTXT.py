from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

description = ""
sapClass = sapAsset.getString("SAPCLASS")
#usata nei casi in cui hasParent=True
headerDescription=""
hasParent = False
#usata solamente se hasParent = False
whereClause = "(assetnum,siteid) in (select objectnum,siteid from X3SAPTOMAXOBJECT where :x3sapassetid=sapassetid and hierarchypath='%s')"

# determino whereClause o headerDescription
if sapClass == "LINEA":
    whereClause = whereClause%"LINEA"
elif sapClass == "CAMPATA":
    whereClause = whereClause%"LINEA\CAMPATA AEREA"
elif sapClass == "CONDUTTORE":
    hasParent = True
    headerDescription = "CONDUTTORI"
elif sapClass == "MORSETTERIA":
    hasParent = True
    headerDescription = "MORSETTERIA"   
elif sapClass == "CORDA DI GUARDIA":
    hasParent = True
    headerDescription = "CDG"
elif sapClass == "SOSTEGNO":
    whereClause = whereClause%"LINEA\SOSTEGNO AEREO"
elif sapClass == "ISOLATORE":
    hasParent = True
    headerDescription = "ISOLATORI"
elif sapClass == "TRATTA CAVO":
    whereClause = whereClause%"LINEA\TRATTA CAVO"  
elif sapClass == "CAVO":
    hasParent = True
    headerDescription = "CAVO"   
elif sapClass == "TERMINALE" or sapClass == "CAMERA GIUNTI":
    whereClause = whereClause%"LINEA\ESTREMO CAVO" 
elif sapClass == "TRONCO DI ISPEZIONE":
    whereClause = whereClause%"LINEA\TRONCO DI ISPEZIONE"     
    
# casi in cui prendere description dell'asset
if not hasParent:
    assetMboSet = sapAsset.getMboSet("$X3CONPLTXT", "ASSET" ,whereClause)
    if assetMboSet.count() == 0:
        if sapAsset.getBoolean("DUMMY") and sapAsset.getString("SAPCLASS")=='CAMPATA':
            description="CAMPATA FITTIZIA "+sapAsset.getString("STRNO")
        if sapAsset.getBoolean("DUMMY") and sapAsset.getString("SAPCLASS")=='TRATTA CAVO':
            description="TRATTA CAVO FITTIZIA "+sapAsset.getString("STRNO")
        else:
            ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        assetMbo = assetMboSet.moveFirst()
        description = assetMbo.getString("DESCRIPTION")
#casi in cui prendere strno del parent
else:
    parentStrno= str(sapAsset.getString("PARENTSTRNO"))
    description = headerDescription+ " "+parentStrno

mappedRawValue = description