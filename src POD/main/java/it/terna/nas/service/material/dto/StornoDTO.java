package it.terna.nas.service.material.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class StornoDTO implements Serializable
{
	private static final long	serialVersionUID	= -5887807973382721846L;
    private String idConsuntivo;
    private String matricola;
    @JsonProperty("X4STORNOSAP")
    @JsonAlias({"x4stornosap"})  
    private String tipoStorno;
    private String wonum;

}
