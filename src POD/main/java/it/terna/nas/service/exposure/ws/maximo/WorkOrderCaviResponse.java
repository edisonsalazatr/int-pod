package it.terna.nas.service.exposure.ws.maximo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class WorkOrderCaviResponse extends MaximoResponse 
{	
	private static final long serialVersionUID = -1657272493665796856L;
	
	private String href;
	private String wonum;
	private String status;
	private String changedate;
	private String assetnum;
	
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
