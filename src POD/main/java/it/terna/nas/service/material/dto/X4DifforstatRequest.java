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
public class X4DifforstatRequest implements Serializable {

	private static final long serialVersionUID = -5163548824072577495L;
	
	private String description;
    private String assetnum;
    private String siteid;
    private String idinterferenza;
    private String status;
    private String primarilevazione;
    private Integer hasld;
    private String rowstamp;

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

