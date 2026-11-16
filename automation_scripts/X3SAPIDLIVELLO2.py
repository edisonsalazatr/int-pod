from psdi.mbo import Mbo
from java.util.regex import Pattern

arrclassliv2 = ['CAMPATA AEREA','TRATTA CAVO']
arrclassliv3 = ['SOSTEGNO AEREO','ESTREMO CAVO']
sapid = ""
REGEX_1_LVL = "^2[0-5][A-Z0-9-]{3}[A-Z][0-9]$"
REGEX_2_LVL = "^[0-9]{3}[A-Z0-9-]{0,5}$"
REGEX_3_LVL = "^[S|H|K|R][0-9]?$"


def isCorrectLvl1(stringa):        
    if not Pattern.matches(REGEX_1_LVL, stringa):
        service.error("x3err_format_liv1", "x3err_format_liv1", None)        

def isCorrectLvl2(stringa):
    if not Pattern.matches(REGEX_2_LVL, stringa):
        service.error("x3err_format_liv2", "x3err_format_liv2", None)
        
def isCorrectLvl3(stringa):
    if not Pattern.matches(REGEX_3_LVL, stringa):
        service.error("x3err_format_liv3", "x3err_format_liv3", None)        
             
def stringFiller(msg, filler, align, width):
    return '{message:{fill}{align}{width}}'.format(message=msg,
                                                   fill=filler,
                                                   align=align,
                                                   width=width)



if interactive and vi_sapid is not None:
    if vi_classstrac in arrclassliv2:
        sapid = str(vi_sapid)
        firstLvl    = sapid[:7] 
        secondLvl   = sapid[8:]
        isCorrectLvl1(firstLvl)
        isCorrectLvl2(secondLvl)
        if len(secondLvl) < 8:
            secondLvl=stringFiller(secondLvl, '-', '<', 8)
            new_sapid = firstLvl + '-' + secondLvl
            mbo.setValue("X3SAPID", new_sapid)
            #Save the changes
            mbo.getThisMboSet().save()
            mbo.getThisMboSet().close()
            
    elif vi_classstrac in arrclassliv3:
        
        new_sapid = None
        sapid = str(vi_sapid)
        
        idxLastLvl  = sapid.rfind("-") if sapid.rfind("-") == 16 else len(sapid)
        firstLvl    = sapid[:7] 
        secondLvl   = sapid[8:idxLastLvl]
        thirdLvl    = sapid[idxLastLvl+1:]
        
        #check first level
        isCorrectLvl1(firstLvl)
        
        #check second level
        isCorrectLvl2(secondLvl)
        if len(secondLvl) < 8:
            secondLvl=stringFiller(secondLvl, '-', '<', 8)
            new_sapid = firstLvl + '-' + secondLvl
                        
        #check third level
        if len(thirdLvl) > 0:
            isCorrectLvl3(thirdLvl)
            if new_sapid is not None:
                new_sapid = firstLvl + '-' + secondLvl + '-' + thirdLvl   
        else:
            if vi_classstrac == 'SOSTEGNO AEREO':
                new_sapid = firstLvl + '-' + secondLvl + '-S'
                
        if new_sapid is not None:
            #Save the changes
            mbo.setValue("X3SAPID", new_sapid)
            mbo.getThisMboSet().save()
            mbo.getThisMboSet().close()