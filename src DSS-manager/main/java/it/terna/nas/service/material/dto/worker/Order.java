package it.terna.nas.service.material.dto.worker;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import it.terna.nas.service.material.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
    private String                  tronconum;	
	private String					x4Trainante;
	private List<String>		  	assetRelated;
	private String				  	x4Note;
	private String				  	x4OrdinePrevalente;
	private Boolean                 toCreate;
	private Boolean				  	prevalent;   
	private String					wonum;
	private String					x4MicroControllo;
	private String                  sapIdAsset;
	private Integer                 x4aggregati;
	private Integer                 x4outsourcing;
	
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
        this.sapIdAsset = other.sapIdAsset;
        this.x4aggregati = other.x4aggregati;
        this.x4outsourcing = other.x4outsourcing;
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
}

