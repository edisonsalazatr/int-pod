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
@Table(name = "X4POLICYCLASSIFICATIONS")
@Getter
@Setter
@NoArgsConstructor
public class X4PolicyClassifications 
{
    @Id
    @Column(name = "X4POLICYCLASSIFICATIONSID")
    private Long x4PolicyClassificationId;

    @Column(name = "CLASSIFICATIONID")
    private String classificationId;

    @Column(name = "X4POLICYID")
    private Long x4policyId;
    
    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;

	@Override
	public int hashCode()
	{
		return Objects.hash(x4PolicyClassificationId);
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
		X4PolicyClassifications other = (X4PolicyClassifications) obj;
		return Objects.equals(x4PolicyClassificationId, other.x4PolicyClassificationId);
	}
}

