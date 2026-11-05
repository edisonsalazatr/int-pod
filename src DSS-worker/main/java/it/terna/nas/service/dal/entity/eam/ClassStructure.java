package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CLASSSTRUCTURE")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ClassStructure {
	
    @Id
    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;
	
	@Column(name = "DESCRIPTION")
	private String description;

    @Column(name = "CLASSIFICATIONID")
    private String classification;
    
    @Column(name = "HASLD")
    private Integer hsld;

    @Column(name = "ROWSTAMP")
    private String rowStamp;

    @Column(name = "X4ELEMENTO")
    private String elemento;
	
    
}

