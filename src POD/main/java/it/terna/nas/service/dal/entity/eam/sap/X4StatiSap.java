package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "X4_STATI_SAP")
@Data
@ToString
public class X4StatiSap {

    @Id
    @Column(name = "STATO")
    private String stato;

    @Column(name = "CODICE")
    private String codice;

    @Column(name = "DESCRIZIONE")
    private String descrizione;

}
