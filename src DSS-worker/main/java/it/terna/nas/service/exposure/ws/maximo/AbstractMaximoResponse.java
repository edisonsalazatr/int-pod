package it.terna.nas.service.exposure.ws.maximo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import it.terna.kernel.service.exposure.ExposureResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractMaximoResponse extends ExposureResponse 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4657045449897549352L;
	
	private String apikey;
	
	@SerializedName("Error")
	@JsonProperty("Error")
	private ErrorInfo errorInfo;
	
	@Getter @Setter @ToString
	public static class ErrorInfo implements Serializable 
	{
		private static final long serialVersionUID = -6659449187702619314L;
		private String errorattrname;
		private String message;
		private String reasonCode;
		private String statusCode;
	}
}
