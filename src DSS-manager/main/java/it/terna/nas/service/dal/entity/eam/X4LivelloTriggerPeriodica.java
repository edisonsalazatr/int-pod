package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;

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
import lombok.ToString;

@Entity
@Table(name = "X4_LVL_TRIGGER_PERIODICHE")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class X4LivelloTriggerPeriodica implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "X4_LVL_TRIGGER_PERIODICHEID")
    @GeneratedValue(generator = "X4_LVL_TRIGGER_PERIODICHEIDSEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "X4_LVL_TRIGGER_PERIODICHEIDSEQ", sequenceName = "X4_LVL_TRIGGER_PERIODICHEIDSEQ", allocationSize = 1)
    private Long              id;
	
	@Column(name = "LIVELLO")
	private Integer	level;

	@Column(name = "ATTIVO")
	private Integer	attivo;

	@Column(name = "ROWSTAMP")
	private String	rowStamp;
	
}
