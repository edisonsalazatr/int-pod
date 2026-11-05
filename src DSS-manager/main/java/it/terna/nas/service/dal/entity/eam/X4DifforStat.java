package it.terna.nas.service.dal.entity.eam;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4DIFFORSTAT")
@Getter
@Setter
@NoArgsConstructor
public class X4DifforStat
{
    @Id
    @Column(name = "X4DIFFORSTATID")
    private Long    x4difforstatid;

    @Column(name = "DESCRIPTION")
    private String  description;

    @Column(name = "ASSETNUM")
    private String  assetnum;

    @Column(name = "SITEID")
    private String  siteId;

    @Column(name = "IDDIFFORMITA")
    private Long    idDifformita;

    @Column(name = "STATUS")
    private String  status;

    @Column(name = "ULTIMARILEVAZIONE")
    private Date    ultimaRilevazione;

    @Column(name = "PRIMARILEVAZIONE")
    private Date    primaRilevazione;

    @Column(name = "GRAVITA")
    private String  gravita;

    @Column(name = "NONCONFERMATO")
    private Integer nonConfermato;

    @Column(name = "ODLRISOLUTORE")
    private String  odlRisolutore;

    @Column(name = "IDDIFFORMITA_EXT")
    private String  iddifformitaExt;

    @Column(name = "CAUSALE")
    private String  causale;

    @Column(name = "FASE")
    private String  fase;

    @Column(name = "IDMISURA")
    private String  idMisura;

    @Column(name = "VALMISURA")
    private Double  valMisura;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4DifforStat other = (X4DifforStat) obj;
        return Objects.equals(x4difforstatid, other.x4difforstatid);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x4difforstatid);
    }
}
