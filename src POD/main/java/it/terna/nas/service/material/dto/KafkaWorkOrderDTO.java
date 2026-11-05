package it.terna.nas.service.material.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class KafkaWorkOrderDTO implements Serializable
{
	private static final long	serialVersionUID	= -5887807973382721846L;
	private List<WorkOrderDTO> 	member;
}
