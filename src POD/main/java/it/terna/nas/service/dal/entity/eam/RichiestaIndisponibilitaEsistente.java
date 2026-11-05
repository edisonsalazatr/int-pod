package it.terna.nas.service.dal.entity.eam;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@SqlResultSetMapping(
		name = "RichiestaIndisponibilitaEsistenteMapping",
		classes = @ConstructorResult(
				targetClass = RichiestaIndisponibilitaEsistente.class,
				columns = {
						@ColumnResult(name = "wonum", type = String.class)
				}
		)
)
public class RichiestaIndisponibilitaEsistente {

	@Id
	@JsonProperty("wonum")
	private String wonum;

	public RichiestaIndisponibilitaEsistente(String wonum) {
		this.wonum = wonum;
	}
}
