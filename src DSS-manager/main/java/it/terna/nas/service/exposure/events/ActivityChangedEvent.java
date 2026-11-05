package it.terna.nas.service.exposure.events;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class ActivityChangedEvent extends ExposureRequest {

	private static final long serialVersionUID = 8745184269636845320L;
	
	private String jpnum;
	private String description;
	private BigDecimal jpduration;
	private String orgid;
	private String siteid;
	private Long jobplanid;
	private String langcode;
	private String status;
	private String interruptible;
	private String classstructureid;
	private String x4tipologiaasset;
	private String x4programma;
	
	@Override
	public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
	
}

