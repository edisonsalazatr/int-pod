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
@Table(name = "SCARTIREPORTELIVE")
@Data
public class ScartiReportElive {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scarti_report_elive_seq_gen")
    @SequenceGenerator(name = "scarti_report_elive_seq_gen", sequenceName = "scarti_report_elive_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOME_FILE", nullable = false, length = 100)
    private String nomeFile;

    @Column(name = "DESC_SCARTO", nullable = false, length = 500)
    private String descScarto;
    
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
    
    public static ScartiReportElive fromRecordFileElive(RecordFileElive record, String nomeFile, String descScarto) {
        ScartiReportElive scarti = new ScartiReportElive();
        scarti.setNomeFile(nomeFile); 
        scarti.setDescScarto(descScarto);
        scarti.setIdInt(record.getId_int());
        scarti.setCampata(record.getCampata());
        scarti.setDistInt(record.getDist_int());
        scarti.setProgres(record.getProgres() != null ? record.getProgres().doubleValue() : null);
        scarti.setPriorita(record.getPriorita());
        scarti.setTempAmb(record.getTemp_amb());
        scarti.setCEstInt(record.getC_est_int());
        scarti.setCEstInt(record.getC_nord_int());
        scarti.setWonum(record.getWonum());
        scarti.setCodGriglia(record.getCod_griglia());
        scarti.setTS(record.getT_s());
        scarti.setDensita(record.getDensita());
        scarti.setTA(record.getT_a());
        scarti.setEstCen(record.getEst_cen());
        scarti.setNordCen(record.getNord_cen());
        scarti.setXCen(record.getX_cen());
        scarti.setYCen(record.getY_cen());
        scarti.setAnnoComp(record.getAnno_comp() != null ? record.getAnno_comp().intValue() : null);
        scarti.setPeriodoComp(record.getPeriodo_comp());
        scarti.setInFascia(record.getIn_fascia());
        scarti.setCatVert(record.getCat_vert());
        scarti.setCatSband(record.getCat_sband());
        scarti.setRibalt(record.getRibalt());
        scarti.setTipologia(record.getTipologia());
        return scarti;
    }
}

