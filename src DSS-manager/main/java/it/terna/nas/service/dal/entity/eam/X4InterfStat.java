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
@Table(name = "X4INTERFSTAT")
@Getter
@Setter
@NoArgsConstructor
public class X4InterfStat
{
    
    @Id
    @Column(name = "X4INTERFSTATID")
    private Long    x4interfstatid;

    @Column(name = "DESCRIPTION")
    private String  description;

    @Column(name = "STATUS")
    private String  status;

    @Column(name = "ASSETNUM")
    private String  assetnum;

    @Column(name = "SITEID")
    private String  siteId;

    @Column(name = "ULTIMARILEVAZIONE")
    private Date    ultimaRilevazione;

    @Column(name = "PRIMARILEVAZIONE")
    private Date    primaRilevazione;
    
    @Column(name = "IDINTERFERENZA")
    private Long    idinterferenza;

    @Column(name = "GRAVITA")
    private String  gravita;

    @Column(name = "ODLRISOLUTORE")
    private String  odlRisolutore;
    
    @Column(name = "IDINTERFERENZA_EXT")
    private String  idInterferenzaExt;

    @Column(name = "CAUSALE")
    private String  causale;

    @Column(name = "FASE")
    private String  fase;

    @Column(name = "NONCONFERMATO")
    private Integer nonConfermato;
    
    @Column(name = "ASSETNUM1LIV")
    private String assetnum1liv;


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4InterfStat other = (X4InterfStat) obj;
        return Objects.equals(x4interfstatid, other.x4interfstatid);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x4interfstatid);
    }
}
