package it.terna.nas.service.material.dto.cavi;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvioOdlDeletionToCaviRequest implements Serializable
{
	private static final long serialVersionUID = -5887807973382721846L;
	
	/**
	 * codice ordine di lavoro
	 */
	private String wonum;

}

