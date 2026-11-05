package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "LABTRANS")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LabTrans implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "LABTRANSID")
    private Long              id;

    @Column(name = "LABORCODE")
    private String            laborCode;

    @Column(name = "REFWO")
    private String            refWo;

    @Column(name = "SITEID")
    private String            siteId;

    @Column(name = "GENAPPRSERVRECEIPT")
    private Long genApprServReceipt;
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LabTrans other = (LabTrans) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}
