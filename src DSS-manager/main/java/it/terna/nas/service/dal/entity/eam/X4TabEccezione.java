package it.terna.nas.service.dal.entity.eam;

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
@Table(name = "X4TABECCEZIONI")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class X4TabEccezione {
	
    @Id
    @Column(name = "X4TABECCEZIONIID")
    private String id;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
    @Column(name = "TIPOLOGIAASSET")
    private String tipologiaAsset;
    
    @Column(name = "HASLD")
    private Integer hsld;

    @Column(name = "ROWSTAMP")
    private String rowStamp;
    
    @Column(name = "IDATTIVITA")
    private Long idAttivita;
    
    @Column(name = "IDATTIVITA_EXT")
    private String idAttivitaExt;

    @Column(name = "STATOIDONEO")
    private String validState;

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
        X4TabEccezione other = (X4TabEccezione) obj;
        return Objects.equals(id, other.id);
    }
}

