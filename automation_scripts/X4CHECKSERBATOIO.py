capacityPmSet=mbo.getMboSet("$CSET$","X4CAPACITYPM","X4CAPACITYID = (     SELECT c.X4CAPACITYID     FROM X4CAPACITY c     WHERE c.UDB = :X4TITRIS  AND c.ANNO = EXTRACT(YEAR FROM :X4DATAINIZIORISORSA)     AND c.ANNO = EXTRACT(YEAR FROM :X4DATAFINERISORSA) ) AND CRAFT = :CRAFT  AND MESE IN (     UPPER(RTRIM(TO_CHAR(:X4DATAINIZIORISORSA, 'MONTH', 'NLS_DATE_LANGUAGE=ITALIAN'))),     UPPER(RTRIM(TO_CHAR(:X4DATAFINERISORSA, 'MONTH', 'NLS_DATE_LANGUAGE=ITALIAN'))) )")

if not capacityPmSet.isEmpty():
    capacityMbo=capacityPmSet.getMbo(0)
    contoBase=capacityMbo.getDouble("CONTOBASEPLAN")
    
    if contoBase-(mbo.getInt("QUANTITY")*mbo.getDouble("LABORHRS"))<0:
        service.error("x3terna", "ErrorMsg", ["Impossibile procedere. Conto base diverrebbe negativo"])