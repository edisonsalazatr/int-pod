package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "X4ODM")
@Data
@ToString
public class X4ODMjdbc {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "x4odm_seq")
    @SequenceGenerator(name = "x4odm_seq", sequenceName = "X4ODMIDSEQ", allocationSize = 1)
    @Column(name = "X4ODMID")
    private Long x4odmid;

    @Column(name = "IDODM", length = 20)
    private String idodm;

    @Column(name = "AUFNR", length = 12)
    private String aufnr;

    @Column(name = "CDCCOSTI", length = 15)
    private String cdccosti;

    @Column(name = "CDCRESP", length = 15)
    private String cdcresp;

    @Column(name = "CDLRESP", length = 15)
    private String cdlresp;

    @Column(name = "CHANGEDDATE")
    private LocalDate changeddate;

    @Column(name = "CREATEDBY", length = 20)
    private String createdby;

    @Column(name = "CREATEDDATE")
    private LocalDate createddate;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "HASLD")
    private BigDecimal hasld;

    @Column(name = "KONTY", length = 10)
    private String konty;

    @Column(name = "PROFITCENTER", length = 15)
    private String profitcenter;

    @Column(name = "SEDETECNICA", length = 30)
    private String sedetecnica;

    @Column(name = "STATOSISTEMA", length = 8)
    private String statosistema;

    @Column(name = "STATOUTENTE", length = 8)
    private String statoutente;

    @Column(name = "TIPOATTIVITA", length = 20)
    private String tipoattivita;

    @Column(name = "TIPOORDINE", length = 20)
    private String tipoordine;

    @Column(name = "WBE", length = 40)
    private String wbe;

}
