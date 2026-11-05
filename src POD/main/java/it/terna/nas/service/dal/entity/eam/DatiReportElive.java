package it.terna.nas.service.dal.entity.eam;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import it.terna.nas.service.exposure.ws.elive.RecordFileElive;
import it.terna.nas.service.material.utility.TipologiaConverter;
import lombok.Data;

@Entity
@Table(name = "DATIREPORTELIVE")
@Data
public class DatiReportElive {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "datiReportEliveSeq")
    @SequenceGenerator(name = "datiReportEliveSeq", sequenceName = "DATIREPORTELIVE_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOME_FILE", nullable = false, length = 100)
    private String nomeFile;

    @Column(name = "ID_INT")
    private Long idInt;

    @Column(name = "CAMPATA", nullable = false, length = 100)
    private String campata;

    @Column(name = "DIST_INT", nullable = false)
    private Double distInt;

    @Column(name = "PROGRES")
    private Double progres;

    @Column(name = "PRIORITA", nullable = false, length = 50)
    private String priorita;

    @Column(name = "TEMP_AMB")
    private Double tempAmb;

    @Column(name = "C_EST_INT")
    private Double cEstInt;

    @Column(name = "C_NORD_INT")
    private Double cNordInt;

    @Column(name = "WONUM", nullable = false, length = 10)
    private String wonum;

    @Column(name = "COD_GRIGLIA", length = 160)
    private String codGriglia;

    @Column(name = "T_S")
    private Integer tS;

    @Column(name = "DENSITA", length = 10)
    private String densita;

    @Column(name = "T_A")
    private Integer tA;

    @Column(name = "EST_CEN")
    private Double estCen;

    @Column(name = "NORD_CEN")
    private Double nordCen;

    @Column(name = "X_CEN")
    private Double xCen;

    @Column(name = "Y_CEN")
    private Double yCen;

    @Column(name = "ANNO_COMP")
    private Integer annoComp;

    @Column(name = "PERIODO_COMP", length = 40)
    private String periodoComp;

    @Column(name = "IN_FASCIA")
    private Integer inFascia;

    @Column(name = "CAT_VERT")
    private Integer catVert;

    @Column(name = "CAT_SBAND")
    private Integer catSband;

    @Column(name = "RIBALT")
    private Integer ribalt;

    @Column(name = "TIPOLOGIA", nullable = false)
    @Convert(converter = TipologiaConverter.class)
    private List<Integer> tipologia;
    
    // Metodo di conversione
    public static DatiReportElive fromRecordFileElive(RecordFileElive record, String nomeFile) {
        if (record == null) {
            return null;
        }

        DatiReportElive datiReportElive = new DatiReportElive();
        datiReportElive.setNomeFile(nomeFile);
        datiReportElive.setIdInt(record.getId_int());
        datiReportElive.setCampata(record.getCampata());
        datiReportElive.setDistInt(record.getDist_int());
        datiReportElive.setProgres(record.getProgres() != null ? record.getProgres().doubleValue() : null);
        datiReportElive.setPriorita(record.getPriorita());
        datiReportElive.setTempAmb(record.getTemp_amb());
        datiReportElive.setCEstInt(record.getC_est_int());
        datiReportElive.setCEstInt(record.getC_nord_int());
        datiReportElive.setWonum(record.getWonum());
        datiReportElive.setCodGriglia(record.getCod_griglia());
        datiReportElive.setTS(record.getT_s());
        datiReportElive.setDensita(record.getDensita());
        datiReportElive.setTA(record.getT_a());
        datiReportElive.setEstCen(record.getEst_cen());
        datiReportElive.setNordCen(record.getNord_cen());
        datiReportElive.setXCen(record.getX_cen());
        datiReportElive.setYCen(record.getY_cen());
        datiReportElive.setAnnoComp(record.getAnno_comp() != null ? record.getAnno_comp().intValue() : null);
        datiReportElive.setPeriodoComp(record.getPeriodo_comp());
        datiReportElive.setInFascia(record.getIn_fascia());
        datiReportElive.setCatVert(record.getCat_vert());
        datiReportElive.setCatSband(record.getCat_sband());
        datiReportElive.setRibalt(record.getRibalt());
        datiReportElive.setTipologia(record.getTipologia());

        return datiReportElive;
    }
    
}

