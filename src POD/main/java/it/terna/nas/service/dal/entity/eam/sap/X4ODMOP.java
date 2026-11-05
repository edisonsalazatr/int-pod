package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "X4ODMOP")
@Data
@ToString
public class X4ODMOP {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "x4odmop_seq")
    @SequenceGenerator(name = "x4odmop_seq", sequenceName = "X4ODMOPIDSEQ", allocationSize = 1)
    @Column(name = "X4ODMOPID")
    private Long X4ODMOPID;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IDODM")
    private String idodm;

    @Column(name = "X4ODMID")
    private Long x4odmid;

    @Column(name = "HASLD")
    private BigDecimal hasld;

    @Column(name = "NOPERAZIONE")
    private String noperazione;

}
