-- correttiva
x4difforstatid in (
	select x4difforstatid from x4difforstat xd 
	where 
	xd.status IN ('VALIDATA', 'DA VALIDARE') 
	and 
	xd.assetnum in (
		select assetnum from x4rilevazioniodl xr where 
			xr.tipo = 'DIFF' and
			xr.workorderid in (
			select b.workorderid  from workorder a 
			left join workorder b on b.parent = a.wonum and b.siteid = a.siteid and b.x4microcontrollo is not null 
			where 
			a.wonum = :wonum and a.siteid = :siteid
			and 
			exists 
				(select 1 from jobplan j 
					where 
						jpnum = :jpnum 
						and status = 'ACTIVE' 
						and j.classstructureid in (select classstructureid from classstructure cs where cs.classstructureid = j.classstructureid and cs.classificationid in ('CONTROLLO DI LEGGE', 'CONTROLLO OCCASIONALE', 'CONTROLLO PERIODICO', 'CONTROLLO SU CONDIZIONE', 'CONTROLLO TECNICO', 'PRONTO INTERVENTO'))
				)
			)
		)
	union
	select X4DIFFORSTATID from X4DIFFORSTAT where
	X4DIFFORSTATID IN (WITH prefissi AS ( SELECT /*+ MATERIALIZE */ DISTINCT SUBSTR(x3sapid, 1, 16) AS prefisso FROM assetlocrelation WHERE targetassetnum = :assetnum ), collegati AS ( SELECT /*+ MATERIALIZE */ DISTINCT rel.targetassetnum FROM assetlocrelation rel JOIN prefissi p ON SUBSTR(rel.x3sapid, 1, 16) = p.prefisso ), asset_ammessi AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.assetnum FROM ( SELECT :assetnum AS assetnum FROM dual UNION ALL SELECT aa.assetnum FROM assetancestor aa WHERE aa.ancestor = :assetnum UNION ALL SELECT /*+ LEADING(c a) USE_NL(a) INDEX(a ASSET_NDX_ANCESTOR_ASSETNUM) */ a.assetnum FROM collegati c JOIN asset a ON a.ancestor = c.targetassetnum UNION ALL SELECT c.targetassetnum AS assetnum FROM collegati c UNION ALL SELECT af.assetnum FROM asset af WHERE af.parent = :asset.parent ) elenco ), difformita AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.iddifformita FROM ( SELECT da.difformita AS iddifformita FROM x4diffatt da WHERE da.jpnum = :jpnum UNION ALL SELECT ca.iddifformita AS iddifformita FROM x3catassociazione ca WHERE ca.idorigine IN (:jpnum, :x4microcontrollo) ) elenco ) SELECT /*+ NO_PARALLEL LEADING(aa s d) USE_NL(s) */ s.x4difforstatid FROM asset_ammessi aa JOIN x4difforstat s ON s.assetnum = aa.assetnum JOIN difformita d ON d.iddifformita = s.iddifformita_ext WHERE s.status IN ('VALIDATA', 'DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('DEMOLIZIONE','MANUTENZIONE SU CONDIZIONE','NUOVA COSTRUZIONE/INSTALLAZIONE','SOSTITUZIONE') UNION ALL SELECT S.X4DIFFORSTATID FROM WORKORDER W JOIN WORKORDER T ON T.WORKORDERID=W.WORKORDERID OR (W.X4MICROCONTROLLO IS NULL AND T.PARENT=W.WONUM AND T.SITEID=W.SITEID AND T.X4MICROCONTROLLO IS NOT NULL) JOIN X4RILEVAZIONIODL R ON R.WORKORDERID=T.WORKORDERID AND R.TIPO='DIFF' JOIN X4DIFFORSTAT S ON S.ASSETNUM=R.ASSETNUM AND S.SITEID=W.SITEID AND S.IDDIFFORMITA_EXT=R.ID WHERE W.WORKORDERID=:workorderid AND S.STATUS IN ('VALIDATA','DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('CONTROLLO DI LEGGE','CONTROLLO OCCASIONALE','CONTROLLO PERIODICO','CONTROLLO SU CONDIZIONE','CONTROLLO TECNICO'))
)


-- previous
X4DIFFORSTATID IN (WITH prefissi AS ( SELECT /*+ MATERIALIZE */ DISTINCT SUBSTR(x3sapid, 1, 16) AS prefisso FROM assetlocrelation WHERE targetassetnum = :assetnum ), collegati AS ( SELECT /*+ MATERIALIZE */ DISTINCT rel.targetassetnum FROM assetlocrelation rel JOIN prefissi p ON SUBSTR(rel.x3sapid, 1, 16) = p.prefisso ), asset_ammessi AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.assetnum FROM ( SELECT :assetnum AS assetnum FROM dual UNION ALL SELECT aa.assetnum FROM assetancestor aa WHERE aa.ancestor = :assetnum UNION ALL SELECT /*+ LEADING(c a) USE_NL(a) INDEX(a ASSET_NDX_ANCESTOR_ASSETNUM) */ a.assetnum FROM collegati c JOIN asset a ON a.ancestor = c.targetassetnum UNION ALL SELECT c.targetassetnum AS assetnum FROM collegati c UNION ALL SELECT af.assetnum FROM asset af WHERE af.parent = :asset.parent ) elenco ), difformita AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.iddifformita FROM ( SELECT da.difformita AS iddifformita FROM x4diffatt da WHERE da.jpnum = :jpnum UNION ALL SELECT ca.iddifformita AS iddifformita FROM x3catassociazione ca WHERE ca.idorigine IN (:jpnum, :x4microcontrollo) ) elenco ) SELECT /*+ NO_PARALLEL LEADING(aa s d) USE_NL(s) */ s.x4difforstatid FROM asset_ammessi aa JOIN x4difforstat s ON s.assetnum = aa.assetnum JOIN difformita d ON d.iddifformita = s.iddifformita_ext WHERE s.status IN ('VALIDATA', 'DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('DEMOLIZIONE','MANUTENZIONE SU CONDIZIONE','NUOVA COSTRUZIONE/INSTALLAZIONE','SOSTITUZIONE') UNION ALL SELECT S.X4DIFFORSTATID FROM WORKORDER W JOIN WORKORDER T ON T.WORKORDERID=W.WORKORDERID OR (W.X4MICROCONTROLLO IS NULL AND T.PARENT=W.WONUM AND T.SITEID=W.SITEID AND T.X4MICROCONTROLLO IS NOT NULL) JOIN X4RILEVAZIONIODL R ON R.WORKORDERID=T.WORKORDERID AND R.TIPO='DIFF' JOIN X4DIFFORSTAT S ON S.ASSETNUM=R.ASSETNUM AND S.SITEID=W.SITEID AND S.IDDIFFORMITA_EXT=R.ID WHERE W.WORKORDERID=:workorderid AND S.STATUS IN ('VALIDATA','DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('CONTROLLO DI LEGGE','CONTROLLO OCCASIONALE','CONTROLLO PERIODICO','CONTROLLO SU CONDIZIONE','CONTROLLO TECNICO'))