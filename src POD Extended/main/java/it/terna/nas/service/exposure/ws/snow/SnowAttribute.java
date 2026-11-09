package it.terna.nas.service.exposure.ws.snow;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
@Slf4j
public class SnowAttribute extends ExposureRequest{
	
	private static final long serialVersionUID = 3100609668903619321L;
	
	private String assetAttrId;
    private String value;

	@Override
	public String toJson() {
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

}
