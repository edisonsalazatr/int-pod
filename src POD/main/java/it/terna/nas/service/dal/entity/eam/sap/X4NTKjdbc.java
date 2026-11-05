package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "X4NTK")
@Data
@ToString
public class X4NTKjdbc {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "x4ntk_seq")
    @SequenceGenerator(name = "x4ntk_seq", sequenceName = "X4NTKIDSEQ", allocationSize = 1)
    @Column(name = "X4NTKID")
    private Long x4ntkid;

    @Column(name = "IDNTK", length = 20)
    private String idntk;

    @Column(name = "CDCRESP", length = 29)
    private String cdcresp;

    @Column(name = "CHANGEDBY", length = 20)
    private String changedby;

    @Column(name = "CHANGEDDATE")
    private LocalDate changeddate;

    @Column(name = "CREATEDATE")
    private LocalDate createdate;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "HASLD")
    private BigDecimal hasld;

    @Column(name = "NOPERAZIONE", length = 20)
    private String noperazione;

    @Column(name = "PROFITCENTER", length = 15)
    private String profitcenter;

    @Column(name = "PROGETTO", length = 20)
    private String progetto;

    @Column(name = "SOCIETA", length = 15)
    private String societa;

    @Column(name = "STATOSISTEMA", length = 500)
    private String statosistema;

    @Column(name = "STATOSISTEMASAP", length = 80)
    private String statosistemasap;

    @Column(name = "STATOUTENTESAP", length = 80)
    private String statoutentesap;

    @Column(name = "TIPO", length = 10)
    private String tipo;

    @Column(name = "WBE", length = 50)
    private String wbe;

}
