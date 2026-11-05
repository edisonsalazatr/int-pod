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
@Table(name = "ASSETSPEC")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Assetspec {

    @Id
    @Column(name = "ASSETSPECID")
    private Long assetSpecId;

    @Column(name = "ASSETNUM")
    private String assetNum;

    @Column(name = "TABLEVALUE")
    private String tableValue;
    
    @Column(name = "ALNVALUE")
    private String alnValue;
    
}

