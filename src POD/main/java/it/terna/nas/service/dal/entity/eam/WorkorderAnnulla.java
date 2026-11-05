package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WORKORDER")
@Getter
@Setter
@NoArgsConstructor
public class WorkorderAnnulla implements Serializable
{
	private static final long	serialVersionUID	= 8336495487814374263L;

	@Id
	@Column(name = "WONUM")
	private String				wonum;
	
	@Column(name = "WOLO1")
	private String 				wolo1;

	@Override
	public int hashCode() {
		return Objects.hash(wonum);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkorderAnnulla other = (WorkorderAnnulla) obj;
		return Objects.equals(wonum, other.wonum);
	}
}
