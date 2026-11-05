package it.terna.nas.service.material.converter;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class IdentityConverter<S, T extends S> implements Converter<S, T> {
	
	@SuppressWarnings("unchecked")
	@Override
	public T convert(S source) {
		// No conversion at all
		log.debug(" IdentityConverter: {} -> {}. Return source as targer", 
													source.getClass().getSimpleName(),
													((Class<T>)(source.getClass())).getSimpleName());
		return (T) source;
	}

}
