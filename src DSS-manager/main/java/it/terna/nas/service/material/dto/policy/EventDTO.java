package it.terna.nas.service.material.dto.policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EventDTO implements Serializable
{
	private static final long serialVersionUID = -5887807973382721846L;
	private String asset;
	private Map<String, String> attributes;
	private String event;
	private String kafkaKey;
	private String policy;
	private String tipology;
	private String type;
	private String wonum;
	private Boolean addTronco;
}
