package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FlagRichiestaIndisponibilita {

	@Id
	@Column(name = "JOBPLANID")
	private Long jobplanid;

	@Column(name = "X4INDISPONIBILITA")
	private Integer x4Indisponibilita;

}
