package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4POLICY")
@Getter
@Setter
@NoArgsConstructor
public class AX4Policy
{

	@Id
	@Column(name = "EAUDITTRANSID")
	private String eauditTransId;
	
	@Column(name = "X4POLICYID")
	private Long	x4PolicyId;

	@Column(name = "CAMPAGNA")
	private Integer	campagna;

	@Column(name = "CHANGEDBY")
	private String	changedBy;

	@Column(name = "CHANGEDDATE")
	private Date	changedDate;

	@Column(name = "COMBINPUT")
	private String	combInput;

	@Column(name = "DATAFINEVAL")
	private Date	dataFineVal;

	@Column(name = "DATAINIZIOVAL")
	private Date	dataInizioVal;

	@Column(name = "DESCRIPTION")
	private String	description;

	@Column(name = "DIFFERIBILITA")
	private Integer	differibilita;

	@Column(name = "ELEGGIBILITA")
	private Integer	eleggibilita;

	@Column(name = "FREQUENZA")
	private Integer	frequenza;

	@Column(name = "HASLD")
	private Integer	hasLd;

	@Column(name = "IDATTIVITA")
	private String	idAttivita;

	@Column(name = "IDPOLICY")
	private String	idPolicy;

	@Column(name = "IDTIMING")
	private String	idTiming;

	@Column(name = "LASTREVIEWDATE")
	private Date	lastReviewDate;

	@Column(name = "LIVAGGR")
	private Integer	livAggr;

	@Column(name = "LIVCOMB")
	private Integer	livComb;

	@Column(name = "LIVPOLICY")
	private Integer	livPolicy;

	@Column(name = "MAXANTICIPO")
	private Integer	maxAnticipo;

	@Column(name = "MAXPOSTICIPO")
	private Integer	maxPosticipo;

	@Column(name = "POLICYAGGR")
	private Integer	policyAggr;

	@Column(name = "ROWSTAMP")
	private String	rowStamp;

	@Column(name = "SITEID")
	private String	siteId;

	@Column(name = "STATUS")
	private String	status;

	@Column(name = "TIPOLOGIAASSET")
	private String	tipologiaAsset;

	@Column(name = "TIPOPOLICY")
	private String	tipoPolicy;

	@Column(name = "UM")
	private String	um;

	@Column(name = "X4CLUSTER")
	private String	x4Cluster;
	
	@Column(name = "X4RIFTEMPORALE")
	private String	x4RifTemporale;
	
	@Column(name = "X4SOTTOPROGRAMMA")
	private String	x4SottoProgramma;
	
	@Column(name = "APARTIREDA")
	private Date	aPartireDa;
	
	@Column(name = "VERSION")
	private Integer	version;
	
	@Column(name = "IDMACROATTIVITA")
	private String	idMacroAttivita;
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AX4Policy other = (AX4Policy) obj;
		return Objects.equals(eauditTransId, other.eauditTransId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(eauditTransId);
	}
}
