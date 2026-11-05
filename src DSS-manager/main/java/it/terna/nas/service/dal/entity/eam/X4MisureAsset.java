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
import lombok.ToString;

@Entity
@Table(name = "X4MISUREASSET")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class X4MisureAsset {

    @Id
    @Column(name = "X4MISUREASSETID")
    private Long x4MisureAssetId;

    @Column(name = "ASSETNUM")
    private String assetNum;

    @Column(name = "SITEID")
    private String siteId;

    @Column(name = "IDMISURA")
    private String idMisura;

    @Column(name = "IDMISURA_EXT")
    private String idMisuraExt;

    @Column(name = "VALORE")
    private Double valore;

    @Column(name = "ULTIMARILEVAZIONE")
    private Date ultimaRilevazione;

    @Column(name = "PRIMARILEVAZIONE")
    private Date primaRilevazione;

    @Column(name = "ROWSTAMP")
    private String rowstamp;

    @Override
    public int hashCode() {
        return Objects.hash(x4MisureAssetId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        X4MisureAsset other = (X4MisureAsset) obj;
        return Objects.equals(x4MisureAssetId, other.x4MisureAssetId);
    }
}
