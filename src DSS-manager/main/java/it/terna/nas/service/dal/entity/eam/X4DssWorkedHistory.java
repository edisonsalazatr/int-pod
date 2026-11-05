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
@Table(name = "X4_DSS_WORKED_HISTORY")
@Getter
@Setter
@NoArgsConstructor
public class X4DssWorkedHistory implements Serializable
{
    private static final long serialVersionUID = 8336495487814374263L;

    @Id
    @Column(name = "X4_DSS_WORKED_HISTORYID")
    @GeneratedValue(generator = "X4_DSS_WORKED_HISTORYIDSEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "X4_DSS_WORKED_HISTORYIDSEQ", sequenceName = "X4_DSS_WORKED_HISTORYIDSEQ", allocationSize = 1)
    private Long              idDssWorked;

    @Column(name = "STATUS")
    private String            status;

    @Column(name = "POLICY_ID")
    private String            policyId;

    @Column(name = "ASSETNUM")
    private String            assetNum;

    @Column(name = "ORDERNUM")
    private String            orderNum;

    @Column(name = "TOTAL_RELATED_MESSAGE")
    private Integer           totalRelatedMessage;

    @Column(name = "PROGRESSIVE_RELATED_MESSAGE")
    private Integer           progressiveRelatedMessage;

    @Column(name = "TRIGGED_ENTITY")
    private String            triggedEntity;

    @Column(name = "CREATED_BY")
    private String            createdBy;

    @Column(name = "CREATED_TIMESTAMP")
    private Date              createdDate;

    @Column(name = "CHANGED_BY")
    private String            changeBy;

    @Column(name = "CHANGED_TIMESPAMP")
    private Date              changeDate;

    @Column(name = "DSS_PC_CORRELATION_ID")
    private String            correlationId;

    @Column(name = "CODE")
    private String            code;

    @Column(name = "DESCRIPTION")
    private String            description;

    @Column(name = "RECOVERY_COUNT")
    private Integer           recoveryCount;

    @Column(name = "X4_DSS_POLICY_RUNNERID")
    private Long              x4DssPolicyRunnerId;

    @Column(name = "ERROR_FLAG")
    private String            errorFlag;

    @Column(name = "SHORT_DESCRIPTION")
    private String            shortDescription;

    @Column(name = "TRONCONUM")
    private String            troncoNum;

    @Column(name = "HASLD")
    private Integer           hasLd; 
    
    @Column(name = "SENT", length = 1)
    private String sent;

    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4DssWorkedHistory other = (X4DssWorkedHistory) obj;
        return Objects.equals(idDssWorked, other.idDssWorked);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idDssWorked);
    }
}
