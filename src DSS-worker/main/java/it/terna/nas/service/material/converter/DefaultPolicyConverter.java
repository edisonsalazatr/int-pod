package it.terna.nas.service.material.converter;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.material.utility.Constants;

@Configuration
public class DefaultPolicyConverter {

	@Bean
	public Converter<String, Date> stringToDate(){
		return new Converter<String, Date>() {
			@Override
			public String toString() {
				return "StringToDateConverter";
			}
			@Override
			public Date convert(String source) {
				return Assorted.stringToDate(source, Constants.FORMAT_DATA_BASIC_ISO_8601);
			}
		};
	}
	
	@Bean
	public Converter<Date, String> dateToString(){
		return new Converter<Date, String>() {
			@Override
			public String toString() {
				return "DateToStringConverter";
			}
			@Override
			public String convert(Date source) {
				
				return Assorted.dateToString(source, Constants.FORMAT_DATA_BASIC_ISO_8601);
			}
		};
	}
	
	@Bean
	public Converter<Timestamp, Date> timestampToDate(){
		return new Converter<Timestamp, Date>() {
			@Override
			public String toString() {
				return "TimestampToDateConverter";
			}
			@Override
			public Date convert(Timestamp source) {
				return new Date(source.getTime());
			}
		};
	}
	
	@Bean
	public Converter<Date, Timestamp> dateToTimestamp(){
		return new Converter<Date, Timestamp>() {
			
			@Override
			public String toString() {
				return "DateToTimestampConverter";
			}
			
			@Override
			public Timestamp convert(Date source) {
				return new Timestamp(source.getTime());
			}
		};
	}
}
