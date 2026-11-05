
package it.terna.nas.service.material.dto.policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AttributesDTO  implements Serializable
{
	private static final long serialVersionUID = 7374828938823372570L;
	private PairAttributeDTO deferrability;
	private PairAttributeDTO frequency;
	private PairAttributeDTO maxAdvance;
	private PairAttributeDTO maxSnooze;
	private PairAttributeStringDTO rifTemporale;
	private String troncoNum;
}