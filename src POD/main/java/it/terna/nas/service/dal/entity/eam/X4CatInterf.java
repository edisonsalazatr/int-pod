package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "X4CATINTERF")
@Data
public class X4CatInterf {

    @Id
    @Column(name = "X4CATINTERFID")
    private Long id;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HASLD")
    private Boolean hasLd;

    @Column(name = "ASSIEME")
    private String assieme;

    @Column(name = "IDINTERFERENZA")
    private String idInterferenza;

    @Column(name = "IDMISURA")
    private String idMisura;

    @Column(name = "LOGICAINT")
    private String logicaInt;

    @Column(name = "MISURA")
    private String misura;

    @Column(name = "SOTTOASSIEME")
    private String sottoAssieme;

    @Column(name = "TEMPOVAL")
    private String tempoVal;

    @Column(name = "TIPOLOGIAASSET")
    private String tipologiaAsset;

    @Column(name = "ROWSTAMP")
    private String rowStamp;
}

