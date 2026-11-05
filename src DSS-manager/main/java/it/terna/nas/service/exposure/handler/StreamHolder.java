package it.terna.nas.service.exposure.handler;

import java.util.Map;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class StreamHolder<T> implements AutoCloseable {
	
	final private Stream<T> stream;
	
	private Map<String, Integer> mapParentCountChildren;
	
	final private int count;
	
	public StreamHolder(Stream<T> stream, int count) {
		this.count = count;
		this.stream = stream;
	}
	
	public StreamHolder(Stream<T> stream, Map<String, Integer> mapParentCountChildren) {
		this.count = 0;
		this.stream = stream;
		this.mapParentCountChildren = mapParentCountChildren;
	}

	@Override
	public void close() {
		log.info("  - Autoclose of stream");
		this.stream.close();
	}

}
