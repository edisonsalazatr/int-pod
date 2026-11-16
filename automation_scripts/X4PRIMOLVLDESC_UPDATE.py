from psdi.server import MXServer

assetnum = mbo.getString("ASSETNUM")

if assetnum is not None and assetnum != "":

    # Cerca il rootassetnum su X3SAPTOMAXOBJECT
    sapSet = MXServer.getMXServer().getMboSet("X3SAPTOMAXOBJECT", mbo.getUserInfo())
    sapSet.setWhere("objectnum = '" + assetnum + "'")
    sapSet.reset()
    sapMbo = sapSet.getMbo(0)

    if sapMbo is not None:
        rootassetnum = sapMbo.getString("ROOTASSETNUM")
        sapSet.close()

        # Cerca la DESCRIPTION su ASSET
        assetSet = MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
        assetSet.setWhere("assetnum = '" + rootassetnum + "'")
        assetSet.reset()
        assetMbo = assetSet.getMbo(0)

        if assetMbo is not None:
            description = assetMbo.getString("DESCRIPTION")
            mbo.setValue("ENVIRONMENT", description, 11L)
        else:
            mbo.setValueNull("ENVIRONMENT", 11L)

        assetSet.close()
    else:
        sapSet.close()
        mbo.setValueNull("ENVIRONMENT", 11L)

else:
    mbo.setValueNull("ENVIRONMENT", 11L)