-- difformità duplicate (109)
select xc.IDORIGINE, xc.PLUSCREVNUM, xc.IDDIFFORMITA, xc.GRAVITA, count(*) from X3CATASSOCIAZIONE xc 
where xc.IDDIFFORMITA is not null and xc.IDORIGINE  is not null and (xc.IDORIGINE, xc.PLUSCREVNUM) in (select j.JPNUM, j.PLUSCREVNUM  from JOBPLAN j where j.STATUS = 'ACTIVE')
group by xc.IDORIGINE, xc.PLUSCREVNUM, xc.IDDIFFORMITA, xc.GRAVITA 
having count (*) > 1
order by IDORIGINE, PLUSCREVNUM desc ;

-- rimozione difformità duplicate (109)
delete from X3CATASSOCIAZIONE a
where a.X3CATASSOCIAZIONEID in
(
select min(xc.X3CATASSOCIAZIONEID ) from X3CATASSOCIAZIONE xc 
where xc.IDDIFFORMITA is not null and xc.IDORIGINE  is not null and (xc.IDORIGINE, xc.PLUSCREVNUM) in (select j.JPNUM, j.PLUSCREVNUM  from JOBPLAN j where j.STATUS = 'ACTIVE')
group by xc.IDORIGINE, xc.PLUSCREVNUM, xc.IDDIFFORMITA, xc.GRAVITA 
having count (*) > 1
);

-- interferenze duplicate (0)
select xc.IDORIGINE, xc.PLUSCREVNUM, xc.IDINTERFERENZA , xc.GRAVITA, count(*) from X3CATASSOCIAZIONE xc 
where xc.IDINTERFERENZA is not null and xc.IDORIGINE  is not null and (xc.IDORIGINE, xc.PLUSCREVNUM) in (select j.JPNUM, j.PLUSCREVNUM  from JOBPLAN j where j.STATUS = 'ACTIVE')
group by xc.IDORIGINE, xc.PLUSCREVNUM, xc.IDINTERFERENZA, xc.GRAVITA 
having count (*) > 1
order by IDORIGINE, PLUSCREVNUM desc ;