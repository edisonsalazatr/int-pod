package it.terna.nas.service.material.dto;

import java.io.Serializable;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Data
@Slf4j
public class KafkaProvvedimentiPianteMessage implements Serializable{
	
	private static final long serialVersionUID = -6910616184690661962L;
	
	private String uuid;
	private InvioProvvedimentiRequest report;
	
	public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- KafkaOdlToEliveRequest - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

}
