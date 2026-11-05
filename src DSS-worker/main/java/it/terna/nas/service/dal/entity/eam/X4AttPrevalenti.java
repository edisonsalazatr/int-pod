package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "X4ATTPREVALENTI")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class X4AttPrevalenti
{
	@Id
	@Column(name = "X4ATTPREVALENTIID")
	private String	id;

	@Column(name = "DESCRIPTION")
	private String	description;

	@Column(name = "ATTIVITAPREV")
	private String	attivitaPrev;

	@Column(name = "SITEID")
	private String	siteId;

	@Column(name = "JPNUM")
	private String	jpNum;

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4AttPrevalenti other = (X4AttPrevalenti) obj;
		return Objects.equals(id, other.id);
	}
}
