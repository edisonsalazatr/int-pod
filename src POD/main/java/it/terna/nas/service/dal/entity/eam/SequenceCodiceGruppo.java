package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SequenceCodiceGruppo implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "NEXTVAL")
	private Long codiceGruppo;

	@Override
	public int hashCode() {
		return Objects.hash(codiceGruppo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequenceCodiceGruppo other = (SequenceCodiceGruppo) obj;
		return Objects.equals(codiceGruppo, other.codiceGruppo);
	}
}
