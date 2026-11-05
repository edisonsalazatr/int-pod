package it.terna.nas.service.dal.entity.eam;

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
public class ClassStructureLevel2 
{
	
	@Id
    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;
	
	@Column(name = "LEVEL")
	private Integer level;

	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "CLASSIFICATIONID")
	private String classificationId;

    @Override
    public int hashCode()
    {
        return Objects.hash(classStructureId);
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
        ClassStructureLevel2 other = (ClassStructureLevel2) obj;
        return Objects.equals(classStructureId, other.classStructureId);
    }
}