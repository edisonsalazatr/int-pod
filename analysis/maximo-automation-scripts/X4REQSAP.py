from psdi.server import MXServer
from psdi.util import MXException
from psdi.iface.mic import PublishChannelCache


try:
    checkOdL=mbo.getMboSet("WORKORDER")
    if not checkOdL.isEmpty():
        checkOdLMbo=checkOdL.getMbo(0)
        if (checkOdLMbo.isNull("X4ODM") and checkOdLMbo.getString("X4TIPOSPESA")!="OPEX") and checkOdLMbo.isNull("X4NTK"):
            service.error("x3terna","ErrorMsg", ["Impossibile procedere con l'invio. Si prega di popolare il campo OdM o NTK dell'ordine."])
        elif checkOdLMbo.getString("X4TIPOSPESA")=="OPEX" and checkOdLMbo.isNull("X4ODM"):
            create=True
    
    mbo.setValue("APPROVER","XT5854",11L)
    if checkOdL.getMbo(0).getBoolean("X4AGGREGATI")==True:
        wpSet=mbo.getMboSet("$WPLABORODL$","WPLABOR","wonum in (select wonum from workorder where parent=:refwo and siteid=:siteid) and siteid=:siteid")
    else:
        wpSet=mbo.getMboSet("$WPLABORODL$","WPLABOR","wonum=:refwo and siteid=:siteid")
    cdl={}
    mapping = {
        ("AM-LGP","IMPIEGAT",None): "AMPAM_I",
        ("AM-LGP","QUADRO",None): "AMPAM_Q",
        ("AM-PC","IMPIEGAT",None): "AMPC_I",
        ("AM-PRO","IMPIEGAT",None): "AMPRO_I",
        ("AM-PRO","QUADRO",None): "AMPRO_Q",
        ("AM-SPM","IMPIEGAT",None): "AMSPM_I",
        ("AM-SPM","QUADRO",None): "AMSPM_Q",
        ("AM-SPM","IMPIEGAT",None): "SPMPRV_I",
        ("DPCE-AT-COAM","IMPIEGAT",None): "CENCOA_I",
        ("DPCE-AT-COAM","QUADRO",None): "CENCOA_Q",
        ("ATCEN-MPL","IMPIEGAT",None): "CENMPL_I",
        ("ATCEN-MPL","QUADRO",None): "CENMPL_Q",
        ("DPCE-AT-COAM","IMPIEGAT",None): "ATCERL_I",
        ("DPCE-AT-COAM","QUADRO",None): "RLCETS_Q",
        ("ATCNR-COA","IMPIEGAT",None): "CNRCOA_I",
        ("ATCNR-COA","QUADRO",None): "CNRCOA_Q",
        ("ATCNR-MPL","IMPIEGAT",None): "CNRMPL_I",
        ("ATCNR-MPL","QUADRO",None): "CNRMPL_Q",
        ("ATCNR-RL","IMPIEGAT",None): "ATCNRL_I",
        ("ATCNR-RL","QUADRO",None): "ATCNRL_Q",
        ("ATNES-COA","IMPIEGAT",None): "NESCOA_I",
        ("ATNES-COA","QUADRO",None): "NESCOA_Q",
        ("ATNES-MPL","IMPIEGAT",None): "NESMPL_I",
        ("ATNES-MPL","QUADRO",None): "NESMPL_Q",
        ("ATNES-RL","QUADRO",None): "DTNEP_Q",
        ("DPND-AT-COAM","IMPIEGAT",None): "DPNDCO_I",
        ("DPND-AT-COAM","QUADRO",None): "ATNOCO_Q",
        ("ATNOR-MPL","IMPIEGAT",None): "NORMPL_I",
        ("ATNOR-MPL","QUADRO",None): "NORMPL_Q",
        ("ATNOR-RL","IMPIEGAT",None): "ATNRRL_I",
        ("ATNOR-RL","QUADRO",None): "ATNRRL_Q",
        ("ATNOV-COA","QUADRO",None): "ATNCOA_Q",
        ("ATNOV-MPL","IMPIEGAT",None): "NOVMPL_I",
        ("ATNOV-MPL","QUADRO",None): "NOVMPL_Q",
        ("ATNOV-RL","IMPIEGAT",None): "ATNVRL_I",
        ("ATNOV-RL","QUADRO",None): "ATNVRL_Q",
        ("ATSUD-CESS","IMPIEGAT",None): "SUDCOA_I",
        ("ATSUD-CESS","QUADRO",None): "SUDCOA_Q",
        ("ATSUD-MPL","IMPIEGAT",None): "SUDMPL_I",
        ("ATSUD-MPL","QUADRO",None): "SUDMPL_Q",
        ("ATSUD-RL","IMPIEGAT",None): "DPSURL_I",
        ("ATSUD-RL","QUADRO",None): "DTCSP_Q",
        ("DSSA-MPL","IMPIEGAT",None): "SARMPL_I",
        ("DSSA-MPL","QUADRO",None): "SARMPL_Q",
        ("DTCEN-AT","IMPIEGAT",None): "DTCENA_I",
        ("DTCEN-AT","QUADRO",None): "DTCENA_Q",
        ("DTCEN-PSA","IMPIEGAT",None): "CENPSA_I",
        ("DTCEN-PSA","QUADRO",None): "CENPSA_Q",
        ("DTCEN-SG","IMPIEGAT",None): "DTCESG_I",
        ("DTCEN-SG","QUADRO",None): "DTCESG_Q",
        ("DTCEN-UAM","IMPIEGAT",None): "UAMTT_I",
        ("DTCEN-UAM","OPERAIO",None): "UAMTT_O",
        ("DTCEN-UAM","QUADRO",None): "UAMTT_Q",
        ("DTCEN-ULN","IMPIEGAT",None): "ULNTT_I",
        ("DTCEN-ULN","OPERAIO",None): "ULNTT_O",
        ("DTCEN-ULN","QUADRO",None): "ULNTT_Q",
        ("DTCEN-ULS","IMPIEGAT",None): "ULSTT_I",
        ("DTCEN-ULS","OPERAIO",None): "ULSTT_O",
        ("DTCEN-ULS","QUADRO",None): "ULSTT_Q",
        ("DTCEN-UMA","IMPIEGAT",None): "UMATT_I",
        ("DTCEN-UMA","OPERAIO",None): "UMATT_O",
        ("DTCEN-UMA","QUADRO",None): "UMATT_Q",
        ("DTCEN-UMB","IMPIEGAT",None): "UMBTT_I",
        ("DTCEN-UMB","OPERAIO",None): "UMBTT_O",
        ("DTCEN-UMB","QUADRO",None): "UMBTT_Q",
        ("DTCNR-AT","IMPIEGAT",None): "DTCNRA_I",
        ("DTCNR-AT","QUADRO",None): "DTCNRA_Q",
        ("DTCNR-PSA","IMPIEGAT",None): "CNRPSA_I",
        ("DTCNR-PSA","QUADRO",None): "CNRPSA_Q",
        ("DTCNR-SG","IMPIEGAT",None): "DTCNSG_I",
        ("DTCNR-SG","QUADRO",None): "DTCNSG_Q",
        ("DTCNR-UBO","IMPIEGAT",None): "UBOTT_I",
        ("DTCNR-UBO","OPERAIO",None): "UBOTT_O",
        ("DTCNR-UBO","QUADRO",None): "UBOTT_Q",
        ("DTCNR-UFI","IMPIEGAT",None): "UFITT_I",
        ("DTCNR-UFI","OPERAIO",None): "UFITT_O",
        ("DTCNR-UFI","QUADRO",None): "UFITT_Q",
        ("DTCNR-UPR","IMPIEGAT",None): "UPRTT_I",
        ("DTCNR-UPR","OPERAIO",None): "UPRTT_O",
        ("DTCNR-UPR","QUADRO",None): "UPRTT_Q",
        ("DTCNR-USU","IMPIEGAT",None): "USUTT_I",
        ("DTCNR-USU","OPERAIO",None): "USUTT_O",
        ("DTCNR-USU","QUADRO",None): "USUTT_Q",
        ("DTCS-UFE","IMPIEGAT",None): "UFETT_I",
        ("DTCS-UFE","OPERAIO",None): "UFETT_O",
        ("DTCS-UFE","QUADRO",None): "UFETT_Q",
        ("DTNES-AT","IMPIEGAT",None): "DTNESA_I",
        ("DTNES-AT","QUADRO",None): "DTNESA_Q",
        ("DTNES-PSA","IMPIEGAT",None): "NESPSA_I",
        ("DTNES-PSA","QUADRO",None): "NESPSA_Q",
        ("DTNES-PSA","IMPIEGAT",None): "NESTSA_I",
        ("DTNES-SG","IMPIEGAT",None): "DTNESSG_I",
        ("DTNES-UCA","IMPIEGAT",None): "UCATT_I",
        ("DTNES-UCA","OPERAIO",None): "UCATT_O",
        ("DTNES-UCA","QUADRO",None): "UCATT_Q",
        ("DTNES-UDU","IMPIEGAT",None): "UDUTT_I",
        ("DTNES-UDU","OPERAIO",None): "UDUTT_O",
        ("DTNES-UDU","QUADRO",None): "UDUTT_Q",
        ("DTNES-UFVG","IMPIEGAT",None): "UFVTT_I",
        ("DTNES-UFVG","OPERAIO",None): "UFVTT_O",
        ("DTNES-UFVG","QUADRO",None): "UFVTT_Q",
        ("DTNES-UTA","IMPIEGAT",None): "UTATT_I",
        ("DTNES-UTA","OPERAIO",None): "UTATT_O",
        ("DTNES-UTA","QUADRO",None): "UTATT_Q",
        ("DTNES-UVV","IMPIEGAT",None): "UVVTT_I",
        ("DTNES-UVV","OPERAIO",None): "UVVTT_O",
        ("DTNES-UVV","QUADRO",None): "UVVTT_Q",
        ("DTNOR-AT","IMPIEGAT",None): "DTNRAT_I",
        ("DTNOR-AT","QUADRO",None): "DTNRAT_Q",
        ("DTNOR-PSA","IMPIEGAT",None): "NORPSA_I",
        ("DTNOR-PSA","QUADRO",None): "NORPSA_Q",
        ("DTNOR-SG","IMPIEGAT",None): "DTNRSG_I",
        ("DTNOR-SG","QUADRO",None): "DTNRSG_Q",
        ("DTNOR-UBG","OPERAIO",None): "UBGTT_O",
        ("DTNOR-UBG","QUADRO",None): "UBGTT_Q",
        ("DTNOR-UBG","IMPIEGAT",None): "UBGTP_I",
        ("DTNOR-UBS","IMPIEGAT",None): "UBSTT_I",
        ("DTNOR-UBS","OPERAIO",None): "UBSTT_O",
        ("DTNOR-UBS","QUADRO",None): "UBSTT_Q",
        ("DTNOR-UCH","IMPIEGAT",None): "UCHTT_I",
        ("DTNOR-UCH","OPERAIO",None): "UCHTT_O",
        ("DTNOR-UCH","QUADRO",None): "UCHTT_Q",
        ("DTNOR-UCS","IMPIEGAT",None): "UCSTT_I",
        ("DTNOR-UCS","OPERAIO",None): "UCSTT_O",
        ("DTNOR-UCS","QUADRO",None): "UCSTT_Q",
        ("DTNOR-UGO","IMPIEGAT",None): "UGOTT_I",
        ("DTNOR-UGO","OPERAIO",None): "UGOTT_O",
        ("DTNOR-UGO","QUADRO",None): "UGOTT_Q",
        ("DTNOV-AT","IMPIEGAT",None): "DTNVAT_I",
        ("DTNOV-AT","QUADRO",None): "DTNVAT_Q",
        ("DTNOV-PSA","IMPIEGAT",None): "AVTS_I",
        ("DTNOV-PSA","QUADRO",None): "AVTS_Q",
        ("DTNOV-SG","IMPIEGAT",None): "DTNVSG_I",
        ("DTNOV-SG","QUADRO",None): "DTNVSG_Q",
        ("DTNOV-UGE","IMPIEGAT",None): "UGETT_I",
        ("DTNOV-UGE","OPERAIO",None): "UGETT_O",
        ("DTNOV-UGE","QUADRO",None): "UGETT_Q",
        ("DTNOV-UNO","IMPIEGAT",None): "UNOTT_I",
        ("DTNOV-UNO","OPERAIO",None): "UNOTT_O",
        ("DTNOV-UNO","QUADRO",None): "UNOTT_Q",
        ("DTNOV-UPM","IMPIEGAT",None): "UPMTT_I",
        ("DTNOV-UPM","OPERAIO",None): "UPMTT_O",
        ("DTNOV-UPM","QUADRO",None): "UPMTT_Q",
        ("DTNOV-UTO","IMPIEGAT",None): "UTOTT_I",
        ("DTNOV-UTO","OPERAIO",None): "UTOTT_O",
        ("DTNOV-UTO","QUADRO",None): "UTOTT_Q",
        ("DTSAR-COA","IMPIEGAT",None): "SARCOA_I",
        ("DTSAR-COA","QUADRO",None): "SARCOA_Q",
        ("DTSAR-PSA","IMPIEGAT",None): "SARPSA_I",
        ("DTSAR-PSA","QUADRO",None): "SARPSA_Q",
        ("DTSAR-RL","IMPIEGAT",None): "DTSARL_I",
        ("DTSAR-RL","QUADRO",None): "DTSARL_Q",
        ("DTSAR-SG","IMPIEGAT",None): "DTSASG_I",
        ("DTSAR-SG","QUADRO",None): "DTSASG_Q",
        ("DSSA-UCO","IMPIEGAT",None): "UCOTT_I",
        ("DSSA-UCO","OPERAIO",None): "UCOTT_O",
        ("DSSA-UCO","QUADRO",None): "UCOTT_Q",
        ("DTSAR-UVI","IMPIEGAT",None): "UVITT_I",
        ("DTSAR-UVI","OPERAIO",None): "UVITT_O",
        ("DTSAR-UVI","QUADRO",None): "UVITT_Q",
        ("DTSIC-COA","IMPIEGAT",None): "SICCOA_I",
        ("DTSIC-COA","QUADRO",None): "SICCOA_Q",
        ("DTSIC-MPL","IMPIEGAT",None): "SICMPL_I",
        ("DTSIC-MPL","QUADRO",None): "SICMPL_Q",
        ("DTSIC-PSA","IMPIEGAT",None): "SICPSA_I",
        ("DTSIC-PSA","QUADRO",None): "SICPSA_Q",
        ("DTSIC-RL","IMPIEGAT",None): "DTSIRL_I",
        ("DTSIC-RL","QUADRO",None): "DTSIRL_Q",
        ("DTSIC-SG","IMPIEGAT",None): "DTSISG_I",
        ("DTSIC-SG","QUADRO",None): "DTSISG_Q",
        ("DTSIC-UCT","IMPIEGAT",None): "UCTTT_I",
        ("DTSIC-UCT","OPERAIO",None): "UCTTT_O",
        ("DTSIC-UCT","QUADRO",None): "UCTTT_Q",
        ("DTSIC-UPA","IMPIEGAT",None): "UPATT_I",
        ("DTSIC-UPA","OPERAIO",None): "UPATT_O",
        ("DTSIC-UPA","QUADRO",None): "UPATT_Q",
        ("DTSUD-AT","IMPIEGAT",None): "DTSUAT_I",
        ("DTSUD-AT","QUADRO",None): "DTSUAT_Q",
        ("DTSUD-PSA","IMPIEGAT",None): "SUDPSA_I",
        ("DTSUD-PSA","QUADRO",None): "SUDPSA_Q",
        ("DTSUD-SG","IMPIEGAT",None): "DPSUSG_I",
        ("DTSUD-UBA","IMPIEGAT",None): "UBATT_I",
        ("DTSUD-UBA","OPERAIO",None): "UBATT_O",
        ("DTSUD-UBA","QUADRO",None): "UBATT_Q",
        ("DTSUD-UBR","IMPIEGAT",None): "UBRTT_I",
        ("DTSUD-UBR","OPERAIO",None): "UBRTT_O",
        ("DTSUD-UBR","QUADRO",None): "UBRTT_Q",
        ("DTSUD-UFM","IMPIEGAT",None): "UFMTT_I",
        ("DTSUD-UFM","OPERAIO",None): "UFMTT_O",
        ("DTSUD-UFM","QUADRO",None): "UFMTT_Q",
        ("DTSUD-UMD","IMPIEGAT",None): "UMDTT_I",
        ("DTSUD-UMD","OPERAIO",None): "UMDTT_O",
        ("DTSUD-UMD","QUADRO",None): "UMDTT_Q",
        ("DTSUD-URO","IMPIEGAT",None): "UROTT_I",
        ("DTSUD-URO","OPERAIO",None): "UROTT_O",
        ("DTSUD-URO","QUADRO",None): "UROTT_Q",
        ("DTNOV-UGE","IMPIEGAT","TT"):"UGETT_I",
        ("DTNOV-UGE","OPERAIO","TT"):"UGETT_O",
        ("DTNOV-UGE","QUADRO","TT"):"UGETT_Q",
        ("DTNOV-UGE","IMPIEGAT","TP"):"UGETP_I",
        ("DTNOV-UNO","IMPIEGAT","TT"):"UNOTT_I",
        ("DTNOV-UNO","OPERAIO","TT"):"UNOTT_O",
        ("DTNOV-UNO","QUADRO","TT"):"UNOTT_Q",
        ("DTNOV-UNO","IMPIEGAT","TP"):"UNOTP_I",
        ("DTNOV-UPM","IMPIEGAT","TT"):"UPMTT_I",
        ("DTNOV-UPM","OPERAIO","TT"):"UPMTT_O",
        ("DTNOV-UPM","QUADRO","TT"):"UPMTT_Q",
        ("DTNOV-UPM","IMPIEGAT","TP"):"UPMTP_I",
        ("DTNOV-UPM","QUADRO","TP"):"UPMTP_Q",
        ("DTNOV-UTO","IMPIEGAT","TT"):"UTOTT_I",
        ("DTNOV-UTO","OPERAIO","TT"):"UTOTT_O",
        ("DTNOV-UTO","QUADRO","TT"):"UTOTT_Q",
        ("DTNOV-UTO","IMPIEGAT","TP"):"UTOTP_I",
        ("DTNOR-UBS","IMPIEGAT","TT"):"UBSTT_I",
        ("DTNOR-UBS","OPERAIO","TT"):"UBSTT_O",
        ("DTNOR-UBS","QUADRO","TT"):"UBSTT_Q",
        ("DTNOR-UBS","IMPIEGAT","TP"):"UBSTP_I",
        ("DTNOR-UBG","IMPIEGAT","TT"):"UBGTT_I",
        ("DTNOR-UBG","OPERAIO","TT"):"UBGTT_O",
        ("DTNOR-UBG","QUADRO","TT"):"UBGTT_Q",
        ("DTNOR-UBG","IMPIEGAT","TP"):"UBGTP_I",
        ("DTNOR-UCH","IMPIEGAT","TT"):"UCHTT_I",
        ("DTNOR-UCH","OPERAIO","TT"):"UCHTT_O",
        ("DTNOR-UCH","QUADRO","TT"):"UCHTT_Q",
        ("DTNOR-UCH","IMPIEGAT","TP"):"UCHTP_I",
        ("DTNOR-UCH","QUADRO","TP"):"UCHTP_Q",
        ("DTNOR-UCS","IMPIEGAT","TT"):"UCSTT_I",
        ("DTNOR-UCS","OPERAIO","TT"):"UCSTT_O",
        ("DTNOR-UCS","QUADRO","TT"):"UCSTT_Q",
        ("DTNOR-UCS","IMPIEGAT","TP"):"UCSTP_I",
        ("DTNOR-UGO","IMPIEGAT","TT"):"UGOTT_I",
        ("DTNOR-UGO","OPERAIO","TT"):"UGOTT_O",
        ("DTNOR-UGO","QUADRO","TT"):"UGOTT_Q",
        ("DTNOR-UGO","IMPIEGAT","TP"):"UGOTP_I",
        ("DTNES-UCA","IMPIEGAT","TP"):"UCATP_I",
        ("DTNES-UCA","IMPIEGAT","TT"):"UCATT_I",
        ("DTNES-UCA","OPERAIO","TT"):"UCATT_O",
        ("DTNES-UCA","QUADRO","TT"):"UCATT_Q",
        ("DTNES-UDU","IMPIEGAT","TP"):"UDUTP_I",
        ("DTNES-UDU","IMPIEGAT","TT"):"UDUTT_I",
        ("DTNES-UDU","OPERAIO","TT"):"UDUTT_O",
        ("DTNES-UDU","QUADRO","TT"):"UDUTT_Q",
        ("DTNES-UFVG","IMPIEGAT","TP"):"UFVTP_I",
        ("DTNES-UFVG","IMPIEGAT","TT"):"UFVTT_I",
        ("DTNES-UFVG","OPERAIO","TT"):"UFVTT_O",
        ("DTNES-UFVG","QUADRO","TT"):"UFVTT_Q",
        ("DTNES-UTA","IMPIEGAT","TP"):"UTATP_I",
        ("DTNES-UTA","IMPIEGAT","TT"):"UTATT_I",
        ("DTNES-UTA","OPERAIO","TT"):"UTATT_O",
        ("DTNES-UTA","QUADRO","TT"):"UTATT_Q",
        ("DTNES-UVV","IMPIEGAT","TP"):"UVVTP_I",
        ("DTNES-UVV","IMPIEGAT","TT"):"UVVTT_I",
        ("DTNES-UVV","OPERAIO","TT"):"UVVTT_O",
        ("DTNES-UVV","QUADRO","TT"):"UVVTT_Q",
        ("DTCNR-UBO","IMPIEGAT","TP"):"UBOTP_I",
        ("DTCNR-UBO","IMPIEGAT","TT"):"UBOTT_I",
        ("DTCNR-UBO","OPERAIO","TT"):"UBOTT_O",
        ("DTCNR-UBO","QUADRO","TT"):"UBOTT_Q",
        ("DTCNR-UFI","IMPIEGAT","TP"):"UFITP_I",
        ("DTCNR-UFI","IMPIEGAT","TT"):"UFITT_I",
        ("DTCNR-UFI","OPERAIO","TT"):"UFITT_O",
        ("DTCNR-UFI","QUADRO","TT"):"UFITT_Q",
        ("DTCNR-UPR","IMPIEGAT","TP"):"UPRTP_I",
        ("DTCNR-UPR","IMPIEGAT","TT"):"UPRTT_I",
        ("DTCNR-UPR","OPERAIO","TT"):"UPRTT_O",
        ("DTCNR-UPR","QUADRO","TT"):"UPRTT_Q",
        ("DTCNR-USU","IMPIEGAT","TP"):"USUTP_I",
        ("DTCNR-USU","IMPIEGAT","TT"):"USUTT_I",
        ("DTCNR-USU","OPERAIO","TT"):"USUTT_O",
        ("DTCNR-USU","QUADRO","TT"):"USUTT_Q",
        ("DTCEN-UAM","IMPIEGAT","TP"):"UAMTP_I",
        ("DTCEN-UAM","IMPIEGAT","TT"):"UAMTT_I",
        ("DTCEN-UAM","OPERAIO","TT"):"UAMTT_O",
        ("DTCEN-UAM","QUADRO","TT"):"UAMTT_Q",
        ("DTCEN-ULS","IMPIEGAT","TP"):"ULSTP_I",
        ("DTCEN-ULS","IMPIEGAT","TT"):"ULSTT_I",
        ("DTCEN-ULS","OPERAIO","TT"):"ULSTT_O",
        ("DTCEN-ULS","QUADRO","TT"):"ULSTT_Q",
        ("DTCEN-ULN","IMPIEGAT","TP"):"ULNTP_I",
        ("DTCEN-ULN","QUADRO","TP"):"ULNTP_Q",
        ("DTCEN-ULN","IMPIEGAT","TT"):"ULNTT_I",
        ("DTCEN-ULN","OPERAIO","TT"):"ULNTT_O",
        ("DTCEN-ULN","QUADRO","TT"):"ULNTT_Q",
        ("DTCEN-UMA","IMPIEGAT","TP"):"UMATP_I",
        ("DTCEN-UMA","IMPIEGAT","TT"):"UMATT_I",
        ("DTCEN-UMA","OPERAIO","TT"):"UMATT_O",
        ("DTCEN-UMA","QUADRO","TT"):"UMATT_Q",
        ("DTCEN-UMB","IMPIEGAT","TP"):"UMBTP_I",
        ("DTCEN-UMB","IMPIEGAT","TT"):"UMBTT_I",
        ("DTCEN-UMB","OPERAIO","TT"):"UMBTT_O",
        ("DTCEN-UMB","QUADRO","TT"):"UMBTT_Q",
        ("DTSUD-UBA","IMPIEGAT","TP"):"UBATP_I",
        ("DTSUD-UBA","QUADRO","TP"):"UBATP_Q",
        ("DTSUD-UBA","IMPIEGAT","TT"):"UBATT_I",
        ("DTSUD-UBA","OPERAIO","TT"):"UBATT_O",
        ("DTSUD-UBA","QUADRO","TT"):"UBATT_Q",
        ("DTSUD-UBR","IMPIEGAT","TP"):"UBRTP_I",
        ("DTSUD-UBR","IMPIEGAT","TT"):"UBRTT_I",
        ("DTSUD-UBR","OPERAIO","TT"):"UBRTT_O",
        ("DTSUD-UBR","QUADRO","TT"):"UBRTT_Q",
        ("DTCS-UFE","IMPIEGAT","TP"):"UFETP_I",
        ("DTCS-UFE","IMPIEGAT","TT"):"UFETT_I",
        ("DTCS-UFE","OPERAIO","TT"):"UFETT_O",
        ("DTCS-UFE","QUADRO","TT"):"UFETT_Q",
        ("DTSUD-UFM","IMPIEGAT","TP"):"UFMTP_I",
        ("DTSUD-UFM","QUADRO","TP"):"UFMTP_Q",
        ("DTSUD-UFM","IMPIEGAT","TT"):"UFMTT_I",
        ("DTSUD-UFM","OPERAIO","TT"):"UFMTT_O",
        ("DTSUD-UFM","QUADRO","TT"):"UFMTT_Q",
        ("DTSUD-UMD","IMPIEGAT","TP"):"UMDTP_I",
        ("DTSUD-UMD","IMPIEGAT","TT"):"UMDTT_I",
        ("DTSUD-UMD","OPERAIO","TT"):"UMDTT_O",
        ("DTSUD-UMD","QUADRO","TT"):"UMDTT_Q",
        ("DTSUD-URO","IMPIEGAT","TP"):"UROTP_I",
        ("DTSUD-URO","IMPIEGAT","TT"):"UROTT_I",
        ("DTSUD-URO","OPERAIO","TT"):"UROTT_O",
        ("DTSUD-URO","QUADRO","TT"):"UROTT_Q",
        ("DTSIC-UCT","IMPIEGAT","TP"):"UCTTP_I",
        ("DTSIC-UCT","IMPIEGAT","TT"):"UCTTT_I",
        ("DTSIC-UCT","OPERAIO","TT"):"UCTTT_O",
        ("DTSIC-UCT","QUADRO","TT"):"UCTTT_Q",
        ("DTSIC-UPA","IMPIEGAT","TP"):"UPATP_I",
        ("DTSIC-UPA","IMPIEGAT","TT"):"UPATT_I",
        ("DTSIC-UPA","OPERAIO","TT"):"UPATT_O",
        ("DTSIC-UPA","QUADRO","TT"):"UPATT_Q",
        ("DSSA-UCO","IMPIEGAT","TP"):"UCOTP_I",
        ("DSSA-UCO","IMPIEGAT","TT"):"UCOTT_I",
        ("DSSA-UCO","OPERAIO","TT"):"UCOTT_O",
        ("DSSA-UCO","QUADRO","TT"):"UCOTT_Q",
        ("DTSAR-UVI","IMPIEGAT","TP"):"UVITP_I",
        ("DTSAR-UVI","IMPIEGAT","TT"):"UVITT_I",
        ("DTSAR-UVI","OPERAIO","TT"):"UVITT_O",
        ("DTSAR-UVI","QUADRO","TT"):"UVITT_Q"
    }
    if not wpSet.isEmpty():
        for i in range(0,wpSet.count()):
            wpMbo=wpSet.getMbo(i)
            x4titr = udbCons=mapping.get((wpMbo.getString("X4TITRIS"), wpMbo.getString("CRAFT"),None), None)
            if x4titr and x4titr not in cdl:
                cdl[x4titr] = True
    craftCons=mbo.getString("CRAFT")
    pgroupConsSet=mbo.getMboSet("$PERSONGROUPTEAM$","PERSONGROUPTEAM","respparty=:laborcode and persongroup in (select persongroup from persongroup where x4udb=1)")
    if pgroupConsSet.isEmpty():
        service.error("x3terna","ErrorMsg", ["Risorsa non censita in EAM"])
        #service.error("x3terna", "Risorsa non censita in EAM.")
    else:
        pgroupCons=pgroupConsSet.getMbo(0).getString("PERSONGROUP")
        tipo=pgroupConsSet.getMbo(0).getString("X4TIPO")
        if pgroupConsSet.getMbo(0).isNull("X4TIPO"):
            tipo=None
    udbCons=mapping.get((pgroupCons, craftCons,tipo), None)
    #service.error("TEST",str(udbCons))
    def evalCdl(titris,craft):
     return mapping.get((titris, craft,None), None)
    if not udbCons in cdl:
        workorderMbo=mbo.getMboSet("WORKORDER").getMbo(0)
        tipospesa=workorderMbo.getString("X4TIPOSPESA")
        ntk=False
        odm=True
        
        # URL dell'endpoint
        url_odm = "http://servicebus-col.servizi.prv/ESB/SAP/ODM_Manage/1.0" 
        url_ntk = "http://servicebus-col.servizi.prv/ESB/SAP/NTW_Manage/1.0"
        
        username = "ESB_APP_EAM"
        password = "[REDACTED]"
        if tipospesa=="CAPEX":
            url=url_ntk
        else:
            url=url_odm
            
        # Formattazione delle date
        date_format = SimpleDateFormat("yyyy-MM-dd")
        TARGSTARTDATE = date_format.format(workorderMbo.getDate("TARGSTARTDATE"))
        TARGCOMPDATE = date_format.format(workorderMbo.getDate("TARGCOMPDATE"))
        ESTDUR = workorderMbo.getDouble("ESTDUR")
            
        # Variabili messaggio
        WONUM = workorderMbo.getString("WONUM")
        X4ODM = workorderMbo.getString("X4ODM")
        DESCRIPTION = str(workorderMbo.getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "&lt;").replace(">", "&gt;").replace("à", "a").replace("è", "e").replace("é", "e").replace("ì", "i").replace("ò", "o").replace("ù", "u").replace("À", "A").replace("È", "E").replace("É", "E").replace("Ì", "I").replace("Ò", "O").replace("Ù", "U")
        X4NTK = workorderMbo.getString("X4NTK")
        OWNERGROUP= workorderMbo.getString("OWNERGROUP")
        JPNUM= workorderMbo.getString("JPNUM")
        X4TITRIS= workorderMbo.getString("X4TITRIS")
        
        NTKSET = workorderMbo.getMboSet("$X4NTK$", "X4NTK", "IDNTK=:X4NTK")
        if NTKSET.isEmpty():
            TIPONTK="Z_PM"
        else:
            TIPONTK = NTKSET.getMbo(0).getString("TIPO")
        NOPERAZIONE = workorderMbo.getString("X4NOPERAZIONE")
        
        JPNUMDESC = str(workorderMbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("DESCRIPTION").encode("ascii", "ignore")).replace("<", "&lt;").replace(">", "&gt;").replace("à", "a").replace("è", "e").replace("é", "e") \
        .replace("ì", "i").replace("ò", "o").replace("ù", "u") \
        .replace("À", "A").replace("È", "E").replace("É", "E") \
        .replace("Ì", "I").replace("Ò", "O").replace("Ù", "U")
        IDSTANDARD = workorderMbo.getMboSet("$JOBPLAN$","JOBPLAN","JPNUM=:JPNUM").getMbo(0).getString("X4ATTALL")
        
        #TARGSTARTDATE = mbo.getDate("TARGSTARTDATE")
        #TARGCOMPDATE = mbo.getDate("TARGCOMPDATE")
        WOPRIORITY = workorderMbo.getString("WOPRIORITY")
        X4TIPOATTIVITA = workorderMbo.getString("X4TIPOATTIVITA")
        if workorderMbo.isNull("X4TIPOORDINE") and workorderMbo.getString("X4TIPOOI")=="T09":
            #X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
            X4TIPOORDINE = "PMTG"
        elif workorderMbo.isNull("X4TIPOORDINE") and workorderMbo.getString("X4TIPOOI")=="T06":
            #X4TIPOORDINE = mbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
            X4TIPOORDINE = "PMT9"
        elif workorderMbo.isNull("X4TIPOORDINE"):
            X4TIPOORDINE = workorderMbo.getMboSet("$X4COIN$","X4COIN","IDCOIN=:X4COIN").getMbo(0).getString("TIPOORDINE")
        else:
            X4TIPOORDINE = workorderMbo.getString("X4TIPOORDINE")
        if tipospesa=="CAPEX":
            X4WBSCONT = workorderMbo.getString("X4WBE")
        else:
            if workorderMbo.getString("X4TIPOORDINE")=="PMT0" or workorderMbo.getString("X4TIPOORDINE")=="PMAC":
                X4WBS=""
            else:
                X4WBS = workorderMbo.getString("X4WBEPIAN")
              
            if workorderMbo.getString("X4TIPOORDINE") in ["PMT0","PML9","PMTG"] or (workorderMbo.getString("X4TIPOATTIVITA")=="EPC" and workorderMbo.getString("X4TIPOSPESA")=="ANR"):
                X4WBSCONT = workorderMbo.getString("X4WBE")
            else:
                X4WBSCONT=""
                
        X4SAPIDASSET = workorderMbo.getString("X4SAPIDASSET")
        classID=workorderMbo.getMboSet("ASSET").getMbo(0).getString("CLASSSTRUCTUREID")
        equipmentMboSet=MXServer.getMXServer().getMboSet("CLASSANCESTOR",MXServer.getMXServer().getSystemUserInfo())
        equipmentMboSet.setWhere("CLASSSTRUCTUREID='"+classID+"' and ancestorclassid='DISPOSITIVI E COMPONENTI'")
        equipmentMboSet.reset()
        if not equipmentMboSet.isEmpty():
            X4SAPIDASSET=X4SAPIDASSET[:-11]
        equipmentMboSet.close()
        X4NORDINEINTERNO = workorderMbo.getString("X4NORDINEINTERNO")
        ESTDUR = workorderMbo.getDouble("ESTDUR")
        isParent=workorderMbo.getBoolean("X4AGGREGATI")
        if isParent==True:
            wplaborSet=workorderMbo.getMboSet("$WPFIGLI$","WPLABOR","wonum in (select wonum from workorder where parent=:wonum and siteid=:siteid)")
        else:
            wplaborSet=workorderMbo.getMboSet("WPLABOR")
        operazioni = []
        if not wplaborSet.isEmpty():
            if tipospesa=='OPEX' or tipospesa=='ANR':
                first_cdl_set=workorderMbo.getMboSet("$X4ODMID$","X4ODM",":x4odm=idodm")
                if not first_cdl_set.isEmpty():
                    first_cdl=first_cdl_set.getMbo(0).getString("CDLRESP")
                    if first_cdl_set.getMbo(0).getString("STATOSISTEMA")=="CHIU":
                        service.error("x3terna","ErrorMsg", ["L'OdM si trova in stato CHIUSO, impossibile procedere."] )
                wplaborNewMbo = wplaborSet.moveFirst()
                if first_cdl_set.isEmpty():
                    first_cdl=mbo.getMboSet("WORKORDER").getMbo(0).getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
                #first_cdl=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                aggregati = {}
                aggregati[udbCons] = {
                    'dauno': (mbo.getDouble("X4ORETOT")),
                    'ntend': date_format.format(mbo.getDate("FINISHDATE")),
                    'hrwrk': mbo.getDouble("REGULARHRS"),
                    'hrtra': mbo.getDouble("X4ORETRASF")
                }       
                while wplaborNewMbo:
                    cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                    dauno = (wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))+(wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY"))
                    ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
                    arbpl = cdltitris
                    hrwrk = wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")
                    hrtra = wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")
                    # Se il CDL è già nel dizionario, somma i valori
                    if cdltitris in aggregati:
                        aggregati[cdltitris]['dauno'] += dauno
                        aggregati[cdltitris]['hrwrk'] += hrwrk
                        aggregati[cdltitris]['hrtra'] += hrtra
                    else:
                        aggregati[cdltitris] = {
                            'dauno': dauno,
                            'ntend': ntend,
                            'hrwrk': hrwrk,
                            'hrtra': hrtra
                        }
                    wplaborNewMbo = wplaborSet.moveNext()  # Passo al prossimo Mbo
    
                for cdltitris, valori in aggregati.items():
                    operazione_tag = """<item>
                        <Dauno>{}</Dauno>
                        <Ntend>{}</Ntend>
                        <Personale>
                          <item>
                            <Arbpl>{}</Arbpl>
                            <Hrwrk>{}</Hrwrk>
                            <Hrtra>{}</Hrtra>
                          </item>
                        </Personale>
                      </item>""".format(
                        valori['dauno'],
                        valori['ntend'],
                        cdltitris,
                        valori['hrwrk'],
                        valori['hrtra']
                    )
                    operazioni.append(operazione_tag)
                    
    
                # Unisco tutte le operazioni in un unico blocco XML
                operation_xml = "<Operation>{}</Operation>".format("\n".join(operazioni))
                #service.error("TEST",operation_xml)
            elif tipospesa=='CAPEX':
                wplaborNewMbo = wplaborSet.moveFirst()
                first_cdl=mbo.getMboSet("WORKORDER").getMbo(0).getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
                aggregati_capex = {}
                aggregati_capex[udbCons] = {
                    'dauno': (mbo.getDouble("X4ORETOT")),
                    'hrwrk': mbo.getDouble("REGULARHRS"),
                    'hrtra': mbo.getDouble("X4ORETRASF")
                }
                while wplaborNewMbo:
                    cdltitris=evalCdl(wplaborNewMbo.getString("X4TITRIS"),wplaborNewMbo.getString("CRAFT"))
                    dauno = (wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY"))+(wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY"))
                    ntend = date_format.format(wplaborNewMbo.getDate("X4DATAFINERISORSA"))
                    ntstart = date_format.format(wplaborNewMbo.getDate("X4DATAINIZIORISORSA"))
                    arbpl = cdltitris
                    hrwrk = wplaborNewMbo.getDouble("LABORHRS")*wplaborNewMbo.getDouble("QUANTITY")
                    hrtra = wplaborNewMbo.getDouble("X4ORETRASF")*wplaborNewMbo.getDouble("QUANTITY")
    
                    # Se il CDL è già nel dizionario, somma i valori
                    if cdltitris in aggregati_capex:
                        aggregati_capex[cdltitris]['dauno'] += dauno
                        aggregati_capex[cdltitris]['hrwrk'] += hrwrk
                        aggregati_capex[cdltitris]['hrtra'] += hrtra
                    else:
                        aggregati_capex[cdltitris] = {
                            'dauno': dauno,
                            'hrwrk': hrwrk,
                            'hrtra': hrtra
                        }
                    wplaborNewMbo = wplaborSet.moveNext()  # Passo al prossimo Mbo
                for cdltitris, valori in aggregati_capex.items():
                    operazione_tag = """
                              <item>
                                <Cdl>{}</Cdl>
                                <Orelavoro>{}</Orelavoro>
                                <Oretrasferimento>{}</Oretrasferimento>
                              </item>
                            """.format(cdltitris, valori['hrwrk'], valori['hrtra'])
                    operazioni.append(operazione_tag)
            # Unisco tutte le operazioni in un unico blocco XML
                operation_xml = "<Personale>{}</Personale>".format("\n".join(operazioni))
        if wplaborSet.isEmpty():
            ORELAVORO=0
            ORETRASFERIMENTO=0
            DURATA=0
        else:
            ORELAVORO=0
            ORETRASFERIMENTO=0
            DURATA=0
            for i in range(0,wplaborSet.count()):
                wplaborMbo=wplaborSet.getMbo(i)
                ORELAVORO=ORELAVORO+(wplaborMbo.getDouble("LABORHRS")*wplaborMbo.getDouble("QUANTITY"))
                ORETRASFERIMENTO=ORETRASFERIMENTO+wplaborMbo.getDouble("X4ORETRASF")
                DURATA = ORELAVORO + ORETRASFERIMENTO
        wplaborSet.close()
        #ORELAVORO = ORELAVORO
        #ORETRASFERIMENTO = ORETRASFERIMENTO
        
        if ORELAVORO==0:
            service.error("x3terna","ErrorMsg", ["Si prega di compilare almeno una riga di pianificazione con delle ore di esecuzione."] )
        CDL = workorderMbo.getMboSet("ASSET").getMbo(0).getMboSet("X4ANCESTOR").getMbo(0).getString("X3GEWRK")
        if tipospesa=="CAPEX":
            #XML da inviare nella richiesta POST per NTW
            xml_data ="""<?xml version="1.0" encoding="utf-8"?>
            <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                    <tns:ZrichiestaCreazioneNtw xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
                      <NtwMaintain>
                        <Tipontw>{}</Tipontw>
                        <Network>{}</Network>
                        <Wbs>{}</Wbs>
                        <Descrizione>{}</Descrizione>
                        <Centrolavoro>{}</Centrolavoro>
                        <Operazioni>
                        <item>
                            <Istanza>{}</Istanza>
                            <Operazione>{}</Operazione>
                            <Idattivita>{}</Idattivita>
                            <Sede>{}</Sede>
                            <Iniziocardine>{}</Iniziocardine>
                            <Finecardine>{}</Finecardine>
                            <Datatarget>{}</Datatarget>
                            <Durata>{}</Durata>
                            {}
                            <Sedi>
                              <item>
                                <Tplnr>{}</Tplnr>
                              </item>
                            </Sedi>
                          </item>
                      </Operazioni>
                      </NtwMaintain>
                      <Trtyp>V</Trtyp>
                    </tns:ZrichiestaCreazioneNtw>
                </soap:Body>
            </soap:Envelope>""".format(TIPONTK,X4NTK,X4WBSCONT,JPNUMDESC[:40],first_cdl,WONUM, NOPERAZIONE, IDSTANDARD, X4SAPIDASSET,TARGSTARTDATE,TARGCOMPDATE,TARGCOMPDATE,ESTDUR,operation_xml,X4SAPIDASSET)
            
        else:
            # XML da inviare nella richiesta POST per OdM
            if create==False:
                xml_data = """<?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                  <soap:Header>
                    <wsse:Security soap:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"/>
                  </soap:Header>
                  <soap:Body>
                    <tns:Zfmpms4EamOdmMaintain xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
                      <IOdmMaintain>
                        <Instance>{}</Instance>
                        <IdGeocall>{}</IdGeocall>
                        <Aufnr>{}</Aufnr>
                        <Auftext>{}</Auftext>
                        <Gstrp>{}</Gstrp>
                        <Gltrp>{}</Gltrp>
                        <Priok>2</Priok>
                        <Ilart>{}</Ilart>
                        <Auart>{}</Auart>
                        <Wbs>{}</Wbs>
                        <Functloc>
                          <item>
                            <Tplnr>{}</Tplnr>
                          </item>
                        </Functloc>
                        <Tplnr>{}</Tplnr>
                        <CdlResp>{}</CdlResp>
                        <Costs>
                          <Aufnr>{}</Aufnr>
                          <Wbs>{}</Wbs>
                        </Costs>
                        {}
                      </IOdmMaintain>
                      <Trtyp>V</Trtyp>
                    </tns:Zfmpms4EamOdmMaintain>
                  </soap:Body>
                </soap:Envelope>""".format(WONUM,IDSTANDARD,X4ODM,DESCRIPTION[:40], TARGSTARTDATE, TARGCOMPDATE, X4TIPOATTIVITA, X4TIPOORDINE, X4WBS, X4SAPIDASSET,X4SAPIDASSET,first_cdl,X4NORDINEINTERNO,X4WBSCONT,operation_xml)
            else:
                xml_data = """<?xml version="1.0" encoding="utf-8"?>
                    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                      <soap:Header>
                        <wsse:Security soap:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"/>
                      </soap:Header>
                      <soap:Body>
                        <tns:Zfmpms4EamOdmMaintain xmlns:tns="urn:sap-com:document:sap:soap:functions:mc-style">
                          <IOdmMaintain>
                            <Instance>{}</Instance>
                            <IdGeocall>{}</IdGeocall>
                            <Aufnr>{}</Aufnr>
                            <Auftext>{}</Auftext>
                            <Gstrp>{}</Gstrp>
                            <Gltrp>{}</Gltrp>
                            <Priok>2</Priok>
                            <Ilart>{}</Ilart>
                            <Auart>{}</Auart>
                            <Wbs>{}</Wbs>
                            <Functloc>
                              <item>
                                <Tplnr>{}</Tplnr>
                              </item>
                            </Functloc>
                            <Tplnr>{}</Tplnr>
                            <CdlResp>{}</CdlResp>
                            <Costs>
                              <Aufnr>{}</Aufnr>
                              <Wbs>{}</Wbs>
                            </Costs>
                            {}
                          </IOdmMaintain>
                          <Trtyp>H</Trtyp>
                        </tns:Zfmpms4EamOdmMaintain>
                      </soap:Body>
                    </soap:Envelope>""".format(WONUM,IDSTANDARD,X4ODM,DESCRIPTION[:40], TARGSTARTDATE, TARGCOMPDATE, X4TIPOATTIVITA, X4TIPOORDINE, X4WBS, X4SAPIDASSET,X4SAPIDASSET,first_cdl,X4NORDINEINTERNO,X4WBSCONT,operation_xml)

        #service.error("TEST",xml_data)
        # Funzione per eseguire la richiesta POST e restituire la risposta
        def send_post_request(url, xml_data):
            url_object = URL(url)
            connection = url_object.openConnection()
            connection.setDoOutput(True)
            connection.setRequestMethod("POST")
            connection.setRequestProperty("Content-Type", "application/xml")
            # Creazione manuale dell'header di autenticazione
            auth_string = str(username)+":"+str(password)
            #service.error("TEST",str(auth_string))
            #auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"))
            auth_encoded = Base64.getEncoder().encodeToString(auth_string.encode("utf-8"));
            connection.setRequestProperty("Authorization", "Basic {}".format(auth_encoded))
        
            # Scrivere il body della richiesta
            writer = OutputStreamWriter(connection.getOutputStream())
            writer.write(xml_data)
            writer.flush()
            writer.close()
        
            # Leggere la risposta
            response_code = connection.getResponseCode()
            if response_code == HttpURLConnection.HTTP_OK:
                reader = BufferedReader(InputStreamReader(connection.getInputStream()))
                response = ""
                line = reader.readLine()
                while line:
                    response += line
                    line = reader.readLine()
                reader.close()
                return response
            else:
                raise Exception("HTTP request failed with code: " + str(response_code))
                
                
        #Prendo valore di OdM        
        def extract_odm_from_response(response):
            # Parse XML response
            factory = DocumentBuilderFactory.newInstance()
            builder = factory.newDocumentBuilder()
            isr = InputSource(StringReader(response))
            doc = builder.parse(isr)
            # Find <OdM> tag
            odm_elements = doc.getElementsByTagName("Type")
            odm_text = odm_elements.item(0).getTextContent().strip()
            if odm_text:
                odm_text = odm_elements.item(0).getTextContent().strip()
                if odm_text=="E":
                    errore=True
                    odm_elements = doc.getElementsByTagName("Message")
                    resperrore= odm_elements.item(0).getTextContent().strip()
                else:
                    resperrore=None
                    errore=False
                return resperrore, errore
                    
        # Funzione per aggiornare il workorder (set del campo ODM)
        def update_mbo_with_response_odm(mbo, odm):
            checkOdLMbo.setValue("X4ODM", odm, 11L)
            checkOdL.save()
            #service.error("x3terna", "Odm ricevuto e aggiornato.")
            
        #Prendo valore di NTK      
        def extract_ntk_from_response(response):
            # Parse XML response
            factory = DocumentBuilderFactory.newInstance()
            builder = factory.newDocumentBuilder()
            isr = InputSource(StringReader(response))
            doc = builder.parse(isr)
            
            ntk_elements = doc.getElementsByTagName("Network")
            ntk_text = ntk_elements.item(0).getTextContent()
            if not ntk_text:
                #ntk_text = ntk_elements.item(0).getTextContent().strip()
                message_elements = doc.getElementsByTagName("Message")
                resperrore = message_elements.item(0).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
                if resperrore:
                    errore = True
                    return resperrore, None, errore
            
            #op_elements = doc.getElementsByTagName("Operazione")
            #op_text = op_elements.item(1).getTextContent().strip()
            
            if ntk_text:
                ntk_text = ntk_elements.item(0).getTextContent().strip()
                errore = False
                return ntk_elements.item(0).getTextContent(), "NA", errore
            
            else:
                #service.error("Errore nella creazione della Network  ",(resperrore))
                message_elements = doc.getElementsByTagName("Message")
                resperrore = message_elements.item(2).getTextContent().strip().encode('ascii', 'ignore').decode('ascii')
               
                errore = True
                return resperrore, None, errore
                        
        # Funzione per aggiornare il workorder (set del campo NTK)
        def update_mbo_with_response_ntk(mbo, ntk, noperazione):
            mbo.setValue("X4NTK", ntk, 11L)
            mbo.setValue("X4NOPERAZIONE", noperazione, 11L)
            mbo.getThisMboSet().save()
            #service.error("x3terna", "Ntk ricevuto e aggiornato.")
            
            
        # Invocazione delle funzioni
        #service.error("TEST",str(xml_data))
        response = send_post_request(url, xml_data)
        #service.error("TEST",str(response))
        if tipospesa=='CAPEX':
            ntk,noperazione,errore=extract_ntk_from_response(response)
            if errore==True:
                service.error("x3terna","ErrorMsg", ["Errore nella creazione: "+str(ntk)])
            #if mbo:
                #update_mbo_with_response_ntk(mbo, ntk)
                #service.error("x3terna", "Odm ricevuto e aggiornato.")
        else:       
        #service.error("TEST",response)
            odm,errore = extract_odm_from_response(response)
            # Ottenere il MBO corrente. Questo potrebbe variare a seconda del contesto in cui lo script viene eseguito.
            if errore==True:
                service.error("Errore nella creazione: ",(odm))
            if create==True:
                update_mbo_with_response_odm(checkOdLMbo, odm)
                #service.error("x3terna", "Odm ricevuto e aggiornato.")
    else:
        if mbo.getMboSet("WORKORDER").getMbo(0).getString("X4TIPOSPESA")=='OPEX' or mbo.getMboSet("WORKORDER").getMbo(0).getString("X4TIPOSPESA")=='ANR':
                first_cdl_set=mbo.getMboSet("WORKORDER").getMbo(0).getMboSet("$X4ODMID$","X4ODM",":x4odm=idodm")
                if not first_cdl_set.isEmpty():
                    first_cdl=first_cdl_set.getMbo(0).getString("CDLRESP")
                    if first_cdl_set.getMbo(0).getString("STATOSISTEMA")=="CHIU":
                        service.error("x3terna","ErrorMsg", ["L'OdM si trova in stato CHIUSO, impossibile procedere."] )
                        
                        
    '''PublishChannelCache.getInstance().getPublishChannel("X4PBLABTRANSSAP").publish(mbo, True);
    server = MXServer.getMXServer()
    userinfo=server.getSystemUserInfo()'''
    
    mbo.setValue("GENAPPRSERVRECEIPT","1")
    
except MXException:
    print("Error in the script")