package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "X4NTK")
@Data
@ToString
public class X4NTK {

    @Id
    @Column(name = "X4NTKID")
    private Long X4NTKID;

    @Column(name = "CREATEDATE")
    private Date createdate;

    @Column(name = "CHANGEDDATE")
    private Date changeddate;

    @Column(name = "STATOSISTEMASAP")
    private String statosistema;

}
