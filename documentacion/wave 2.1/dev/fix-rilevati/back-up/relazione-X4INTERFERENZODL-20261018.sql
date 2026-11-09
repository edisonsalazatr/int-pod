X4INTERFSTATID in 
(
/* query 2.1 per OdL di tipo 'CONTROLLO DI LEGGE', 'CONTROLLO OCCASIONALE', 'CONTROLLO PERIODICO', 'CONTROLLO SU CONDIZIONE', 'CONTROLLO TECNICO', 'PRONTO INTERVENTO' */
select X4INTERFSTATID from X4INTERFSTAT where 
(
exists (select 1 from JOBPLAN j where jpnum = :jpnum and status = 'ACTIVE' and 
j.CLASSSTRUCTUREID in (select CLASSSTRUCTUREID from classstructure cs where cs.classstructureid = j.CLASSSTRUCTUREID and cs.CLASSIFICATIONID in ('CONTROLLO DI LEGGE', 'CONTROLLO OCCASIONALE', 'CONTROLLO PERIODICO', 'CONTROLLO SU CONDIZIONE', 'CONTROLLO TECNICO', 'PRONTO INTERVENTO')))
and
x4interfstatid in (select x4interfstatid from x4interfstat d where d.status IN ('VALIDATA', 'DA VALIDARE') and 
d.ASSETNUM in (select assetnum from X4WORELATEDASSET ra where ra.jpnum = :jpnum and ra.woassetnum = :assetnum))
and IDINTERFERENZA_EXT in (select ca.IDINTERFERENZA  from X3CATASSOCIAZIONE ca where ca.IDINTERFERENZA is not null and ca.IDORIGINE in (select jpnum from jobplan where jpnum = :jpnum and status = 'ACTIVE') ) 
)
union 
/* query 2.0 per OdL di tipo 'DEMOLIZIONE', 'MANUTENZIONE SU CONDIZIONE', 'NUOVA COSTRUZIONE/INSTALLAZIONE', 'SOSTITUZIONE' */
select X4INTERFSTATID from X4INTERFSTAT where
(
exists (select 1 from JOBPLAN j where jpnum = :jpnum and status = 'ACTIVE' and 
j.CLASSSTRUCTUREID in (select CLASSSTRUCTUREID from classstructure cs where cs.classstructureid = j.CLASSSTRUCTUREID and cs.CLASSIFICATIONID in ('DEMOLIZIONE', 'MANUTENZIONE SU CONDIZIONE', 'NUOVA COSTRUZIONE/INSTALLAZIONE', 'SOSTITUZIONE')))
and
x4interfstatid IN (
    WITH
    prefissi AS (
        SELECT /*+ MATERIALIZE */
               DISTINCT SUBSTR(x3sapid, 1, 16) AS prefisso
        FROM assetlocrelation
        WHERE targetassetnum = :assetnum
    ),
    collegati AS (
        SELECT /*+ MATERIALIZE */
               DISTINCT rel.targetassetnum
        FROM assetlocrelation rel
        JOIN prefissi p
          ON SUBSTR(rel.x3sapid, 1, 16) = p.prefisso
    ),
    asset_ammessi AS (
        SELECT /*+ MATERIALIZE */
               DISTINCT elenco.assetnum
        FROM (
            SELECT :assetnum AS assetnum
            FROM dual
            UNION ALL
            SELECT aa.assetnum
            FROM assetancestor aa
            WHERE aa.ancestor = :assetnum
            UNION ALL
            SELECT /*+ LEADING(c a)
           USE_NL(a)
           INDEX(a ASSET_NDX_ANCESTOR_ASSETNUM) */
       a.assetnum
FROM collegati c
JOIN asset a
  ON a.ancestor = c.targetassetnum
            UNION ALL
            SELECT c.targetassetnum AS assetnum
            FROM collegati c
        ) elenco
    ),
    interferenze AS (
        SELECT /*+ MATERIALIZE */
               DISTINCT elenco.idinterferenza
        FROM (
            SELECT ia.interferenza AS idinterferenza
            FROM x4interfatt ia
            WHERE ia.attivita = :jpnum
            UNION ALL
            SELECT ca.IDINTERFERENZA AS idinterferenza
            FROM X3CATASSOCIAZIONE ca
            WHERE ca.IDORIGINE IN (:jpnum, :X4MICROCONTROLLO)
        ) elenco
    )
    SELECT /*+ NO_PARALLEL
               LEADING(aa s i)
               USE_NL(s)
               INDEX(s X4INTERFSTAT_NDX2) */
           s.x4interfstatid
    FROM asset_ammessi aa
    JOIN x4interfstat s
      ON s.assetnum = aa.assetnum
    JOIN interferenze i
      ON i.idinterferenza = s.IDINTERFERENZA_EXT
    WHERE s.status IN ('VALIDATA', 'DA VALIDARE')
)
)
)