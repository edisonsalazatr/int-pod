package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4SNOW")
@Getter
@Setter
@NoArgsConstructor
public class X4Snow
{
    @Column(name = "CHANGEDATE")
    private Date   changeDate;

    @Column(name = "CLASSIFICATIONID")
    private String classificationId;

    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;

    @Id
    @Column(name = "CODICE_OGGETTO")
    private String codiceOggetto;

    @Column(name = "CODICE_PADRE")
    private String codicePadre;

    @Column(name = "CODICE_PADRE2")
    private String codicePadre2;

    @Column(name = "CODICE_RADICE")
    private String codiceRadice;

    @Column(name = "CODICE_RADICE2")
    private String codiceRadice2;

    @Column(name = "CODICE_SAP")
    private String codiceSap;

    @Column(name = "CODICE_SAP2")
    private String codiceSap2;

    @Column(name = "COORDINATE_LAT")
    private Double coordinateLat;

    @Column(name = "COORDINATE_LONG")
    private Double coordinateLong;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INSTALLDATE")
    private Date   installDate;

    @Column(name = "LIN_ANNO_COSTRUZIONE")
    private Double linAnnoCostruzione;

    @Column(name = "LIN_CONDUTTORE_AEREO")
    private String linConduttoreAereo;

    @Column(name = "LIN_CONDUTTORE_CAVO")
    private String linConduttoreCavo;

    @Column(name = "LIN_COSTRUTTORE_CAVO")
    private String linCostruttoreCavo;

    @Column(name = "LIN_COSTRUTTORE_GIUNTO")
    private String linCostruttoreGiunto;

    @Column(name = "LIN_COSTRUTTORE_TERMINALE")
    private String linCostruttoreTerminale;

    @Column(name = "LIN_ELEMENTO_INDISP")
    private String linElementoIndisp;

    @Column(name = "LIN_ANNO_INSTALLAZIONE")
    private Double linAnnoInstallazione;

    @Column(name = "LIN_ISOLAMENTO_CAVO")
    private String linIsolamentoCavo;

    @Column(name = "LIN_LUNGHEZZA")
    private Double linLunghezza;

    @Column(name = "LIN_NUM_CATENE")
    private Double linNumCatene;
    
    @Column(name = "LIN_RIF_ASSET_ARM_COND")
    private String linRifAssetArmCond;

    @Column(name = "LIN_RIF_ASSET_ARM_FDG")
    private String linRifAssetArmFdg;

    @Column(name = "LIN_RIF_FASE")
    private String linRifFase;

    @Column(name = "LIN_RIF_FUNE")
    private String linRifFune;

    @Column(name = "LIN_RIF_POSIZIONE")
    private Double linRifPosizione;

    @Column(name = "LIN_SEZIONE_CONDUTTORE_CAVO")
    private String linSezioneConduttoreCavo;

    @Column(name = "LIN_STRUTTURA_SOSTEGNO")
    private String linStrutturaSostegno;

    @Column(name = "LIN_TIPO_CONDUTTORE_CAVO")
    private String linTipoConduttoreCavo;

    @Column(name = "LIN_TIPO_FUNE_GUARDIA")
    private String linTipoFuneGuardia;

    @Column(name = "LIN_TIPO_GIUNTO")
    private String linTipoGiunto;

    @Column(name = "LIN_TIPO_ISOLATORI")
    private String linTipoIsolatori;

    @Column(name = "LIN_TIPO_ISOLATORI_DER")
    private String linTipoIsolatoriDer;

    @Column(name = "LIN_TIPO_ISOLATORI_LIN")
    private String linTipoIsolatoriLin;

    @Column(name = "LIN_TIPO_PALIFICAZIONE")
    private String linTipoPalificazione;

    @Column(name = "LIN_TIPO_SOSTEGNO")
    private String linTipoSostegno;

    @Column(name = "LIN_TIPO_TERMINALE")
    private String linTipoTerminale;

    @Column(name = "ROOTSAPID1")
    private String rootSapId1;

    @Column(name = "ROOTSAPID2")
    private String rootSapId2;

    @Column(name = "SEQUENCE1")
    private String sequence1;

    @Column(name = "SEQUENCE2")
    private String sequence2;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STATUSDATE")
    private Date   statusDate;
    
    @Column(name = "STZ_DATA_COSTRUZIONE")
    private Date   stzDataCostruzione;
    
    @Column(name = "STZ_ELEMENTO_INDISP")
    private String stzElementoIndisp;
    
    @Column(name = "STZ_FUNZIONAMENTO_COMANDI")
    private String stzFunzionamentoComandi;
    
    @Column(name = "STZ_POTENZA_NOMINALE")
    private Double stzPotenzaNominale;
    
    @Column(name = "STZ_TENSIONE_NOMINALE")
    private Double stzTensioneNominale;
    
    @Column(name = "STZ_TIPO_COMPARTO")
    private String stzTipoComparto;

    @Column(name = "SWERK")
    private String swerk;

    @Column(name = "SWERKDESCRIPTION")
    private String swerkDescription;

    @Column(name = "X3COSTRUTTORECOMPONENTE")
    private String x3CostruttoreComponente;

    @Column(name = "COSTRUTTORECOMPONENTEDESCR")
    private String x3CostruttoreComponenteDescr;
    
    @Column(name = "X3FUNZIONEELEMENTO")
    private String x3FunzioneElemento;
    
    @Column(name = "X3GEWRK")
    private String x3Gewrk;

    @Column(name = "X3INGRP")
    private String x3Ingrp;

    @Column(name = "X3RIFERIMENTOFASE")
    private String x3RiferimentoFase;

    @Column(name = "X3TIPOCOMPONENTE")
    private String x3TipoComponente;

    @Override
    public int hashCode()
    {
        return Objects.hash(codiceOggetto);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4Snow other = (X4Snow) obj;
        return Objects.equals(codiceOggetto, other.codiceOggetto);
    }
}