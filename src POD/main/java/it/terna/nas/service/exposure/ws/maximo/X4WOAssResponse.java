package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.List;
import it.terna.kernel.basement.utility.Assorted;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class X4WOAssResponse extends MaximoResponse
{
	@Getter @Setter @ToString
	public static class MemberType implements Serializable 
	{
		private static final long serialVersionUID = -6659449187702619314L;
		private List<AssignmentType> assignment;
		private String href;
		private Boolean istask;
		private String jpnum;
		private String lead;
		private String orgid;
		private String schedfinish;
		private String schedstart;
		private String siteid;
		private String status;
		private String statusdate;
		private String woclass;
		private String wonum;
	}
	
	private static final long serialVersionUID = 6967251629688775952L;
	private String href;
	private List<MemberType> member;
	
	@Override
	public String toJson()
	{
		try {
			return Assorted.toJson(this);
		}
		catch (Exception ex) {
			//log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("X4WOAssResponse [member=");
		builder.append(member);
		builder.append(", getApikey()=");
		builder.append(getApikey());
		builder.append(", getErrorInfo()=");
		builder.append(getErrorInfo());
		builder.append("]");
		return builder.toString();
	}
}
