package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class JobPlan
{
    @Column(name = "CLASSSTRUCTUREID")
    private String  classStructureId;

    @Column(name = "DESCRIPTION")
    private String  description;

    @Id
    @Column(name = "JOBPLANID")
    private Long    id;

    @Column(name = "JPDURATION")
    private String  jpDuration;

    @Column(name = "JPNUM")
    private String  jpNum;

    @Column(name = "STATUS")
    private String  status;

    @Column(name = "X4INDISPONIBILITA")
    private Integer x4Indisponibilita;

    @Column(name = "X4ELIVE")
    private Integer x4Elive;
    
    @Column(name = "X4MACROATTIVITA")
    private String  x4MacroAttivita;

    @Column(name = "PLUSCREVNUM")
    private Integer plusRevNum;
    
    @Override
    public int hashCode()
    {
        return Objects.hash(id);
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
        JobPlan other = (JobPlan) obj;
        return Objects.equals(id, other.id);
    }
}
