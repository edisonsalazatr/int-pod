package it.terna.nas.service.dal.entity.eam;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "JOBPLAN")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Activity {

    @Id
    @Column(name = "JPNUM")
    private String jpnum;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "JPDURATION")
    private BigDecimal duration;
    
    @Column(name = "X4MACROATTIVITA")
    private String idMacroAttivita;
    
    @Column(name = "JOBPLANID")
    private Long jobPlanId;
    
    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;
    
    @Column(name = "CLASSIFICATIONID")
    private String classificationId;
    
    @Column(name = "X4OUTSOURCING")
    private Integer x4Outsourcing;
    
    @Column(name = "X4ATRIDONEITA")
    private String x4atridoneita;
	
}

