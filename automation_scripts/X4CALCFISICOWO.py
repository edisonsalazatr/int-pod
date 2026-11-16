from psdi.server import MXServer
from psdi.mbo import MboConstants

wonum=mbo.getString("WONUM")
status=mbo.getString("STATUS")
def evaluateCalc(jpnum):
	tipoCalcolo = None
	jpMboSet = MXServer.getMXServer().getMboSet("JOBPLAN", mbo.getUserInfo())
	jpMboSet.setWhere("jpnum='" + str(jpnum) + "' and status='ACTIVE'")
	jpMboSet.reset()
	if not jpMboSet.isEmpty():
		jpMbo = jpMboSet.getMbo(0)
		tipoCalcolo = jpMbo.getString("X4UM")
	return tipoCalcolo
if (
	mbo.getBoolean("X4AGGREGATI") == True
	and not mbo.isModified("STATUS")
	and mbo.getString("STATUS") in ("APERTO", "DA VALIDARE")
	and mbo.isNull("X4FISICOPLAN")
):
	childSet = mbo.getMboSet("CHILDNOTASK")
	tipoCalcolo = evaluateCalc(mbo.getString("JPNUM"))
	if tipoCalcolo == "KM" and not childSet.isEmpty():
		linLunghezza = 0
		for i in range(0, childSet.count()):
			childMbo = childSet.getMbo(i)
			AssetMboSet = childMbo.getMboSet("ASSET")
			if not AssetMboSet.isEmpty():
				AssetMbo = AssetMboSet.getMbo(0)
				AssetCampataSet = AssetMbo.getMboSet("X4ANCESTOR")
				if not AssetCampataSet.isEmpty():
					AssetCampata = AssetCampataSet.getMbo(0)
					LunghezzaMboSet = AssetCampata.getMboSet(
						"$ASSET_LUNGAA$",
						"ASSETSPEC",
						":assetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
					)
					if not LunghezzaMboSet.isEmpty():
						linLunghezza = (
							linLunghezza
							+ LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
						)
					else:
						assetCampataLocRelSet = AssetCampata.getMboSet(
							"$ALOCSOSTBB$",
							"ASSETLOCRELATION",
							"x3sapid in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)"
						)
						if not assetCampataLocRelSet.isEmpty():
							LunghezzaMboSet = assetCampataLocRelSet.getMbo(0).getMboSet(
								"$ASSET_LUNGCC$",
								"ASSETSPEC",
								":targetassetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
							)
							if not LunghezzaMboSet.isEmpty():
								linLunghezza = (
									LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
								)
								mbo.setValue("X4FISICOPLAN", linLunghezza)
		mbo.setValue("X4FISICOPLAN", linLunghezza)
if (
	not mbo.isNull("PARENT")
	and mbo.isNull("X4MICROCONTROLLO")
	and mbo.getString("STATUS")=="ESEGUITO"
	and (mbo.isNull("X4FISICO") or mbo.getDouble("X4FISICO")==0 or mbo.getString("X4FISICO")=="")
):
	parentSet = mbo.getMboSet("PARENT")
	parentWO = parentSet.getMbo(0)
	tipoCalcolo = evaluateCalc(mbo.getString("JPNUM"))
	if tipoCalcolo == "KM":
		AssetMboSet = mbo.getMboSet("ASSET")
		if not AssetMboSet.isEmpty():
			AssetMbo = AssetMboSet.getMbo(0)
			AssetCampataSet = AssetMbo.getMboSet("X4ANCESTOR")
			if not AssetCampataSet.isEmpty():
				AssetCampata = AssetCampataSet.getMbo(0)
				LunghezzaMboSet = AssetCampata.getMboSet(
					"$ASSET_LUNG12$",
					"ASSETSPEC",
					":assetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
				)
				if not LunghezzaMboSet.isEmpty():
					linLunghezza = LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
					oldfisicoplan = parentWO.getDouble("X4FISICO")
					somma = oldfisicoplan + linLunghezza
					mbo.setValue("X4FISICO", linLunghezza,11L)
					parentWO.setValue("X4FISICO", somma,11L)
					#parentSet.save()
				else:
					assetCampataLocRelSet = AssetCampata.getMboSet(
						"$ALOCSOST12$",
						"ASSETLOCRELATION",
						"x3sapid in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)"
					)
					if not assetCampataLocRelSet.isEmpty():
						LunghezzaMboSet = assetCampataLocRelSet.getMbo(0).getMboSet(
							"$ASSET_LUNG123$",
							"ASSETSPEC",
							":targetassetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
						)
						if not LunghezzaMboSet.isEmpty():
							linLunghezza = (
								LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
							)
							mbo.setValue("X4FISICO", linLunghezza,11L)
							oldfisicoplan = parentWO.getDouble("X4FISICO")
							somma = oldfisicoplan + linLunghezza
							parentWO.setValue("X4FISICO", somma,11L)
							#parentSet.save()
	elif tipoCalcolo == "QTY":
		fisicoPadre = parentWO.getDouble("X4FISICO")
		newFisico = fisicoPadre + mbo.getDouble("X4FISICOPLAN")
		parentWO.setValue("X4FISICO", newFisico,11L)
		mbo.setValue("X4FISICO", mbo.getDouble("X4FISICOPLAN"),11L)
		#parentSet.save()
		
	elif tipoCalcolo == "ETT":
		fisicoPadre = parentWO.getDouble("X4FISICO")
		assetSet=mbo.getMboSet("ASSET")
		if not assetSet.isEmpty():
			assetMbo=assetSet.getMbo(0)
			superficeSpecSet=assetMbo.getMboSet("$SUPERFICESPEC$","ASSETSPEC",":ASSETNUM=ASSETNUM AND SITEID=:SITEID and ASSETATTRID='STZ_SUPERFICIE_AREA_VERDE'")
			if not superficeSpecSet.isEmpty():
				superficeSpec=superficeSpecSet.getMbo(0)
				mbo.setValue("X4FISICO",superficeSpec.getDouble("NUMVALUE"))
				newFisico=fisicoPadre+superficeSpec.getDouble("NUMVALUE")
				parentWO.setValue("X4FISICO", newFisico,11L)
				#parentSet.save()
			else:
				mbo.setValue("X4FISICO","0")
else:
	tipoCalcolo = evaluateCalc(mbo.getString("JPNUM"))
	if tipoCalcolo == "KM" and mbo.getString("STATUS") == "ESEGUITO":
		if (mbo.isNull("X4FISICO") or mbo.getDouble("X4FISICO")==0 or mbo.getString("X4FISICO")=="") and mbo.isNull("PARENT") and mbo.getBoolean("X4AGGREGATI")==False:
			AssetMboSet = mbo.getMboSet("ASSET")
			if not AssetMboSet.isEmpty():
				AssetMbo = AssetMboSet.getMbo(0)
				AssetCampataSet = AssetMbo.getMboSet("X4ANCESTOR")
				if not AssetCampataSet.isEmpty():
					AssetCampata = AssetCampataSet.getMbo(0)
					LunghezzaMboSet = AssetCampata.getMboSet(
						"$ASSET_LUNG22$",
						"ASSETSPEC",
						":assetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
					)
					if not LunghezzaMboSet.isEmpty():
						linLunghezza = (
							LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
						)
						mbo.setValue("X4FISICO", linLunghezza,11L)
						#mbo.getThisMboSet().save()
						
					else:
						assetCampataLocRelSet = AssetCampata.getMboSet(
							"$ALOCSOST23$",
							"ASSETLOCRELATION",
							"x3sapid in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)"
						)
						if not assetCampataLocRelSet.isEmpty():
							LunghezzaMboSet = assetCampataLocRelSet.getMbo(0).getMboSet(
								"$ASSET_LUNG234$",
								"ASSETSPEC",
								":targetassetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
							)
							if not LunghezzaMboSet.isEmpty():
								linLunghezza = (
									LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
								)
								mbo.setValue("X4FISICO", linLunghezza)
	elif (
		tipoCalcolo == "QTY"
		and str(mbo.getString("STATUS")) == "ESEGUITO"
		and mbo.isNull("PARENT")
		and (mbo.isNull("X4FISICO") or mbo.getDouble("X4FISICO") == 0)
	):
		mbo.setValue("X4FISICO", "1",11L)
	elif tipoCalcolo == "ETT" and str(mbo.getString("STATUS")) == "ESEGUITO" and mbo.isNull("PARENT") and (mbo.isNull("X4FISICO") or mbo.getDouble("X4FISICO") == 0):
		assetSet=mbo.getMboSet("ASSET")
		if not assetSet.isEmpty():
			assetMbo=assetSet.getMbo(0)
			superficeSpecSet=assetMbo.getMboSet("$SUPERFICESPEC$","ASSETSPEC",":ASSETNUM=ASSETNUM AND SITEID=:SITEID and ASSETATTRID='STZ_SUPERFICIE_AREA_VERDE'")
			if not superficeSpecSet.isEmpty():
				superficeSpec=superficeSpecSet.getMbo(0)
				mbo.setValue("X4FISICO",superficeSpec.getDouble("NUMVALUE"))
			else:
				mbo.setValue("X4FISICO","0")
	if (
		tipoCalcolo == "KM"
		and mbo.getBoolean("X4AGGREGATI") == False
		and mbo.isNull("X4FISICOPLAN")
		and mbo.getString("STATUS") in ("APERTO", "DA VALIDARE")
	):
		AssetMboSet = mbo.getMboSet("ASSET")
		if not AssetMboSet.isEmpty():
			AssetMbo = AssetMboSet.getMbo(0)
			AssetCampataSet = AssetMbo.getMboSet("X4ANCESTOR")
			if not AssetCampataSet.isEmpty():
				AssetCampata = AssetCampataSet.getMbo(0)
				LunghezzaMboSet = AssetCampata.getMboSet(
					"$ASSET_LUNGA$",
					"ASSETSPEC",
					":assetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
				)
				if not LunghezzaMboSet.isEmpty():
					linLunghezza = (
						LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
					)

					mbo.setValue("X4FISICOPLAN", linLunghezza)
				else:
					assetCampataLocRelSet = AssetCampata.getMboSet(
						"$ALOCSOSTB$",
						"ASSETLOCRELATION",
						"x3sapid in (select substr(x3sapid,1,16) from assetlocrelation where targetassetnum=:assetnum)"
					)
					if not assetCampataLocRelSet.isEmpty():
						LunghezzaMboSet = assetCampataLocRelSet.getMbo(0).getMboSet(
							"$ASSET_LUNGC$",
							"ASSETSPEC",
							":targetassetnum=assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA'"
						)
						if not LunghezzaMboSet.isEmpty():
							linLunghezza = (
								LunghezzaMboSet.getMbo(0).getDouble("NUMVALUE") / 1000
							)
							mbo.setValue("X4FISICOPLAN", linLunghezza)
	if (
		(tipoCalcolo == "QTY" or tipoCalcolo == "N°")
		and mbo.isNull("X4FISICOPLAN")
		and mbo.getString("STATUS") in ("APERTO", "DA VALIDARE")
		and mbo.getBoolean("X4AGGREGATI") == False
		and mbo.getBoolean("X4SERBATOIO") == False
	):
		mbo.setValue("X4FISICOPLAN", "1")
	if tipoCalcolo == "ETT" and mbo.getBoolean("X4AGGREGATI") == False and mbo.isNull("X4FISICOPLAN") and mbo.getString("STATUS") in ("APERTO", "DA VALIDARE"):
		assetSet=mbo.getMboSet("ASSET")
		if not assetSet.isEmpty():
			assetMbo=assetSet.getMbo(0)
			superficeSpecSet=assetMbo.getMboSet("$SUPERFICESPEC$","ASSETSPEC",":ASSETNUM=ASSETNUM AND SITEID=:SITEID and ASSETATTRID='STZ_SUPERFICIE_AREA_VERDE'")
			if not superficeSpecSet.isEmpty():
				superficeSpec=superficeSpecSet.getMbo(0)
				mbo.setValue("X4FISICOPLAN",superficeSpec.getDouble("NUMVALUE"))
			else:
				mbo.setValue("X4FISICOPLAN","0")