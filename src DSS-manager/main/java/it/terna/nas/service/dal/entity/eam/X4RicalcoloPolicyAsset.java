package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
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
@Getter @Setter
@Table(name = "X4RICALCOLO_POLICY_ASSET")
@NoArgsConstructor
@SequenceGenerator(name = "x4RicalcoloPolicyAssetSeq", sequenceName = "SEQ_X4RICALCOLO_POLICY_ASSET", allocationSize = 1)
public class X4RicalcoloPolicyAsset implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -512791957338423091L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "x4RicalcoloPolicyAssetSeq")
    @Column(name = "ID", nullable = false, updatable = false)
    private Long   id;

    @Column(name = "X4POLICYID", nullable = false)
    private Long   x4PolicyId;

    @Column(name = "X4_DSS_WORKED_HISTORYID")
    private Long   x4DssWorkedHistoryId;

    @Column(name = "ASSETNUM", length = 25)
    private String assetNum;

    /**
     * Oracle CHAR default 'N' In Java lo gestiamo con default 'N' in @PrePersist o
     * direttamente sul campo.
     */
    @Column(name = "ELABORATO", length = 1)
    private String elaborato = "N";

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
        X4RicalcoloPolicyAsset other = (X4RicalcoloPolicyAsset) obj;
        return Objects.equals(id, other.id);
    }
}