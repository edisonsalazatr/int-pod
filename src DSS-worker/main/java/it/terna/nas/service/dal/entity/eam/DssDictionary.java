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
@Table(name = "X4_DSS_EAM_DICTIONARY")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DssDictionary {
	
    @Id
    @Column(name = "DSS_KEY")
    private String key;
	
	@Column(name = "EAM_VALUE")
	private String value;

    
}

