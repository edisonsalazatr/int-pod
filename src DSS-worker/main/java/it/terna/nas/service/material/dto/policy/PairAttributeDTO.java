package it.terna.nas.service.material.dto.policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PairAttributeDTO implements Serializable
{
	private static final long serialVersionUID = 5537918528994008540L;
	private Integer oldValue;
	private Integer newValue;
}
