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
@Table(name = "X3TRONCHI")
@Getter
@Setter
@NoArgsConstructor
public class X3Tronchi
{
    @Id
    @Column(name = "X3TRONCHIID")
    private Long   x3Tronchi;

    @Column(name = "ASSETNUM")
    private String   assetNum;

    @Column(name = "SAPID")
    private String sapId;

    @Column(name = "TRONCONUM")
    private String troncoNum;

    @Override
    public int hashCode()
    {
        return Objects.hash(x3Tronchi);
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
        return Objects.equals(x3Tronchi, other.x3Tronchi);
    }
}
