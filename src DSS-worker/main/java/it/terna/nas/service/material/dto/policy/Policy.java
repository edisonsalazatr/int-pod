package it.terna.nas.service.material.dto.policy;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import it.terna.nas.service.dal.entity.eam.X4PolicyInput;
import it.terna.nas.service.material.utility.Catalog.PolicyTimeUnit;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Policy implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4234597796887182063L;

	private Long id;
	
	private String code;
	
	private String description;
	
	private String macroActivityId;
	
	private String assetType;
	
	private String status;
	
	private String formula;
	
	private String tipoPolicy;
	
	private Integer maxAnticipo;
	
	private int maxPosticipo;
	
	private PolicyTimeUnit timeUnit;
	
	private int differibilita;
	
	private int eleggibilita;
	
	private Integer frequenza;

	private Integer logicaTronco;
	
	private List<Input> inputs;
	
	private List<X4PolicyInput> listPolicyInput;
	
	private Integer hasInputs;
	
	private Date aPartireDa;
	
	private Integer policyAggr; 
	
	@Override
	public int hashCode()
	{	
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Policy other = (Policy) obj;
		return Objects.equals(id, other.id);
	}
}
