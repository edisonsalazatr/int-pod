package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class HealthIndexAsset {
	
    @Id
    @Column(name = "ahscoreid")
    private Long ahscoreId;

    @Column(name = "objectname")
    private String objectName;

    @Column(name = "attributename")
    private String attributeName;

    @Column(name = "ownerrecordid")
    private Long ownerRecordId;

    @Column(name = "value")
    private Double value;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HealthIndexAsset other = (HealthIndexAsset) obj;
        return Objects.equals(ahscoreId, other.ahscoreId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(ahscoreId);
    }
}
