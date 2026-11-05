package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "X4ELIVEINTERFCONFIG")
@Data
public class X4EliveInterfConfig {

    @Id
    @Column(name = "X4ELIVEINTERFCONFIGID")
    private Long id;

    @Column(name = "PRIORITA", length = 20)
    private String priorita;

    @Column(name = "IDINTERFERENZA", nullable = false, length = 50)
    private String idInterferenza;

    @Column(name = "FLUSSO", nullable = false, length = 50)
    private String flusso;

    @Column(name = "ATTIVO", nullable = false)
    private Integer attivo;
}
