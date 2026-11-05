package it.terna.nas.service.material.dto.elive;

import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EliveRecoveryProcessResult {

    private boolean success;
    private String errorMessage;
    private KafkaOdlToEliveRequest request;

    public static EliveRecoveryProcessResult ok(KafkaOdlToEliveRequest request) {
        return new EliveRecoveryProcessResult(true, null, request);
    }

    public static EliveRecoveryProcessResult ko(String errorMessage) {
        return new EliveRecoveryProcessResult(false, errorMessage, null);
    }
}