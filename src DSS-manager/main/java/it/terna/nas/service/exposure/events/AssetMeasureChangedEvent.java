package it.terna.nas.service.exposure.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
Class to map an event from MAXIMO to notify an asset measure has been changed.
An example of a message from AT documentation.
{   
    "assetnum": "1734733",
    "siteid": "SIT01",
    "idmisura": "VAR.COND.f.1",
    "valore": "58",
    "hasld": "0",
    "rowstamp": "381592460",
    "primarilevazione": "",
    "ultimarilevazione": "",
    "iddifformita_ext": ""
}
 * @author marco.degidio
 *
 */
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class AssetMeasureChangedEvent extends ExposureRequest {
	
	private static final long serialVersionUID = 7932568650316591989L;
	
	private String x4misureassetid;
	private String description;
	private String assetnum;
	private String siteid;
	private String idmisura;
	private String valore;
	private String hasld;
	private String rowstamp;
	private String primarilevazione;
	private String ultimarilevazione;
	private String iddifformita_ext;
	
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

