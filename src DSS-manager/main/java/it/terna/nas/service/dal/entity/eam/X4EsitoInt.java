package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4ESITOINT")
@Getter
@Setter
@NoArgsConstructor
public class X4EsitoInt implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336495487814374263L;

	@Column(name = "CODESITO")
	private String				codeResult;

	@Column(name = "DESC_ESITO")
	private String				description;

	@Column(name = "DEST")
	private String				dest;

	@Column(name = "END_ELAB")
	private Date				endElab;

	@Column(name = "HASLD")
	private int					hasLd;

	@Column(name = "IDINTERFACCIA")
	private String				idInterface;

	@Column(name = "KAFKATOPIC")
	private String				kafkaTopic;

	@Column(name = "MESSAGE")
	private String				message;

	@Column(name = "PARENT")
	private Long				parent;

	@Column(name = "ESITO")
	private String				result;

	@Column(name = "ROWSTAMP")
	private String				rowStamp;

	@Column(name = "SOURCE")
	private String				source;

	@Column(name = "START_ELAB")
	private Date				startElab;

	@Column(name = "UNIQUEID")
	private String				uniqueId;

	@Id
	@Column(name = "X4ESITOINTID")
	private Long				x4EsitoIntId;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4EsitoInt other = (X4EsitoInt) obj;
		return Objects.equals(x4EsitoIntId, other.x4EsitoIntId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(x4EsitoIntId);
	}
}
