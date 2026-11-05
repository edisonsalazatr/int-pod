package it.terna.nas.service.material.dto.policy;

import java.util.Date;

import it.terna.nas.service.dal.service.repository.IPolicyRun;
import lombok.Data;

@Data
public class PolicyRunDTO implements IPolicyRun {
	
	private Long runnerId;
	
	private Long x4PolicyId;
	 
	private String correlationId;
	
	private Date elaborationDate;

}
