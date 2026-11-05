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
@Table(name = "X4TABIDONEITA")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class X4TabIdoneita {
	
    @Id
    @Column(name = "X4TABIDONEITAID")
    private String id;
	
    @Column(name = "TIPOLOGIAASSET")
    private String tipologiaAsset;

    @Column(name = "STATOIDONEITA")
    private String validState;
	
}

