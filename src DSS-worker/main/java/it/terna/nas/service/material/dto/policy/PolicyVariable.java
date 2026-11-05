package it.terna.nas.service.material.dto.policy;

import java.io.Serializable;

import it.terna.nas.service.material.utility.Catalog.VariableType;
import lombok.Data;

@Data
public class PolicyVariable implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8078224378196468154L;

	private String name;
		
	private VariableType type;
	
	private String rawValue;
	
	
	public PolicyVariable() {
		
	}
	
	public PolicyVariable(String name, VariableType type) {
		this.name = name;
		this.type = type;
	}
	
	public PolicyVariable(PolicyVariableBuilder builder) {
		this.name = builder.name;
		this.type = builder.type;
		this.rawValue = builder.rawValue;
	}
	

	public static class PolicyVariableBuilder{

		private String name;
		private VariableType type;
		private String rawValue;
		
		public PolicyVariableBuilder name(String name) {
			this.name = name;
			return this;
		}
		
		public PolicyVariableBuilder type(VariableType type) {
			this.type = type;
			return this;
		}
		
		public PolicyVariableBuilder rawValue(String value) {
			this.rawValue = value;
			return this;
		}
		
		public PolicyVariable build() {
			return new PolicyVariable(this);
		}
	}
}
