tolleranza = mbo.getDouble("X4TTRASF")

if tolleranza and tolleranza < 0:
        service.error("x3terna", "ErrorMsg", ["Il tempo di trasferimento deve essere valorizzato con un valore superiore o uguale a 0"])