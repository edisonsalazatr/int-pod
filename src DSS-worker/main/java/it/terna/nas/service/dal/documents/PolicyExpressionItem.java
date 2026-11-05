package it.terna.nas.service.dal.documents;

import it.terna.nas.service.material.dto.policy.PolicyOperator;
import it.terna.nas.service.material.dto.policy.PolicyVariable;
import it.terna.nas.service.material.dto.policy.types.PolicyValue;
import it.terna.nas.service.material.utility.Catalog.PolicyInputType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class PolicyExpressionItem {
	
	private Long policyInputId;
	
	private String formulaId;
	
	private PolicyOperator operator;
	
	@Setter(AccessLevel.NONE)
	private Integer alias;
	
	private PolicyInputType type;
	
	//
	private PolicyVariable leftVar;
	private PolicyVariable rightVar;
	
	private PolicyValue<?> leftVal;
	private PolicyValue<?> targetVal;
	
	private String expressionFormula;
	private Boolean expressionResult;

}
