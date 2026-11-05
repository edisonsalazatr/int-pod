package it.terna.nas.service.material.converter;

public interface PolicyConverterResolver {
	
	<S> FromStep<S> from(S source);
	
	interface FromStep<S> {
		<T> T to(Class<T> targetType);
	}

}
