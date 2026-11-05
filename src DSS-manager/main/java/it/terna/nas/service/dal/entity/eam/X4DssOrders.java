package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4_DSS_ORDERS")
@Getter
@Setter
@NoArgsConstructor
public class X4DssOrders
{

    @Id
    @Column(name = "X4_DSS_ORDERS_ID")
    @GeneratedValue(generator = "SEQ_X4_DSS_ORDERS", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_X4_DSS_ORDERS", sequenceName = "SEQ_X4_DSS_ORDERS", allocationSize = 1)
    private Long   x4DssOrderId;

    @Column(name = "ID_DSS_WORKED")
    private Long   idDssWorked;

    @Column(name = "DSS_PC_CORRELATION_ID")
    private String dssPcCorrelationId;

    @Column(name = "ASSETNUM")
    private String assetNum;

    @Column(name = "JPNUM")
    private String jpNum;

    @Column(name = "DATA_MODELLO")
    private Date   dataModello;

    @Column(name = "DATA_NON_PRIMA_DI")
    private Date   dataNonPrimaDi;

    @Column(name = "DATA_MAX_POSTICIPO")
    private Date   dataMaxPosticipo;

    @Column(name = "DATA_PIANIF_INIZIO")
    private Date   dataPianifInizio;

    @Column(name = "DATA_PIANIF_FINE")
    private Date   dataPianifFine;

    @Column(name = "TO_CREATE")
    private String toCreate;

    @Column(name = "X4POLICYID")
    private Long   x4PolicyId;

    @Column(name = "X4POLICY")
    private String x4Policy;

    @Column(name = "X4TRAINANTE")
    private String x4Trainante;

    @Column(name = "WONUM")
    private String wonum;

    @Column(name = "WOPARENT")
    private String woParent;

    @Column(name = "TRONCONUM")
    private String troncoNum;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "X4NOTE")
    private String x4Note;

    @Column(name = "SAPIDASSET")
    private String sapIdAsset;

    @Column(name = "X4WOPREVALENTE")
    private String x4WoPrevalente;

    @Column(name = "X4AGGREGATI")
    private Integer x4Aggregate;
    
    @Column(name = "TRATTA")
    private String tratta;
    
    @Column(name = "X3TRONCHIID")
    private Long   x3TronchiId;
    
    @Column(name = "X4OUTSOURCING")
    private Integer x4outsourcing;
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4DssOrders other = (X4DssOrders) obj;
        return Objects.equals(x4DssOrderId, other.x4DssOrderId);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x4DssOrderId);
    }
}
