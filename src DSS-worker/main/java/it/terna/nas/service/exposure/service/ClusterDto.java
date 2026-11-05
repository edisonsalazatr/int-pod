package it.terna.nas.service.exposure.service;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClusterDto {

	private Integer idCluster;
	private Map<String, String> attributes;
	private BigDecimal n1;
	
	public ClusterDto(Integer idCluster, Map<String, String> attributes, BigDecimal n1) {
		this.idCluster = idCluster;
		this.attributes = attributes;
		this.n1 = n1;
	}
	
}
