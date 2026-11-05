package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "X4CATMODELLOASSET")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class X4CatModelloAsset implements Serializable {

    private static final long serialVersionUID = 8517712791473329797L;

    @Id
    @Column(name = "X4CATMODELLOASSETID")
    private Long x4CatModelloAssetId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ATTRIBUTO")
    private String attributo;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "IDCLUSTER")
    private Long idCluster;

    @Column(name = "HASLD")
    private Long hasLd;
    
    @Column(name = "X4CATMODELLOID")
    private Long X4CatModelloId;

    @Column(name = "ROWSTAMP")
    private String rowStamp;

    @Column(name = "N1")
    private Double n1;
    
}


