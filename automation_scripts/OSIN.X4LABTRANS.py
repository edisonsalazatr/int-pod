from psdi.mbo import MboConstants
from java.text import SimpleDateFormat
from java.util import Date
import time

def beforeMboData(ctx):
    time.sleep(4)
    if(ctx.getProcessTable()=="LABTRANS"):
        mbo = ctx.getMbo()
        data = ctx.getData()
        datainizio_str = ctx.getData().getCurrentData("STARTDATETIME")
        if datainizio_str:
            datainizio_part, timeinizio_part = datainizio_str.split("T")
            mbo.setValue("STARTDATETIME", datainizio_str, 11L)
            mbo.setValue("STARTDATE", datainizio_part, 11L)
            mbo.setValue("STARTTIME", timeinizio_part, 11L)
            
        datafine_str = ctx.getData().getCurrentData("FINISHDATETIME")
        if datafine_str:
            datafine_part, timefine_part = datafine_str.split("T")
            mbo.setValue("FINISHDATETIME", datafine_str, 11L)
            mbo.setValue("FINISHDATE", datafine_part, 11L)
            mbo.setValue("FINISHTIME", timefine_part, 11L)
            
        durata = ctx.getData().getCurrentData("regularhrs")
        if durata:
            mbo.setValue("regularhrs", durata, 11L)
            
        genapp=data.getCurrentData("GENAPPRSERVRECEIPT");
        if genapp:
            
            if data.getCurrentData("X4CHECKSAP")=="OK":
                data.setCurrentData("GENAPPRSERVRECEIPT","1")
                mbo.setValue("GENAPPRSERVRECEIPT","1",11L)
            if data.getCurrentData("X4CHECKSAP")=="KO" and "connect timed out" not in data.getCurrentData("X4ESITOSAPDESC").lower():
                data.setCurrentData("GENAPPRSERVRECEIPT","0")
                mbo.setValue("GENAPPRSERVRECEIPT","0",11L)
        elif not genapp:
            if data.getCurrentData("X4CHECKSAP")=="KO" and "connect timed out" not in data.getCurrentData("X4ESITOSAPDESC").lower():
                mbo.setValue("GENAPPRSERVRECEIPT","0",11L)
            elif data.getCurrentData("X4CHECKSAP")=="OK":
                mbo.setValue("GENAPPRSERVRECEIPT","1",11L)

        '''
        date_format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        datainizio_str = ctx.getData().getCurrentData("STARTDATETIME")
        datafine_str = ctx.getData().getCurrentData("FINISHDATETIME")
    
        # Converte stringhe in oggetti Date
        datainizio = date_format.parse(datainizio_str) if datainizio_str else None
        datafine = date_format.parse(datafine_str) if datafine_str else None
    
        durata = ctx.getData().getCurrentData("regularhrs")
    
        if datainizio and datafine:
            diff_millis = datafine.getTime() - datainizio.getTime()
            diff_ore = diff_millis / (1000 * 60 * 60)  # Conversione da millisecondi a ore
            if float(durata) < float(diff_ore):
                #ctx.error("TEST","durata: "+str(durata)+", diffore:"+str(diff_ore))
                mbo = ctx.getMbo()
                if durata:
                    mbo.setValue("regularhrs", durata, 11L)
                    mbo.setValue("STARTDATETIME", datafine_str, 11L)
                    mbo.setValue("FINISHDATETIME", datafine_str, 11L)
                else:
                    mbo.setValueNull("regularhrs", MboConstants.NOVALIDATION_AND_NOACTION)