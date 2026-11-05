package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "X4_STORNO_SAP")
public class X4STORNOSAP {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ODL")
    private String odl;

    @Column(name = "ODM")
    private String odm;

    @Column(name = "CONFERMA_ORE")
    private String confermaore;

    @Column(name = "CONTATORE_PROGRESSIVO")
    private String contatoreprogressivo;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "LOADED")
    private String loaded;

    @Override
    public String toString() {
        return "X4STORNOSAP{" +
                "id=" + id +
                ", odl='" + odl + '\'' +
                ", odm='" + odm + '\'' +
                ", confermaore='" + confermaore + '\'' +
                ", contatoreprogressivo='" + contatoreprogressivo + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
