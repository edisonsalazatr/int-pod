package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;

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
public class X4RenameSapIdGeo implements Serializable
{
	private static final long serialVersionUID = 6917868575647482274L;

	@Id
	@Column(name = "X4RENAMESAPIDGEOID")
	private Long x4RenameSapIdGeoId;
	
	@Column(name = "ASSETNUM")
	private String assetnum;

	@Column(name = "ROWSTAMP")
	private String rowstamp;
	
	@Column(name = "SAPID_DEST")
	private String sapIdDest;
	
	@Column(name = "SAPID_ORIG")
	private String sapIdOrig;
	
	@Column(name = "SITEID")
	private String siteId;
	
	@Column(name = "UPDATETIME")
	private String updateTime;
	
	@Column(name = "DA_NOTIFICARE_GEOCALL")
	private String daNotificareGeocall;
	

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("X4RenameSapIdGeo [x4RenameSapIdGeoId=");
		builder.append(x4RenameSapIdGeoId);
		builder.append(", assetNum=");
		builder.append(assetnum);
		builder.append(", rowstamp=");
		builder.append(rowstamp);
		builder.append(", sapIdDest=");
		builder.append(sapIdDest);
		builder.append(", sapIdOrig=");
		builder.append(sapIdOrig);
		builder.append(", siteId=");
		builder.append(siteId);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", daNotificareGeocall=");
		builder.append(daNotificareGeocall);
		builder.append("]");
		return builder.toString();
	}
}
