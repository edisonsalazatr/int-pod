package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "X4OPNTK")
@Data
@ToString
public class X4OPNTK {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "x4opntk_seq")
    @SequenceGenerator(name = "x4opntk_seq", sequenceName = "X4OPNTKIDSEQ", allocationSize = 1)
    @Column(name = "X4OPNTKID")
    private Long x4opntkid;

    @Column(name = "X4NTKID")
    private Long x4ntkid;

    @Column(name = "IDNTK", length = 20)
    private String idntk;

    @Column(name = "IDOPERAZIONE", length = 5)
    private String idoperazione;

    @Column(name = "CDCRESP", length = 20)
    private String cdcresp;

    @Column(name = "CDLRESP", length = 15)
    private String cdlresp;

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

    @Column(name = "ROWSTAMP", length = 40)
    private String rowstamp;

    @Column(name = "STATO", length = 80)
    private String stato;

    @Column(name = "STATOSISTEMA", length = 500)
    private String statosistema;

    @Column(name = "WBE", length = 50)
    private String wbe;

}