package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "X4WBE")
@Data
@ToString
public class X4WBENTk {

    @Id
    @Column(name = "IDWBE")
    private String idwbe;

}
