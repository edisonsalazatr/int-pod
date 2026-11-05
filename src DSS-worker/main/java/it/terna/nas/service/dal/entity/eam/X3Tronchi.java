package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "X3TRONCHI")
@Getter @Setter @ToString
public class X3Tronchi 
{
	
	@Id
	@Column(name = "X3TRONCHIID")
	private Long x3Tronchiid;
	
	@Column(name = "ASSETNUM")
	private String assetnum;
	
	@Column(name = "SAPID")
	private String sapid;
	
	@Column(name = "TRONCONUM")
	private String tronconum;

    @Override
    public int hashCode()
    {
        return Objects.hash(x3Tronchiid);
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
        X3Tronchi other = (X3Tronchi) obj;
        return Objects.equals(x3Tronchiid, other.x3Tronchiid);
    }
}
