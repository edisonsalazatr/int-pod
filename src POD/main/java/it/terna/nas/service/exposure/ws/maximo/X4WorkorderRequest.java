package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class X4WorkorderRequest extends ExposureRequest
{
	private static final long	serialVersionUID	= 4657045449897549352L;
	private String href;
	private String wonum;
	private String status;
	private String changedate;
	private String x4statogeocall;
	private String x3notechiusura;
	private Boolean x4geocallclosed;
	private Boolean woeq3;
	private String x4stornogeocall;
	private String x4errsap;
	
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
