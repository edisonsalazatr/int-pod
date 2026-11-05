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
public class AssetWithYearTronchi {
    
    @Id
    @Column(name = "ANNO")
    private Integer anno;

    @Column(name = "TRONCHI")
    private String tronchi;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AssetWithYearTronchi other = (AssetWithYearTronchi) obj;
        return Objects.equals(anno, other.anno);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(anno);
    }
}
