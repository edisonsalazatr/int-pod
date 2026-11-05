package it.terna.nas.service.material.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RecordFileEliveGrigliaDto {
	private String  campata;
	private Integer t_s;
	private String  densita;
	private Integer t_a;
	private Integer tipologia;
	private String  prioritaEam;
	private String  wonum;
    private Double  dist_int;

}
