package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4_ODL_INDISP_RECOVERY")
@Getter
@Setter
@NoArgsConstructor
public class X4OdlIndispRecovery implements Serializable {

    private static final long serialVersionUID = 8335743487814374263L;

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "WONUM", nullable = false)
    private String wonum;

    @Column(name = "BATCHCODE", nullable = false)
    private String batchCode;

    @Column(name = "PROCESSED", nullable = false)
    private int processed = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ELABDATE")
    private Date elabDate;

    @Override
    public String toString() {
        return "X4OdlIndispRecovery [id=" + id 
                + ", wonum=" + wonum 
                + ", batchCode=" + batchCode 
                + ", processed=" + processed 
                + ", elabDate=" + elabDate 
                + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4OdlIndispRecovery other = (X4OdlIndispRecovery) obj;
        return Objects.equals(id, other.id);
    }
}