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
public class AssetDTO implements Serializable
{
	private static final long	serialVersionUID	= -5887807973382721846L;
    private String parent;
    private String x3ingrp;
    private String status;
    private String assetnum;
    private String x3gewrk;
    private String classstructureid;
    private String x3sapid;
    private String x3sapideq;
    private String description;
    private String ancestor;
    private String x3rootassetnum;
    
	public String toJson() {
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
}
