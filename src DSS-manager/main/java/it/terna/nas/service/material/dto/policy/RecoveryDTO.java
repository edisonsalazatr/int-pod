package it.terna.nas.service.material.dto.policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
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
public class RecoveryDTO implements Serializable
{
	private static final long serialVersionUID = -5887807973382721846L;
	
	@SerializedName("x4_dss_policy_runnerid")
    @JsonProperty("x4_dss_policy_runnerid")
    private Long idrunner;
}
