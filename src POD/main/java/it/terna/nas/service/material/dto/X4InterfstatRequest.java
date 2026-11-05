package it.terna.nas.service.material.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class X4InterfstatRequest implements Serializable {

	

	private static final long serialVersionUID = -7360749171258376436L;
	
	private String 	description;
    private String 	assetnum;
    private String 	siteid;
    private String 	stato;
    private String 	dataultimaril;
    private Integer hasld;
    private String 	rowstamp;
    private String  gravita;
    private String  idinterferenza;
    private String  wonum;
    private Long 	numero_collisioni;
    private Double  x4_distanza;
    private Long    x4densalta;
    private Long    x4densmedia;
    private Long    x4densbassa;
    
    public String toJson() {
        try {
            return Assorted.toJson(this);
        } catch (Exception ex) {
            // log occurred error
            log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
            // return empty JSON representation
            return null;
        }
    }
}
