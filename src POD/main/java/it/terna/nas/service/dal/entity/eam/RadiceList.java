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
@Table(name = "X4GEOCALLADDRESS")
@Getter
@Setter
@NoArgsConstructor
public class RadiceList implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336495487814374263L;
		
	@Id
	@Column(name = "RADICE")
	private String radice;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RadiceList other = (RadiceList) obj;
		return Objects.equals(radice, other.radice);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(radice);
	}
}