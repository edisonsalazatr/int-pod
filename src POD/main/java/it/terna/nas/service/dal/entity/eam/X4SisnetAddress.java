package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class X4SisnetAddress implements Serializable
{
	private static final long serialVersionUID = 6917868575647482274L;

	@Id
	@GeneratedValue(generator = "SEQ_X4SISNETADDRESS", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SEQ_X4SISNETADDRESS", sequenceName = "SEQ_X4SISNETADDRESS", allocationSize=1)
	@Column(name = "X4SISNETADDRESSID")
	private Long x4SisnetAddressId;

	@Column(name = "TIPO_OGGETTO")
	private String tipoOggetto;

	@Column(name = "STATO_OGGETTO")
	private String statoOggetto;

	@Column(name = "CODICE_OGGETTO")
	private String codiceOggetto;

	@Column(name = "CODICE_PADRE")
	private String codicePadre;
	
	@Column(name = "CODICE_RADICE")
	private String codiceRadice;
	
	@Column(name = "DESCRIZIONE")
	private String descrizione;

	@Column(name = "AREA_LAVORAZIONE")
	private String areaLavorazione;

	@Column(name = "CENTRO_DI_LAVORO")
	private String centroDiLavoro;

	@Column(name = "FILIALE")
	private String filiale;

	@Column(name = "TOPONIMO")
	private String toponimo;

	@Column(name = "VIA")
	private String via;

	@Column(name = "CIVICO")
	private String civico;

	@Column(name = "LOCALITA")
	private String localita;

	@Column(name = "PROVINCIA")
	private String provincia;

	@Column(name = "CAP")
	private String cap;

	@Column(name = "COMUNE")
	private String comune;

	@Column(name = "ISTAT")
	private String istat;

	@Column(name = "LATITUDINE1")
	private Double latitudine1;

	@Column(name = "LONGITUDINE1")
	private Double longitudine1;

	@Column(name = "LATITUDINE2")
	private Double latitudine2;

	@Column(name = "LONGITUDINE2")
	private Double longitudine2;
	
	@Column(name = "LIVELLO")
	private String livello;
	
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "DA_NOTIFICARE_GEOCALL")
	private String daNotificareGeocall;

	@Column(name = "NOTES")
	private String notes;

	@Override
	public int hashCode()
	{
		return Objects.hash(x4SisnetAddressId);
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
		X4SisnetAddress other = (X4SisnetAddress) obj;
		return Objects.equals(x4SisnetAddressId, other.x4SisnetAddressId);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("X4SisnetAddress [x4SisnetAddressId=");
		builder.append(x4SisnetAddressId);
		builder.append(", tipoOggetto=");
		builder.append(tipoOggetto);
		builder.append(", statoOggetto=");
		builder.append(statoOggetto);
		builder.append(", codiceOggetto=");
		builder.append(codiceOggetto);
		builder.append(", codicePadre=");
		builder.append(codicePadre);
		builder.append(", codiceRadice=");
		builder.append(codiceRadice);
		builder.append(", descrizione=");
		builder.append(descrizione);
		builder.append(", areaLavorazione=");
		builder.append(areaLavorazione);
		builder.append(", centroDiLavoro=");
		builder.append(centroDiLavoro);
		builder.append(", filiale=");
		builder.append(filiale);
		builder.append(", toponimo=");
		builder.append(toponimo);
		builder.append(", via=");
		builder.append(via);
		builder.append(", civico=");
		builder.append(civico);
		builder.append(", localita=");
		builder.append(localita);
		builder.append(", provincia=");
		builder.append(provincia);
		builder.append(", cap=");
		builder.append(cap);
		builder.append(", comune=");
		builder.append(comune);
		builder.append(", istat=");
		builder.append(istat);
		builder.append(", latitudine1=");
		builder.append(latitudine1);
		builder.append(", longitudine1=");
		builder.append(longitudine1);
		builder.append(", latitudine2=");
		builder.append(latitudine2);
		builder.append(", longitudine2=");
		builder.append(longitudine2);
		builder.append(", livello=");
		builder.append(livello);
		builder.append(", creation_date=");
		builder.append(creationDate);
		builder.append(", update_date=");
		builder.append(updateDate);
		builder.append(", daNotificareGeocall=");
		builder.append(daNotificareGeocall);
		builder.append("]");
		return builder.toString();
	}
}
