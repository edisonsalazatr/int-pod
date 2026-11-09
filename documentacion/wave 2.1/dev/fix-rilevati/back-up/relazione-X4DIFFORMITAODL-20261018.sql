x4difforstatid in 
(
/* query 2.1 per OdL di tipo 'CONTROLLO DI LEGGE', 'CONTROLLO OCCASIONALE', 'CONTROLLO PERIODICO', 'CONTROLLO SU CONDIZIONE', 'CONTROLLO TECNICO', 'PRONTO INTERVENTO' */
select x4difforstatid from x4difforstat where 
(
exists (select 1 from JOBPLAN j where jpnum = :jpnum and status = 'ACTIVE' and 
j.CLASSSTRUCTUREID in (select CLASSSTRUCTUREID from classstructure cs where cs.classstructureid = j.CLASSSTRUCTUREID and cs.CLASSIFICATIONID in ('CONTROLLO DI LEGGE', 'CONTROLLO OCCASIONALE', 'CONTROLLO PERIODICO', 'CONTROLLO SU CONDIZIONE', 'CONTROLLO TECNICO', 'PRONTO INTERVENTO')))
and
x4difforstatid in (select x4difforstatid from x4difforstat d where d.status IN ('VALIDATA', 'DA VALIDARE') and 
d.ASSETNUM in (select assetnum from X4WORELATEDASSET ra where ra.jpnum = :jpnum and ra.woassetnum = :assetnum))
and IDDIFFORMITA_EXT in (select ca.IDDIFFORMITA  from X3CATASSOCIAZIONE ca where ca.IDDIFFORMITA is not null and ca.IDORIGINE in (select jpnum from jobplan where jpnum = :jpnum and status = 'ACTIVE') ) 
)
union
/* query 2.0 per OdL di tipo 'DEMOLIZIONE', 'MANUTENZIONE SU CONDIZIONE', 'NUOVA COSTRUZIONE/INSTALLAZIONE', 'SOSTITUZIONE' */
select x4difforstatid from x4difforstat where
(
exists (select 1 from JOBPLAN j where jpnum = :jpnum and status = 'ACTIVE' and 
j.CLASSSTRUCTUREID in (select CLASSSTRUCTUREID from classstructure cs where cs.classstructureid = j.CLASSSTRUCTUREID and cs.CLASSIFICATIONID in ('DEMOLIZIONE', 'MANUTENZIONE SU CONDIZIONE', 'NUOVA COSTRUZIONE/INSTALLAZIONE', 'SOSTITUZIONE')))
and
x4difforstatid IN (
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
    difformita AS (
        SELECT /*+ MATERIALIZE */
               DISTINCT elenco.iddifformita
        FROM (
            SELECT da.difformita AS iddifformita
            FROM x4diffatt da
            WHERE da.jpnum = :jpnum
            UNION ALL
            SELECT ca.iddifformita AS iddifformita
            FROM x3catassociazione ca
            WHERE ca.idorigine IN (:jpnum, :x4microcontrollo)
        ) elenco
    )
    SELECT /*+ NO_PARALLEL
               LEADING(aa s d)
               USE_NL(s) */
           s.x4difforstatid
    FROM asset_ammessi aa
    JOIN x4difforstat s
      ON s.assetnum = aa.assetnum
    JOIN difformita d
      ON d.iddifformita = s.iddifformita_ext
    WHERE s.status IN ('VALIDATA', 'DA VALIDARE')
)
)
)