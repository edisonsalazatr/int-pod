package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "X4ODM")
@Data
@ToString
public class X4ODM {

    @Id
    @Column(name = "X4ODMID")
    private Long X4ODMID;

    @Column(name = "CREATEDDATE")
    private Date createddate;

    @Column(name = "CHANGEDDATE")
    private Date changeddate;

    @Column(name = "TIPOORDINE")
    private String tipoordine;

    @Column(name = "STATOSISTEMA")
    private String statosistema;

    @Column(name = "TIPOATTIVITA")
    private String tipoattivita;

}
