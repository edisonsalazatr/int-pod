package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter @Setter
public class JwtTokenResponse extends ExposureResponse
{

	private static final long	serialVersionUID	= 8729890776457021736L;

	@JsonProperty("token_type")
	private String				tokenType;

	@JsonProperty("expires_in")
	private Integer				expiresIn;

	@JsonProperty("ext_expires_in")
	private Integer				extExpiresIn;

	@JsonProperty("access_token")
	private String				accessToken;

	@JsonProperty("code")
	private String				code;

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
