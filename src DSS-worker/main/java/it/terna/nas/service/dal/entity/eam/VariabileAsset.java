package it.terna.nas.service.dal.entity.eam;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class VariabileAsset {
	
	@Id
    @Column(name = "x4misureassetid")
    private Long   x4MisureAssetId;
	
    @Column(name = "assetnum")
    private String assetNum;

    @Column(name = "siteid")
    private String siteId;
    
    @Column(name = "x4catmisureid")
    private Long   x4CatMisureId;

    @Column(name = "idmisura")
    private String idMisura;

    @Column(name = "valore")
    private Double valore;

    @Column(name = "um")
    private String um;

    @Column(name = "tipoparametro")
    private String tipoParametro;
    
    @Column(name = "primaRilevazione")
    private Date primaRilevazione;
    
    @Column(name = "ultimaRilevazione")
    private Date ultimaRilevazione;

}
