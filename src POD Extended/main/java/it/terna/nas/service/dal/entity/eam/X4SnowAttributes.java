package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Date;
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
public class X4SnowAttributes implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "ASSETATTRID")
	private String assetAttrId;
	
	@Column(name = "DATATYPE")
	private String dataType;
	
	@Column(name = "ALNVALUE")
	private String alnValue;
	
	@Column(name = "NUMVALUE")
	private Double numValue;
	
	@Column(name = "TABLEVALUE")
	private String tableValue;
	
	@Column(name = "DATEVALUE")
	private Date dateValue;

	@Override
	public int hashCode() {
		return Objects.hash(alnValue, assetAttrId, dataType, dateValue, numValue, tableValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4SnowAttributes other = (X4SnowAttributes) obj;
		return Objects.equals(alnValue, other.alnValue) && Objects.equals(assetAttrId, other.assetAttrId)
				&& Objects.equals(dataType, other.dataType) && Objects.equals(dateValue, other.dateValue)
				&& Objects.equals(numValue, other.numValue) && Objects.equals(tableValue, other.tableValue);
	}
}
