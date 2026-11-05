package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4_DSS_AGGREGATE")
@Getter
@Setter
@NoArgsConstructor
public class X4DssAggregate
{

    @Id
    @Column(name = "X4_DSS_AGGREGATE_ID")
    @GeneratedValue(generator = "SEQ_X4_DSS_AGGREGATE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_X4_DSS_AGGREGATE", sequenceName = "SEQ_X4_DSS_AGGREGATE", allocationSize = 1)
    private Long   x4DssAggregateId;

    @Column(name = "ID_DSS_WORKED")
    private Long   idDssWorked;

    @Column(name = "DSS_PC_CORRELATION_ID")
    private String dssPcCorrelationId;

    @Column(name = "ASSET")
    private String assetNum;

    @Column(name = "KELIGIBILITA")
    private Double keligibilita;
    
    @Column(name = "ASSETLV2")
    private String assetNumLv2;
    
    @Column(name = "LINEA")
    private String linea;
    
    @Column(name = "TRATTA")
    private String tratta;
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        X4DssAggregate other = (X4DssAggregate) obj;
        return Objects.equals(x4DssAggregateId, other.x4DssAggregateId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x4DssAggregateId);
    }
}
