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
public class X4GeoCallAddressRestricted implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336495487814374263L;
		
	@Id
	@Column(name = "CODICE_OGGETTO")
	private String codiceOggetto;
	
	@Id
	@Column(name = "CODICE_RADICE")
	private String codiceRadice;
	
	@Column(name = "CODICE_RADICE2")
	private String codiceRadice2;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4GeoCallAddressRestricted other = (X4GeoCallAddressRestricted) obj;
		return Objects.equals(codiceOggetto, other.codiceOggetto);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(codiceOggetto);
	}
}
