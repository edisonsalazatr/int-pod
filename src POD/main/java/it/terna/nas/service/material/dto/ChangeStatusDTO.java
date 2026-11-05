package it.terna.nas.service.material.dto;

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
public class ChangeStatusDTO implements Serializable
{
	private static final long	serialVersionUID	= -5887807973382721846L;
	private String					wonum;
	private String 					href;
	private String 					reqStatus;
	private Boolean 				father;
	private String					parent;

}
