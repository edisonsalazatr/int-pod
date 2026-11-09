package it.terna.nas.service.exposure.ws.maximo;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class MaximoResponse extends AbstractMaximoResponse 
{	/**
	 * 
	 */
	private static final long serialVersionUID = -1657272493445796856L;

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
		StringBuilder builder = new StringBuilder();
		builder.append("Apikey=");
		builder.append(getApikey());
		if (Assorted.notNull(getErrorInfo()))
		{
			builder.append(", errorattrname=").append(getErrorInfo().getErrorattrname());
			builder.append(", message=").append(getErrorInfo().getMessage());
			builder.append(", reasonCode=").append(getErrorInfo().getReasonCode());
			builder.append(", statusCode=").append(getErrorInfo().getStatusCode());
		}
		
		return builder.toString();
	}
}
