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
public class PolicyDTO implements Serializable
{
	private static final long serialVersionUID = -5887807973382721846L;
    private String x4policyid;
    private String idpolicy;
    private Integer differibilita; 
    private Boolean eleggibilita; 
    private Integer frequenza; 
    private String idtiming; 
    private Integer maxanticipo;
    private Integer maxposticipo; 
    private String tipologiaasset; 
    private String tipopolicy; 
    private Boolean policyaggr; 
    private Integer livaggr;
    private String status; 
    private String x4riftemporale; 
    private String x4sottoprogramma; 
    private String x4cluster; 
    private String siteid; 
    private String apartireda; 
    private Integer version; 
    private String idmacroattivita;
}
