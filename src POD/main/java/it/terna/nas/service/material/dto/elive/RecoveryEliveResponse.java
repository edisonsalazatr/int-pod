package it.terna.nas.service.material.dto.elive;

import it.terna.kernel.service.exposure.ExposureResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryEliveResponse extends ExposureResponse {

 
	private static final long serialVersionUID = -4958751007473136496L;
	
	private Integer httpCode;
    private String esito;
    private String message;
    
	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
