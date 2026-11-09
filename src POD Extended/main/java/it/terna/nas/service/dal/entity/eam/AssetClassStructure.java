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
public class AssetClassStructure implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "ASSETNUM")
	private String assetNum;
	
	@Column(name = "CLASSSTRUCTUREID")
	private String				classStructureId;

	@Override
	public int hashCode() {
		return Objects.hash(assetNum, classStructureId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetClassStructure other = (AssetClassStructure) obj;
		return Objects.equals(assetNum, other.assetNum) && Objects.equals(classStructureId, other.classStructureId);
	}
}
