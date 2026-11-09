-- Svuota gli endpoint url verso SAP usati dal Connettore 

--UPDATE MAXENDPOINTDTL SET VALUE = NULL WHERE PROPERTY = 'ENDPOINTURL' AND ENDPOINTNAME IN ('X3CONSTEP4EQ','X3CONSTEP4EQOAUTH','X3CONSTEP4INV','X3CONSTEP4INVOAUTH','X3CONSTEP4ST','X3CONSTEP4STOAUTH'); 

select PROPERTY,VALUE,ENDPOINTNAME from MAXENDPOINTDTL WHERE PROPERTY = 'ENDPOINTURL' AND ENDPOINTNAME IN ('X3CONSTEP4EQ','X3CONSTEP4EQOAUTH','X3CONSTEP4INV','X3CONSTEP4INVOAUTH','X3CONSTEP4ST','X3CONSTEP4STOAUTH'); 



-- Stoppa il Connettore 

UPDATE CRONTASKINSTANCE c SET ACTIVE = 0 WHERE c.CRONTASKNAME = 'X3SAPCONNECTOR'; 

select CRONTASKNAME,ACTIVE from CRONTASKINSTANCE WHERE CRONTASKNAME = 'X3SAPCONNECTOR';


-- Resetta le proprietà del Connettore 

UPDATE X3CONNECTORPROP  
SET VALUE = 0 
WHERE NAME IN ('connector.step2.enabled','connector.step3.enabled','connector.step4.enabled','connector.scheduler.enabled','connector.trans.cleanup.enabled','objectchange.cleanup.enabled'); 

select NAME,VALUE from X3CONNECTORPROP
WHERE NAME IN ('connector.step2.enabled','connector.step3.enabled','connector.step4.enabled','connector.scheduler.enabled','connector.trans.cleanup.enabled','objectchange.cleanup.enabled'); 


---------------------------------------------------------------------------------------------


select count(*) FROM APPLICATIONAUTH where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 

delete FROM APPLICATIONAUTH where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 


select count(*) FROM GROUPUSER where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 

delete FROM GROUPUSER where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 


select count(*) FROM GRPREASSIGNAUTH where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 

delete FROM GRPREASSIGNAUTH where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 


select count(*) FROM MAXGROUP where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 

delete FROM MAXGROUP where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese'); 


----------------------------------------------------------

select * from APIKEYTOKEN


TRUNCATE TABLE APIKEYTOKEN;
COMMIT;