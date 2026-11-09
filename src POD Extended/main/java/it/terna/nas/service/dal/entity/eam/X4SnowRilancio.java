package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4SNOW_RILANCIO")
@Getter
@Setter
@NoArgsConstructor
public class X4SnowRilancio implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long   id;

    @Column(name = "ASSETNUM")
    private String assetnum;

    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;

    @Column(name = "DA_ELABORARE")
    private String daElaborare;

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
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
        X4SnowRilancio other = (X4SnowRilancio) obj;
        return Objects.equals(id, other.id);
    }
}
