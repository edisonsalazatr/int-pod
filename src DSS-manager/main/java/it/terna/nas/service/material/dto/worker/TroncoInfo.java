package it.terna.nas.service.material.dto.worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TroncoInfo implements Serializable
{
    private static final long serialVersionUID = 932531073287078374L;
    private String            linea;
    private String            sapId;
    private Long              x3TroncoId;
    private String            troncoNum;

    @Override
    public int hashCode()
    {
        return Objects.hash(x3TroncoId);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TroncoInfo other = (TroncoInfo) obj;
        return Objects.equals(x3TroncoId, other.x3TroncoId);
    }
}
