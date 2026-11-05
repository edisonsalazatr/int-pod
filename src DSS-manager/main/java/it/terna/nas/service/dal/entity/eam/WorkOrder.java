package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "WORKORDER")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WorkOrder {
	
    @Id
    @Column(name = "WONUM")
    private String wonum;
	
    @Column(name = "PARENT")
    private String parent;

    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "ASSETNUM")
    private String assetNum;
    
    @Column(name = "JPNUM")
    private String jpnum;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SNECONSTRAINT")
    private Date sneConstraint;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FNLCONSTRAINT")
    private Date fnlConstraint;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TARGSTARTDATE")
    private Date targStartDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "TARGCOMPDATE")
    private Date targCompDate;
    
    @Column(name = "SITEID")
    private String siteId;
    
    @Column(name = "X4AGGREGATI")
    private Integer x4Aggregati;

    @Column(name = "X4POLICYID")
    private Long x4Policyid;
    
    @Column(name = "X4POLICY")
    private String x4Policy;

    @Column(name = "X4TRAINANTE")
    private String x4Trainante;
    
    @Column(name = "X4MICROCONTROLLO")
    private String x4Microcontrollo;
    
    @Column(name = "X4TRONCONUM")
    private String x4TroncoNum;
    
    @Column(name = "X4NOTE")
    private String x4Note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "X4DATAMODELLO")
    private Date x4DataModello;
    
    @Column(name = "X4ORDINEPREVALENTE")
    private String x4OrdinePrevalente;
    
    @Column(name = "X4SAPIDASSET")
    private String x4SapIdAsset;
    
    @Column(name = "X4OUTSOURCING")
    private Integer x4outsourcing;
    
    @Column(name = "X4NATURALAVORO")
    private String x4naturalavoro;
    
	@Override
	public int hashCode()
	{
		return Objects.hash(wonum);
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
		WorkOrder other = (WorkOrder) obj;
		return Objects.equals(wonum, other.wonum);
	}
}

