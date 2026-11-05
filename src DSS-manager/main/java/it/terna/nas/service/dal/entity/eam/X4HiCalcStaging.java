package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
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
@Table(name = "X4HICALCSTAGING")
@Getter
@Setter
@NoArgsConstructor
public class X4HiCalcStaging
{
    @Id
    @Column(name = "X4HICALCSTAGINGID")
    @GeneratedValue(generator = "X4HICALCSTAGINGIDSEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "X4HICALCSTAGINGIDSEQ", sequenceName = "X4HICALCSTAGINGIDSEQ", allocationSize=1)
    private Long    x4HiCalcStagingId;

    @Column(name = "SITEID")
    private String  siteId;

    @Column(name = "ASSETNUM")
    private String  assetnum;
    
    @Column(name = "HICALCID")
    private String  hiCalcId;

    @Column(name = "CREATEDATE")
    private Date createDate;
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4HiCalcStaging other = (X4HiCalcStaging) obj;
        return Objects.equals(x4HiCalcStagingId, other.x4HiCalcStagingId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x4HiCalcStagingId);
    }
}
