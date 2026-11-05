package it.terna.nas.service.exposure.ws.maximo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.material.dto.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class WorkorderRequest extends ExposureRequest
{
	private static final long	serialVersionUID	= 4657045449897549352L;
    private String 	href;
    private String 	_action;
    private String 	assetnum;
    private String 	wonum;
    private String 	schedfinish;
    private String 	schedstart;
    private String 	sneconstraint;
    private String 	fnlconstraint;
    private String 	targcompdate;
    private String 	targstartdate;
    private String 	siteid;
    private Integer x4aggregati;
    private String 	x4datamodello;
    private Long   	x4policyid;
    private String 	x4policy;
    private String 	x4trainante;
    private Integer x3trainante;
    private String 	jpnum;
    private String 	parent;
    private String 	x4microcontrollo;
    private String 	x4tronconum;
    private String 	status;
    private String  x4note;
    private String  x4sapidasset;
    private String  x4ordineprevalente;
    private String  x4tipoodl;
    private List<Task> woactivity;
    private Integer x4datemod;
    private Integer x4dss;
    private String 	x4primaeleggibilita;
    private String  x4burks;
    private String  x4naturalavoro;
    private String  x4respasset;
    
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

	
    @Override
    public String toString()
    {
       return toJson();
    }
}
