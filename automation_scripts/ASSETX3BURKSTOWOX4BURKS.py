from psdi.server import MXServer

"""
Popolare automaticamente il campo cliente. Deve essere ereditato dall'asset presente nell'odl.
Se quest'informazione non è presente sullo specifico asset, 
allora il valore del campo cliente deve essere ereditato dal primo livello (risalendo la gerarchia). 
Se anche il primo livello non ce l'ha allora bisogna inserirlo manualmente.
"""


if mbo.isNull("X4BURKS"):
    if launchPoint=="ASSETX3BURKSTOWOX4BURKS_MASS":
        assetnum = mbo.getString("X4PRIMOLVL")
    else:
        assetnum = mbo.getString("ASSETNUM")
    if assetnum:
        asset_set = MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
        asset_set.setWhere("ASSETNUM='%s'" % assetnum)
        if not asset_set.isEmpty():
            asset = asset_set.getMbo(0)
            asset_x3burks = asset.getString("X3ZZSOCP")
            if asset_x3burks:
                mbo.setValue("X4BURKS", asset_x3burks)
            else:
                asset_parent_set = asset.getMboSet("X4ANCESTOR")
                if not asset_parent_set.isEmpty():
                    asset_parent = asset_parent_set.getMbo(0)
                    asset_parent_x3burks = asset_parent.getString("X3ZZSOCP")
                    if asset_parent_x3burks:
                        mbo.setValue("X4BURKS", asset_parent_x3burks)
                    else:
                        asset_linea=asset.getMboSet("X4LINEA")
                        if not asset_linea.isEmpty():
                            assetLineaMbo=asset_linea.getMbo(0)
                            mbo.setValue("X4BURKS",assetLineaMbo.getString("X3ZZSOCP"))