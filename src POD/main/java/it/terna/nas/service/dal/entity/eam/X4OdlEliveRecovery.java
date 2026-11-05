package it.terna.nas.service.dal.entity.eam;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "X4_ODL_ELIVE_RECOVERY")
@Getter
@Setter
public class X4OdlEliveRecovery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BATCH_CODE")
    private String batchCode;

    @Column(name = "WONUM")
    private String wonum;

    @Column(name = "PROCESSED")
    private Integer processed;

    @Column(name = "ELABDATE")
    private Date elabdate;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "CREATEDATE")
    private Date createdate;
}
