package it.terna.nas.service.dal.entity.eam;

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
public class ClassStructureLevel {
	
	@Id
    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;
	
	@Column(name = "LEVEL")
	private Integer level;


}

