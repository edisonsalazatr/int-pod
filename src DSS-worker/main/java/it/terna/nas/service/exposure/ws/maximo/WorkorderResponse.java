package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import it.terna.kernel.basement.utility.Assorted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WorkorderResponse extends MaximoResponse {

	private static final long serialVersionUID = 7948086949715533325L;
	
	private boolean apptrequired;
	private boolean historyflag;
	private boolean x4aggregati;
	private String status;
	private String assetnum;
	private String changeby;
	private String statusdate;
	private String wonum;
	private String href;
	private int workorderid;
	private String jpnum;
	private String reportdate;
	private String description;
	private boolean x3trainante;
	
	@SerializedName("Error")
    @JsonProperty("Error")
	private ErrorData errorData;

	@Getter
	@Setter
	public static class ErrorData {
		private String errorattrname;
		private String errorobjpath;
		private String correlationid;
		private String reasonCode;
		private String message;
		private String statusCode;
		private ExtendedError extendedError;

		@Getter
		@Setter
		public static class ExtendedError {
			private MoreInfo moreInfo;

			@Data
			public static class MoreInfo {
				private String href;
			}
		}
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
