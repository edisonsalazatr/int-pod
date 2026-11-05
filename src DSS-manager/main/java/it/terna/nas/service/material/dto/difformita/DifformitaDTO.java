package it.terna.nas.service.material.dto.difformita;

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
public class DifformitaDTO implements Serializable
{
    private static final long serialVersionUID = -5887807973382721846L;

    @SerializedName("_action")
    @JsonProperty("_action")
    private String            action;
    private String            assieme;
    private String            description;
    private String            iddifformita;
    private String            idmisura;
    private String            logicadiff;
    private String            sottoassieme;
    private String            status;
    private String            tempovalid;
    private String            tipologia;
    private String            tipologiaasset;
    private Long              x4catdifforid;
}
