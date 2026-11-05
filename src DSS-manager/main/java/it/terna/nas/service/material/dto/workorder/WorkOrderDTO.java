package it.terna.nas.service.material.dto.workorder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
	private String				x4coddelindisp;
	private String				x4dinizioindisp;
	private String				x4dfineindisp;
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
	private Long				x4policyid;
	private String              x4policy;
	private String				x4tronconum;
	private String              x4noperazione;
	private String              x4stornosap;
	private String              x4statusindisp;
	private String              x4wbspian;
	private String              x4tipoodl;
	private String              x4tecmig;
	private String              x4sapidasset;
	private String              x4preownergroup;
	private String              x4perimetroint;
	private String              x4clusterindisp;
	private String              x4burks;
	private Boolean				x4outsourcing;
	private Boolean             x4aggregati;
	private String              x4note;
	private String              x4ordineprevalente;
	private String				x4respasset;
	
	@SerializedName("_action")
	@JsonProperty("_action")
	private String				action;
	
    @SerializedName("_event")
    @JsonProperty("_event")
    private String              event;

    @SerializedName("_id")
    @JsonProperty("_id")
    private String              id;
    private Integer             pluscjprevnum;
    private String              targcompdate;
	private String              changedate;
    private String              ownerGroup;
    private String              wolo1;
    private List<AssetLocKafka> assetlocrelation;
    private List<WorkOrderDTO>  workorder;
    
    @Getter @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AssetLocKafka {
        private String x3sapid;
    }
}
