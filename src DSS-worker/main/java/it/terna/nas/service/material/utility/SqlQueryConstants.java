package it.terna.nas.service.material.utility;

public class SqlQueryConstants {
    
    public static final String QUERY_INTERFERENZE_ASSETDATA = """
            SELECT 
                   s.x4interfstatid,
                   s.assetnum,
                   s.siteid,
                   i.x4catinterfid,
                   i.description,
                   i.idinterferenza,
                   s.status,
                   i.idmisura,
                   i.misura,
                   i.tipologiaasset,
                   s.primaRilevazione,
                   s.gravita,
                   TO_CHAR(XT.X4TARGETINTERFID) AS  X4TARGET
            FROM X4INTERFSTAT S, X4CATINTERF I, X4TARGETINTERF XT 
            WHERE S.ASSETNUM = :assetNum 
                AND S.STATUS = 'VALIDATA'
                AND S.IDINTERFERENZA = I.X4CATINTERFID
                AND XT.X4CATINTERFID (+) = I.X4CATINTERFID
                AND XT.VALUE (+) = S.GRAVITA
        """;
    
    public static final String QUERY_DIFFORMITA_ASSETDATA =
            """
            SELECT 
                   s.x4difforstatid,
                   s.assetnum,
                   s.siteid,
                   d.x4catdifforid,
                   d.description,
                   d.iddifformita,
                   s.status,
                   s.gravita,
                   d.idmisura,
                   d.tipologia,
                   s.primaRilevazione,
                   to_char(xt.X4TARGETDIFFORID) AS X4TARGET
            FROM x4difforstat s, x4catdiffor d, X4TARGETDIFFOR xt 
            WHERE 
               s.assetnum = :assetNum         
               AND s.status = 'VALIDATA'
               AND s.iddifformita = d.x4catdifforid
               AND xt.X4CATDIFFORID(+)  = d.x4catdifforid
               AND xt.VALUE(+) = s.GRAVITA
            """;
    
    public static final String QUERY_ATTIVITA_ASSETDATA = """
            SELECT w.wonum, 
                   w.assetnum, 
                   w.status, 
                   w.jpnum,
                   w.parent,
                   w.sneconstraint,
                   w.fnlconstraint,
                   w.targstartdate,
                   w.targcompdate,
                   w.siteid,
                   w.x4aggregati,
                   w.x4policyid,
                   w.x4policy,
                   w.x4trainante,
                   w.x4microcontrollo,
                   w.x4tronconum,
                   w.x4note,
                   w.x4datamodello,
                   w.reportDate,
                   w.x4ordineprevalente,
                   j.description, 
                   j.jpduration, 
                   j.templatetype,
                   j.x4outsourcing,
                   p.eleggibilita,
                   p.differibilita,
                   p.x4RifTemporale,
                   w.actfinish,
                   w.x4primaEleggibilita,
                   cls.classificationid as TIPOLOGIA_ATTIVITA,
                   w.x4tecmig              
            FROM WORKORDER w
            JOIN JOBPLAN j
              ON w.jpnum = j.jpnum
             AND ((j.orgid = w.orgid AND j.siteid = w.siteid) 
                  OR (j.orgid = w.orgid AND j.siteid IS NULL) 
                  OR (j.orgid IS NULL AND j.siteid IS NULL))
             AND j.status IN (
                 SELECT value 
                 FROM synonymdomain 
                 WHERE domainid = 'JOBPLANSTATUS' 
                   AND maxvalue = 'ACTIVE'
             )
             JOIN classstructure cls ON cls.classstructureid = j.classstructureid
             LEFT JOIN X4Policy p
              ON w.X4POLICYID = p.X4POLICYID
            WHERE w.assetnum = :assetNum 
             AND (
                  (w.status IN ('APERTO', 'AVVIATO', 'DA VALIDARE', 'ESEGUIBILE', 'ESEGUITO', 'ESEGUITO PARZ.', 'INOLTRATO', 'INVIATO', 'SCHEDULABILE', 'SCHEDULATO' )) 
                  OR (w.status = 'CHIUSO' AND p.tipopolicy = 'PERIODICA')
              )
            """;

    
    public static final String QUERY_VARIABILI_ASSETDATA = """
            SELECT
                   c.x4misureassetid,
                   c.assetnum, 
                   c.siteid,
                   m.x4catmisureid, 
                   m.idmisura, 
                   c.valore, 
                   m.um, 
                   m.tipoparametro,
                   c.primaRilevazione,
                   c.ultimaRilevazione
            FROM x4misureasset c
            JOIN x4catmisure m
              ON c.idmisura = m.x4catmisureid 
             AND (m.tipoparametro = 'Variabile' OR m.tipoparametro = 'Variabile di stato')
            WHERE c.assetnum = :assetNum
            """;
    
    public static final String QUERY_ATTRIBUTI_ASSETDATA = """
            SELECT 
                   s.assetspecid,
                   s.assetnum, 
                   s.siteid, 
                   s.classstructureid, 
                   a.datatype,
                   s.measureunitid,
                   s.numvalue,
                   s.alnvalue,
                   s.tablevalue, 
                   a.assetattrid, 
                   a.description, 
                   a.assetattributeid,
                   s.DATEVALUE
            FROM assetspec s
            JOIN assetattribute a
              ON s.assetattrid = a.assetattrid
            WHERE s.assetnum = :assetNum
            """;
    
    public static final String QUERY_HEALTH_INDEX_ASSETDATA = """
              SELECT s.objectname, 
                   s.attributename, 
                   s.ownerrecordid, 
                   s.value, 
                   s.ahscoreid
              FROM ahscore s, asset a
              WHERE s.OWNERRECORDID = a.ASSETID AND a.ASSETNUM = :assetNum """;
    
    public static final String QUERY_ASSET_ASSETDATA = """
            SELECT a.ANCESTOR,
                   a.ASSETNUM,
                   a.CLASSSTRUCTUREID,
                   cl.CLASSIFICATIONID,
                   cl.X4ELEMENTO,
                   a.description,
                   a.parent,
                   a.status,
                   a.X3GEWRK,
                   a.X3INGRP,
                   a.X3ROOTASSETNUM,
                   a.X3SAPID,
                   a.X3COSTRUTTORECOMPONENTE,
                   a.X3TIPOCOMPONENTE,
                   a.SITEID,
                   a.X3BUKRS,
                   a.X3EQFNR,
                   a.INSTALLDATE,
                   a.X3KOKRS,
                   a.X3KOSTL,
                   a.X3SWERK,
                   a.X3ZZSOCP,
                   a.X3FUNZIONEELEMENTO,
                   a.X3RIFERIMENTOFASE,
                   a.X3MSGRP
            FROM Asset a
            JOIN 
                ClassStructure cl
                ON a.CLASSSTRUCTUREID = cl.CLASSSTRUCTUREID
            WHERE a.assetNum = :assetNum
            """;
    
    public static final String QUERY_MISURE_ASSETDATA = """
            SELECT
                   c.x4misureassetid,
                   c.assetnum, 
                   c.siteid, 
                   m.x4catmisureid, 
                   m.idmisura, 
                   c.valore, 
                   m.um, 
                   m.tipoparametro,
                   c.primaRilevazione,
                   c.ultimaRilevazione
            FROM x4misureasset c
            JOIN x4catmisure m
              ON c.idmisura = m.x4catmisureid 
             AND m.tipoparametro = 'Misura'
            WHERE c.assetnum = :assetNum
            """;
    

    public static final String QUERY_FIND_ANCESTORCLASSID = """
            SELECT * FROM classancestor 
            WHERE classstructureid IN (
                SELECT classstructureid
                FROM asset
                WHERE assetnum = :assetNum)
            AND hierarchylevels = (
                SELECT MAX(hierarchylevels)
                FROM classancestor
                WHERE classstructureid IN (
                    SELECT classstructureid
                    FROM asset
                    WHERE assetnum = :assetNum)
                )
            """;
    
    //Needs:jpnum and classstructureid
    public static final String QUERY_RETRIVE_NO_CONDITION_MICROCONTROL = """
       SELECT DISTINCT X4MICROCONTROLLO, 
                X4MICROCONTROLLI.DESCRIPTION as taskDescription, 
                a.assetnum as assetNum,
                a.classstructureid,
                a.parent,
                a.description AS assetDescription,
                a.siteid,
                a.status,
                cls.classificationId
        FROM JOBTASK
            LEFT JOIN X4MICROCONTROLLI ON (X4MICROCONTROLLO=IDMICROCONTROLLO)
            LEFT JOIN X4MICROCLASSIFICATIONS ON (X4MICROCLASSIFICATIONS.X4MICROCONTROLLIID = x4microcontrolli.X4MICROCONTROLLIID)
            LEFT JOIN CLASSSTRUCTURE cls ON (X4MICROCLASSIFICATIONS.CLASSIFICATIONID=cls.CLASSIFICATIONID)
            LEFT JOIN ASSET a ON (cls.CLASSSTRUCTUREID=a.CLASSSTRUCTUREID)
        WHERE jpnum=:jpnum AND PLUSCJPREVNUM IN (SELECT PLUSCREVNUM FROM jobplan WHERE jpnum=:jpnum AND status='ACTIVE')
        AND NOT EXISTS (SELECT 1 FROM x4cardmicro WHERE x4cardmicro.X4MICROCONTROLLIID=X4MICROCONTROLLI.X4MICROCONTROLLIID)
        AND a.assetnum IN (SELECT assetnum FROM assetancestor WHERE ancestor=:assetNum)
        """;
    
    
    
    //Needs:jpnum and classstructureid
    public static final String QUERY_RETRIVE_CONDITION_MICROCONTROL = """
    SELECT
        asset.assetnum,
        asset.status,
        controlli.taskDescription,
        asset.siteid,
        asset.classstructureid,
        controlli.idmicrocontrollo,
        asset.parent,
        controlli.attribute,
        controlli.value AS controllValue,
        assetspec.alnvalue AS anlValue,
        assetspec.numvalue AS numValue,
        assetspec.tablevalue AS tableValue,
        assetspec.datevalue AS dateValue,
        cls.classificationId
    FROM (
        SELECT
            card.attribute,
            card.value,
            card.x4carmicroid,
            mc.idmicrocontrollo,
            mc.description as taskDescription,
            mc.tipologiaAsset
        FROM X4CARDMICRO card
            JOIN x4microcontrolli mc
              ON card.x4microcontrolliid = mc.X4MICROCONTROLLIID
        WHERE card.x4microcontrolliid IN 
            (
                SELECT mc.X4MICROCONTROLLIID
                FROM X4MICROCLASSIFICATIONS mcls
                  JOIN X4MICROCONTROLLI mc
                    ON mcls.X4MICROCONTROLLIID = mc.X4MICROCONTROLLIID
                  JOIN JOBTASK t
                    ON mc.idmicrocontrollo = t.x4microcontrollo
                    AND t.jpnum =  :jpnum
                WHERE mc.X4MICROCONTROLLIID = card.x4microcontrolliid
            )
        ) controlli
    JOIN assetspec
        ON (controlli.attribute = assetspec.assetattrid 
                AND assetspec.classstructureid IN (
                        SELECT classstructureid 
                        FROM CLASSSTRUCTURE 
                        WHERE x4elemento=controlli.tipologiaasset))
    JOIN asset asset
        ON asset.assetnum = assetspec.assetnum
    JOIN CLASSSTRUCTURE cls ON cls.CLASSSTRUCTUREID=asset.CLASSSTRUCTUREID
    WHERE
        asset.assetnum IN (SELECT assetnum FROM assetancestor WHERE ancestor=:assetNum)
        AND asset.classstructureid IN (
            SELECT classstructureid
            FROM classancestor
            WHERE ancestorclassid = 'DISPOSITIVI E COMPONENTI'
        )
    and
    controlli.value = assetspec.alnvalue
    """;

    public static final String QUERY_TASSI_GA_ASSETDATA = """
            SELECT 
                   s.objectname, 
                   s.attributename, 
                   s.ownerrecordid, 
                   s.value, 
                   s.ahscoreid
            FROM ahscore s
            WHERE s.ownerrecordid IN (SELECT locationsid FROM locations WHERE classstructureid=:classstructureid)
            """;

    
    public static final String QUERY_ASSET_TESATURA_CONDUTTORE = """
                            SELECT
                                Q.ASSETNUM
                            FROM
                                ASSET Q, ASSETSPEC P1
                            WHERE
                                Q.PARENT IN (
                                SELECT K.ASSETNUM
                                FROM ASSET K
                                WHERE
                                    K.ASSETNUM = Q.PARENT AND K.CLASSSTRUCTUREID = '1005'
                                    AND K.PARENT IN (
                                        SELECT IN01.TARGETASSETNUM
                                        FROM (
                                            SELECT TARGETASSETNUM, SOURCEASSETNUM, ROW_NUMBER() OVER (ORDER BY X3SEQUENCE) AS SEQ_NUM
                                            FROM ASSETLOCRELATION
                                            WHERE X3SEQUENCE > (
                                                    SELECT MAX(X3SEQUENCE)
                                                    FROM ASSETLOCRELATION ASL1
                                                    WHERE ASL1.SOURCEASSETNUM = :X3ROOTASSET
                                                        AND ASL1.TARGETASSETNUM IN (
                                                            SELECT ASS0.ANCESTOR
                                                            FROM ASSETANCESTOR ASS0
                                                            WHERE ASL1.TARGETASSETNUM = ASS0.ANCESTOR
                                                                AND ASS0.HIERARCHYLEVELS = '2'                                                
                                                                AND ASS0.ASSETNUM IN (
                                                                    SELECT SP0.ASSETNUM
                                                                    FROM ASSETSPEC SP0
                                                                    WHERE SP0.ASSETATTRID = 'LIN_TIPO_ATTACCO'
                                                                        AND SP0.ASSETNUM = ASS0.ASSETNUM
                                                                        AND sp0.CLASSSTRUCTUREID = '1038'
                                                                        AND SP0.ALNVALUE = 'O' ))
                                                        AND X3SEQUENCE < (
                                                            SELECT MAX(X3SEQUENCE)
                                                            FROM ASSETLOCRELATION
                                                            WHERE TARGETASSETNUM = :ANCESTOR
                                                                AND  SOURCEASSETNUM = :X3ROOTASSET))
                                                AND X3SEQUENCE < (
                                                    SELECT MIN(AL01.X3SEQUENCE)
                                                    FROM ASSETLOCRELATION AL01
                                                    WHERE AL01.SOURCEASSETNUM = :X3ROOTASSET
                                                        AND AL01.TARGETASSETNUM IN (
                                                            SELECT ANC01.ANCESTOR
                                                            FROM ASSETANCESTOR ANC01
                                                            WHERE ANC01.HIERARCHYLEVELS = '2'
                                                                AND ANC01.ANCESTOR = AL01.TARGETASSETNUM
                                                                AND ANC01.ASSETNUM IN (
                                                                    SELECT ASP01.ASSETNUM
                                                                    FROM ASSETSPEC ASP01
                                                                    WHERE ASP01.ASSETATTRID = 'LIN_TIPO_ATTACCO' 
                                                                    AND ASP01.CLASSSTRUCTUREID = '1038'
                                                                    AND ASP01.ALNVALUE = 'O' 
                                                                    AND ASP01.ASSETNUM = ANC01.ASSETNUM))
                                                        AND X3SEQUENCE > (
                                                            SELECT  MIN(X3SEQUENCE)
                                                            FROM ASSETLOCRELATION
                                                            WHERE TARGETASSETNUM = :ANCESTOR AND SOURCEASSETNUM = :X3ROOTASSET))
                                                AND SOURCEASSETNUM = :X3ROOTASSET) IN01
                                        WHERE IN01.SOURCEASSETNUM = :X3ROOTASSET AND IN01.TARGETASSETNUM = K.PARENT))
                                AND P1.ASSETNUM = Q.ASSETNUM
                                AND P1.CLASSSTRUCTUREID = Q.CLASSSTRUCTUREID 
                                AND P1.ASSETATTRID = 'LIN_RIF_POSIZIONE'
                                AND P1.NUMVALUE = :ATTRVALUE
                                AND Q.STATUS != 'DIST'
                                AND Q.CLASSSTRUCTUREID ='1009' """;
    
    
    public static final String QUERY_ASSET_TESATURA_FUNE = """
            
            SELECT Q.ASSETNUM
            FROM ASSET Q
            WHERE Q.PARENT IN (
                SELECT K.ASSETNUM
                FROM ASSET K
                WHERE K.ASSETNUM = Q.PARENT
                    AND K.PARENT IN (
                    SELECT TARGETASSETNUM
                    FROM (
                        SELECT TARGETASSETNUM, SOURCEASSETNUM, ROW_NUMBER() OVER (ORDER BY X3SEQUENCE) AS SEQ_NUM
                        FROM ASSETLOCRELATION
                        WHERE X3SEQUENCE > (
                                SELECT MAX(X3SEQUENCE)
                                FROM ASSETLOCRELATION ASL1
                                WHERE ASL1.SOURCEASSETNUM IN (
                                    SELECT ASS1.SOURCEASSETNUM
                                    FROM ASSETLOCRELATION ASS1
                                    WHERE ASS1.TARGETASSETNUM = :ANCESTOR
                                        AND ASS1.SOURCEASSETNUM = ASL1.SOURCEASSETNUM)
                                    AND ASL1.TARGETASSETNUM IN (
                                        SELECT ASS0.ANCESTOR
                                        FROM ASSETANCESTOR ASS0
                                        WHERE ASL1.TARGETASSETNUM = ASS0.ANCESTOR
                                            AND ASS0.HIERARCHYLEVELS = '2'
                                            AND ASS0.ASSETNUM IN (
                                                SELECT SP0.ASSETNUM
                                                FROM ASSETSPEC SP0
                                                WHERE SP0.ASSETNUM = ASS0.ASSETNUM
                                                    AND SP0.ASSETATTRID = 'LIN_TIPO_ATTACCO'
                                                    AND SP0.ASSETNUM = ASS0.ASSETNUM
                                                    AND SP0.ALNVALUE = 'O' ))
                                    AND X3SEQUENCE < (
                                        SELECT MAX(X3SEQUENCE)
                                        FROM ASSETLOCRELATION
                                        WHERE TARGETASSETNUM = :ANCESTOR))
                            AND X3SEQUENCE < (
                                SELECT MIN(X3SEQUENCE)
                                FROM ASSETLOCRELATION H
                                WHERE H.SOURCEASSETNUM IN (
                                    SELECT ASS02.SOURCEASSETNUM
                                    FROM ASSETLOCRELATION ASS02
                                    WHERE ASS02.TARGETASSETNUM = :ANCESTOR 
                                        AND ASS02.SOURCEASSETNUM = H.SOURCEASSETNUM)
                                    AND H.TARGETASSETNUM IN (
                                        SELECT ASC02.ANCESTOR
                                        FROM ASSETANCESTOR ASC02
                                        WHERE HIERARCHYLEVELS = '2'
                                            AND ASC02.ANCESTOR = H.TARGETASSETNUM
                                            AND ASC02.ASSETNUM IN (
                                                SELECT ASS02.ASSETNUM
                                                FROM ASSETSPEC ASS02
                                                WHERE ASS02.ASSETNUM = ASC02.ASSETNUM
                                                    AND ASS02.ASSETATTRID = 'LIN_TIPO_ATTACCO'
                                                    AND ASS02.ALNVALUE = 'O' ) )
                                    AND X3SEQUENCE > (
                                        SELECT MIN(X3SEQUENCE)
                                        FROM ASSETLOCRELATION
                                        WHERE TARGETASSETNUM = :ANCESTOR))) D
                    WHERE SOURCEASSETNUM IN (
                        SELECT G.SOURCEASSETNUM
                        FROM ASSETLOCRELATION G
                        WHERE G.TARGETASSETNUM = :ANCESTOR
                            AND G.SOURCEASSETNUM = D.SOURCEASSETNUM)))
                AND Q.ASSETNUM IN (
                SELECT P1.ASSETNUM
                FROM ASSETSPEC P1
                WHERE P1.ASSETNUM = Q.ASSETNUM
                    AND P1.ASSETATTRID = 'LIN_RIF_FUNE'
                    AND P1.ALNVALUE IN (
                        SELECT P.ALNVALUE
                        FROM ASSETSPEC P
                        WHERE
                            :ASSETNUM = P.ASSETNUM
                            AND :SITEID = P.SITEID
                            AND P.ASSETATTRID = 'LIN_RIF_FUNE')
                    AND P1.CLASSSTRUCTUREID = :CLASSSTRUCTUREID)
                AND Q.STATUS != 'DIST'
                AND Q.CLASSSTRUCTUREID =:CLASSSTRUCTUREID
            
            """;
    
    public static final String QUERY_CONDUTTORE = """
                                        SELECT a.ANCESTOR,
                                        a.ASSETNUM,
                                        a.CLASSSTRUCTUREID,
                                        a.DESCRIPTION,
                                        a.PARENT,
                                        a.STATUS,
                                        a.X3GEWRK,
                                        a.X3INGRP,
                                        a.X3ROOTASSETNUM,
                                        a.X3SAPID,
                                        a.X3COSTRUTTORECOMPONENTE,
                                        a.X3TIPOCOMPONENTE,
                                        p.CLASSIFICATIONID,
                                        a.SITEID,
                                        a.X3BUKRS,
                                        a.X3EQFNR,
                                        a.INSTALLDATE,
                                        a.X3KOKRS,
                                        a.X3KOSTL,
                                        a.X3SWERK,
                                        a.X3ZZSOCP,
                                        a.X3FUNZIONEELEMENTO,
                                        a.X3RIFERIMENTOFASE,
                                        a.X3MSGRP,
                                        p.X4ELEMENTO
                                        FROM ASSET a, CLASSSTRUCTURE p
                                        WHERE a.PARENT = :ASSETNUM
                                            AND p.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID 
                                            AND p.CLASSIFICATIONID = 'CONDUTTORE' """;

    public static final String QUERY_CONDUTTORI = """
                                        SELECT 
                                        a.ANCESTOR,
                                        a.ASSETNUM,
                                        a.CLASSSTRUCTUREID,
                                        a.DESCRIPTION,
                                        a.PARENT,
                                        a.STATUS,
                                        a.X3GEWRK,
                                        a.X3INGRP,
                                        a.X3ROOTASSETNUM,
                                        a.X3SAPID,
                                        a.X3COSTRUTTORECOMPONENTE,
                                        a.X3TIPOCOMPONENTE,
                                        p.CLASSIFICATIONID,
                                        a.SITEID,
                                        a.X3BUKRS,
                                        a.X3EQFNR,
                                        a.INSTALLDATE,
                                        a.X3KOKRS,
                                        a.X3KOSTL,
                                        a.X3SWERK,
                                        a.X3ZZSOCP,
                                        a.X3FUNZIONEELEMENTO,
                                        a.X3RIFERIMENTOFASE,
                                        a.X3MSGRP,
                                        p.X4ELEMENTO
                                        FROM ASSET a, CLASSSTRUCTURE p
                                        WHERE a.CLASSSTRUCTUREID  = p.CLASSSTRUCTUREID 
                                            AND p.CLASSIFICATIONID = 'CONDUTTORI'
                                            AND a.x3rootassetnum = :ASSETNUM """;
    
    public static final String QUERY_X4CATMODELLOASSET_BY_IDMODELLO = """
                                        SELECT xca.*
                                        FROM X4CATMODELLO xc, X4CATMODELLOASSET xca 
                                        WHERE xc.X4CATMODELLOID = xca.X4CATMODELLOID  
                                            AND xc.IDMODELLO = :IDMODELLO
                                        """;

    public static final String QUERY_WONUM_BY_ASSET_01 = """
                    SELECT W.WONUM
                    FROM WORKORDER W, JOBPLAN J, CLASSSTRUCTURE C  
                    WHERE W.STATUS IN ('APERTO', 'AVVIATO', 'ESEGUITO PARZ.', 'INOLTRATO', 'INVIATO', 'SCHEDULABILE', 'SCHEDULATO' )
                            AND W.ASSETNUM = :ASSETNUM 
                            AND W.JPNUM = J.JPNUM 
                            AND J.CLASSSTRUCTUREID = C.CLASSSTRUCTUREID 
                            AND C.CLASSIFICATIONID = 'SOSTITUZIONE' """;

    public static final String QUERY_FUNE_DI_GUARDIA = """
                                 SELECT a.ANCESTOR,
                                        a.ASSETNUM,
                                        a.CLASSSTRUCTUREID,
                                        a.DESCRIPTION,
                                        a.PARENT,
                                        a.STATUS,
                                        a.X3GEWRK,
                                        a.X3INGRP,
                                        a.X3ROOTASSETNUM,
                                        a.X3SAPID,
                                        a.X3COSTRUTTORECOMPONENTE,
                                        a.X3TIPOCOMPONENTE,
                                        p.CLASSIFICATIONID,
                                        a.SITEID,
                                        a.X3BUKRS,
                                        a.X3EQFNR,
                                        a.INSTALLDATE,
                                        a.X3KOKRS,
                                        a.X3KOSTL,
                                        a.X3SWERK,
                                        a.X3ZZSOCP,
                                        a.X3FUNZIONEELEMENTO,
                                        a.X3RIFERIMENTOFASE,
                                        a.X3MSGRP,
                                        p.X4ELEMENTO
                                        FROM ASSET a, CLASSSTRUCTURE p
                                        WHERE a.PARENT = :ASSETNUM
                                            AND p.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID 
                                            AND p.CLASSIFICATIONID = 'FUNE DI GUARDIA' """;

    public static final String QUERY_LINEE_FOR_ASSETLOCRELATION = "SELECT SOURCEASSETNUM FROM ASSETLOCRELATION a WHERE TARGETASSETNUM = :ASSETNUM";

    public static final String QUERY_FUNE_DI_GUARDIA2 = """
                                 SELECT a3.ANCESTOR,
                                        a3.ASSETNUM,
                                        a3.CLASSSTRUCTUREID,
                                        a3.DESCRIPTION,
                                        a3.PARENT,
                                        a3.STATUS,
                                        a3.X3GEWRK,
                                        a3.X3INGRP,
                                        a3.X3ROOTASSETNUM,
                                        a3.X3SAPID,
                                        a3.X3COSTRUTTORECOMPONENTE,
                                        a3.X3TIPOCOMPONENTE,
                                        p.CLASSIFICATIONID,
                                        a3.SITEID,
                                        a3.X3BUKRS,
                                        a3.X3EQFNR,
                                        a3.INSTALLDATE,
                                        a3.X3KOKRS,
                                        a3.X3KOSTL,
                                        a3.X3SWERK,
                                        a3.X3ZZSOCP,
                                        a3.X3FUNZIONEELEMENTO,
                                        a3.X3RIFERIMENTOFASE,
                                        a3.X3MSGRP,
                                        P.X4ELEMENTO
                                FROM ASSET A, ASSETLOCRELATION A2, ASSET A3, CLASSSTRUCTURE P, X4TABIDONEITA T 
                                WHERE A.ASSETNUM = :ASSETNUM 
                                    AND A2.SOURCEASSETNUM = A.ASSETNUM
                                    AND A2.TARGETASSETNUM = A3.ANCESTOR
                                    AND A3.CLASSSTRUCTUREID =  P.CLASSSTRUCTUREID 
                                    AND P.CLASSIFICATIONID = 'FUNE DI GUARDIA'
                                    AND T.TIPOLOGIAASSET = p.X4ELEMENTO
                                    AND T.STATOIDONEITA = a3.STATUS
                                """;

    public static final String QUERY_STRUTTURA_SAVE = """
            SELECT a3.ANCESTOR,
                   a3.ASSETNUM,
                   a3.CLASSSTRUCTUREID,
                   a3.DESCRIPTION,
                   a3.PARENT,
                   a3.STATUS,
                   a3.X3GEWRK,
                   a3.X3INGRP,
                   a3.X3ROOTASSETNUM,
                   a3.X3SAPID,
                   a3.X3COSTRUTTORECOMPONENTE,
                   a3.X3TIPOCOMPONENTE,
                   p.CLASSIFICATIONID,
                   a3.SITEID,
                   a3.X3BUKRS,
                   a3.X3EQFNR,
                   a3.INSTALLDATE,
                   a3.X3KOKRS,
                   a3.X3KOSTL,
                   a3.X3SWERK,
                   a3.X3ZZSOCP,
                   a3.X3FUNZIONEELEMENTO,
                   a3.X3RIFERIMENTOFASE,
                   a3.X3MSGRP,
                   P.X4ELEMENTO
           FROM ASSET A, ASSETLOCRELATION A2, 
                ASSET A3, CLASSSTRUCTURE P, X4TABIDONEITA b 
           WHERE A.ASSETNUM = :ASSETNUM 
               AND A2.SOURCEASSETNUM = A.ASSETNUM
               AND A2.TARGETASSETNUM = A3.ANCESTOR
               AND A3.CLASSSTRUCTUREID =  P.CLASSSTRUCTUREID 
               AND P.CLASSIFICATIONID = 'STRUTTURA'
               AND b.TIPOLOGIAASSET = p.CLASSIFICATIONID
               AND b.STATOIDONEITA = A3.STATUS
           """;

    
    
    public static final String QUERY_CLASSIFICATION_01 = "select c from ClassStructure c where c.classification = :classification";
    
    public static final String QUERY_STRUTTURA = """
            SELECT a.ANCESTOR,
            a.ASSETNUM,
            a.CLASSSTRUCTUREID,
            a.DESCRIPTION,
            a.PARENT,
            a.STATUS,
            a.X3GEWRK,
            a.X3INGRP,
            a.X3ROOTASSETNUM,
            a.X3SAPID,
            a.X3COSTRUTTORECOMPONENTE,
            a.X3TIPOCOMPONENTE,
            p.CLASSIFICATIONID,
            a.SITEID,
            a.X3BUKRS,
            a.X3EQFNR,
            a.INSTALLDATE,
            a.X3KOKRS,
            a.X3KOSTL,
            a.X3SWERK,
            a.X3ZZSOCP,
            a.X3FUNZIONEELEMENTO,
            a.X3RIFERIMENTOFASE,
            a.X3MSGRP,
            p.X4ELEMENTO                               
            FROM ASSET a, CLASSSTRUCTURE p, X4TABIDONEITA b
            WHERE a.PARENT = :ASSETNUM
                AND p.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID 
                AND p.CLASSIFICATIONID = 'STRUTTURA' 
                AND b.TIPOLOGIAASSET = p.CLASSIFICATIONID
                AND b.STATOIDONEITA = a.STATUS """;
}
