# X3CONTERMINALEFINE
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_TERMINALE_FINE.
# Calcolare il valore dell'ultimo estremo cavo seguendo il verso convenzionale della linea.
# v1.0 - 12\07\2023
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from com.terna.common.maximo import MXUtils

if sapAsset.getBoolean("dummy"):
	strno = sapAsset.getString("strno")
	childSet = sapAsset.getMboSet("$child$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")
	childSet.reset()
	childMbo = childSet.getMbo(0)
	
	if childMbo == None:
		ruleHandler.addSimpleWarningForRule(mappingRule, "Estremo cavo non trovato")
	elif childSet.getMbo(1) != None:
		ruleHandler.addSimpleWarningForRule(mappingRule, "La tratta cavo ha più di un elemento associato")
	else:
		mappedRawValue = childMbo.getString("x3sapid")
else:
	# prendo tratta cavo
	trattaCavoRelSet = sapAsset.getMboSet("$trattaassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from x3saptomaxobject where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\TRATTA CAVO')")
	trattaCavoRelSet.reset()
	trattaCavoRelMbo = trattaCavoRelSet.getMbo(0)
	
	if trattaCavoRelMbo == None:
		# la relazione non esiste
		ruleHandler.addSimpleWarningForRule(mappingRule)
	else:
		sequenceId = trattaCavoRelMbo.getString("x3sequence")
		if sequenceId == "":
			ruleHandler.addSimpleWarningForRule(mappingRule, "X3SEQUENCE is null for TRATTA CAVO" + trattaCavoRelMbo.getString("x3sapid"))
		else:
			# prendo targetasset per lo stato
			trattaCavoMbo = trattaCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
			#statusTrattaCavo = MXUtils.getInternalValue(trattaCavoMbo, "status")
			statusTrattaCavo = trattaCavoMbo.getString("status")
			
			if statusTrattaCavo == "DEMO":
				strno = sapAsset.getString("strno")
				childSet = sapAsset.getMboSet("$child$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")
				childSet.reset()
				childMbo = childSet.getMbo(0)
	
				if childMbo == None:
					ruleHandler.addSimpleWarningForRule(mappingRule, "Estremo cavo non trovato")
				elif childSet.getMbo(1) != None:
					ruleHandler.addSimpleWarningForRule(mappingRule, "La tratta cavo ha più di un elemento associato")
				else:
					mappedRawValue = childMbo.getString("x3sapid")
			else:
				sqlWhere = "sourceassetnum = :sourceassetnum and siteid = :siteid and (x3sapid like '%-R' or x3sapid like '%-H') and x3sequence > :x3sequence"
				# verifica se esiste un asset di tipo ESTREMO CAVO legato alla tratta
				estremoCavoRelSet = trattaCavoRelMbo.getMboSet("$estremisuccessivi$", "ASSETLOCRELATION", sqlWhere)
				estremoCavoRelSet.setOrderBy("x3sequence asc")
				estremoCavoRelMbo = estremoCavoRelSet.getMbo(0)
			
				if estremoCavoRelMbo == None:
					ruleHandler.addSimpleWarningForRule(mappingRule, "there are no following ESTREMO CAVO")
				else:
					estremoCavoMbo = estremoCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
					statusEstremoCavo = MXUtils.getInternalValue(estremoCavoMbo, "status")
					# se stato dell'estremo è alias di DECOMMISSIONED
					if statusEstremoCavo == "DECOMMISSIONED":
						# prendo estremo con sequence successiva
						i = 0
						while i < estremoCavoRelSet.getSize():
							i += 1
							estremoCavoRelMbo = estremoCavoRelSet.getMbo(i)
							if estremoCavoRelMbo == None:
								mappedRawValue = ""
								break
							else:
								estremoCavoMbo = estremoCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
								statusEstremoCavoSucc = MXUtils.getInternalValue(estremoCavoMbo, "status")
								if statusEstremoCavoSucc != "DECOMMISSIONED":
									mappedRawValue = estremoCavoRelMbo.getString("x3sapid")
									break
								else:
									continue
					else:
						mappedRawValue = estremoCavoRelMbo.getString("x3sapid")