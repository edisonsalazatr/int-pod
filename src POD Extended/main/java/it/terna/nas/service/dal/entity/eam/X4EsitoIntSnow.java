package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4ESITOINTSNOW")
@Getter
@Setter
@NoArgsConstructor
public class X4EsitoIntSnow implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336495487814374263L;

	@Column(name = "COD_ESITO")
	private String				codeResult;

	@Column(name = "DESC_ESITO")
	private String				description;

	@Column(name = "END_ELAB")
	private Date				endElab;

	@Column(name = "HASLD")
	private int					hasLd;

	@Column(name = "IDINTERFACCIA")
	private String				idInterface;

	@Column(name = "KAFKA_TOPIC")
	private String				kafkaTopic;

	@Column(name = "MESSAGE")
	private String				message;

	@Column(name = "PARENT")
	private Long				parent;

	@Column(name = "ESITO")
	private String				result;

	@Column(name = "ROWSTAMP")
	private String				rowStamp;

	@Column(name = "START_ELAB")
	private Date				startElab;

	@Column(name = "UNIQUEID")
	private String				uniqueId;
	
	@Column(name = "RETRY")
	private Long				retry;

	@Id
	@Column(name = "X4ESITOINTSNOWID")
	private Long				x4EsitoIntSnowId;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4EsitoIntSnow other = (X4EsitoIntSnow) obj;
		return Objects.equals(x4EsitoIntSnowId, other.x4EsitoIntSnowId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(x4EsitoIntSnowId);
	}
}
