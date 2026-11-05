package it.terna.nas.service.material.converter;

public interface Converter<S, T> {
	
	T convert(S source);
}
