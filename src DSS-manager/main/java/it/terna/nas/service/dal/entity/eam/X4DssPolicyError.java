package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4_DSS_POLICY_ERROR")
@Getter
@Setter
@NoArgsConstructor
public class X4DssPolicyError implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336495487814374263L;

	@Id
	@Column(name = "DSS_POLICY_ERROR_ID")
	@GeneratedValue(generator = "SEQ_DSS_POLICY_ERROR", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SEQ_DSS_POLICY_ERROR", sequenceName = "SEQ_DSS_POLICY_ERROR", allocationSize=1)
	private Long				dssPolicyErrorId;

	@Column(name = "POLICY_ID")
	private Long				policyId;

	@Column(name = "ERROR_TIMESTAMP")
	private Date				errorDate;

	@Column(name = "ERROR_DESCRIPTION")
	private String				errorDescription;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4DssPolicyError other = (X4DssPolicyError) obj;
		return Objects.equals(dssPolicyErrorId, other.dssPolicyErrorId);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(dssPolicyErrorId);
	}
}
