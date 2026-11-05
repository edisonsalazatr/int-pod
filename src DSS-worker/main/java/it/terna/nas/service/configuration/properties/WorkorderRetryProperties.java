package it.terna.nas.service.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class WorkorderRetryProperties 
{
    private Integer maxAttempts;
    private Long delay;
}
