package it.terna.nas.service.exposure.ws.maximo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class X4IntOdLGeoCallResponse extends MaximoResponse 
{	
	private static final long serialVersionUID = -1657272493445796856L;
	private String				wonum;
	private Boolean				istask;
	private String				orgid;
	private String				siteid;
	private String				description;
	private String				status;
	private String				status_description;
	private String				jpnum;
	private String				assetnum;
	private Double				estdur;
	private String				actstart;
	private String				actfinish;
	private String				x3naturalavoro;
	private String				x3datamodello;
	private String				targstartdate;
	private String				targfinishdate;
	private String				x3dataottinizio;
	private String				x3dataottfine;
	private String				sneconstraint;
	private String				fnlconstraint;
	private Double				x3tprep;
	private Double				x3toper;
	private Double				x3ttrasf;
	private String				x4tipospesa;
	private String				x4coin;
	private String				x4wbe;
	private String				x4wbepian;
	private String				x4wbs;
	private String				x4tipoattivita;
	private String				x4tipoordine;
	private String				x4odm;
	private String				x4ntk;
	private String				x4nordineinterno;
	private String 				x4coddelindisp; 
	private String				x4dinizioindisp;
	private String				x4dfineindisp;
	private String				x4sapidasset;
	private String				x4titris;
	private String              x4statusindisp;
	private Integer				wopriority;
	private String				externalrefid;
	private String				schedstart;
	private String				schedfinish;
	private String				predecessorwos;
	private String				lead;
	private String				parent;
	private String				href;
	private String				woclass;
	private String				woclass_description;
	private String				newchildclass;
	private Boolean				haschildren;
	private List<X4IntOdLGeoCallResponse> 	workorder;
	
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
