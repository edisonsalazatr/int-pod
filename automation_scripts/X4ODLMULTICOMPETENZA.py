if interactive:
    
    # --- 1. Contesto dal job plan ---
    attivita = mbo.getString("JPNUM")
    jobplanSet = mbo.getMboSet("$NEWJP$", "JOBPLAN",
                               "status='ACTIVE' and jpnum='" + str(attivita) + "'")

    domainid = None
    if not jobplanSet.isEmpty():
        jobplanMbo = jobplanSet.getMbo(0)

        aggregati  = mbo.getBoolean("X4AGGREGATI")     # True / False
        isLinea    = attivita.startswith("AT.L.") or attivita.startswith("AT.LC.")
        isStazione = attivita.startswith("AT.S.")

        # --- 2. Matrice aggregati per tipo ---
        if isLinea and aggregati == True:
            domainid = "X4LINAGGR"
        elif isLinea and aggregati == False:
            domainid = "X4LINNOAGGR"
        elif isStazione and aggregati == True:
            domainid = "X4STAZAGGR"
        elif isStazione and aggregati == False:
            domainid = "X4STAZNOAGGR"
        elif not isLinea and not isStazione and aggregati == True:
            domainid = "X4STAZAGGR"
        elif not isLinea and not isStazione and aggregati == False:
            domainid = "X4STAZNOAGGR"

    # --- 3. Applica  ---
    if domainid is not None:
        mbo.getMboValue("ASSETNUM").setCustomLookupName(domainid)