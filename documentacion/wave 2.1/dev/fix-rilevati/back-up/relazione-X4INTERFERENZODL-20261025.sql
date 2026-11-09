-- correttiva
x4interfstatid in (
	select x4interfstatid from x4interfstat xd 
	where 
	xd.status IN ('VALIDATA', 'DA VALIDARE') 
	and 
	xd.assetnum in (
		select assetnum from x4rilevazioniodl xr where 
			xr.tipo = 'INT' and
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
	select x4interfstatid from x4interfstat where
	X4INTERFSTATID IN (WITH prefissi AS ( SELECT /*+ MATERIALIZE */ DISTINCT SUBSTR(x3sapid, 1, 16) AS prefisso FROM assetlocrelation WHERE targetassetnum = :assetnum ), collegati AS ( SELECT /*+ MATERIALIZE */ DISTINCT rel.targetassetnum FROM assetlocrelation rel JOIN prefissi p ON SUBSTR(rel.x3sapid, 1, 16) = p.prefisso ), asset_ammessi AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.assetnum FROM ( SELECT :assetnum AS assetnum FROM dual UNION ALL SELECT aa.assetnum FROM assetancestor aa WHERE aa.ancestor = :assetnum UNION ALL SELECT /*+ LEADING(c a) USE_NL(a) INDEX(a ASSET_NDX_ANCESTOR_ASSETNUM) */ a.assetnum FROM collegati c JOIN asset a ON a.ancestor = c.targetassetnum UNION ALL SELECT c.targetassetnum AS assetnum FROM collegati c UNION ALL SELECT af.assetnum FROM asset af WHERE af.parent = :asset.parent ) elenco ), interferenze AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.idinterferenza FROM ( SELECT ia.interferenza AS idinterferenza FROM x4interfatt ia WHERE ia.attivita = :jpnum UNION ALL SELECT ca.IDINTERFERENZA AS idinterferenza FROM X3CATASSOCIAZIONE ca WHERE ca.IDORIGINE IN (:jpnum, :X4MICROCONTROLLO) ) elenco ) SELECT /*+ NO_PARALLEL LEADING(aa s i) USE_NL(s) INDEX(s X4INTERFSTAT_NDX2) */ s.x4interfstatid FROM asset_ammessi aa JOIN x4interfstat s ON s.assetnum = aa.assetnum JOIN interferenze i ON i.idinterferenza = s.IDINTERFERENZA_EXT WHERE s.status IN ('VALIDATA', 'DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('DEMOLIZIONE','MANUTENZIONE SU CONDIZIONE','NUOVA COSTRUZIONE/INSTALLAZIONE','SOSTITUZIONE') UNION ALL SELECT S.X4INTERFSTATID FROM WORKORDER W JOIN WORKORDER T ON T.WORKORDERID=W.WORKORDERID OR (W.X4MICROCONTROLLO IS NULL AND T.PARENT=W.WONUM AND T.SITEID=W.SITEID AND T.X4MICROCONTROLLO IS NOT NULL) JOIN X4RILEVAZIONIODL R ON R.WORKORDERID=T.WORKORDERID AND R.TIPO='INT' JOIN X4INTERFSTAT S ON S.ASSETNUM=R.ASSETNUM AND S.SITEID=W.SITEID AND S.IDINTERFERENZA_EXT=R.ID WHERE W.WORKORDERID=:workorderid AND S.STATUS IN ('VALIDATA','DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('CONTROLLO DI LEGGE','CONTROLLO OCCASIONALE','CONTROLLO PERIODICO','CONTROLLO SU CONDIZIONE','CONTROLLO TECNICO'))
)


-- previous
X4INTERFSTATID IN (WITH prefissi AS ( SELECT /*+ MATERIALIZE */ DISTINCT SUBSTR(x3sapid, 1, 16) AS prefisso FROM assetlocrelation WHERE targetassetnum = :assetnum ), collegati AS ( SELECT /*+ MATERIALIZE */ DISTINCT rel.targetassetnum FROM assetlocrelation rel JOIN prefissi p ON SUBSTR(rel.x3sapid, 1, 16) = p.prefisso ), asset_ammessi AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.assetnum FROM ( SELECT :assetnum AS assetnum FROM dual UNION ALL SELECT aa.assetnum FROM assetancestor aa WHERE aa.ancestor = :assetnum UNION ALL SELECT /*+ LEADING(c a) USE_NL(a) INDEX(a ASSET_NDX_ANCESTOR_ASSETNUM) */ a.assetnum FROM collegati c JOIN asset a ON a.ancestor = c.targetassetnum UNION ALL SELECT c.targetassetnum AS assetnum FROM collegati c UNION ALL SELECT af.assetnum FROM asset af WHERE af.parent = :asset.parent ) elenco ), interferenze AS ( SELECT /*+ MATERIALIZE */ DISTINCT elenco.idinterferenza FROM ( SELECT ia.interferenza AS idinterferenza FROM x4interfatt ia WHERE ia.attivita = :jpnum UNION ALL SELECT ca.IDINTERFERENZA AS idinterferenza FROM X3CATASSOCIAZIONE ca WHERE ca.IDORIGINE IN (:jpnum, :X4MICROCONTROLLO) ) elenco ) SELECT /*+ NO_PARALLEL LEADING(aa s i) USE_NL(s) INDEX(s X4INTERFSTAT_NDX2) */ s.x4interfstatid FROM asset_ammessi aa JOIN x4interfstat s ON s.assetnum = aa.assetnum JOIN interferenze i ON i.idinterferenza = s.IDINTERFERENZA_EXT WHERE s.status IN ('VALIDATA', 'DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('DEMOLIZIONE','MANUTENZIONE SU CONDIZIONE','NUOVA COSTRUZIONE/INSTALLAZIONE','SOSTITUZIONE') UNION ALL SELECT S.X4INTERFSTATID FROM WORKORDER W JOIN WORKORDER T ON T.WORKORDERID=W.WORKORDERID OR (W.X4MICROCONTROLLO IS NULL AND T.PARENT=W.WONUM AND T.SITEID=W.SITEID AND T.X4MICROCONTROLLO IS NOT NULL) JOIN X4RILEVAZIONIODL R ON R.WORKORDERID=T.WORKORDERID AND R.TIPO='INT' JOIN X4INTERFSTAT S ON S.ASSETNUM=R.ASSETNUM AND S.SITEID=W.SITEID AND S.IDINTERFERENZA_EXT=R.ID WHERE W.WORKORDERID=:workorderid AND S.STATUS IN ('VALIDATA','DA VALIDARE') AND CASE WHEN :x4microcontrollo IS NULL THEN :jobplan.classstructure.classificationid ELSE :parent.jobplan.classstructure.classificationid END IN ('CONTROLLO DI LEGGE','CONTROLLO OCCASIONALE','CONTROLLO PERIODICO','CONTROLLO SU CONDIZIONE','CONTROLLO TECNICO'))
