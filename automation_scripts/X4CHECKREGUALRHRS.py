regularhrs = mbo.getInt("REGULARHRS")

if regularhrs and regularhrs < 0:
    service.error("x3terna", "ErrorMsg", ["Il campo 'Ore esecuzione' non puo' essere negativo"])