package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "X4_SAP_FILTER")
public class X4_SAP_FILTER {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "FILTRO")
    private String filtro;

    @Column(name = "TIPO_SPESA")
    private String tipoSpesa;

    @Column(name = "TIPO_ORDINE")
    private String tipoOrdine;

    @Column(name = "TIPO_ATTIVITA")
    private String tipoAttivita;

    @Column(name = "LIVELLO")
    private Integer livello;

    @Column(name = "STATO")
    private String stato;

}
