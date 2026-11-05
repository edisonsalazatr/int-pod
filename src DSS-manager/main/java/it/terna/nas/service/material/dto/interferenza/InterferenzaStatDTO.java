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
public class InterferenzaStatDTO implements Serializable
{
    private static final long serialVersionUID = -5887807973382721846L;

    private Long              x4interfstatid;
    private String            description;
    private String            assetnum;
    private String            siteid;
    private String            ultimarilevazione;
    private String            primarilevazione;
    private String            status;
    private Long              idinterferenza;
    private String            gravita;
    private String            odlrisolutore;
    private Boolean           nonconfermato;

    @SerializedName("idinterferenza_ext")
    @JsonProperty("idinterferenza_ext")
    private String            idinterferenzaExt;
    private String            causale;
    private String            fase;
    private String            assetnum1liv;

    @SerializedName("_action")
    @JsonProperty("_action")
    private String            action;
}
