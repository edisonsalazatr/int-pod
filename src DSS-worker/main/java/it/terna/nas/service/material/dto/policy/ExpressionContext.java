package it.terna.nas.service.material.dto.policy;

import java.util.Date;
import it.terna.nas.service.material.dto.policy.types.PolicyValue;
import it.terna.nas.service.material.utility.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpressionContext {
	
	public final static String OPERAND_LEFT = "LEFT";
	public final static String OPERAND_RIGHT = "RIGHT";
	
	private BinaryExpression expression;
	
	private PolicyValue<?> leftVal;
	private PolicyValue<?> targetVal;
	
	private String formula;

	private Boolean result;
	
	public ExpressionContext(BinaryExpression expression) {
		this.expression = expression;
	}

	public BinaryExpression getExpression() {
		return expression;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public void setExpression(BinaryExpression expression) {
		this.expression = expression;
	}
	
	public String writeFormula() {
		
	    if (isOperatorNotDefined())
	        return "false";
	    else if (isDateExpression()) 
			return writeDateFormula();
		
		PolicyOperator operator = expression.getOperator();
		if(operator == PolicyOperator.EQUAL 
				|| operator == PolicyOperator.NOT_EQUAL
				|| operator == PolicyOperator.GREATER_THAN_EQUAL
				|| operator == PolicyOperator.GREATER_THAN
				|| operator == PolicyOperator.LESS_THAN
				|| operator == PolicyOperator.LESS_THAN_EQUAL){
			formula = String.format("#%s?.value %s #%s?.value", OPERAND_LEFT, 
												operator.getSupportedSymbol(),
												OPERAND_RIGHT);
		} else if(operator == PolicyOperator.EXIST) {
			formula = String.format("#%s != null", OPERAND_LEFT);
		} else if(operator == PolicyOperator.NOT_EXIST) {
			formula = String.format("#%s == null", OPERAND_LEFT);
		} else if(operator == PolicyOperator.PARI) {
			formula = String.format("(#%s.value %% 2) == 0", OPERAND_LEFT);
		} else if(operator == PolicyOperator.DISPARI) {
			formula = String.format("(#%s.value %% 2) != 0", OPERAND_LEFT);
		} else if(operator == PolicyOperator.PARI_DISPARI) {
			formula = String.format("#%s == #%s", OPERAND_LEFT, OPERAND_RIGHT);
		} 
		
		return formula;
	}
	
	
	public String writeDateFormula() {
		PolicyOperator operator = expression.getOperator();

		return switch (operator) {
	        case EQUAL -> String.format(
	            "#%s?.value != null and #%s?.value != null ? #%s.value.equals(#%s.value) : false",
	            OPERAND_LEFT, OPERAND_RIGHT, OPERAND_LEFT, OPERAND_RIGHT);
	
	        case NOT_EQUAL -> String.format(
	            "#%s?.value != null and #%s?.value != null ? !#%s.value.equals(#%s.value) : false",
	            OPERAND_LEFT, OPERAND_RIGHT, OPERAND_LEFT, OPERAND_RIGHT);
	
	        case LESS_THAN -> String.format(
	            "#%s?.value != null and #%s?.value != null ? #%s.value.before(#%s.value) : false",
	            OPERAND_LEFT, OPERAND_RIGHT, OPERAND_LEFT, OPERAND_RIGHT);
	
	        case LESS_THAN_EQUAL -> String.format(
	            "#%s?.value != null and #%s?.value != null ? !#%s.value.after(#%s.value) : false",
	            OPERAND_LEFT, OPERAND_RIGHT, OPERAND_LEFT, OPERAND_RIGHT);
	
	        case GREATER_THAN -> String.format(
	            "#%s?.value != null and #%s?.value != null ? #%s.value.after(#%s.value) : false",
	            OPERAND_LEFT, OPERAND_RIGHT, OPERAND_LEFT, OPERAND_RIGHT);
	
	        case GREATER_THAN_EQUAL -> String.format(
	            "#%s?.value != null and #%s?.value != null ? !#%s.value.before(#%s.value) : false",
	            OPERAND_LEFT, OPERAND_RIGHT, OPERAND_LEFT, OPERAND_RIGHT);
	
	        default -> throw new UnsupportedOperationException("Unsupported operator: " + operator);
        };
	}
	
	
	public boolean isDateExpression() 
	{
		return leftVal != null && leftVal.getValue() instanceof Date;
	}
	

	public boolean isOperatorNotDefined()
	{
	    return (leftVal != null && Constants.NOT_DEFINED.equals(leftVal.getKey())) || (targetVal != null && Constants.NOT_DEFINED.equals(targetVal.getKey()));
	}
}
