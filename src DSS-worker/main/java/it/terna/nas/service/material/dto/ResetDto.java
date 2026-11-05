package it.terna.nas.service.material.dto;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class ResetDto extends ExposureResponse {

	private static final long serialVersionUID = 1L;
	
	private int wostatus;
	private int woancestors;
	private int ownerHistories;
	private int aWorkorders;
	private int workorders;
	
	@Override
	public String toJson ()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			// log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
	
	
}
