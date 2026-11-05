package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4CATMODELLOASSET")
@Getter
@Setter
@NoArgsConstructor
public class X4CatModelloAsset {      

    @Id
    @Column(name = "X4CATMODELLOASSETID")
    private Long id;
	
	@Column(name = "DESCRIPTION")
	private String description;

    @Column(name = "ATTRIBUTO")
    private String attributo;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "IDCLUSTER")
    private Integer idCluster;
    
    @Column(name = "HASLD")
    private Integer hsld;

    @Column(name = "ROWSTAMP")
    private String rowStamp;

    @Column(name = "X4CATMODELLOID")
    private Long x4CatModelloId;
    
    @Column(name = "N1")
    private Double n1;

	@Override
	public int hashCode() {
		return Objects.hash(attributo, description, hsld, id, idCluster, n1, rowStamp, value, x4CatModelloId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4CatModelloAsset other = (X4CatModelloAsset) obj;
		return Objects.equals(attributo, other.attributo) && Objects.equals(description, other.description)
				&& Objects.equals(hsld, other.hsld) && Objects.equals(id, other.id)
				&& Objects.equals(idCluster, other.idCluster) && Objects.equals(n1, other.n1)
				&& Objects.equals(rowStamp, other.rowStamp) && Objects.equals(value, other.value)
				&& Objects.equals(x4CatModelloId, other.x4CatModelloId);
	}

	@Override
	public String toString() {
		return "X4CatModello [id=" + id + ", description=" + description + ", attributo=" + attributo + ", value="
				+ value + ", idCluster=" + idCluster + ", hsld=" + hsld + ", rowStamp=" + rowStamp + ", x4CatModelloId="
				+ x4CatModelloId + ", n1=" + n1 + "]";
	}
    
	
    
}

