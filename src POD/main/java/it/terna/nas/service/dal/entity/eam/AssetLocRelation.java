package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AssetLocRelation implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "ID")
	private String				id;
	
	@Column(name = "SOURCEASSETNUM")
	private String				sourceAssetNum;
	
	@Column(name = "TARGETASSETNUM")
	private String				targetAssetNum;

	@Column(name = "X3SAPID")
	private String				x3SapId;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetLocRelation other = (AssetLocRelation) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id);
	}
}
