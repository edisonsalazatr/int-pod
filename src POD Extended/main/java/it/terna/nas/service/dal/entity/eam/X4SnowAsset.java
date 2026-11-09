package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class X4SnowAsset implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "ASSETNUM")
	private String assetnum;
	
	@Column(name = "CHANGEDATE")
	private Date changeDate;
	
	@Column(name = "INGRP")
	private String ingrp;
	
	@Column(name = "GEWRK")
	private String gewrk;
	
	@Column(name = "INSTALLDATE")
	private Date installDate;
	
	@Column(name = "SEQUENCE")
	private String sequence;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "STATUSDATE")
	private Date statusDate;
	
	@Column(name = "SWERK")
	private String swerk;
	
	@Column(name = "SWERKDESCRIPTION")
	private String swerkDescription;
	
	@Column(name = "CLASSIFICATIONID")
	private String classificationId;
	
	@Column(name = "ROOTSAPID")
	private String rootSapId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "COSTRUTTORECOMPONENTE")
	private String costruttoreComponente;
	
	@Column(name = "COSTRUTTORECOMPONENTEDESCR")
	private String costruttoreComponenteDescr;
	
	@Column(name = "FUNZIONEELEMENTO")
	private String funzioneElemento;
	
	@Column(name = "RIFERIMENTOFASE")
	private String riferimentoFase;
	
	@Column(name = "TIPOCOMPONENTE")
	private String tipoComponente;
	
	@Id
	@Column(name = "SAPID")
	private String sapId;
	
	@Column(name = "PARENT")
	private String codicePadre;
	
	@Column(name = "ROOT_LEVEL")
	private String codiceRadice;
	
	@Override
	public int hashCode() {
		return Objects.hash(assetnum, changeDate, classificationId, codicePadre, codiceRadice, costruttoreComponente,
				costruttoreComponenteDescr, description, funzioneElemento, gewrk, ingrp, installDate, riferimentoFase,
				rootSapId, sapId, sequence, status, statusDate, swerk, swerkDescription, tipoComponente);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4SnowAsset other = (X4SnowAsset) obj;
		return Objects.equals(assetnum, other.assetnum) && Objects.equals(changeDate, other.changeDate)
				&& Objects.equals(classificationId, other.classificationId)
				&& Objects.equals(codicePadre, other.codicePadre) && Objects.equals(codiceRadice, other.codiceRadice)
				&& Objects.equals(costruttoreComponente, other.costruttoreComponente)
				&& Objects.equals(costruttoreComponenteDescr, other.costruttoreComponenteDescr)
				&& Objects.equals(description, other.description)
				&& Objects.equals(funzioneElemento, other.funzioneElemento) && Objects.equals(gewrk, other.gewrk)
				&& Objects.equals(ingrp, other.ingrp) && Objects.equals(installDate, other.installDate)
				&& Objects.equals(riferimentoFase, other.riferimentoFase) && Objects.equals(rootSapId, other.rootSapId)
				&& Objects.equals(sapId, other.sapId) && Objects.equals(sequence, other.sequence)
				&& Objects.equals(status, other.status) && Objects.equals(statusDate, other.statusDate)
				&& Objects.equals(swerk, other.swerk) && Objects.equals(swerkDescription, other.swerkDescription)
				&& Objects.equals(tipoComponente, other.tipoComponente);
	}
}
