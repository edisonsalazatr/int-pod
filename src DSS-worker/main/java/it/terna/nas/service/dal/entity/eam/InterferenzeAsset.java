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
public class InterferenzeAsset implements Rilevazione {
	
		@Id
		@Column(name = "x4interfstatid")
	    private Long   x4InterfStatId;
		
	    @Column(name = "assetnum")
	    private String assetnum;

	    @Column(name = "siteid")
	    private String siteId;

	    @Column(name = "idinterferenza")
	    private String idInterferenza;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "x4catinterfid")
	    private Long   x4CatInterfId;

	    @Column(name = "description")
	    private String description;

	    @Column(name = "idmisura")
	    private String idMisura;

	    @Column(name = "misura")
	    private String misura;

	    @Column(name = "tipologiaasset")
	    private String tipologiaAsset;
	    
	    @Column(name = "gravita")
	    private String gravita;
	    
	    @Column(name = "PRIMARILEVAZIONE")
	    private Date primaRilevazione;
	    
	    @Column(name = "X4TARGET")
	    private String target;
}
