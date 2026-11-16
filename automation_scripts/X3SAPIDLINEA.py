from psdi.mbo import Mbo
from psdi.mbo import MboConstants
from psdi.mbo import MboSet
from psdi.server import MXServer

#####Controllo Digit su singolo livello

def isCorrectLvl1(sapid):
    if (len(sapid) != 7):
        service.error("x3err_length_liv1","x3err_length_liv1",None)
    elif(sapid[0] !='2' or sapid[1].isnumeric() == False or sapid[2].isalnum() == False or sapid[3].isalnum() == False or sapid[4].isalnum() == False or sapid[5].isalpha() == False or sapid[6].isnumeric() == False):
        service.error("x3err_format_liv1","x3err_format_liv1",None)
    elif((sapid[2].isalpha() and sapid[2].islower()) or (sapid[2].isalpha() and sapid[2].islower()) or (sapid[2].isalpha()and sapid[2].islower()) or sapid[5].islower()):
        service.error("x3err_maiuscole_liv1","x3err_maiuscole_liv1",None)



if interactive and vi_sapid is not None:
    if vi_linea == "LINEA":
        isCorrectLvl1(vi_sapid)         
    elif vi_linea == "STAZIONE":
        if (len(vi_sapid) != 6):
            service.error("x3err_lenght_staz","x3err_lenght_staz",None)
        elif(vi_sapid[0] !='3' or vi_sapid[1].isnumeric() == False or vi_sapid[2].isalnum() == False or vi_sapid[3].isalnum() == False or vi_sapid[4].isalnum() == False or vi_sapid[5].isalpha() == False):
            service.error("x3err_format_staz","x3err_format_staz",None)