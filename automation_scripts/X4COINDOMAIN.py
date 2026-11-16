tipospesa=mbo.getString("X4TIPOSPESA")

if tipospesa=="CAPEX":
    domainid='X4COINCAPEX'
elif tipospesa=="OPEX":
    domainid='X4COINOPEX'
elif tipospesa=="ANR":
    domainid='X4COINANR'
else:
    domainid='X4SEARCHCOIN'