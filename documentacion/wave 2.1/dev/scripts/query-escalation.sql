-- query al26/08/2026

historyflag=0 AND (status IN('SCHEDULATO','ESEGUIBILE','AVVIATO','INOLTRATO','ESEGUITO PARZ.','INVIATO')
	OR (x4microcontrollo IS NOT NULL
		AND parent IN (
		SELECT
			wonum
		FROM
			workorder
		WHERE
			status IN ('SCHEDULATO', 'ESEGUIBILE','AVVIATO','INOLTRATO','ESEGUITO PARZ.'))))
AND (jpnum NOT IN (
SELECT
	jpnum
FROM
	jobplan
WHERE
	classstructureid IN (
	SELECT
		classstructureid
	FROM
		classstructure
	WHERE
		classificationid IN ('PRONTO INTERVENTO', 'ALTRE ATTIVITA'))) OR (JPNUM IS NULL AND X4MICROCONTROLLO IS NOT null))
		
-- query escalation originale
assetnum IN (SELECT assetnum FROM asset WHERE CLASSSTRUCTUREID IS NOT null) 
AND 
(
	status in('SCHEDULATO', 'ESEGUIBILE', 'AVVIATO', 'INOLTRATO', 'ESEGUITO PARZ.', 'INVIATO') 
	or 
	(x4microcontrollo is not null and parent in (select wonum from workorder where status in ('SCHEDULATO','ESEGUIBILE','ESEGUITO PARZ.'))
) 
and not exists (select 1 from x4rilevazioniodl where x4rilevazioniodl.workorderid=workorder.workorderid) 
and jpnum not in (select jpnum from jobplan where classstructureid in (select classstructureid from classstructure where classificationid in ('PRONTO INTERVENTO','ALTRE ATTIVITA')))

-- query escalation ottimizzata
x4aggregati = 0 and x4scenariotronchimig = <> 4 and 
workorderid in (
select w.workorderid  from 
workorder w, asset a, jobplan j, classstructure c
where 
(w.siteid = a.siteid and w.assetnum = a.assetnum) and (w.jpnum = j.jpnum) and (j.classstructureid = c.classstructureid) 
and a.classstructureid is not null
and 
w.status in('SCHEDULATO', 'ESEGUIBILE', 'AVVIATO', 'INOLTRATO', 'ESEGUITO PARZ.', 'INVIATO') 
and not exists (select 1 from x4rilevazioniodl r where r.workorderid = w.workorderid) 
and c.classificationid not in ('PRONTO INTERVENTO','ALTRE ATTIVITA')
UNION 
select w.workorderid from 
workorder w, asset a, jobplan j, classstructure c
where 
(w.siteid = a.siteid and w.assetnum = a.assetnum) and (w.jpnum = j.jpnum) and (j.classstructureid = c.classstructureid) 
and a.classstructureid is not NULL
AND w.x4microcontrollo is not null and w.parent in (select wonum from workorder where status in ('SCHEDULATO','ESEGUIBILE','ESEGUITO PARZ.'))
and not exists (select 1 from x4rilevazioniodl r where r.workorderid = w.workorderid) 
and c.classificationid not in ('PRONTO INTERVENTO','ALTRE ATTIVITA')
)


-- query escalation senza microcontrolli
x4aggregati = 0 and (x4scenariotronchimig is null or x4scenariotronchimig <> 4) and 
workorderid in (
select w.workorderid  from 
workorder w, asset a, jobplan j, classstructure c
where 
(w.siteid = a.siteid and w.assetnum = a.assetnum) and (w.jpnum = j.jpnum) and (j.classstructureid = c.classstructureid) 
and a.classstructureid is not null
and 
w.status in('SCHEDULATO', 'ESEGUIBILE', 'AVVIATO', 'INOLTRATO', 'ESEGUITO PARZ.', 'INVIATO') 
and not exists (select 1 from x4rilevazioniodl r where r.workorderid = w.workorderid) 
and c.classificationid not in ('PRONTO INTERVENTO','ALTRE ATTIVITA')
and x4microcontrollo is null
)


