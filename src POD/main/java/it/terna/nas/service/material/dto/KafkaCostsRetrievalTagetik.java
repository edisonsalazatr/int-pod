package it.terna.nas.service.material.dto;

import java.io.Serializable;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Data
@Slf4j
public class KafkaCostsRetrievalTagetik implements Serializable{
	
	private static final long serialVersionUID = 7678544795271246719L;
	
	private String data;
	
    public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- KafkaCostsRetrievalTagetik - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
	

}
