package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "JOBPLAN")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class MainActivity {

    @Id
    @Column(name = "JPNUM")
    private String jpnum;

    @Column(name = "DESCRIPTION")
    private String decription;

    @Column(name = "JPDURATION")
    private Double duration;
    
    @Column(name = "X4MACROATTIVITA")
    private String idMacroAttivita;
    
    @Column(name = "JOBPLANID")
    private Long jobplanid;
    
    

	

	
}

