package it.terna.nas.service.dal.entity.eam;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4_DSS_RETRY_ERROR")
@Getter
@Setter
@NoArgsConstructor
public class RetryableErrorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ERROR")
    private Long id;

    @Column(name = "ERROR_CODE", length = 50)
    private String errorCode;

    @Column(name = "MESSAGE_PATTERN", length = 500)
    private String messagePattern;

    @Column(name = "ACTIVE", length = 1)
    private String active;

    @Column(name = "DATA_CREATION")
    private LocalDateTime dataCreation;
    
}

