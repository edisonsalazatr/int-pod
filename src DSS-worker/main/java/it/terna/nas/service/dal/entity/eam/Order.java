package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonFormat;
import it.terna.kernel.basement.utility.Assorted;
import static it.terna.kernel.basement.utility.Assorted.*;
import it.terna.nas.service.exposure.ws.maximo.WorkorderRequest;
import it.terna.nas.service.material.dto.TroncoInfo;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class Order implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = -7632632329716572040L;
    private String                  assetnum;
	private String                  stato;
	private String                  jpnum;
	private Long					x4PolicyId;
	private String					idPolicy;
	private String 					parent;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_JSON_FORMAT, timezone = Constants.TZ_ITALY)
	private Date                    dataModello;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_JSON_FORMAT, timezone = Constants.TZ_ITALY)
	private Date                    dataPianificataInizio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_JSON_FORMAT, timezone = Constants.TZ_ITALY)
	private Date                    dataPianificataFine;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_JSON_FORMAT, timezone = Constants.TZ_ITALY)
	private Date                    dataMaxPosticipo;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_JSON_FORMAT, timezone = Constants.TZ_ITALY)
	private Date                    dataNonPrimaDi;
	private List<String>			tesatura;
	private String 				  	tronconum;
	private String					x4Trainante;
	private List<String>		  	assetRelated;
	private String				  	x4Note;
	private String				  	x4OrdinePrevalente;
	private Boolean				  	toCreate;
	private Boolean				  	prevalent;   
	private String					wonum;
	private String					x4MicroControllo;
	private List<TroncoInfo>        listInfoTronco;
	private String                  sapIdAsset;
	private Integer                 x4aggregati;
	private String                  x4tipoodl;
	private Boolean					x4datemod;
	private Integer                 x4outsourcing;
	private String                  x4naturalavoro;
	private String 					x4respasset;
	private Set<String>             troncoIntersection;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_JSON_FORMAT, timezone = Constants.TZ_ITALY)
	private Date                    primaEleggibilita;
		
	public static WorkorderRequest prepareWorkorderRequest(Order order) 
	{
	    WorkorderRequest request = new WorkorderRequest();
	    request.setAssetnum(Assorted.notNull(order.getAssetnum()) ? order.getAssetnum() : null);
	    request.setJpnum(Assorted.notNull(order.getJpnum()) ? order.getJpnum() : null);
	    request.setX4datamodello(Assorted.notNull(order.getDataModello()) ? Assorted.dateToString(order.getDataModello(), Constants.MAXIMO_DATE_AND_TIME) : null);
	    
	    List<Date> dates = Arrays.asList(order.getDataNonPrimaDi(), order.getDataPianificataInizio());
	    Date min = dates.stream()
                .filter(Objects::nonNull)
                .min(Date::compareTo)
                .orElse(null);
	    request.setSneconstraint(Assorted.notNull(min) 
	    							? Assorted.dateToString(min, Constants.MAXIMO_DATE_AND_TIME) 
	    							: null);
	    request.setTargstartdate(Assorted.notNull(order.getDataPianificataInizio()) 
	    							? Assorted.dateToString(order.getDataPianificataInizio(), Constants.MAXIMO_DATE_AND_TIME) 
	    							: null);
	    
	    request.setFnlconstraint(Assorted.notNull(order.getDataMaxPosticipo()) ? Assorted.dateToString(order.getDataMaxPosticipo(), Constants.MAXIMO_DATE_AND_TIME) : null);
	    request.setTargcompdate(Assorted.notNull(order.getDataPianificataFine()) ? Assorted.dateToString(order.getDataPianificataFine(), Constants.MAXIMO_DATE_AND_TIME) : null);
	    request.setSiteid(Constants.SITEID);
	    request.setX4aggregati(Assorted.notNull(order.getX4aggregati()) ? order.getX4aggregati() : 0);
	    request.setX4policyid(Assorted.notNull(order.getX4PolicyId()) ? order.getX4PolicyId() : null);
	    request.setX4policy(Assorted.notNull(order.getIdPolicy()) ? order.getIdPolicy() : null);
	    request.setX4trainante(Assorted.notNull(order.getX4Trainante()) ? order.getX4Trainante() : null);
	    request.setX3trainante(Assorted.notNull(order.getX4Trainante()) ? (order.getX4Trainante() != null ? 1 : 0) : null);
	    request.setParent(Assorted.notNull(order.getParent()) ? order.getParent() : null);
	    request.setX4microcontrollo(Assorted.notNull(order.getX4MicroControllo()) ? order.getX4MicroControllo() : null);
	    request.setX4tronconum(Assorted.notNull(order.getTronconum()) ? order.getTronconum() : null);
	    request.setStatus(order.getStato());
	    request.setX4note(order.getX4Note());
	    request.setX4sapidasset(order.getSapIdAsset());
	    request.setX4ordineprevalente(order.getX4OrdinePrevalente());
	    request.setX4tipoodl(order.getX4tipoodl());
	    request.setX4datemod(Assorted.notNull(order.getX4datemod()) ? (order.getX4datemod() ? 1 : 0) : null);
	    request.setX4dss(WorkOrderStatus.NON_ESEGUITO.getCode().equals(order.getStato()) ? 1 : 0);
	    
	    request.setX4primaeleggibilita(Assorted.notNull(order.getPrimaEleggibilita()) 
				? Assorted.dateToString(order.getPrimaEleggibilita(), Constants.MAXIMO_DATE_AND_TIME) 
				: null);

	    if (Boolean.TRUE.equals(order.getToCreate()))
	        request.setX4burks(Constants.CLIENTE_DEFAULT);

	    if (Boolean.TRUE.equals(order.getToCreate()) && WorkOrderStatus.NON_ESEGUITO.getCode().equals(order.getStato()) && Constants.HAS_OUTSOURCING.equals(order.getX4outsourcing()) && isEmpty(order.getX4naturalavoro())) 
	    {
	        request.setX4naturalavoro(Constants.X4NATURALAVORO_DSS);
	    }
	    
	    request.setX4respasset(Assorted.notNull(order.getX4respasset()) ? order.getX4respasset() : null);
	    log.debug("Prepared WorkorderRequest for order: {}", request);
	    return request;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(wonum);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(wonum, other.wonum);
	}
	
	public Order(Order other) 
	{
        this.assetnum = other.assetnum;
        this.stato = other.stato;
        this.jpnum = other.jpnum;
        this.x4PolicyId = other.x4PolicyId;
        this.idPolicy = other.idPolicy;
        this.parent = other.parent;
        this.dataModello = other.dataModello != null ? new Date(other.dataModello.getTime()) : null;
        this.dataPianificataInizio = other.dataPianificataInizio != null ? new Date(other.dataPianificataInizio.getTime()) : null;
        this.dataPianificataFine = other.dataPianificataFine != null ? new Date(other.dataPianificataFine.getTime()) : null;
        this.dataMaxPosticipo = other.dataMaxPosticipo != null ? new Date(other.dataMaxPosticipo.getTime()) : null;
        this.dataNonPrimaDi = other.dataNonPrimaDi != null ? new Date(other.dataNonPrimaDi.getTime()) : null;
        this.tesatura = other.tesatura != null ? new ArrayList<>(other.tesatura) : null;
        this.tronconum = other.tronconum;
        this.x4Trainante = other.x4Trainante;
        this.assetRelated = other.assetRelated != null ? new ArrayList<>(other.assetRelated) : null;
        this.x4Note = other.x4Note;
        this.x4OrdinePrevalente = other.x4OrdinePrevalente;
        this.toCreate = other.toCreate;
        this.prevalent = other.prevalent;
        this.wonum = other.wonum;
        this.x4MicroControllo = other.x4MicroControllo;
        this.listInfoTronco = other.listInfoTronco;
        this.sapIdAsset = other.sapIdAsset;
        this.x4aggregati = other.x4aggregati;
        this.x4tipoodl = other.x4tipoodl;
        this.x4datemod = other.x4datemod;
        this.x4outsourcing = other.x4outsourcing;
        this.x4naturalavoro = other.x4naturalavoro;
        this.x4respasset = other.x4respasset;
        this.primaEleggibilita = other.primaEleggibilita != null ? new Date(other.primaEleggibilita.getTime()) : null;
	}
}

