package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CondizioneAssociazione implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	@Id
    @Column(name = "ID")
    private Long id;
	
	@Column(name = "X4ATTIVITAASSOCIAZIONEID")
	private Long x4AttivazioneAssociazioneId;
	
	@Column(name = "ATTIVITA")
	private Long attivita;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "ATTIVITA_EXT")
	private String attivitaExt;
	
	@Column(name = "MACROATTIVITA_EXT")
	private String macroAttivitaExt;

	@Column(name = "TARGET")
	private String  target;

	@Column(name = "VALUE")
	private String	value;
	
	@Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;
    
    @Column(name = "CLASSIFICATIONID")
    private String classificationId;
    
    @Column(name = "JPDURATION")
    private BigDecimal duration;
  
    @Column(name = "X4OUTSOURCING")
    private Integer x4Outsourcing;
    
    @Column(name = "X4ATRIDONEITA")
    private String x4atridoneita;

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
        CondizioneAssociazione other = (CondizioneAssociazione) obj;
        return Objects.equals(id, other.id);
    }
}
