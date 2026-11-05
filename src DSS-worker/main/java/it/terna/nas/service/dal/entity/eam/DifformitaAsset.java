package it.terna.nas.service.dal.entity.eam;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class DifformitaAsset implements Rilevazione {
	
    @Id
    @Column(name = "x4difforstatid")
    private Long   x4DifforStatId;
    
    @Column(name = "assetnum")
    private String assetnum;

    @Column(name = "siteid")
    private String siteId;

    @Column(name = "iddifformita")
    private String   idDifformita;

    @Column(name = "status")
    private String status;

    @Column(name = "gravita")
    private String gravita;

    @Column(name = "x4catdifforid")
    private Long   x4CatDifforId;

    @Column(name = "description")
    private String description;

    @Column(name = "idmisura")
    private String idMisura;

    @Column(name = "tipologia")
    private String tipologia;
    
    @Column(name = "PRIMARILEVAZIONE")
    private Date primaRilevazione;
    
    @Column(name = "X4TARGET")
    private String target;
    
}
