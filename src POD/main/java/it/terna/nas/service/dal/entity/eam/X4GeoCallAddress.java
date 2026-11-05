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
public class X4GeoCallAddress implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336495487814374263L;
	
	@Column(name = "CAP")
	private String cap;
	
	@Column(name = "CIVICO")
	private String civico;
	
	@Id
	@Column(name = "CODICE_OGGETTO")
	private String codiceOggetto;
	
	@Column(name = "CODICE_PADRE")
	private String codicePadre;
	
	@Column(name = "CODICE_PADRE2")
	private String codicePadre2;
	
	@Column(name = "CODICE_RADICE")
	private String codiceRadice;
	
	@Column(name = "CODICE_RADICE2")
	private String codiceRadice2;
	
	@Column(name = "CODICE_SAP")
	private String codiceSap;
	
	@Column(name = "CODICE_SAP2")
	private String codiceSap2;
	
	@Column(name = "COMUNE")
	private String comune;
	
	@Column(name = "DESCRIZIONE")
	private String descrizione;
	
	@Column(name = "FILIALE")
	private String filiale;
	
	@Column(name = "ISTAT")
	private String istat;
	
	@Column(name = "LATITUDINE1")
	private Double latitudine1;
	
	@Column(name = "LATITUDINE2")
	private Double latitudine2;
	
	@Column(name = "LOCALITA")
	private String localita;
	
	@Column(name = "LONGITUDINE1")
	private Double longitudine1;
	
	@Column(name = "LONGITUDINE2")
	private Double longitudine2;
	
	@Column(name = "PROVINCIA")
	private String provincia;
	
	@Column(name = "STATO_OGGETTO")
	private String statoOggetto;
	
	@Column(name = "TIPO_OGGETTO")
	private String tipoOggetto;
	
	@Column(name = "TOPONIMO")
	private String toponimo;
	
	@Column(name = "VIA")
	private String via;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4GeoCallAddress other = (X4GeoCallAddress) obj;
		return Objects.equals(codiceOggetto, other.codiceOggetto);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(codiceOggetto);
	}
}
