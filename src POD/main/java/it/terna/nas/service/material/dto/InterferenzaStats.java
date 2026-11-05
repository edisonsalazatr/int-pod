package it.terna.nas.service.material.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InterferenzaStats {

    private int count;
    private Double minDistInt;
    private long countDensitaAlta;
    private long countDensitaMedia;
    private long countDensitaBassa;

    public InterferenzaStats(int count, Double minDistInt) {
        this.count = count;
        this.minDistInt = minDistInt;
    }
    
}
