package it.terna.nas.service.material.dto.policy;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Policy
{
	private Long				id;
	private String				description;
	private String				activityId;
	private String				assetType;
	private String				status;
	private String				formula;
	private List<PolicyInput>	inputs;

	public boolean isSimple()
	{
		if (inputs == null)
			return true;

		return inputs.size() == 1;
	}
}
