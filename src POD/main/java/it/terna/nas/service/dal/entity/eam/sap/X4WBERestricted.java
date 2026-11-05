package it.terna.nas.service.dal.entity.eam.sap;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name = "X4WBE")
@Data
@ToString
public class X4WBERestricted {

    @Id
    @Column(name = "X4WBEID")
    private Long x4WBEId;

    @Column(name = "CHANGEDATE")
    private Date changeDate;

}
