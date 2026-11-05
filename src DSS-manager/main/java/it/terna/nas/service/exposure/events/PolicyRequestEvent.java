package it.terna.nas.service.exposure.events;

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
public class PolicyRequestEvent extends ExposureRequest
{
	private static final long	serialVersionUID	= 2461714270976018615L;

	private String				notify;
	private String				assetnum;
	private Long				policy;
	private String 				type;
	private String 				event;
	private String 				tipology;
	private String				correlationId;

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
