tolleranza = mbo.getDouble("X4TOPER")

if tolleranza and tolleranza < 0:
        service.error("x3terna", "ErrorMsg", ["Il tempo di esecuzione deve essere valorizzato con un valore superiore o uguale a 0"])