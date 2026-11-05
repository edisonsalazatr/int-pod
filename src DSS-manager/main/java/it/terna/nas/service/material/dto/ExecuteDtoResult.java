package it.terna.nas.service.material.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.utility.Catalog.Esito;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExecuteDtoResult implements Serializable
{
	private static final long			serialVersionUID	= -1160687728116696992L;
	private Esito esito;
	private String codError;
	private String descrError;
	private Date timeStampError;
	private List<WorkedPolicyRequestEvent> listShoot;
}
