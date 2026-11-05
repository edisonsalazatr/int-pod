package it.terna.nas.service.exposure.ws.maximo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorData {
	
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
