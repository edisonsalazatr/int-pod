from psdi.server import MXServer
from psdi.mbo import MboConstants
from psdi.util.logging import MXLoggerFactory

prefix = "[ X4WORKORDER_ADD ] "
#logger = MXLoggerFactory.getLogger("maximo.terna")

persongroup = None
map_attivita = {
    "CONTROLLO PERIODICO": "MONITORAGGIO",
    "CONTROLLO DI LEGGE": "MONITORAGGIO",
    "CONTROLLO TECNICO": "MONITORAGGIO",
    "CONTROLLO OCCASIONALE": "MONITORAGGIO",
    "CONTROLLO SU CONDIZIONE": "MONITORAGGIO",
    "MANUTENZIONE SU CONDIZIONE": "MANUTENZIONE",
    "SOSTITUZIONE": "SOSTITUZIONE",
    "NUOVA COSTRUZIONE/INSTALLAZIONE": "NUOVE INSTALLAZIONI",
    "PRONTO INTERVENTO": "PRONTO INTERVENTO",
    "DEMOLIZIONE": "DEMOLIZIONE",
    "ANR": "ANR",
    "ALTRE ATTIVITA": "ALTRE ATTIVITA",
    "ATTIVITA NON OPERATIVE": "ATTIVITA NON OPERATIVE"
}
'''
if launchPoint!="JPNUMVALIDATE" and (mbo.isNull("JPNUM") or mbo.isNull("ASSETNUM") or mbo.isNull("TARGCOMPDATE") or mbo.isNull("TARGSTARTDATE") or mbo.isNull("FNLCONSTRAINT")) and mbo.getBoolean("X4SERBATOIO")==False:
    
    # service.error("debug", "%s - %s - %s - %s - %s - %s" % (mbo.isNull("JPNUM"), 
    #                                                     mbo.isNull("ASSETNUM"),
    #                                                     mbo.isNull("TARGCOMPDATE"),
    #                                                     mbo.isNull("TARGSTARTDATE"),
    #                                                     mbo.isNull("FNLCONSTRAINT"), 
    #                                                     mbo.getBoolean("X4SERBATOIO")))
    service.error("x3terna","ErrorMsg", ["Campi minimi obbligatori non compilati."] )
'''
def evaluateClass(classification):
    return map_attivita.get(classification, "ALTRO")
    
def evalwbe(tipospesa, udb, coin):
    if tipospesa!="ANR":
        mapping = {
            "UGE": ("OS-AX-00-Gen", "OS-AX-00-Gen-TT-OM"),
            "UNO": ("OS-AX-00-Nov", "OS-AX-00-Nov-TT-OM"),
            "UPM": ("OS-AX-00-PSM", "OS-AX-00-PSM-TT-OM"),
            "UTO": ("OS-AX-00-Tor", "OS-AX-00-Tor-TT-OM"),
            "UBS": ("OS-BX-00-Bre", "OS-BX-00-Bre-TT-OM"),
            "UBG": ("OS-BX-00-Bru", "OS-BX-00-Bru-TT-OM"),
            "UCH": ("OS-BX-00-Chi", "OS-BX-00-Chi-TT-OM"),
            "UCS": ("OS-BX-00-Cis", "OS-BX-00-Cis-TT-OM"),
            "UGO": ("OS-BX-00-Gor", "OS-BX-00-Gor-TT-OM"),
            "UCA": ("OS-CX-00-Cam", "OS-CX-00-Cam-TT-OM"),
            "UDU": ("OS-CX-00-Dug", "OS-CX-00-Dug-TT-OM"),
            "UFV": ("OS-CX-00-FVG", "OS-CX-00-FVG-TT-OM"),
            "UTA": ("OS-CX-00-TAA", "OS-CX-00-TAA-TT-OM"),
            "UVV": ("OS-CX-00-ViV", "OS-CX-00-ViV-TT-OM"),
            "UBO": ("OS-DX-00-Bol", "OS-DX-00-Bol-TT-OM"),
            "UFI": ("OS-DX-00-Fir", "OS-DX-00-Fir-TT-OM"),
            "UPR": ("OS-DX-00-Par", "OS-DX-00-Par-TT-OM"),
            "USU": ("OS-DX-00-Suv", "OS-DX-00-Suv-TT-OM"),
            "UAM": ("OS-EX-00-Abr", "OS-EX-00-Abr-TT-OM"),
            "ULN": ("OS-EX-00-LaN", "OS-EX-00-LaN-TT-OM"),
            "ULS": ("OS-EX-00-LaS", "OS-EX-00-LaS-TT-OM"),
            "UMA": ("OS-EX-00-Mar", "OS-EX-00-Mar-TT-OM"),
            "UMB": ("OS-EX-00-Umb", "OS-EX-00-Umb-TT-OM"),
            "UBA": ("OS-FX-00-Bar", "OS-FX-00-Bar-TT-OM"),
            "UBR": ("OS-FX-00-Bri", "OS-FX-00-Bri-TT-OM"),
            "UFE": ("OS-FX-00-Fer", "OS-FX-00-Fer-TT-OM"),
            "UFM": ("OS-FX-00-Fra", "OS-FX-00-Fra-TT-OM"),
            "UMD": ("OS-FX-00-Mad", "OS-FX-00-Mad-TT-OM"),
            "URO": ("OS-FX-00-Rot", "OS-FX-00-Rot-TT-OM"),
            "UCT": ("OS-GX-00-Cat", "OS-GX-00-Cat-TT-OM"),
            "UPA": ("OS-GX-00-Pal", "OS-GX-00-Pal-TT-OM"),
            "UCO": ("OS-HX-00-Cod", "OS-HX-00-Cod-TT-OM"),
            "UVI": ("OS-HX-00-Vil", "OS-HX-00-Vil-TT-OM"),
            "GTG": ("OS-TG-00-TGR", "OS-TG-00-TGR-TT-OM"),
            "RT2": ("OS-RT-00-RT2", "OS-RT-00-RT2-TT-OM")
        }
        return mapping.get(udb, (None, None))
    elif tipospesa=="ANR":
        mapping = {
            ("EB101A1IRU", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-IRU"),
            ("EB101A1IRU", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-IRU"),
            ("EB101A1IRU", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-IRU"),
            ("EB101A1IRU", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-IRU"),
            ("EB101A1IRU", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-IRU"),
            ("EB101A1IRU", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-IRU"),
            ("EB101A1IRU", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-IRU"),
            ("EB101A1IRU", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-IRU"),
            ("EB101A1IRU", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-IRU"),
            ("EB101A1IRU", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-IRU"),
            ("EB101A1IRU", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-IRU"),
            ("EB101A1IRU", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-IRU"),
            ("EB101A1IRU", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-IRU"),
            ("EB101A1IRU", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-IRU"),
            ("EB101A1IRU", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-IRU"),
            ("EB101A1IRU", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-IRU"),
            ("EB101A1IRU", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-IRU"),
            ("EB101A2TLC", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-TLC"),
            ("EB101A2TLC", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-TLC"),
            ("EB101A2TLC", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-TLC"),
            ("EB101A2TLC", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-TLC"),
            ("EB101A2TLC", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-TLC"),
            ("EB101A2TLC", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-TLC"),
            ("EB101A2TLC", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-TLC"),
            ("EB101A2TLC", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-TLC"),
            ("EB101A2TLC", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-TLC"),
            ("EB101A2TLC", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-TLC"),
            ("EB101A2TLC", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-TLC"),
            ("EB101A2TLC", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-TLC"),
            ("EB101A2TLC", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-TLC"),
            ("EB101A2TLC", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-TLC"),
            ("EB101A2TLC", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-TLC"),
            ("EB101A2TLC", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-TLC"),
            ("EB101A2TLC", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-TLC"),
            ("EB101A2TLC", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-TLC"),
            ("EB101A2TLC", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-TLC"),
            ("EB101A2TLC", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-TLC"),
            ("EB101A2TLC", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-TLC"),
            ("EB101A2TLC", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-TLC"),
            ("EB101A2TLC", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-TLC"),
            ("EB101A2TLC", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-TLC"),
            ("EB101A2TLC", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-TLC"),
            ("EB101A2TLC", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-TLC"),
            ("EB101A2TLC", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-TLC"),
            ("EB101A2TLC", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-TLC"),
            ("EB101A2TLC", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-TLC"),
            ("EB101A2TLC", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-TLC"),
            ("EB101A2TLC", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-TLC"),
            ("EB101A2TLC", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-TLC"),
            ("EB101A2TLC", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-TLC"),
            ("EB101A3APP", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-APP"),
            ("EB101A3APP", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-APP"),
            ("EB101A3APP", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-APP"),
            ("EB101A3APP", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-APP"),
            ("EB101A3APP", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-APP"),
            ("EB101A4HSE", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-HSE"),
            ("EB101A4HSE", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-HSE"),
            ("EB101A4HSE", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-HSE"),
            ("EB101A4HSE", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-HSE"),
            ("EB101A4HSE", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-HSE"),
            ("EB101A4HSE", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-HSE"),
            ("EB101A4HSE", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-HSE"),
            ("EB101A4HSE", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-HSE"),
            ("EB101A4HSE", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-HSE"),
            ("EB101A4HSE", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-HSE"),
            ("EB101A4HSE", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-HSE"),
            ("EB101A4HSE", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-HSE"),
            ("EB101A4HSE", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-HSE"),
            ("EB101A4HSE", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-HSE"),
            ("EB101A4HSE", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-HSE"),
            ("EB101A4HSE", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-HSE"),
            ("EB101A4HSE", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-HSE"),
            ("EB101A4HSE", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-HSE"),
            ("EB101A4HSE", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-HSE"),
            ("EB101A4HSE", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-HSE"),
            ("EB101A4HSE", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-HSE"),
            ("EB101A4HSE", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-HSE"),
            ("EB101A4HSO", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-HSO"),
            ("EB101A4HSO", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-HSO"),
            ("EB101A4HSO", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-HSO"),
            ("EB101A4HSO", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-HSO"),
            ("EB101A4HSO", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-HSO"),
            ("EB101A4HSO", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-HSO"),
            ("EB101A4HSO", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-HSO"),
            ("EB101A4HSO", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-HSO"),
            ("EB101A4HSO", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-HSO"),
            ("EB101A4HSO", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-HSO"),
            ("EB101A5MFO", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-MFO"),
            ("EB101A5MFO", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-MFO"),
            ("EB101A5MFO", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-MFO"),
            ("EB101A5MFO", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-MFO"),
            ("EB101A5MFO", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-MFO"),
            ("EB101A5MFO", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-MFO"),
            ("EB101A5MFO", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-MFO"),
            ("EB101A5MFO", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-MFO"),
            ("EB101A5MFO", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-MFO"),
            ("EB101A5MFO", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-MFO"),
            ("EB101A5MFO", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-MFO"),
            ("EB101A5MFO", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-MFO"),
            ("EB101A5MFO", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-MFO"),
            ("EB101A5MFO", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-MFO"),
            ("EB101A5MFO", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-MFO"),
            ("EB101A5MFO", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-MFO"),
            ("EB101A5MFO", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-MFO"),
            ("EB101A5MFO", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-MFO"),
            ("EB101A5MFO", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-MFO"),
            ("EB101A5MFO", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-MFO"),
            ("EB101A5MFO", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-MFO"),
            ("EB101A5MFO", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-MFO"),
            ("EB101A5MFO", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-MFO"),
            ("EB101A5MFO", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-MFO"),
            ("EB101A5MFO", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-MFO"),
            ("EB101A5MFO", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-MFO"),
            ("EB101A5MFO", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-MFO"),
            ("EB101A5MFO", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-MFO"),
            ("EB101A5MFO", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-MFO"),
            ("EB101A5MFO", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-MFO"),
            ("EB101A5MFO", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-MFO"),
            ("EB101A5MFO", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-MFO"),
            ("EB101A5MFO", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-MFO"),
            ("EB101B1EST", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-EST"),
            ("EB101B1EST", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-EST"),
            ("EB101B1EST", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-EST"),
            ("EB101B1EST", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-EST"),
            ("EB101B1EST", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-EST"),
            ("EB101B1EST", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-EST"),
            ("EB101B1EST", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-EST"),
            ("EB101B1EST", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-EST"),
            ("EB101B1EST", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-EST"),
            ("EB101B1EST", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-EST"),
            ("EB101B1EST", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-EST"),
            ("EB101B1EST", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-EST"),
            ("EB101B1EST", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-EST"),
            ("EB101B1EST", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-EST"),
            ("EB101B1EST", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-EST"),
            ("EB101B1EST", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-EST"),
            ("EB101B1EST", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-EST"),
            ("EB101B1EST", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-EST"),
            ("EB101B1EST", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-EST"),
            ("EB101B1EST", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-EST"),
            ("EB101B1EST", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-EST"),
            ("EB101B1EST", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-EST"),
            ("EB101B1EST", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-EST"),
            ("EB101B1EST", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-EST"),
            ("EB101B1EST", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-EST"),
            ("EB101B1EST", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-EST"),
            ("EB101B1EST", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-EST"),
            ("EB101B1EST", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-EST"),
            ("EB101B1EST", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-EST"),
            ("EB101B1EST", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-EST"),
            ("EB101B1EST", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-EST"),
            ("EB101B1EST", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-EST"),
            ("EB101B1EST", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-EST"),
            ("EB101B3RAM", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-RAM"),
            ("EB101B3RAM", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-RAM"),
            ("EB101B3RAM", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-RAM"),
            ("EB101B3RAM", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-RAM"),
            ("EB101B3RAM", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-RAM"),
            ("EB101B3RAM", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-RAM"),
            ("EB101B3RAM", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-RAM"),
            ("EB101B3RAM", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-RAM"),
            ("EB101B3RAM", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-RAM"),
            ("EB101B3RAM", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-RAM"),
            ("EB101B3RAM", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-RAM"),
            ("EB101B3RAM", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-RAM"),
            ("EB101B3RAM", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-RAM"),
            ("EB101B3RAM", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-RAM"),
            ("EB101B3RAM", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-RAM"),
            ("EB101B3RAM", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-RAM"),
            ("EB101B3RAM", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-RAM"),
            ("EB101B3RAM", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-RAM"),
            ("EB101B3RAM", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-RAM"),
            ("EB101B3RAM", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-RAM"),
            ("EB101B3RAM", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-RAM"),
            ("EB101B3RAM", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-RAM"),
            ("EB101B3RAM", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-RAM"),
            ("EB101B3RAM", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-RAM"),
            ("EB101B3RAM", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-RAM"),
            ("EB101B3RAM", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-RAM"),
            ("EB101B3RAM", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-RAM"),
            ("EB101B3RAM", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-RAM"),
            ("EB101B3RAM", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-RAM"),
            ("EB101B3RAM", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-RAM"),
            ("EB101B3RAM", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-RAM"),
            ("EB101B3RAM", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-RAM"),
            ("EB101B3RAM", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-RAM"),
            ("EB101B4EPC", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-EPC"),
            ("EB101B4EPC", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-EPC"),
            ("EB101B4EPC", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-EPC"),
            ("EB101B4EPC", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-EPC"),
            ("EB101B4EPC", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-EPC"),
            ("EB101B4EPC", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-EPC"),
            ("EB101B4EPC", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-EPC"),
            ("EB101B4EPC", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-EPC"),
            ("EB101B4EPC", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-EPC"),
            ("EB101B4EPC", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-EPC"),
            ("EB101B4EPC", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-EPC"),
            ("EB101B4EPC", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-EPC"),
            ("EB101B4EPC", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-EPC"),
            ("EB101B4EPC", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-EPC"),
            ("EB101B4EPC", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-EPC"),
            ("EB101B4EPC", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-EPC"),
            ("EB101B4EPC", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-EPC"),
            ("EB101B4EPC", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-EPC"),
            ("EB101B4EPC", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-EPC"),
            ("EB101B4EPC", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-EPC"),
            ("EB101B4EPC", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-EPC"),
            ("EB101B4EPC", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-EPC"),
            ("EB101B4EPC", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-EPC"),
            ("EB101B4EPC", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-EPC"),
            ("EB101B4EPC", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-EPC"),
            ("EB101B4EPC", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-EPC"),
            ("EB101B4EPC", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-EPC"),
            ("EB101B4EPC", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-EPC"),
            ("EB101B4EPC", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-EPC"),
            ("EB101B4EPC", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-EPC"),
            ("EB101B4EPC", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-EPC"),
            ("EB101B4EPC", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-EPC"),
            ("EB101B4EPC", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-EPC"),
            ("EB101B4ING", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-ING"),
            ("EB101B4ING", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-ING"),
            ("EB101B4ING", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-ING"),
            ("EB101B4ING", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-ING"),
            ("EB101B4ING", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-ING"),
            ("EB101B4ING", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-ING"),
            ("EB101B4ING", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-ING"),
            ("EB101B4ING", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-ING"),
            ("EB101B4ING", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-ING"),
            ("EB101B4ING", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-ING"),
            ("EB101B4ING", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-ING"),
            ("EB101B4ING", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-ING"),
            ("EB101B4ING", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-ING"),
            ("EB101B4ING", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-ING"),
            ("EB101B4ING", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-ING"),
            ("EB101B4ING", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-ING"),
            ("EB101B4ING", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-ING"),
            ("EB101B4ING", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-ING"),
            ("EB101B4ING", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-ING"),
            ("EB101B4ING", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-ING"),
            ("EB101B4ING", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-ING"),
            ("EB101B4ING", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-ING"),
            ("EB101B4ING", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-ING"),
            ("EB101B4ING", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-ING"),
            ("EB101B4ING", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-ING"),
            ("EB101B4ING", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-ING"),
            ("EB101B4ING", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-ING"),
            ("EB101B4ING", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-ING"),
            ("EB101B4ING", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-ING"),
            ("EB101B4ING", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-ING"),
            ("EB101B4ING", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-ING"),
            ("EB101B4ING", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-ING"),
            ("EB101B4ING", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-ING"),
            ("EB101B4IS3", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-S3"),
            ("EB101B4IS3", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-S3"),
            ("EB101B4IS3", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-S3"),
            ("EB101B4IS3", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-S3"),
            ("EB101B4IS3", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-S3"),
            ("EB101B4IS3", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-S3"),
            ("EB101B4IS3", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-S3"),
            ("EB101B4IS3", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-S3"),
            ("EB101B4IS3", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-S3"),
            ("EB101B4IS3", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-S3"),
            ("EB101B4IS3", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-S3"),
            ("EB101B4IS3", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-S3"),
            ("EB101B4IS3", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-S3"),
            ("EB101B4IS3", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-S3"),
            ("EB101B4IS3", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-S3"),
            ("EB101B4IS3", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-S3"),
            ("EB101B4IS3", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-S3"),
            ("EB101B4IS3", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-S3"),
            ("EB101B4IS3", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-S3"),
            ("EB101B4IS3", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-S3"),
            ("EB101B4IS3", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-S3"),
            ("EB101B4IS3", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-S3"),
            ("EB101B4IS3", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-S3"),
            ("EB101B4IS3", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-S3"),
            ("EB101B4IS3", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-S3"),
            ("EB101B4IS3", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-S3"),
            ("EB101B4IS3", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-S3"),
            ("EB101B4IS3", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-S3"),
            ("EB101B4IS3", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-S3"),
            ("EB101B4IS3", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-S3"),
            ("EB101B4IS3", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-S3"),
            ("EB101B4IS3", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-S3"),
            ("EB101B4IS3", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-S3"),
            ("EB101B5BOO", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-BOO"),
            ("EB101B5BOO", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-BOO"),
            ("EB101B5BOO", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-BOO"),
            ("EB101B5BOO", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-BOO"),
            ("EB101B5BOO", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-BOO"),
            ("EB101B5BOO", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-BOO"),
            ("EB101B5BOO", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-BOO"),
            ("EB101B5BOO", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-BOO"),
            ("EB101B5BOO", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-BOO"),
            ("EB101B5BOO", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-BOO"),
            ("EB101B5BOO", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-BOO"),
            ("EB101B5BOO", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-BOO"),
            ("EB101B5BOO", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-BOO"),
            ("EB101B5BOO", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-BOO"),
            ("EB101B5BOO", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-BOO"),
            ("EB101B5BOO", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-BOO"),
            ("EB101B5BOO", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-BOO"),
            ("EB101B5BOO", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-BOO"),
            ("EB101B5BOO", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-BOO"),
            ("EB101B5BOO", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-BOO"),
            ("EB101B5BOO", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-BOO"),
            ("EB101B5BOO", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-BOO"),
            ("EB101B5BOO", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-BOO"),
            ("EB101B5BOO", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-BOO"),
            ("EB101B5BOO", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-BOO"),
            ("EB101B5BOO", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-BOO"),
            ("EB101B5BOO", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-BOO"),
            ("EB101B5BOO", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-BOO"),
            ("EB101B5BOO", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-BOO"),
            ("EB101B5BOO", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-BOO"),
            ("EB101B5BOO", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-BOO"),
            ("EB101B5BOO", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-BOO"),
            ("EB101B5BOO", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-BOO"),
            ("EB101B5GDR", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-GDR"),
            ("EB101B5GDR", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-GDR"),
            ("EB101B5GDR", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-GDR"),
            ("EB101B5GDR", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-GDR"),
            ("EB101B5GDR", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-GDR"),
            ("EB101B5GDR", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-GDR"),
            ("EB101B5GDR", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-GDR"),
            ("EB101B5GDR", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-GDR"),
            ("EB101B5GDR", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-GDR"),
            ("EB101B5GDR", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-GDR"),
            ("EB101B5GDR", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-GDR"),
            ("EB101B5GDR", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-GDR"),
            ("EB101B5GDR", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-GDR"),
            ("EB101B5GDR", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-GDR"),
            ("EB101B5GDR", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-GDR"),
            ("EB101B5GDR", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-GDR"),
            ("EB101B5GDR", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-GDR"),
            ("EB101B5GDR", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-GDR"),
            ("EB101B5GDR", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-GDR"),
            ("EB101B5GDR", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-GDR"),
            ("EB101B5GDR", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-GDR"),
            ("EB101B5GDR", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-GDR"),
            ("EB101B5GDR", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-GDR"),
            ("EB101B5GDR", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-GDR"),
            ("EB101B5GDR", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-GDR"),
            ("EB101B5GDR", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-GDR"),
            ("EB101B5GDR", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-GDR"),
            ("EB101B5GDR", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-GDR"),
            ("EB101B5GDR", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-GDR"),
            ("EB101B5GDR", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-GDR"),
            ("EB101B5GDR", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-GDR"),
            ("EB101B5GDR", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-GDR"),
            ("EB101B5GDR", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-GDR"),
            ("EB101B5GEA", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-GEA"),
            ("EB101B5GEA", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-GEA"),
            ("EB101B5GEA", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-GEA"),
            ("EB101B5GEA", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-GEA"),
            ("EB101B5GEA", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-GEA"),
            ("EB101B5GEA", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-GEA"),
            ("EB101B5GEA", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-GEA"),
            ("EB101B5GEA", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-GEA"),
            ("EB101B5GEA", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-GEA"),
            ("EB101B5GEA", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-GEA"),
            ("EB101B5GEA", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-GEA"),
            ("EB101B5GEA", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-GEA"),
            ("EB101B5GEA", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-GEA"),
            ("EB101B5GEA", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-GEA"),
            ("EB101B5GEA", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-GEA"),
            ("EB101B5GEA", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-GEA"),
            ("EB101B5GEA", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-GEA"),
            ("EB101B5GEA", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-GEA"),
            ("EB101B5GEA", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-GEA"),
            ("EB101B5GEA", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-GEA"),
            ("EB101B5GEA", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-GEA"),
            ("EB101B5GEA", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-GEA"),
            ("EB101B5GEA", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-GEA"),
            ("EB101B5GEA", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-GEA"),
            ("EB101B5GEA", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-GEA"),
            ("EB101B5GEA", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-GEA"),
            ("EB101B5GEA", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-GEA"),
            ("EB101B5GEA", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-GEA"),
            ("EB101B5GEA", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-GEA"),
            ("EB101B5GEA", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-GEA"),
            ("EB101B5GEA", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-GEA"),
            ("EB101B5GEA", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-GEA"),
            ("EB101B5GEA", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-GEA"),
            ("EB101B5GIA", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-GIA"),
            ("EB101B5GIA", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-GIA"),
            ("EB101B5GIA", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-GIA"),
            ("EB101B5GIA", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-GIA"),
            ("EB101B5GIA", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-GIA"),
            ("EB101B5GIA", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-GIA"),
            ("EB101B5GIA", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-GIA"),
            ("EB101B5GIA", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-GIA"),
            ("EB101B5GIA", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-GIA"),
            ("EB101B5GIA", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-GIA"),
            ("EB101B5GIA", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-GIA"),
            ("EB101B5GIA", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-GIA"),
            ("EB101B5GIA", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-GIA"),
            ("EB101B5GIA", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-GIA"),
            ("EB101B5GIA", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-GIA"),
            ("EB101B5GIA", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-GIA"),
            ("EB101B5GIA", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-GIA"),
            ("EB101B5GIA", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-GIA"),
            ("EB101B5GIA", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-GIA"),
            ("EB101B5GIA", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-GIA"),
            ("EB101B5GIA", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-GIA"),
            ("EB101B5GIA", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-GIA"),
            ("EB101B5GIA", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-GIA"),
            ("EB101B5GIA", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-GIA"),
            ("EB101B5GIA", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-GIA"),
            ("EB101B5GIA", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-GIA"),
            ("EB101B5GIA", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-GIA"),
            ("EB101B5GIA", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-GIA"),
            ("EB101B5GIA", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-GIA"),
            ("EB101B5GIA", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-GIA"),
            ("EB101B5GIA", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-GIA"),
            ("EB101B5GIA", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-GIA"),
            ("EB101B5GIA", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-GIA"),
            ("EB101C1ALT", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-ALT"),
            ("EB101C1ALT", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-ALT"),
            ("EB101C1ALT", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-ALT"),
            ("EB101C1ALT", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-ALT"),
            ("EB101C1ALT", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-ALT"),
            ("EB101C1ALT", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-ALT"),
            ("EB101C1ALT", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-ALT"),
            ("EB101C1ALT", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-ALT"),
            ("EB101C1ALT", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-ALT"),
            ("EB101C1ALT", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-ALT"),
            ("EB101C1ALT", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-ALT"),
            ("EB101C1ALT", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-ALT"),
            ("EB101C1ALT", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-ALT"),
            ("EB101C1ALT", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-ALT"),
            ("EB101C1ALT", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-ALT"),
            ("EB101C1ALT", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-ALT"),
            ("EB101C1ALT", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-ALT"),
            ("EB101C1ALT", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-ALT"),
            ("EB101C1ALT", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-ALT"),
            ("EB101C1ALT", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-ALT"),
            ("EB101C1ALT", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-ALT"),
            ("EB101C1ALT", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-ALT"),
            ("EB101C1ALT", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-ALT"),
            ("EB101C1ALT", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-ALT"),
            ("EB101C1ALT", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-ALT"),
            ("EB101C1ALT", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-ALT"),
            ("EB101C1ALT", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-ALT"),
            ("EB101C1ALT", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-ALT"),
            ("EB101C1ALT", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-ALT"),
            ("EB101C1ALT", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-ALT"),
            ("EB101C1ALT", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-ALT"),
            ("EB101C1ALT", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-ALT"),
            ("EB101C1ALT", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-ALT"),
            ("EB101C1AST", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-AST"),
            ("EB101C1AST", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-AST"),
            ("EB101C1AST", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-AST"),
            ("EB101C1AST", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-AST"),
            ("EB101C1AST", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-AST"),
            ("EB101C1AST", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-AST"),
            ("EB101C1AST", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-AST"),
            ("EB101C1AST", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-AST"),
            ("EB101C1AST", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-AST"),
            ("EB101C1AST", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-AST"),
            ("EB101C1AST", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-AST"),
            ("EB101C1AST", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-AST"),
            ("EB101C1AST", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-AST"),
            ("EB101C1AST", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-AST"),
            ("EB101C1AST", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-AST"),
            ("EB101C1AST", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-AST"),
            ("EB101C1AST", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-AST"),
            ("EB101C1AST", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-AST"),
            ("EB101C1AST", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-AST"),
            ("EB101C1AST", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-AST"),
            ("EB101C1AST", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-AST"),
            ("EB101C1AST", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-AST"),
            ("EB101C1AST", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-AST"),
            ("EB101C1AST", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-AST"),
            ("EB101C1AST", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-AST"),
            ("EB101C1AST", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-AST"),
            ("EB101C1AST", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-AST"),
            ("EB101C1AST", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-AST"),
            ("EB101C1AST", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-AST"),
            ("EB101C1AST", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-AST"),
            ("EB101C1AST", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-AST"),
            ("EB101C1AST", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-AST"),
            ("EB101C1AST", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-AST"),
            ("EB101C1CRI", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-CRI"),
            ("EB101C1CRI", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-CRI"),
            ("EB101C1CRI", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-CRI"),
            ("EB101C1CRI", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-CRI"),
            ("EB101C1CRI", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-CRI"),
            ("EB101C1CRI", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-CRI"),
            ("EB101C1CRI", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-CRI"),
            ("EB101C1CRI", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-CRI"),
            ("EB101C1CRI", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-CRI"),
            ("EB101C1CRI", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-CRI"),
            ("EB101C1CRI", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-CRI"),
            ("EB101C1CRI", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-CRI"),
            ("EB101C1CRI", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-CRI"),
            ("EB101C1CRI", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-CRI"),
            ("EB101C1CRI", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-CRI"),
            ("EB101C1CRI", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-CRI"),
            ("EB101C1CRI", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-CRI"),
            ("EB101C1CRI", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-CRI"),
            ("EB101C1CRI", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-CRI"),
            ("EB101C1CRI", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-CRI"),
            ("EB101C1CRI", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-CRI"),
            ("EB101C1CRI", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-CRI"),
            ("EB101C1CRI", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-CRI"),
            ("EB101C1CRI", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-CRI"),
            ("EB101C1CRI", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-CRI"),
            ("EB101C1CRI", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-CRI"),
            ("EB101C1CRI", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-CRI"),
            ("EB101C1CRI", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-CRI"),
            ("EB101C1CRI", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-CRI"),
            ("EB101C1CRI", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-CRI"),
            ("EB101C1CRI", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-CRI"),
            ("EB101C1CRI", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-CRI"),
            ("EB101C1CRI", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-CRI"),
            ("EB101C1RES", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-RES"),
            ("EB101C1RES", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-RES"),
            ("EB101C1RES", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-RES"),
            ("EB101C1RES", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-RES"),
            ("EB101C1RES", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-RES"),
            ("EB101C1RES", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-RES"),
            ("EB101C1RES", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-RES"),
            ("EB101C1RES", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-RES"),
            ("EB101C1RES", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-RES"),
            ("EB101C1RES", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-RES"),
            ("EB101C1RES", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-RES"),
            ("EB101C1RES", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-RES"),
            ("EB101C1RES", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-RES"),
            ("EB101C1RES", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-RES"),
            ("EB101C1RES", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-RES"),
            ("EB101C1RES", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-RES"),
            ("EB101C1RES", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-RES"),
            ("EB101C1RES", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-RES"),
            ("EB101C1RES", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-RES"),
            ("EB101C1RES", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-RES"),
            ("EB101C1RES", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-RES"),
            ("EB101C1RES", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-RES"),
            ("EB101C1RES", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-RES"),
            ("EB101C1RES", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-RES"),
            ("EB101C1RES", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-RES"),
            ("EB101C1RES", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-RES"),
            ("EB101C1RES", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-RES"),
            ("EB101C1RES", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-RES"),
            ("EB101C1RES", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-RES"),
            ("EB101C1RES", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-RES"),
            ("EB101C1RES", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-RES"),
            ("EB101C1RES", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-RES"),
            ("EB101C1RES", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-RES"),
            ("EB105A1TSU", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-TSU"),
            ("EB105A1TSU", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-TSU"),
            ("EB105A1TSU", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-TSU"),
            ("EB105A1TSU", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-TSU"),
            ("EB105A1TSU", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-TSU"),
            ("EB105A1TSU", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-TSU"),
            ("EB105A1TSU", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-TSU"),
            ("EB105A1TSU", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-TSU"),
            ("EB105A1TSU", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-TSU"),
            ("EB105A1TSU", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-TSU"),
            ("EB105A1TSU", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-TSU"),
            ("EB105A1TSU", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-TSU"),
            ("EB105A1TSU", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-TSU"),
            ("EB105A1TSU", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-TSU"),
            ("EB105A1TSU", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-TSU"),
            ("EB105A1TSU", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-TSU"),
            ("EB105A1TSU", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-TSU"),
            ("EB105A1TSU", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-TSU"),
            ("EB105A1TSU", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-TSU"),
            ("EB105A1TSU", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-TSU"),
            ("EB105A1TSU", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-TSU"),
            ("EB105A1TSU", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-TSU"),
            ("EB105A1TSU", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-TSU"),
            ("EB105A1TSU", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-TSU"),
            ("EB105A1TSU", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-TSU"),
            ("EB105A1TSU", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-TSU"),
            ("EB105A1TSU", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-TSU"),
            ("EB105A1TSU", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-TSU"),
            ("EB105A1TSU", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-TSU"),
            ("EB105A1TSU", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-TSU"),
            ("EB105A1TSU", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-TSU"),
            ("EB105A1TSU", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-TSU"),
            ("EB105A1TSU", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-TSU"),
            ("EB106A2GIN", "UCO"): ("OS-HX-00-COD", "OS-HX-00-COD-00-GEA"),
            ("EB106A2GIN", "UVI"): ("OS-HX-00-VIL", "OS-HX-00-VIL-00-GEA"),
            ("EB106A2GIN", "UCH"): ("OS-BX-00-CHI", "OS-BX-00-CHI-00-GEA"),
            ("EB106A2GIN", "UCS"): ("OS-BX-00-CIS", "OS-BX-00-CIS-00-GEA"),
            ("EB106A2GIN", "UBG"): ("OS-BX-00-BRU", "OS-BX-00-BRU-00-GEA"),
            ("EB106A2GIN", "UGO"): ("OS-BX-00-GOR", "OS-BX-00-GOR-00-GEA"),
            ("EB106A2GIN", "UBS"): ("OS-BX-00-BRE", "OS-BX-00-BRE-00-GEA"),
            ("EB106A2GIN", "UVV"): ("OS-CX-00-VIV", "OS-CX-00-VIV-00-GEA"),
            ("EB106A2GIN", "UDU"): ("OS-CX-00-DUG", "OS-CX-00-DUG-00-GEA"),
            ("EB106A2GIN", "UCA"): ("OS-CX-00-CAM", "OS-CX-00-CAM-00-GEA"),
            ("EB106A2GIN", "UFV"): ("OS-CX-00-FVG", "OS-CX-00-FVG-00-GEA"),
            ("EB106A2GIN", "UTA"): ("OS-CX-00-TAA", "OS-CX-00-TAA-00-GEA"),
            ("EB106A2GIN", "UPM"): ("OS-AX-00-PSM", "OS-AX-00-PSM-00-GEA"),
            ("EB106A2GIN", "UNO"): ("OS-AX-00-NOV", "OS-AX-00-NOV-00-GEA"),
            ("EB106A2GIN", "UTO"): ("OS-AX-00-TOR", "OS-AX-00-TOR-00-GEA"),
            ("EB106A2GIN", "UGE"): ("OS-AX-00-GEN", "OS-AX-00-GEN-00-GEA"),
            ("EB106A2GIN", "UFI"): ("OS-DX-00-FIR", "OS-DX-00-FIR-00-GEA"),
            ("EB106A2GIN", "USU"): ("OS-DX-00-SUV", "OS-DX-00-SUV-00-GEA"),
            ("EB106A2GIN", "UPR"): ("OS-DX-00-PAR", "OS-DX-00-PAR-00-GEA"),
            ("EB106A2GIN", "UBO"): ("OS-DX-00-BOL", "OS-DX-00-BOL-00-GEA"),
            ("EB106A2GIN", "UPA"): ("OS-GX-00-PAL", "OS-GX-00-PAL-00-GEA"),
            ("EB106A2GIN", "UMD"): ("OS-FX-00-MAD", "OS-FX-00-MAD-00-GEA"),
            ("EB106A2GIN", "UCT"): ("OS-GX-00-CAT", "OS-GX-00-CAT-00-GEA"),
            ("EB106A2GIN", "UMB"): ("OS-EX-00-UMB", "OS-EX-00-UMB-00-GEA"),
            ("EB106A2GIN", "UMA"): ("OS-EX-00-MAR", "OS-EX-00-MAR-00-GEA"),
            ("EB106A2GIN", "UAM"): ("OS-EX-00-ABR", "OS-EX-00-ABR-00-GEA"),
            ("EB106A2GIN", "ULN"): ("OS-EX-00-LAN", "OS-EX-00-LAN-00-GEA"),
            ("EB106A2GIN", "ULS"): ("OS-EX-00-LAS", "OS-EX-00-LAS-00-GEA"),
            ("EB106A2GIN", "UFM"): ("OS-FX-00-FRA", "OS-FX-00-FRA-00-GEA"),
            ("EB106A2GIN", "UFE"): ("OS-FX-00-FER", "OS-FX-00-FER-00-GEA"),
            ("EB106A2GIN", "URO"): ("OS-FX-00-ROT", "OS-FX-00-ROT-00-GEA"),
            ("EB106A2GIN", "UBR"): ("OS-FX-00-BRI", "OS-FX-00-BRI-00-GEA"),
            ("EB106A2GIN", "UBA"): ("OS-FX-00-BAR", "OS-FX-00-BAR-00-GEA"),
        }
        return mapping.get((coin, udb), (None, None))
        

if mbo.getBoolean("HISTORYFLAG"):
    mbo.setValue("JPNUM", mbo.getString("JPNUM"), 11L)
if (mbo.isModified("TARGSTARTDATE") or mbo.isModified("TARGCOMPDATE")) and not mbo.isNull("X4DINIZIOINDISP") and not mbo.isNull("X4DFINEINDISP") and (mbo.getString("X4STATUSINDISP")=="DELIBERATO" or mbo.getString("X4STATUSINDISP")=="IN ESECUZIONE"):
    X4DINIZIOINDISP = mbo.getDate("X4DINIZIOINDISP")
    X4DFINEINDISP = mbo.getDate("X4DFINEINDISP")
    if (X4DINIZIOINDISP and X4DFINEINDISP) and X4DINIZIOINDISP >= mbo.getDate("TARGSTARTDATE") and X4DFINEINDISP <= mbo.getDate("TARGCOMPDATE"):
        mbo.setValue("X4ESITOINDISP","OK")
        mbo.setValueNull("X4IDERRORINDISP")
        mbo.setValueNull("X4DESCERRORINDISP")
    else:
        mbo.setValue("X4ESITOINDISP","KO")
        
if mbo.isNull("X4TIPOODL") and mbo.getBoolean("X4TECMIG")==False:
    mbo.setValue("X4TIPOODL","MANUALE")

if mbo.getBoolean("X4OUTSOURCING")==True and mbo.isNull("X4NATURALAVORO") and not mbo.isNull("X4POLICY"):
    mbo.setValue("X4NATURALAVORO","ASSISTENZA")
'''
if (mbo.isNull("ASSETNUM") or (mbo.getBoolean("X4OUTSOURCING")==True and mbo.isNull("X4NATURALAVORO"))) and launchPoint!="JPNUMVALIDATE" and mbo.getBoolean("X4SERBATOIO")==False and mbo.getBoolean("X4TECMIG")==False:
    service.error("x3terna","ErrorMsg", ["Si prega di inserire i campi obbligatori."] )
   
if mbo.isNull("X4BURKS") and (mbo.isNull("X4TIPOODL") or mbo.getString("X4TIPOODL")=="MANUALE") and launchPoint!="JPNUMVALIDATE" and mbo.getBoolean("X4TECMIG")==False:
    service.error("x3terna","ErrorMsg", ["Si prega di inserire i campi obbligatori."] )
   
if onupdate and  (mbo.getMboValue("SNECONSTRAINT").getInitialValue().asDate()!=mbo.getDate("SNECONSTRAINT") or mbo.getMboValue("FNLCONSTRAINT").getInitialValue().asDate()!=mbo.getDate("FNLCONSTRAINT")) and mbo.getBoolean("X4AGGREGATI")==True and launchPoint!="JPNUMVALIDATE" and not interactive and app!="X4WORKORDER":
    service.error("x3terna","ErrorMsg", ["Impossibile procedere con la modifica."] )
'''  
#Set Perimetro intervento
if not mbo.isNull("JPNUM") and mbo.isModified("JPNUM") and mbo.isNull("X4PERIMETROINT"):
    jobPlanMboSet=mbo.getMboSet("JOBPLAN")
    if not jobPlanMboSet.isEmpty():
        jobPlanMbo=jobPlanMboSet.getMbo(0)
        classstructureMboSet=jobPlanMbo.getMboSet("CLASSSTRUCTURE")
        if not classstructureMboSet.isEmpty():
            classstructureMbo=classstructureMboSet.getMbo(0)
            classificationid=classstructureMbo.getString("CLASSIFICATIONID")
            tipoOdL=evaluateClass(classificationid)
            mbo.setValue("X4PERIMETROINT",tipoOdL)
        classstructureMboSet.close()
    jobPlanMboSet.close()

if mbo.isModified("X4TIPOSPESA") or mbo.isModified("X4COIN"):
    uiSet=mbo.getMboSet("OWNERGROUP")
    if not uiSet.isEmpty():
        ui=uiSet.getMbo(0).getString("X3PARENTPERSONGROUP")
        wbspian, wbepian= evalwbe(mbo.getString("X4TIPOSPESA"),ui,mbo.getString("X4COIN"))
        mbo.setValue("X4WBSPIAN",wbspian,11L)
        mbo.setValue("X4WBEPIAN",wbepian,11L)
    uiSet.close()

#Set Macroattivita in OdL
if not mbo.isNull("JPNUM") and mbo.isModified("JPNUM"):
    jobPlanMboSet=mbo.getMboSet("JOBPLAN")
    if not jobPlanMboSet.isEmpty():
        jobPlanMbo=jobPlanMboSet.getMbo(0)
        macroAtt=jobPlanMbo.getString("X4MACROATTIVITA")
        mbo.setValue("WOJP1",macroAtt,11L)
    jobPlanMboSet.close()


# Popola l'attributo WOEQ9 con STRNO della sede tecnica/equipment corrispondente.
# La relazione X4SAPIDALL3 e' strutturata per arrivare alla X3SAPASSET tramite mapping (X3MAXIMOSAPMAP)
# e binding (X3SAPTOMAXOBJECT) del connettore.
if mbo.isNull("woeq9") and launchPoint != "JPNUMVALIDATE":
    # recupero l'asset del work order
    assetMbo = mbo.getMboSet("ASSET").getMbo(0)
    if assetMbo != None:
        # recupero l'asset SAP tramite dati del connettore EAM/SAP corrispondente all'asset
        sapAssetMbo = assetMbo.getMboSet("X4SAPIDALL3").getMbo(0)
        if sapAssetMbo != None:
            if sapAssetMbo.getString("sapobject") == "E":
                # nel caso di equipment occorre sostituire il quinto segmento con il reale EQNR (presente in SAPUID se disponibile)
                if not sapAssetMbo.isNull("sapuid"):
                    strNo = sapAssetMbo.getString("strno")
                    result = "-".join(strNo.split("-")[:-1])
                    result = result + "-" + sapAssetMbo.getString("sapuid")
                    mbo.setValue("woeq9", result, 11L)
            else:
                # nel caso di sede tecnica si utilizza STRNO
                mbo.setValue("woeq9", sapAssetMbo.getString("strno"), 11L)
        
        if mbo.isNull("woeq9"):
            # se non e' stato possibile recuperare dalla SAPASSET il STRNO utilizzo quello del padre se disponibile
            parentSet = assetMbo.getMboSet("PARENT")
            if not parentSet.isEmpty():
                parentMbo = parentSet.getMbo(0)
                parentSapid = parentMbo.getString("x3sapid")
                if parentSapid:
                    mbo.setValue("woeq9", parentSapid, 11L)

#Set SAPID
if mbo.isNull("X4SAPIDASSET") and launchPoint!="JPNUMVALIDATE":
    #geoaddSet=mbo.getMboSet("$SAPIDASSET$","x3sapasset","x3sapassetid IN (SELECT sapassetid FROM X3SAPTOMAXOBJECT WHERE objectnum=:ASSETNUM) and x3sapasset.sapclass in (select sapclass from X3MAXIMOSAPMAP where HIerarchypath in (  SELECT LISTAGG(c.ANCESTORCLASSID,'\\') WITHIN GROUP (ORDER BY HIERARCHYLEVELS DESC) HIERARCHYPATH FROM CLASSANCESTOR c WHERE C.CLASSSTRUCTUREID in ( select classstructureid FROM ASSET where assetnum = :ASSETNUM) GROUP BY c.CLASSSTRUCTUREID)and step1 = 1   )")    #service.error("TEST",str(geoaddSet.count()))
    geoaddSet=mbo.getMboSet("$SAPIDASSET$","x3sapasset","x3sapassetid IN (SELECT sapassetid FROM X3SAPTOMAXOBJECT WHERE objectnum=:ASSETNUM) AND NOT EXISTS (SELECT 1 FROM classstructure WHERE classificationid='STALLO' AND classstructureid IN (SELECT classstructureid FROM asset WHERE assetnum=:ASSETNUM))")
    if not geoaddSet.isEmpty():
        geoadd=geoaddSet.getMbo(0)
        sapid=geoadd.getString("STRNO")
        mbo.setValue("X4SAPIDASSET",sapid)
    else:
        geoadd2Set=mbo.getMboSet("$SAPID2ASSET$","ASSET","assetnum=:assetnum and siteid=:siteid")
        if not geoadd2Set.isEmpty():
            sapid=geoadd2Set.getMbo(0).getString("X3SAPID")
            if sapid and len(sapid)!=10:
                mbo.setValue("X4SAPIDASSET",sapid)
            else:
                assetSetSapid=geoadd2Set.getMbo(0)
                assetSetSapidPadreSet=assetSetSapid.getMboSet("PARENT")
                if not assetSetSapidPadreSet.isEmpty():
                    assetSetSapidPadre=assetSetSapidPadreSet.getMbo(0)
                    mbo.setValue("X4SAPIDASSET",assetSetSapidPadre.getString("X3SAPID"))
        geoadd2Set.close()
    geoaddSet.close()
            
if (mbo.isModified("TARGCOMPDATE") or mbo.isModified("TARGSTARTDATE")) and onupdate:
    mbo.setValue("X4DATEMOD","1",11L)
    
#Set responsabile asset (X4RESPASSET)
if (onadd or onupdate) and not mbo.isNull("ASSETNUM") and mbo.isNull("X4RESPASSET"):
    userId = mbo.getUserInfo().getUserName()
    assetnum = mbo.getString("ASSETNUM")

    multicompSet = MXServer.getMXServer().getMboSet("PERSONGROUP", mbo.getUserInfo())

    # Verifica multicompetenza della linea
    multicompWhere = (
        "PERSONGROUP IN ("
            "SELECT a.x3ingrp FROM asset a WHERE EXISTS ("
                "SELECT 1 FROM assetlocrelation r "
                "WHERE r.targetassetnum = a.assetnum "
                "AND r.siteid = a.siteid "
                "AND r.sourceassetnum = '" + assetnum + "'))"
        " AND EXISTS ("
            "SELECT 1 FROM assetlocrelation r1 "
            "WHERE r1.sourceassetnum = '" + assetnum + "'"
            " AND EXISTS ("
                "SELECT 1 FROM asset a1 "
                "WHERE a1.assetnum = r1.targetassetnum AND a1.siteid = r1.siteid"
                " AND EXISTS ("
                    "SELECT 1 FROM assetlocrelation r2 "
                    "WHERE r2.sourceassetnum = r1.sourceassetnum"
                    " AND EXISTS ("
                        "SELECT 1 FROM asset a2 "
                        "WHERE a2.assetnum = r2.targetassetnum AND a2.siteid = r2.siteid"
                        " AND a2.x3ingrp <> a1.x3ingrp))))"
    )

 
    multicompSet.setWhere(multicompWhere)
    multicompSet.reset()
    isMulticompetente = multicompSet.count() > 1
    multicompSet.close()

    if isMulticompetente:
        # Caso 1 e 2: linea multicompetente, dipende dal tipo gruppo utente
        uiGroupSet = MXServer.getMXServer().getMboSet("PERSONGROUP", mbo.getUserInfo())
        uiGroupSet.setWhere("X4TIPUDB = 'UI' AND PERSONGROUP IN (SELECT PERSONGROUP FROM PERSONGROUPTEAM WHERE RESPPARTY = '" + userId + "')")
        uiGroupSet.reset()
        if not uiGroupSet.isEmpty():
            # Caso 2: utente UI → X3PARENTPERSONGROUP
            mbo.setValue("X4RESPASSET", uiGroupSet.getMbo(0).getString("X3PARENTPERSONGROUP"), 11L)
        else:
            # Caso 1: utente non UI → X3INGRP
            respAssetSet = mbo.getMboSet("ASSET")
            if not respAssetSet.isEmpty():
                mbo.setValue("X4RESPASSET", respAssetSet.getMbo(0).getString("X3INGRP"), 11L)
            respAssetSet.close()
        uiGroupSet.close()
    else:
        '''
        # Caso 3: linea non multicompetente → sempre X3INGRP
        respAssetSet = mbo.getMboSet("ASSET")
        if not respAssetSet.isEmpty():
            mbo.setValue("X4RESPASSET", respAssetSet.getMbo(0).getString("X3INGRP"), 11L)
        respAssetSet.close()'''
        
        # Caso 3: linea/stazione non multicompetente
    
        assetMbo = mbo.getMboSet("ASSET").getMbo(0)
    
        if assetMbo:
            ancestor = assetMbo.getString("ANCESTOR")
            siteid = assetMbo.getString("SITEID")
    
            respAssetSet = MXServer.getMXServer().getMboSet("ASSET", mbo.getUserInfo())
    
            if ancestor:
                # Prende l'asset indicato da ANCESTOR
                respAssetSet.setWhere(
                    "assetnum = '" + ancestor + "' "
                    "and siteid = '" + siteid + "'"
                )
            else:
                # Prende l'asset stesso
                respAssetSet.setWhere(
                    "assetnum = '" + assetnum + "' "
                    "and siteid = '" + siteid + "'"
                )
    
            respAssetSet.reset()
    
            if not respAssetSet.isEmpty():
                mbo.setValue(
                    "X4RESPASSET",
                    respAssetSet.getMbo(0).getString("X3INGRP"),
                    11L
                )
    
            respAssetSet.close()


        
#Popola campo OWNERGROUP
'''
if (mbo.isNull("OWNERGROUP") or mbo.isModified("OWNERGROUP"))and launchPoint!="JPNUMVALIDATE":
    jpSet=mbo.getMboSet("JOBPLAN")
    if not jpSet.isEmpty():
        jpMbo=jpSet.getMbo(0)
        tipoUDB=jpMbo.getString("X4TIPUDB")
        if tipoUDB=="UI":
            #service.error("TEST","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum ='"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
            personSet=mbo.getMboSet("$UDBUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum = '"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
        else:
            personSet=mbo.getMboSet("$PERSONGROUPNONUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum =:JPNUM AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))) OR X3PARENTPERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))))))")
        if mbo.isModified("OWNERGROUP"):
            personSet=mbo.getMboSet("$UDBUI$","PERSONGROUP","PERSONGROUP='"+str(mbo.getString("OWNERGROUP"))+"'")
        if not personSet.isEmpty():
            persongroup=personSet.getMbo(0).getString("PERSONGROUP")
            ui=personSet.getMbo(0).getString("X3PARENTPERSONGROUP")
            if mbo.isNull("OWNERGROUP"):
                mbo.setValue("OWNERGROUP",persongroup,11L)
            wbspian, wbepian= evalwbe(mbo.getString("X4TIPOSPESA"),ui,mbo.getString("X4COIN"))
            mbo.setValue("X4WBSPIAN",wbspian,11L)
            mbo.setValue("X4WBEPIAN",wbepian,11L)
            mbo.getThisMboSet().save()
'''
if mbo.isNull("OWNERGROUP") and launchPoint!="JPNUMVALIDATE":
    jpSet=mbo.getMboSet("JOBPLAN")
    if not jpSet.isEmpty():
        jpMbo=jpSet.getMbo(0)
        tipoUDB=jpMbo.getString("X4TIPUDB")
        '''
        if tipoUDB=="UI":
            #service.error("TEST","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum ='"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
            personSet=mbo.getMboSet("$UDBUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum = '"+str(mbo.getString("JPNUM"))+"' AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3INGRP FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' )))))")
        else:
            personSet=mbo.getMboSet("$PERSONGROUPNONUI$","PERSONGROUP","PERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X4TIPUDB=(SELECT X4TIPUDB FROM JOBPLAN WHERE jpnum =:JPNUM AND STATUS = 'ACTIVE') AND (X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))) OR X3PARENTPERSONGROUP in(SELECT PERSONGROUP FROM PERSONGROUP WHERE X4UDB=1 AND X3PARENTPERSONGROUP =(SELECT X3SWERK FROM asset WHERE assetnum in (select ancestor from assetancestor where assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' and hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum='"+str(mbo.getString("ASSETNUM"))+"' and siteid='"+str(mbo.getString("SITEID"))+"' ))))))")
        '''
        
        personSet = mbo.getMboSet(
            "$PERSONGROUPRESP$",
            "PERSONGROUP",
            "X3PARENTPERSONGROUP = '" + mbo.getString("X4RESPASSET") + "'"
        )        
        if not personSet.isEmpty():
            persongroup=personSet.getMbo(0).getString("PERSONGROUP")
            ui=personSet.getMbo(0).getString("X3PARENTPERSONGROUP")
            mbo.setValue("OWNERGROUP",persongroup,11L)
            wbspian, wbepian= evalwbe(mbo.getString("X4TIPOSPESA"),ui,mbo.getString("X4COIN"))
            mbo.setValue("X4WBSPIAN",wbspian,11L)
            mbo.setValue("X4WBEPIAN",wbepian,11L)
            #mbo.getThisMboSet().save()
        personSet.close()
    jpSet.close()
#if not persongroup:
    #persongroup=mbo.getString("OWNERGROUP")
if persongroup:
    wpLaborSet=mbo.getMboSet("WPLABOR")
    if not wpLaborSet.isEmpty():
        for m in range(0,wpLaborSet.count()):
            wpLabor=wpLaborSet.getMbo(m)
            if wpLabor.isNull("X4TITRIS"):
                wpLabor.setValue("X4TITRIS",persongroup)
            if wpLabor.isNull("X4DATAINIZIORISORSA"):
                wpLabor.setValue("X4DATAINIZIORISORSA",mbo.getDate("TARGSTARTDATE"))
            if wpLabor.isNull("X4DATAFINERISORSA"):
                wpLabor.setValue("X4DATAFINERISORSA",mbo.getDate("TARGCOMPDATE"))
        #wpLaborSet.save()
    wpLaborSet.close()

if not persongroup and not mbo.isNull("OWNERGROUP") and (mbo.getString("STATUS")=="APERTO" or mbo.getString("STATUS")=="DA VALIDARE") and onadd:
    wpLaborSet=mbo.getMboSet("WPLABOR")
    if not wpLaborSet.isEmpty():
        for m in range(0,wpLaborSet.count()):
            wpLabor=wpLaborSet.getMbo(m)
            if wpLabor.isNull("X4TITRIS"):
                wpLabor.setValue("X4TITRIS",mbo.getString("OWNERGROUP"))
            if wpLabor.isNull("X4DATAINIZIORISORSA"):
                wpLabor.setValue("X4DATAINIZIORISORSA",mbo.getDate("TARGSTARTDATE"))
            if wpLabor.isNull("X4DATAFINERISORSA"):
                wpLabor.setValue("X4DATAFINERISORSA",mbo.getDate("TARGCOMPDATE"))
    wpLaborSet.close()
    
#Set tipo attività e tipo ordine
if (not mbo.isNull("X4COIN") and mbo.isModified("X4COIN")) or onadd:
    workoderNewSet=MXServer.getMXServer().getMboSet("WORKORDER",MXServer.getMXServer().getSystemUserInfo())
    workoderNewSet.setWhere("wonum='"+mbo.getString("WONUM")+"' and siteid='"+mbo.getString("SITEID")+"'")
    coinMboSet=mbo.getMboSet("$COIN$","X4COIN",":X4COIN=IDCOIN")
    if not coinMboSet.isEmpty() and not workoderNewSet.isEmpty():
        coinMbo=coinMboSet.getMbo(0)
        tipoAttivita=coinMbo.getString("TIPOATTIVITA")
        if mbo.getString("X4TIPOSPESA")!="ANR":
            tipoOrdine=coinMbo.getString("TIPOORDINE")            mbo.setValue("X4TIPOORDINE",tipoOrdine)
        if mbo.isModified("X4COIN") and mbo.getString("X4TIPOSPESA")=="ANR" and not mbo.isNull("X4TIPOORDINE"):
            mbo.setValueNull("X4TIPOORDINE")
        mbo.setValue("X4TIPOATTIVITA",tipoAttivita)
        #mbo.setValue("X4WBEPIAN","OS-DX-00-BOL-TT-OM",11L)
        #service.error("TEST",str(tipoAttivita))
        #mbo.getThisMboSet().save()
    workoderNewSet.close()
    coinMboSet.close()
    childSet=mbo.getMboSet("CHILDNOTASK")
    #coinValue = mbo.getMboValue("X4COIN")
    #coincurrValue = coinValue.getCurrentValue().asString()
    coincurrValue=mbo.getString("X4COIN")
    spesacurrValue=mbo.getString("X4TIPOSPESA")
    if not childSet.isEmpty():
        for figlio in range(0,childSet.count()):
            child=childSet.getMbo(figlio)
            child.setValue("X4COIN",coincurrValue,11L)
            child.setValue("X4TIPOSPESA",spesacurrValue,11L)

        #childSet.save()
    childSet.close()
    #mbo.getThisMboSet().save()
    

'''
if mbo.isModified("X4TIPOATTIVITA") and (mbo.getString("X4TIPOATTIVITA")!="EPC" and mbo.getString("X4TIPOATTIVITA")!="GEA"):
    mbo.setValueNull("X4NORDINEINTERNO")
elif mbo.isNull("X4COIN") and mbo.isModified("X4COIN"):
    mbo.setValueNull("X4TIPOATTIVITA")
    mbo.setValueNull("X4TIPOORDINE")
'''
#Set date per ris est
risEst=mbo.getMboSet("WPSERVICE")
if not risEst.isEmpty() and mbo.getString("STATUS")=="APERTO":
    for risorsa in range(0,risEst.count()):
        risoEstMbo=risEst.getMbo(risorsa)
        if risoEstMbo.isNull("X4DATAINIZIORISORSA"):
            risoEstMbo.setValue("X4DATAINIZIORISORSA",mbo.getDate("TARGSTARTDATE"))
        if risoEstMbo.isNull("X4DATAFINERISORSA"):
            risoEstMbo.setValue("X4DATAFINERISORSA",mbo.getDate("TARGCOMPDATE"))
        '''jobplanSet = risoEstMbo.getMboSet("JOBSERVICE")
        if not jobplanSet.isEmpty():
            jobplanSetMbo = jobplanSet.getMbo(0)
            x4kpi = jobplanSetMbo.getDouble("X4KPI")
            qty = jobplanSetMbo.getDouble("ITEMQTY")
            #service.error("TEST","KPI:"+str(x4kpi)+", fisico:"+str(workorderMbo.getDouble("X4FISICOPLAN")))
            if x4kpi and onadd:
                linecost = x4kpi*qty #* (il numero fisico dell'odl)
                risoEstMbo.setValue("LINECOST", linecost,MboConstants.NOACCESSCHECK)
    #risEst.save()'''
risEst.close()


if onupdate and mbo.getBoolean("X4AGGREGATI")==True:
    childSet = mbo.getMboSet("CHILDNOTASK")

    mbo.setValue(
        "X4EFFORTI",
        childSet.sum("X4EFFORTI"),
        11L
    )
    mbo.setValue(
        "X4EFFORTQ",
        childSet.sum("X4EFFORTQ"),
        11L
    )
    mbo.setValue(
        "X4EFFORTO",
        childSet.sum("X4EFFORTO"),
        11L
    )
    childSet.close()

#Set tipo ordine OdM
if not mbo.isNull("X4ODM") and mbo.isModified("X4ODM") and launchPoint!="JPNUMVALIDATE":
    odmMboSet=mbo.getMboSet("$ODM$","X4ODM",":X4ODM=IDODM")
    if not odmMboSet.isEmpty():
        odmMbo=odmMboSet.getMbo(0)
        tipoOrdine=odmMbo.getString("TIPOORDINE")
        mbo.setValue("X4TIPOORDINE",tipoOrdine)
        #mbo.getThisMboSet().save()
    else:
        if not mbo.isNull("X4TIPOOI"):
            if mbo.getString("X4TIPOOI")=="T06":
                mbo.setValue("X4TIPOORDINE","PMT9")
            elif mbo.getString("X4TIPOOI")=="T09":
                mbo.setValue("X4TIPOORDINE","PMTG")
    odmMboSet.close()
#Setto tipo OI per EPC
if not mbo.isNull("X4NORDINEINTERNO") and mbo.isModified("X4NORDINEINTERNO") and launchPoint!="JPNUMVALIDATE":
    ordIntMboSet=mbo.getMboSet("$OI$","X4ORDINT","IDOI=:X4NORDINEINTERNO")
    if not ordIntMboSet.isEmpty():
        ordIntMbo=ordIntMboSet.getMbo(0)
        tipoOI=ordIntMbo.getString("TIPOOI")
        mbo.setValue("X4TIPOOI",tipoOI)
    ordIntMboSet.close()
if mbo.isNull("X4NORDINEINTERNO") and mbo.isModified("X4NORDINEINTERNO") and launchPoint!="JPNUMVALIDATE":
    mbo.setValueNull("X4TIPOOI")

'''
#Set tipo OI per non EPC
if mbo.getString("X4TIPOATTIVITA")!="EPC" and mbo.getString("X4TIPOSPESA")=="ANR" and not mbo.isNull("X4NORDINEINTERNO") and mbo.isModified("X4NORDINEINTERNO") and launchPoint!="JPNUMVALIDATE":
    ordIntMboSet=mbo.getMboSet("$OI$","X4ORDINT","IDOI=:X4NORDINEINTERNO")
    if not ordIntMboSet.isEmpty():
        ordIntMbo=ordIntMboSet.getMbo(0)
        tipoOI=ordIntMbo.getString("TIPOOI")
        mbo.setValue("X4TIPOOI",tipoOI)
'''
#Set Tipo OI
if not mbo.isNull("X4TIPOORDINE"):
    if mbo.getString("X4TIPOORDINE")=="PMT9" and mbo.getString("X4TIPOOI")!="T06":
        mbo.setValue("X4TIPOOI","T06",11L)
    elif mbo.getString("X4TIPOORDINE")=="PMTG" and mbo.getString("X4TIPOOI")!="T09":
        mbo.setValue("X4TIPOOI","T09",11L)

if mbo.isModified("X4NORDINEINTERNO") and mbo.isNull("X4NORDINEINTERNO") and not mbo.isNull("X4TIPOOI"):
    mbo.setValueNull("X4TIPOOI")
   
#Set WBE e WBS per NtK
if not mbo.isNull("X4NTK") and mbo.isModified("X4NTK") and mbo.getString("X4TIPOSPESA")=="CAPEX" and launchPoint!="JPNUMVALIDATE" and mbo.isNull("PARENT"):
    ntkMboSet=mbo.getMboSet("$NTK$","X4OPNTK",":X4NTK=IDNTK AND IDOPERAZIONE IN (SELECT min(IDOPERAZIONE) FROM x4opntk WHERE idntk=:X4NTK)")
    if not ntkMboSet.isEmpty():
        ntkMbo=ntkMboSet.getMbo(0)
        wbe=ntkMbo.getString("WBE")
        wbsMboSet=ntkMbo.getMboSet("$WBS$","X4WBE",":WBE=IDWBE")
        if not wbsMboSet.isEmpty():
            wbsMbo=wbsMboSet.getMbo(0)
            wbs=wbsMbo.getString("IDWBS")
            mbo.setValue("X4WBS",wbs,11L)
        wbsMboSet.close()
        mbo.setValue("X4WBE",wbe,11L)
        if mbo.isNull("X4NOPERAZIONE"):
            opntkMboSet=mbo.getMboSet("$X4OPNTK$","X4OPNTK",":X4NTK=IDNTK AND IDOPERAZIONE IN (SELECT min(IDOPERAZIONE) FROM x4opntk WHERE idntk=:X4NTK)")
            if not opntkMboSet.isEmpty():
                minIdOperazione = None
                for x in range(0,opntkMboSet.count()):
                    nopMbo = opntkMboSet.getMbo(x)
                    idoperazione = nopMbo.getString("IDOPERAZIONE")
                    #if (idoperazione < minIdOperazione) or not minIdOperazione:
                    minIdOperazione = idoperazione
                mbo.setValue("X4NOPERAZIONE", str(minIdOperazione))
            opntkMboSet.close()
    ntkMboSet.close()

#Set WBE e WBS per OdM
if not mbo.isNull("X4ODM") and mbo.isModified("X4ODM") and mbo.getString("X4TIPOSPESA")=="ANR" :
    odmMboSet=mbo.getMboSet("$ODMANR$","X4ODM",":X4ODM=IDODM")
    if not odmMboSet.isEmpty():
        odmANRMbo=odmMboSet.getMbo(0)
        wbeAnr=odmANRMbo.getString("WBE")
        wbsAnrMboSet=odmANRMbo.getMboSet("$WBS$","X4WBE",":WBE=IDWBE")
        if not wbsAnrMboSet.isEmpty():
            wbsAnrMbo=wbsAnrMboSet.getMbo(0)
            wbsAnr=wbsAnrMbo.getString("IDWBS")
            mbo.setValue("X4WBS",wbsAnr,11L)
        wbsAnrMboSet.close()
        mbo.setValue("X4WBE",wbeAnr,11L)
        if odmANRMbo.isNull("WBE"):
            mbo.setValueNull("X4WBS")
        oiAnr=odmANRMbo.getString("AUFNR")
        mbo.setValue("X4NORDINEINTERNO",oiAnr,11L)
        ordIntMboSet=mbo.getMboSet("$OI$","X4ORDINT","IDOI=:X4NORDINEINTERNO")
        if not ordIntMboSet.isEmpty():
            ordIntMbo=ordIntMboSet.getMbo(0)
            tipoOI=ordIntMbo.getString("TIPOOI")
            mbo.setValue("X4TIPOOI",tipoOI)
        ordIntMboSet.close()
    odmMboSet.close()


if mbo.isModified("X4WBE") or mbo.isModified("X4WBS") or mbo.isModified("X4NORDINEINTERNO") or mbo.isModified("X4ODM") or mbo.isModified("X4NTK") or mbo.isModified("X4NOPERAZIONE"):
    childSet=mbo.getMboSet("CHILDNOTASK")
    if not childSet.isEmpty():
        for figlio in range(0,childSet.count()):
            child=childSet.getMbo(figlio)
            child.setValue("X4NORDINEINTERNO",mbo.getString("X4NORDINEINTERNO"),11L)
            child.setValue("X4TIPOOI",mbo.getString("X4TIPOOI"),11L)
            if child.getString("OWNERGROUP")==mbo.getString("OWNERGROUP"):
                child.setValue("X4WBE",mbo.getString("X4WBE"),11L)
                child.setValue("X4WBS",mbo.getString("X4WBS"),11L)
                child.setValue("X4ODM",mbo.getString("X4ODM"),11L)
                child.setValue("X4NTK",mbo.getString("X4NTK"),11L)
                child.setValue("X4NOPERAZIONE",mbo.getString("X4NOPERAZIONE"),11L)
                child.setValue("X4TIPOORDINE",mbo.getString("X4TIPOORDINE"),11L)
          
#Set Description WO per OdL
if not mbo.isNull("JPNUM") and mbo.isModified("JPNUM") and  launchPoint=="JPNUMVALIDATE":
    jobPlanMboSet = mbo.getMboSet("JOBPLAN")
    if not jobPlanMboSet.isEmpty():
        jobPlanMbo = jobPlanMboSet.getMbo(0)
        jobPlanDesc = jobPlanMbo.getString("DESCRIPTION")
        woDesc = mbo.getString("DESCRIPTION")
        if woDesc != jobPlanDesc:
            mbo.setValue("DESCRIPTION", jobPlanDesc,  MboConstants.NOACCESSCHECK)
    jobPlanMboSet.close()

# Set COIN prevalente in WO
if (((onadd and mbo.isNull("X4COIN") and mbo.isNull("X4TIPOSPESA")) ) or (onupdate and mbo.isModified("JPNUM")) and mbo.getMboValue("JPNUM").getPreviousValue().asString()=="") and launchPoint=="JPNUMVALIDATE":
    jpnum = mbo.getString("JPNUM")
    if jpnum:
        # Recupera il Job Plan associato
        jobPlanMboSet=mbo.getMboSet("JOBPLAN")
        if not jobPlanMboSet.isEmpty():
            
            jobPlanMbo = jobPlanMboSet.getMbo(0)
            x4Attall = jobPlanMbo.getString("X4ATTALL")
            
            catmosMboSet = MXServer.getMXServer().getMboSet("X4CATMOS", mbo.getUserInfo()) 
            catmosMboSet.setWhere("IDATTSTANDARD = '%s'" % x4Attall)
           
            #catmosMboSet = jobPlanMbo.getMboSet("$CTMS$","X4CATMOS","IDATTSTANDARD =:x4Attall")
            if not catmosMboSet.isEmpty():
                catmosMbo = catmosMboSet.getMbo(0)
                
                spesaPrev = catmosMbo.getString("SPESAPREVALENTE")
                coinPrev = catmosMbo.getString("COINPREVALENTE")
                
                mbo.setValue("X4TIPOSPESA", spesaPrev, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
                mbo.setValue("X4COIN", coinPrev, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
                coinMboSet=mbo.getMboSet("$COIN$","X4COIN",":X4COIN=IDCOIN")
                if not coinMboSet.isEmpty():
                    coinMbo=coinMboSet.getMbo(0)
                    tipoAttivita=coinMbo.getString("TIPOATTIVITA")
                    if mbo.getString("X4TIPOSPESA")!="ANR":
                        tipoOrdine=coinMbo.getString("TIPOORDINE")
                        mbo.setValue("X4TIPOORDINE",tipoOrdine)
                    mbo.setValue("X4TIPOATTIVITA",tipoAttivita)
                    #mbo.setValue("X4WBEPIAN","OS-DX-00-BOL-TT-OM",11L)
                    #service.error("TEST",str(tipoAttivita))
                coinMboSet.close()
                childSet=mbo.getMboSet("CHILDNOTASK")
                #coinValue = mbo.getMboValue("X4COIN")
                #coincurrValue = coinValue.getCurrentValue().asString()
                coincurrValue=mbo.getString("X4COIN")
                spesacurrValue=mbo.getString("X4TIPOSPESA")
                childSet.close()
            catmosMboSet.close()
        jobPlanMboSet.close()
    #mbo.getThisMboSet().save()
#mbo.getThisMboSet().save()
#Set classificazione dell'asset nell'ordine
if (onadd and not mbo.isNull("ASSETNUM")) or (onupdate and not mbo.isNull("ASSETNUM") and (mbo.isModified("ASSETNUM") or mbo.isNull("X4CLASSASSET"))):
    assetSet=mbo.getMboSet("ASSET")
    assetMbo=assetSet.getMbo(0)
    classificationID=assetMbo.getMboSet("CLASSSTRUCTURE").getMbo(0).getString("X4ELEMENTO")
    mbo.setValue("X4CLASSASSET",classificationID,11L)
    assetSet.close()

if onadd and mbo.isNull("SNECONSTRAINT") and mbo.getBoolean("X4TECMIG")==False and interactive:
    if not mbo.isNull("REPORTDATE"):
        report_date = mbo.getDate("REPORTDATE")  # Ottieni la data originale
        from java.util import Calendar
        cal = Calendar.getInstance()
        cal.setTime(report_date)  # Imposta la data nel calendario
        cal.set(Calendar.SECOND, 0)  # Imposta i secondi a 00
        cal.set(Calendar.MILLISECOND, 0)  # Facoltativo: imposta i millisecondi a 00
        mbo.setValue("SNECONSTRAINT", cal.getTime())

if onupdate and mbo.isNull("PLUSCJPREVNUM") and mbo.isModified("PLUSCJPREVNUM"):
    jobPlanMboSet=mbo.getMboSet("$JOBPLANCR$","JOBPLAN","JPNUM=:JPNUM and status='ACTIVE'")
    mbo.setValue("JPNUM","",11L)
    if not jobPlanMboSet.isEmpty():
        jobPlanMbo=jobPlanMboSet.getMbo(0)
        macroAtt=jobPlanMbo.getString("X4MACROATTIVITA")
        mbo.setValue("WOJP1",macroAtt,11L)
    jobPlanMboSet.close()
    wpSetAggr=mbo.getMboSet("WPLABOR")
    if not wpSetAggr.isEmpty():
        wpSetAggr.deleteAll()
        #wpSetAggr.save()
    wpSetAggr.close()
    #mbo.getThisMboSet().save()
    #mbo.setValue("X4TIPOSPESA","",11L)
    #mbo.setValue("X4COIN","",11L)
if onupdate and not mbo.getMboSet("X4WOINDISPINLAV").isEmpty() and (mbo.isModified("TARGSTARTDATE") or mbo.isModified("TARGCOMPDATE") or mbo.isModified("OWNERGROUP") or mbo.isModified("JPNUM") or mbo.isModified("X4TIPOSPESA")):
    warngroup = "X4INDISP"
    warnkey = "X4INDISP"
if launchPoint=="X4WORKORDER_ADD" and mbo.isNull("X4STATUSINDISP") and mbo.getMboSet("X4INDISP").isEmpty() and not mbo.getMboSet("$INDISPJP$","JOBPLAN","JPNUM=:JPNUM and status='ACTIVE' and X4INDISPONIBILITA=1").isEmpty() and mbo.getString("STATUS")=="APERTO" and mbo.getBoolean("X4TECMIG")==False:
    mbo.setValue("X4STATUSINDISP","INVIATO")
    mbo.setValue("X4ESITOINDISP","-")
    
"""
if onupdate and launchPoint=="X4WORKORDER_ADD" and  mbo.getString("STATUS") == "NON ESEGUITO DSS":
    mbo.changeStatus("NON ESEGUITO",None, None)
"""

if (mbo.isModified("X4OUTSOURCING") or mbo.isModified("X4NATURALAVORO")) and mbo.getBoolean("HASCHILDREN")==True and onupdate and launchPoint!="JPNUMVALIDATE":
    childSet2=mbo.getMboSet("CHILDNOTASK")
    if not childSet2.isEmpty():
        for figlio in range(0,childSet2.count()):
            child=childSet2.getMbo(figlio)
            child.setValue("X4NATURALAVORO",mbo.getString("X4NATURALAVORO"),11L)
            child.setValue("X4OUTSOURCING",mbo.getString("X4OUTSOURCING"),11L)
        childSet2.save()
    childSet2.close()


#Controllo su X4ATTCAR quando si seleziona un Job Plan
currentUI = mbo.getUserInfo()  # o da service, dipende dal contesto
userId = currentUI.getUserName()
if not mbo.isNull("JPNUM") and mbo.isModified("JPNUM") and mbo.getString("X4TIPOODL")=="MANUALE" and userId!='MAXADMIN' and mbo.getBoolean("X4AGGREGATI") == False:
    jobPlanMboSet = mbo.getMboSet("JOBPLAN")
    if not jobPlanMboSet.isEmpty():
        jobPlanMbo = jobPlanMboSet.getMbo(0)
        
        # Controllo X4ATTCAR
        if jobPlanMbo.getInt("X4ATTCAR") == 0:
            service.error("x3terna", "ErrorMsg", ["L'attivita' selezionata non e' caricabile manualmente."])
    jobPlanMboSet.close()

if onupdate:
    '''if mbo.getMboValue("X4TTRASF").getPreviousValue().asDouble()!=mbo.getMboValue("X4TTRASF").getCurrentValue().asDouble() and mbo.getBoolean("X4AGGREGATI")==False and not interactive:
        service.error("x3terna", "ErrorMsg", ["Impossibile cambiare il tempo di trasferimento dell'ordine di lavoro."])'''
    '''if mbo.getBoolean("X4AGGREGATI")==True  and mbo.getMboValue("ESTDUR").getPreviousValue().asDouble()!=mbo.getMboValue("ESTDUR").getCurrentValue().asDouble():
        service.error("x3terna", "ErrorMsg", ["OdL aggregato, impossibile procedere con la modifica della durata pianificata."])
    if (mbo.getMboValue("TARGSTARTDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGSTARTDATE").getCurrentValue().asDate() or mbo.getMboValue("TARGCOMPDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGCOMPDATE").getCurrentValue().asDate() or mbo.getMboValue("X4BURKS").getPreviousValue().asString()!=mbo.getMboValue("X4BURKS").getCurrentValue().asString() or mbo.getMboValue("X4OUTSOURCING").getPreviousValue().asBoolean()!=mbo.getMboValue("X4OUTSOURCING").getCurrentValue().asBoolean() or mbo.getMboValue("ESTDUR").getPreviousValue().asDouble()!=mbo.getMboValue("ESTDUR").getCurrentValue().asDouble() or mbo.getMboValue("SNECONSTRAINT").getPreviousValue().asDate()!=mbo.getMboValue("SNECONSTRAINT").getCurrentValue().asDate() or mbo.getMboValue("FNLCONSTRAINT").getPreviousValue().asDate()!=mbo.getMboValue("FNLCONSTRAINT").getCurrentValue().asDate() or mbo.getMboValue("X4NATURALAVORO").getPreviousValue().asString()!=mbo.getMboValue("X4NATURALAVORO").getCurrentValue().asString()) and mbo.getString("STATUS") not in ["APERTO","DA VALIDARE"]:
        service.error("x3terna", "ErrorMsg", ["OdL con stato diverso da APERTO, impossibile procedere con la modifica."])'''
#Gestione Mxloader aggiornato
if onupdate and app:
    if mbo.isModified("X4OUTSOURCING") and mbo.getBoolean("X4OUTSOURCING")==True and mbo.isNull("X4NATURALAVORO") and mbo.isNull("X4POLICY") and app=="X4MXWO":
        service.error("x3terna", "ErrorMsg", ["Si prega di popolare il campo Assistenza/Sorveglianza."])
    '''if (mbo.getMboValue("TARGSTARTDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGSTARTDATE").getCurrentValue().asDate() or mbo.getMboValue("TARGCOMPDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGCOMPDATE").getCurrentValue().asDate() or mbo.getMboValue("X4BURKS").getPreviousValue().asString()!=mbo.getMboValue("X4BURKS").getCurrentValue().asString() or mbo.getMboValue("X4OUTSOURCING").getPreviousValue().asBoolean()!=mbo.getMboValue("X4OUTSOURCING").getCurrentValue().asBoolean() or mbo.getMboValue("ESTDUR").getPreviousValue().asDouble()!=mbo.getMboValue("ESTDUR").getCurrentValue().asDouble() or mbo.getMboValue("SNECONSTRAINT").getPreviousValue().asDate()!=mbo.getMboValue("SNECONSTRAINT").getCurrentValue().asDate() or mbo.getMboValue("FNLCONSTRAINT").getPreviousValue().asDate()!=mbo.getMboValue("FNLCONSTRAINT").getCurrentValue().asDate() or mbo.getMboValue("X4NATURALAVORO").getPreviousValue().asString()!=mbo.getMboValue("X4NATURALAVORO").getCurrentValue().asString()) and mbo.getString("STATUS") not in ["APERTO","DA VALIDARE"]:
        service.error("x3terna", "ErrorMsg", ["OdL con stato diverso da APERTO, impossibile procedere con la modifica."])'''
    if (mbo.getMboValue("SNECONSTRAINT").getPreviousValue().asDate()!=mbo.getMboValue("SNECONSTRAINT").getCurrentValue().asDate() or mbo.getMboValue("FNLCONSTRAINT").getPreviousValue().asDate()!=mbo.getMboValue("FNLCONSTRAINT").getCurrentValue().asDate() or mbo.getMboValue("TARGSTARTDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGSTARTDATE").getCurrentValue().asDate() or mbo.getMboValue("TARGCOMPDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGCOMPDATE").getCurrentValue().asDate()) and mbo.getString("STATUS") in ["APERTO","DA VALIDARE"] and not mbo.isNull("PARENT") and app=="X4MXWO" :
        service.error("x3terna", "ErrorMsg", ["OdL aggregato, impossibile procedere con la modifica delle date di pianificazione."])
    if mbo.getMboValue("TARGSTARTDATE").getPreviousValue().asDate()!=mbo.getMboValue("TARGSTARTDATE").getCurrentValue().asDate() and mbo.getString("STATUS") in ["APERTO","DA VALIDARE"] and app=="X4MXWO" and mbo.getBoolean("X4AGGREGATI")==True:
        service.error("x3terna", "ErrorMsg", ["OdL aggregato, impossibile procedere con la modifica delle date di pianificazione."])
    '''if app!="X4WORKORDER" and (mbo.isModified("TARGSTARTDATE") or mbo.isModified("TARGCOMPDATE") or mbo.isModified("SNECONSTRAINT") or mbo.isModified("FNLCONSTRAINT")) and (mbo.getDate("SNECONSTRAINT")>mbo.getDate("TARGSTARTDATE") or mbo.getDate("TARGCOMPDATE")>mbo.getDate("FNLCONSTRAINT") or mbo.getDate("TARGCOMPDATE")<mbo.getDate("TARGSTARTDATE")):
        service.error("x3terna", "ErrorMsg", ["Modifica alle date di pianificazione non ammissibile."])'''