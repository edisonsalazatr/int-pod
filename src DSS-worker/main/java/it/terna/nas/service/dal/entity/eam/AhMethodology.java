package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "AHMETHODOLOGY")
@Data
public class AhMethodology 
{	
	@Id
	@Column(name = "AHMETHODOLOGYID")
	private Long ahMethodologyId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "SCORETYPE")
	private String scoreType;

	@Column(name = "ACTIVE")
	private Integer active;
	
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AhMethodology other = (AhMethodology) obj;
        return Objects.equals(ahMethodologyId, other.ahMethodologyId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ahMethodologyId);
    }
}
