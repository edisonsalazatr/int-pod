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
public class FilialeForAsset implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "ASSETNUM")
	private String				assetNum;
	
	@Column(name = "X3INGRP")
	private String				x3Ingrp;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilialeForAsset other = (FilialeForAsset) obj;
		return Objects.equals(assetNum, other.assetNum);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(assetNum);
	}
}
