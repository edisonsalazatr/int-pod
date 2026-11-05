package it.terna.nas.service.dal.entity.eam;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4POLICYAGGREGATEKP")
@Getter
@Setter
@NoArgsConstructor
public class X4PolicyAggregateKP
{

	@Id
	@Column(name = "X4POLICYAGGREGATEKPID")
	private Long	id;

	@Column(name = "HASLD")
	private Integer	hasLd;

	@Column(name = "ROWSTAMP")
	private String	rowStamp;

	@Column(name = "TIPOLOGIAASSET")
	private String	tipologiaAsset;
	
	@Column(name = "KP")
	private BigDecimal	kp;
	
	@Column(name = "PARAMELEG")
	private BigDecimal	paramEleg;
	
	@Column(name = "SELEG")
	private BigDecimal	sEleg;

	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4PolicyAggregateKP other = (X4PolicyAggregateKP) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}

}
