package it.terna.nas.service.exposure.events;

import java.io.Serializable;
import it.terna.kernel.basement.utility.Assorted;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Slf4j
public class WODeleteCAVI implements Serializable
{
	private static final long serialVersionUID = -7746552626723231835L;
	
	private String _action;
	
	private String _event;
	
	private String _id;
	
	private String				href;
	
	
	private String wonum;
	

	public String toJson()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n",
					this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

	/*
	 * User friendly object representation
	 * 
	 * (non-Javadoc) @see java.lang.Object#toString()
	 * 
	 */
	@Override
	public String toString ()
	{
		StringBuilder builder = new StringBuilder();

		builder.append("- Base WS Data - Outer Request - Odl delete CAVI Event Request \n")
			   .append("{\n")
			   .append("  - Values :\n")
			   .append("    [\n")
			   .append("      Wonum                    : |").append(wonum).append("|\n")
			   .append("    ]\n")
			   .append("}\n");
		
		return builder.toString();
	}

}
