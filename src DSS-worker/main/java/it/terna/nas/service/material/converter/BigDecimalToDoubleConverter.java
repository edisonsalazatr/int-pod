package it.terna.nas.service.material.converter;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BigDecimalToDoubleConverter implements Converter<BigDecimal, Double> {
	
	@Override
	public Double convert(BigDecimal source) {
		if(source == null) {
			log.error(" - Cannot convert null BigDecimal !!!");
		}
		
		return source.doubleValue();
	}
}
