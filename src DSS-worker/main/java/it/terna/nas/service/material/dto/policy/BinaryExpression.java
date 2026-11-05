package it.terna.nas.service.material.dto.policy;

import java.io.Serializable;

import it.terna.nas.service.material.utility.Catalog.PolicyInputType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BinaryExpression implements Input, Comparable<BinaryExpression>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2189361167403173908L;

	private Long id;
	
	private PolicyOperator operator;
	
	@Setter(AccessLevel.NONE)
	private Integer alias;
	
	private PolicyInputType type;
	
	//
	private PolicyVariable leftVar;
	private PolicyVariable rightVar;

	@Override
	public int compareTo(BinaryExpression o) {
		return Integer.compare(alias, o.alias);
	}

	public void setAlias(Integer alias) {
		this.alias = alias;
	}

	@Override
	public String toString() {
		return "BinaryExpression [id=" + id  + ", operator=" + operator
				+ ", alias=" + alias + ", type=" + type + ", leftVar=" + leftVar + ", rightVar=" + rightVar + "]";
	}
	
}
