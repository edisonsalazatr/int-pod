oreuff =  mbo.getDouble("X4OREUFF")

if oreuff and oreuff < 0:
    service.error("x3terna", "ErrorMsg", ["Ore Ufficio non possono essere negative"])