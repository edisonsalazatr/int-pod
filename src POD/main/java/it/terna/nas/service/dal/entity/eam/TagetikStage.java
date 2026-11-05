package it.terna.nas.service.dal.entity.eam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class TagetikStage implements Serializable {

    public static final long serialVersionUID = -4557431681963240963L;

    @Id
    @Column(name = "CO_NUM_DOC")
    private String coNumDoc;

    @Column(name = "PERIODO")
    private String periodo;
    
    @Column(name = "CDC_RESP_ORG")
    private String cdcRespOrg;
    
    @Column(name = "TIPO_OGG_CONTAB")
    private String tipoOggContab;
    
    @Column(name = "TITOLARE_RIS")
    private String titolareRis;

	@Override
	public int hashCode() {
		return Objects.hash(cdcRespOrg, coNumDoc, periodo, tipoOggContab, titolareRis);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagetikStage other = (TagetikStage) obj;
		return Objects.equals(cdcRespOrg, other.cdcRespOrg) && Objects.equals(coNumDoc, other.coNumDoc)
				&& Objects.equals(periodo, other.periodo) && Objects.equals(tipoOggContab, other.tipoOggContab)
				&& Objects.equals(titolareRis, other.titolareRis);
	}
}
