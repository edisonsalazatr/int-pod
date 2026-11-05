package it.terna.nas.service.exposure.ws.maximo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class WorkorderGetResponse extends MaximoResponse 
{	

	private static final long serialVersionUID = 2445510277989844919L;
	private List<WorkorderResponseItem> member;
	
	@JsonInclude(Include.NON_NULL)
	@ToString
	@Getter
	@Setter
	public static class WorkorderResponseItem implements Serializable
	{

		private static final long serialVersionUID = 6163505464649180398L;
		private String href;
		private String wonum;
		private String status;
		private String changedate;
		private String assetnum;
	    private String jpnum;
	    private Date targcompdate;
	    private Date targstartdate;
	}
	
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
