package it.terna.nas.service.material.dto.policy;

import java.io.Serializable;

import it.terna.nas.service.material.utility.Catalog.PolicyInputType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@NoArgsConstructor
@ToString
public class CombinationInput implements Input, 
										 Comparable<CombinationInput>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1765571015489574719L;

	private Long id;
	
	private int order;

	private String operator;
	
	private Long[] operands;
	
	private PolicyInputType type;
	
	public CombinationInput(String operator, Long[] operands) {
		this.operator = operator;
		this.operands = operands;
	}
	
	public CombinationInput(Long id, 
							String operator, 
							Long[] operands,
							int order) {
		this.id = id;
		this.order = order;
		this.operator = operator;
		this.operands = operands;
	}

	@Override
	public int compareTo(CombinationInput o) {
		return Integer.compare(order, o.getOrder());
	}
}
