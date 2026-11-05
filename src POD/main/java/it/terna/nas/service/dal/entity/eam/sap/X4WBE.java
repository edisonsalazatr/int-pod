package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "X4WBE")
@Data
@ToString
public class X4WBE {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "x4wbe_seq")
    @SequenceGenerator(name = "x4wbe_seq", sequenceName = "X4WBEIDSEQ", allocationSize = 1)
    @Column(name = "X4WBEID")
    private Long x4wbeid;

    @Column(name = "IDWBE")
    private String idwbe;

    @Column(name = "IDWBS")
    private String idwbs;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CDCRESP")
    private String cdcresp;

    @Column(name = "CDCRICH")
    private String cdcrich;

    @Column(name = "CHANGEDATE")
    private LocalDate changedate;

    @Column(name = "CODINCENTIVAZIONE")
    private String codincentivazione;

    @Column(name = "CODINTERVENTO")
    private String codintervento;

    @Column(name = "COIN")
    private String coin;

    @Column(name = "CREATEDATE")
    private LocalDate createdate;

    @Column(name = "HASLD")
    private BigDecimal hasld;

    @Column(name = "MOTINVESTIMENTO")
    private String motinvestimento;

    @Column(name = "PIANIFICAZIONE")
    private BigDecimal pianificazione;

    @Column(name = "PROFITCENTER")
    private String profitcenter;

    @Column(name = "SEDETECNICA")
    private String sedetecnica;

    @Column(name = "SOCRICH")
    private String socrich;

    @Column(name = "STATOSISTEMA")
    private String statosistema;

    @Column(name = "STATOSISTEMASAP")
    private String statosistemasap;

    @Column(name = "STATOUTENTESAP")
    private String statoutentesap;

    @Column(name = "TIPOPROG")
    private String tipoprog;

    @Column(name = "TIPOSPESA")
    private String tipospesa;

}
