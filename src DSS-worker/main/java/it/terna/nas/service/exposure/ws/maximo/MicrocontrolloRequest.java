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
public class MicrocontrolloRequest extends ExposureRequest
{
	private static final long	serialVersionUID	= 4675045449897549712L;
	
	private String 	parent;
	private String 	assetnum;
    private String 	description;
    private String 	siteid;
    private String 	classstructureid;
    private String 	x4microcontrollo;
    private Integer	taskid;
    private String 	woclass;
    private String 	istask;
    
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

	
    @Override
    public String toString()
    {
       return toJson();
    }
}
