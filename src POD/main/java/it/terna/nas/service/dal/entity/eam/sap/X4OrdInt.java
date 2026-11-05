package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "X4ORDINT")
@Data
@ToString
public class X4OrdInt implements Serializable {

    private static final long serialVersionUID = -453114384810683260L;

    @Id
    @Column(name = "X4ORDINTID")
    private Long x4ordintid;

    @Column(name = "CREATEDDATE")
    private Date createddate;

    @Column(name = "CHANGEDDATE")
    private Date changeddate;

    @Column(name = "TIPOOI")
    private String tipooi;

    @Column(name = "STATOSISTEMA")
    private String statosistema;

}
