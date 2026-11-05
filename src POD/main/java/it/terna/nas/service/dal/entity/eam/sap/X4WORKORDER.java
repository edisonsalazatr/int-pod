package it.terna.nas.service.dal.entity.eam.sap;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "WORKORDER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class X4WORKORDER {

    @Id
    @Column(name = "WORKORDERID")
    private String id;

    @Column(name = "WONUM")
    private String wonum;

    @Column(name = "X4ODM")
    private String x4odm;

    @Column(name= "X4NTK")
    private String x4ntk;

    @Column(name = "CHANGEBY")
    private String changedby;

    @Column(name = "X4TIPOSPESA")
    private String x4tipospesa;

    @Column(name = "X4BURKS")
    private String x4burks;

    @Column(name = "X4NOPERAZIONE")
    private String x4noperazione;

}
