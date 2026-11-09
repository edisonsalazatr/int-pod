package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Date;

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
public class X4RenameSapIdSnow implements Serializable
{
	private static final long serialVersionUID = 6917868575647482274L;

	@Id
	@Column(name = "X4RENAMESAPIDSNOWID")
	private Long x4RenameSapIdSnowId;
	
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
	private Date updateTime;
	
	@Column(name = "DA_NOTIFICARE_SNOW")
	private String daNotificareSnow;

	@Override
	public String toString() {
		return "X4RenameSapIdSnow [x4RenameSapIdSnowId=" + x4RenameSapIdSnowId + ", assetnum=" + assetnum
				+ ", rowstamp=" + rowstamp + ", sapIdDest=" + sapIdDest + ", sapIdOrig=" + sapIdOrig + ", siteId="
				+ siteId + ", updateTime=" + updateTime + ", daNotificareSnow=" + daNotificareSnow + "]";
	}
}
