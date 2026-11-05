package it.terna.nas.service.material.dto.policy.types;

public interface PolicyValue<T> {
	
	public String getKey();
	
	public T getValue();
	
	public void setValue(T newVal);
	
	public String getDescription();

}
