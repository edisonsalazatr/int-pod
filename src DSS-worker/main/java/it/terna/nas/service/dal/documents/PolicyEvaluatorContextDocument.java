package it.terna.nas.service.dal.documents;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.policy.Policy;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Document(collection= "policy_evaluator_context")
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PolicyEvaluatorContextDocument implements Serializable {
	
	private static final long serialVersionUID = -561641789215590606L;

	@Id
	@SerializedName("_id")
	@JsonProperty("_id")
    private String 					id;
	
	@NonNull
    private String 					documentDay; //
	@NonNull
    private Policy 					policy; //
	@NonNull
    private AssetData 				assetData;
	@NonNull
    private Long 					creationDate;		//timestamp
	
	private List<PolicyExpressionItem> expressions;
	
	private String policyFormula;
	private Boolean policyResult;

}
