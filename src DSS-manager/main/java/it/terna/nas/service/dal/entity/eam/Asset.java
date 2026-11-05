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
public class Asset implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Column(name = "ANCESTOR")
	private String				ancestor;

	@Id
	@Column(name = "ASSETNUM")
	private String				assetNum;

	@Column(name = "CLASSSTRUCTUREID")
	private String				classStructureId;

	@Column(name = "DESCRIPTION")
	private String				description;

	@Column(name = "PARENT")
	private String				parent;

	@Column(name = "STATUS")
	private String				status;
	
	@Column(name = "X3GEWRK")
	private String				x3Gewrk;
	
	@Column(name = "X3INGRP")
	private String				x3Ingrp;
	
	@Column(name = "X3ROOTASSETNUM")
	private String				x3RootAssetNum;
	
	@Column(name = "X3SAPID")
	private String				x3SapId;

	@Column(name = "SITEID")
	private String siteId;
	
	@Column(name = "X4OLDSTATUS")
	private String x4oldstatus;
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Asset other = (Asset) obj;
		return Objects.equals(assetNum, other.assetNum);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(assetNum);
	}
}
