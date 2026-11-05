package it.terna.nas.service.exposure.ws.elive;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class RecordFileElive {
	
	@SerializedName("id_int")
	@JsonProperty("id_int")
	private Long 	id_int;
	private String  campata;
	private Double  dist_int;
	private Long    progres;
	private String  priorita;
	private Double  temp_amb;
	private Double  c_est_int;
	private Double  c_nord_int;
	private String  wonum;
	private String  cod_griglia;
	private Integer t_s;
	@JsonAlias("Densita")
	private String  densita;
	private Integer t_a;
	private Double  est_cen;
	private Double  nord_cen;
	private Double  x_cen;
	private Double  y_cen;
	private Long    anno_comp;
	private String  periodo_comp;
	private Integer in_fascia;
	private Integer cat_vert;
	private Integer cat_sband;
	private Integer ribalt;
	private List<Integer> tipologia;
	private String  prioritaEam;
	
}
