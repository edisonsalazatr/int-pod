package it.terna.nas.service.exposure.events;

import java.util.List;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Slf4j
public class WorkOrderCaviInterrati extends ExposureRequest 
{
	private static final long serialVersionUID = -7746552626723231835L;
	
	private String				href;
	
	private String assetnum;
	
	private String changedate;
	
	private String targstartdate;
	
	private String jpnum;
	
	private String parent;
	
	private String wonum;
	
	private String siteid;
	
	private String status;
	
	private String uuid;
	
	private String x4tronconum;
	
	private String x3ingrp;
	
	private String x3gewrk;
	
	private String x4sapidasset;
	
	private List<WorkOrderCaviInterrati> workorder;



	/**
	 * Method used to get unique identifier of passed event data
	 * 
	 * @return
	 */
	public String getEventUUID()
	{
		return uuid;
	}

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

		builder.append("- Base WS Data - Outer Request - Odl tp CAVI Event Request \n")
			   .append("{\n")
			   .append(getExposureRequestForLog())
			   .append("  - Values :\n")
			   .append("    [\n")
			   .append("      Correlation Key          : |").append(getNasTransactionId()).append("|\n")
			   .append("      Assetnum                 : |").append(assetnum).append("|\n")
			   .append("      Wonum                    : |").append(wonum).append("|\n")
			   .append("      jpnum                    : |").append(jpnum).append("|\n")
			   .append("      Parent                   : |").append(parent).append("|\n")
			   .append("    ]\n")
			   .append("}\n");
		
		return builder.toString();
	}

}
