package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

import lombok.NoArgsConstructor;


@Entity
@Table(name = "CLASSANCESTOR")
@Data
@NoArgsConstructor
public class ClassAncestor {

    @Id
    @Column(name = "CLASSANCESTORID")
    private Long classAncestorId;

    @Column(name = "CLASSSTRUCTUREID")
    private String classStructureId;

    @Column(name = "CLASSIFICATIONID")
    private String classificationId;

    @Column(name = "ANCESTOR")
    private String ancestor;

    @Column(name = "ANCESTORCLASSID")
    private String ancestorClassId;

    @Column(name = "HIERARCHYLEVELS")
    private Integer hierarchyLevels;

    @Column(name = "ORGID")
    private String orgId;

    @Column(name = "SITEID")
    private String siteId;

    @Column(name = "ROWSTAMP")
    private String rowStamp;
}
