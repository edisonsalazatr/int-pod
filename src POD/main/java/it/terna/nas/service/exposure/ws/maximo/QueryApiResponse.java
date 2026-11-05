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
public class QueryApiResponse extends AbstractMaximoResponse 
{
	private static final long serialVersionUID = 6967251629688775952L;
	private List<HrefInfo> member;
	private String href;
	private HrefInfo responseInfo;
	
	@Override
	public String toJson()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
	
	
	@Getter @Setter @ToString
	public static class HrefInfo implements Serializable 
	{
		private static final long serialVersionUID = -6659449187702619314L;
		private String href;
	}

		

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("QueryApiResponse [apikey=");
		builder.append(getApikey());
		builder.append(", member size =");
		builder.append(Assorted.isNotEmpty(member) ? member.size() : 0);
		builder.append(", error=");
		builder.append(getErrorInfo());
		builder.append("]");
		return builder.toString();
	}
}
