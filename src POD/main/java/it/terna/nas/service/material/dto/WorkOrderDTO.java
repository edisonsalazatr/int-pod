package it.terna.nas.service.material.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkOrderDTO implements Serializable
{
	private static final long	serialVersionUID	= -5887807973382721846L;
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
	private String 				x4statusindisp;
	private String              x4clusterindisp;
	private Boolean				x4aggregati;
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
	private String				x4respasset;
	private List<WorkOrderDTO> 	workorder;
}
