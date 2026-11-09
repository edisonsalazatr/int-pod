package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ClassStructureTipology implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	
	@Column(name = "TYPE")
	private String				type;

	@Id
	@Column(name = "CLASSSTRUCTUREID")
	private String				classStructureId;

	@Column(name = "CLASSIFICATIONID")
	private String				classificationId;

	@Column(name = "DESCRIPTION")
	private String				description;

	@Override
	public int hashCode()
	{
		return Objects.hash(classStructureId);
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
		ClassStructureTipology other = (ClassStructureTipology) obj;
		return Objects.equals(classStructureId, other.classStructureId);
	}
}
