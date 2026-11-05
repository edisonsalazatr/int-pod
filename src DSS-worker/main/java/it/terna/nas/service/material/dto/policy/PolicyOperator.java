package it.terna.nas.service.material.dto.policy;

import lombok.Getter;

public enum PolicyOperator {
	
	LESS_THAN 			("<", 				"<"),
	LESS_THAN_EQUAL 	("<=", 				"<="),
	EQUAL 				("=", 				"=="),
	NOT_EQUAL 			("!=", 				"!="),
	GREATER_THAN    	(">", 				">"),
	GREATER_THAN_EQUAL 	(">=", 				">="),
	EXIST          		("EXIST", 			""),
	NOT_EXIST           ("NOT EXIST", 		""),
	OP_OR     			("OR", 				"||"),
	OP_AND    			("AND", 			"&&"),
	PARI    			("PARI", 			""),
	DISPARI    			("DISPARI", 		""),
	PARI_DISPARI    	("PariOrDispari", 	"")
	;
	
	@Getter
	private final String symbol;
	
	@Getter
	private final String supportedSymbol;
	
	private PolicyOperator(String symbol, String supportedSymbol) {
		this.symbol = symbol;
		this.supportedSymbol = supportedSymbol;
	}
	
	public static PolicyOperator getBySymbol(String symbol) {
		for(PolicyOperator op : PolicyOperator.values()) {
			if(op.getSymbol().equals(symbol)) {
				return op;
			}
		}
		return null;
	}
	
}
