package it.terna.nas.service.material.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class WoServiceAddress
{
    private String wonum;

    @Override
    public int hashCode()
    {
        return Objects.hash(wonum);
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
        WoServiceAddress other = (WoServiceAddress) obj;
        return Objects.equals(wonum, other.wonum);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("WoServiceAddress [wonum=");
        builder.append(wonum);
        builder.append("]");
        return builder.toString();
    }
}
