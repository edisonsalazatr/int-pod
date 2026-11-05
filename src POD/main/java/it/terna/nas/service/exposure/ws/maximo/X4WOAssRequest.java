package it.terna.nas.service.exposure.ws.maximo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X4WOAssRequest extends MaximoResponse 
{
	private static final long serialVersionUID = 6736984139441474683L;
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
		builder.append("X4WOAssRequest [assignment=");
		builder.append(assignment);
		builder.append(", istask=");
		builder.append(istask);
		builder.append(", jpnum=");
		builder.append(jpnum);
		builder.append(", lead=");
		builder.append(lead);
		builder.append(", schedfinish=");
		builder.append(schedfinish);
		builder.append(", schedstart=");
		builder.append(schedstart);
		builder.append(", status=");
		builder.append(status);
		builder.append(", statusdate=");
		builder.append(statusdate);
		builder.append(", woclass=");
		builder.append(woclass);
		builder.append(", wonum=");
		builder.append(wonum);
		builder.append(", getApikey()=");
		builder.append(getApikey());
		builder.append(", getErrorInfo()=");
		builder.append(getErrorInfo());
		builder.append("]");
		return builder.toString();
	}
}
