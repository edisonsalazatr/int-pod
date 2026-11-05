package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class AttivitaAsset {
	
	 @Id
	    @Column(name = "wonum")
	    private String wonum;

	    @Column(name = "assetnum")
	    private String assetnum;

	    @Column(name = "status")
	    private String status;

	    @Column(name = "jpnum")
	    private String jpnum;

	    @Column(name = "parent")
	    private String parent;

	    @Column(name = "sneconstraint")
	    private Date sneConstraint;

	    @Column(name = "fnlconstraint")
	    private Date fnlConstraint;

	    @Column(name = "targstartdate")
	    private Date targStartDate;

	    @Column(name = "targcompdate")
	    private Date targCompDate;

	    @Column(name = "siteid")
	    private String siteId;

	    @Column(name = "x4aggregati")
	    private Integer x4Aggregati;

	    @Column(name = "x4policyid")
	    private Long x4policyid;

	    @Column(name = "x4policy")
	    private String x4policy;

	    @Column(name = "x4trainante")
	    private String x4Trainante;

	    @Column(name = "x4microcontrollo")
	    private String x4Microcontrollo;

	    @Column(name = "x4tronconum")
	    private String x4TroncoNum;

	    @Column(name = "x4note")
	    private String x4Note;

	    @Column(name = "x4datamodello")
	    private Date x4datamodello;

	    @Column(name = "reportDate")
	    private Date reportDate;

	    @Column(name = "description")
	    private String description;

	    @Column(name = "jpduration")
	    private Long jpduration;

	    @Column(name = "templatetype")
	    private String templatetype;
	    
	    @Column(name = "ELEGGIBILITA")
	    private Integer eleggibilita;
	    
	    @Column(name = "ACTFINISH")
	    private Date actfinish;
	    
	    @Column(name = "X4ORDINEPREVALENTE")
	    private String x4OrdinePrevalente;
	    
	    @Column(name = "TIPOLOGIA_ATTIVITA")
	    private String tipologiaAttivita;

	    @Column(name = "X4TECMIG")
	    private Integer x4TecMig;
	    
	    @Column(name = "X4RIFTEMPORALE")
	    private String x4RifTemporale;
		
		@Column(name = "DIFFERIBILITA")
	    private Integer differibilita;
		
		@Column(name = "X4PRIMAELEGGIBILITA")
	    private Date primaEleggibilita;

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
            AttivitaAsset other = (AttivitaAsset) obj;
            return Objects.equals(wonum, other.wonum);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(wonum);
        }
}
