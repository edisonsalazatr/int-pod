tolleranza = mbo.getDouble("X4ORETRASF")

if tolleranza and tolleranza < 0:
        service.error("x3terna", "ErrorMsg", ["Tempo di trasferimento deve essere valorizzato con un valore superiore o uguale a 0"])