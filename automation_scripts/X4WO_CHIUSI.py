if not mbo.isNull("OWNERGROUP") and not mbo.isNull("TARGSTARTDATE") and not mbo.isNull("TARGCOMPDATE"):
    wpLaborSet=mbo.getMboSet("WPLABOR")
    if not wpLaborSet.isEmpty():
        for m in range(0,wpLaborSet.count()):
            wpLabor=wpLaborSet.getMbo(m)
            if wpLabor.isNull("X4TITRIS"):
                wpLabor.setValue("X4TITRIS",mbo.getString("OWNERGROUP"))
            if wpLabor.isNull("X4DATAINIZIORISORSA"):
                wpLabor.setValue("X4DATAINIZIORISORSA",mbo.getDate("TARGSTARTDATE"))
            if wpLabor.isNull("X4DATAFINERISORSA"):
                wpLabor.setValue("X4DATAFINERISORSA",mbo.getDate("TARGCOMPDATE"))