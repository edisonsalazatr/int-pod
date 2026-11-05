package it.terna.nas.service.material.dto.interferenza;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InterferenzaDTO implements Serializable
{
    private static final long serialVersionUID = -5887807973382721846L;

    @SerializedName("_action")
    @JsonProperty("_action")
    private String            action;
    private String            assieme;
    private String            description;
    private String            idinterferenza;
    private String            idmisura;
    private String            logicaint;
    private String            misura;
    private String            sottoassieme;
    private String            status;
    private Long              x4catinterfid;
}
