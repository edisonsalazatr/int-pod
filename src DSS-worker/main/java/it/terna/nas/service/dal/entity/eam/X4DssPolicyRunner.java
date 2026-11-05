package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
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
@Table(name = "X4_DSS_POLICY_RUNNER")
@Getter
@Setter
@NoArgsConstructor
public class X4DssPolicyRunner implements Serializable
{
    private static final long serialVersionUID = 8336495487814374263L;

    @Id
    @Column(name = "X4_DSS_POLICY_RUNNERID")
    @GeneratedValue(generator = "X4_DSS_POLICY_RUNNERIDSEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "X4_DSS_POLICY_RUNNERIDSEQ", sequenceName = "X4_DSS_POLICY_RUNNERIDSEQ", allocationSize = 1)
    private Long              x4DssPolicyRunnerId;

    @Column(name = "STATUS")
    private String            status;

    @Column(name = "X4POLICYID")
    private Long              x4PolicyId;

    @Column(name = "IDPOLICY")
    private String            idPolicy;

    @Column(name = "DATA_ELABORAZIONE")
    private Date              dataElaborazione;

    @Column(name = "IDMACROATTIVITA")
    private String            idMacroAttivita;

    @Column(name = "CORRELATION_ID")
    private String            correlationId;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4DssPolicyRunner other = (X4DssPolicyRunner) obj;
        return Objects.equals(x4DssPolicyRunnerId, other.x4DssPolicyRunnerId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x4DssPolicyRunnerId);
    }
}
