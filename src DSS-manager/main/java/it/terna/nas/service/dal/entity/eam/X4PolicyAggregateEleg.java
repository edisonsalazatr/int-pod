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
@Table(name = "X4POLICYAGGREGATEELEG")
@Getter
@Setter
@NoArgsConstructor
public class X4PolicyAggregateEleg
{
    @Id
    @Column(name = "X4POLICYAGGREGATEELEGID")
    private Long   X4PolicyAggregateElegId;

    @Column(name = "TIPOLOGIAASSET")
    private String typeAsset;

    @Column(name = "PARAMETRO")
    private String parameter;

    @Column(name = "VALUE")
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
        X4PolicyAggregateEleg other = (X4PolicyAggregateEleg) obj;
        return Objects.equals(X4PolicyAggregateElegId, other.X4PolicyAggregateElegId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(X4PolicyAggregateElegId);
    }
}
