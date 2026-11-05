package it.terna.nas.service.material.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DoubleToBigDecimalConverter implements Converter<Double, BigDecimal> {
	
	@Override
	public BigDecimal convert(Double source) {
		if(source == null) {
			log.error(" - Cannot convert null Double !!!");
		}
		
		return BigDecimal.valueOf(source);
	}
}
