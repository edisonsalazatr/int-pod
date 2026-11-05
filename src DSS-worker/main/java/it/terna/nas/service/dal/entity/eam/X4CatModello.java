package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "X4CATMODELLO")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class X4CatModello {    
	
	@Id
    @Column(name = "X4CATMODELLOID")
    private Long id;
	
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ROWSTAMP")
	private String rowStamp;
	    
    @Column(name = "TIPOLOGIAASSET")
    private String tipologiaAsset;

    @Column(name = "IDMODELLO")
    private String modelloId;
    
    @Column(name = "X4MXL")
    private Integer x4mxl;

    @Override
	public int hashCode() {
		return Objects.hash(description, id, modelloId, rowStamp, tipologiaAsset, x4mxl);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4CatModello other = (X4CatModello) obj;
		return Objects.equals(description, other.description)
				&& Objects.equals(id, other.id) && Objects.equals(modelloId, other.modelloId)
				&& Objects.equals(rowStamp, other.rowStamp) && Objects.equals(tipologiaAsset, other.tipologiaAsset)
				&& Objects.equals(x4mxl, other.x4mxl);
	}
}

