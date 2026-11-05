package it.terna.nas.service.material.dto.hi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class HiDTO implements Serializable
{
    private static final long serialVersionUID = -5887807973382721846L;

    @SerializedName("_action")
    @JsonProperty("_action")
    private String            action;
    private Long              ahscoreid;
    private String            objectname;
    private String            value;
    private String            previousvalue;
    private String            attributename;
    private Long              ownerrecordid;
}
