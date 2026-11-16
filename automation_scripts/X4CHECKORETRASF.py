oretrasf = mbo.getDouble("X4ORETRASF")

if oretrasf and oretrasf< 0:
    service.error("x3terna", "ErrorMsg", ["Le ore trasferimento non possono essere negative"])