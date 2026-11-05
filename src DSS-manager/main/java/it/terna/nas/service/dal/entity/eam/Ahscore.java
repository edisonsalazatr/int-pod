package it.terna.nas.service.dal.entity.eam;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "AHSCORE")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Ahscore
{
    @Id
    @Column(name = "AHSCOREID")
    private Long        ahscoreId;

    @Column(name = "VALUE")
    private BigDecimal  value;

    @Column(name = "PREVIOUSVALUE")
    private BigDecimal  previousValue;

    @Column(name = "ATTRIBUTENAME")
    private String      attributeName;

    @Column(name = "OWNERRECORDID")
    private Long        ownerRecordId;

    @Override
    public int hashCode() {
        return Objects.hash(ahscoreId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ahscore other = (Ahscore) obj;
        return Objects.equals(ahscoreId, other.ahscoreId);
    }
}
