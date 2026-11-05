package it.terna.nas.service.material.dto.asset;

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
public class AssetDTO implements Serializable
{
    private static final long serialVersionUID = -5887807973382721846L;
    
    @SerializedName("_action")
    @JsonProperty("_action")
    private String            action;
    private String            parent;
    private String            x3ingrp;
    private String            status;
    private String            assetnum;
    private String            x3gewrk;
    private String            classstructureid;
    private String            x3sapid;
    private String            x3sapideq;
    private String            description;
    private String            ancestor;
    private String            x3rootassetnum;
    private String            siteid;
}
