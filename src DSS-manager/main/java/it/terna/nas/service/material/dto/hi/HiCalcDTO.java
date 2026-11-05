package it.terna.nas.service.material.dto.hi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
public class HiCalcDTO implements Serializable
{
    private static final long serialVersionUID = -5887807973382721846L;
    private String assetNum;
    private String siteId;
}
