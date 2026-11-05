package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4_MICROCONTROLLI_MIG_BATCH")
@Getter
@Setter
@NoArgsConstructor
public class X4MicrocontrolloMigBatch implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8336458487814374263L;

	@Id
	@Column(name = "X4_MICROCONTROLLI_MIG_BATCHID")
	@GeneratedValue(generator = "SEQ_X4_MICROCONTROLLI_MIG_BATCHIDS", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "SEQ_X4_MICROCONTROLLI_MIG_BATCHIDS", 
					   sequenceName = "X4_MICROCONTROLLI_MIG_BATCHIDS", 
					   allocationSize=1)
	private Long				id;

	@Column(name = "STATUS")
	private String				status;

	@Column(name = "WONUM")
	private String				wonum;
	
	@Column(name = "ASSETNUM")
	private String				assetnum;
	
	@Column(name = "JPNUM")
	private String				jpnum;

	@Column(name = "CREATED_BY")
	private String				createdBy;

	@Column(name = "CREATED_TIMESTAMP")
	private Date				createdDate;

	@Column(name = "CHANGED_BY")
	private String				changeBy;

	@Column(name = "CHANGED_TIMESTAMP")
	private Date				changeDate;
	
	@Column(name = "MESSAGE_CODE")
    private String              code;

    @Column(name = "MESSAGE_TEXT")
    private String              description;

	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4MicrocontrolloMigBatch other = (X4MicrocontrolloMigBatch) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
}
