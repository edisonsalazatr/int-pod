package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class GeoCallWorkOrder implements Serializable {

    public static final long serialVersionUID = -4557431681963240963L;

    @Id
    @Column(name = "WONUM")
    private String 				wonum;
    
    @Column(name = "DESCRIPTION")
	private String				description;
    
    @Column(name = "JPNUM")
	private String				jpnum;
    
    @Column(name = "ASSETNUM")
	private String				assetnum;
    
    @Column(name = "ESTDUR")
	private Double				estdur;
    
    @Column(name = "SNECONSTRAINT")
	private String				sneconstraint;
    
    @Column(name = "FNLCONSTRAINT")
	private String				fnlconstraint;
    
    @Column(name = "X4TIPOSPESA")
	private String				x4tipospesa;
    
    @Column(name = "X4CODDELINDISP")
	private String 				x4coddelindisp;
    
    @Column(name = "X4DINIZIOINDISP")
	private String				x4dinizioindisp;
    
    @Column(name = "X4DFINEINDISP")
	private String				x4dfineindisp;
    
    @Column(name = "X4SAPIDASSET")
	private String				x4sapidasset;
    
    @Column(name = "X4TITRIS")
	private String				x4titris;
    
    @Column(name = "X4STATUSINDISP")
	private String              x4statusindisp;
    
    @Column(name = "WOPRIORITY")
	private Integer				wopriority;
    
    @Column(name = "SCHEDSTART")
	private String				schedstart;
    
    @Column(name = "SCHEDFINISH")
	private String				schedfinish;
    
    @Column(name = "PARENT")
	private String				parent;
    
    @Column(name = "TARGSTARTDATE")
	private String				targstartdate;
    
    @Column(name = "TARGCOMPDATE")
	private String				targcompdate;
    
    @Column(name = "X4INDISPONIBILITA")
    private String 				x4indisponibilita;
    
    @Column(name = "SITEID")
    private String 				siteid;
}
