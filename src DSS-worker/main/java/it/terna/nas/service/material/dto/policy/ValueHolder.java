package it.terna.nas.service.material.dto.policy;

import it.terna.nas.service.material.dto.policy.types.PolicyValue;

public class ValueHolder<T> implements PolicyValue<T> {
	
	private String key;
	private String description;
	private T value;

	public ValueHolder(String key, String description, T value) {
		this.key = key;
		this.description = description;
		this.value = value;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public T getValue() {
		return value;
	}
	
	public void setValue(T newVal) {
		this.value = newVal;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "ValueHolder [key=" + key + ", description=" + description + ", value=" + value + "]";
	}
}
