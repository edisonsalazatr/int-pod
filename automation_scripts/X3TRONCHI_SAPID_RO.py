from psdi.mbo import MboConstants

if interactive and not mbo.toBeAdded():
    mbo.setFieldFlag("SAPID", MboConstants.READONLY, True)