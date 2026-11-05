package it.terna.nas.service.dal.entity.eam;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "X4POLICYINPUT")
@Getter
@Setter
@NoArgsConstructor
public class X4PolicyInput {

    @Id
    @Column(name = "X4POLICYINPUTID")
    private Long x4PolicyInputId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "HASLD")
    private Integer hasLd;

    @Column(name = "IDINPUT")
    private String idInput;

    @Column(name = "IDMISURA")
    private String idMisura;

    @Column(name = "IDPOLICY")
    private String idPolicy;

    @Column(name = "LIVINPUT")
    private Integer livInput;

    @Column(name = "NINPUT")
    private Integer nInput;

    @Column(name = "OPERATORE")
    private String operatore;

    @Column(name = "ROWSTAMP")
    private String rowStamp;

    @Column(name = "TARGETRAFFRONTO")
    private String targetRaffronto;

    @Column(name = "TIPOLOGIAINPUT")
    private String tipologiaInput;

    @Column(name = "X4AND")
    private String x4And;

    @Column(name = "X4OR")
    private String x4Or;

    @Column(name = "X4EQUAL")
    private String x4Equal;

    @Column(name = "X4NOTEQUAL")
    private String x4NotEqual;

	@Override
	public int hashCode()
	{
		return Objects.hash(x4PolicyInputId);
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
		X4PolicyInput other = (X4PolicyInput) obj;
		return Objects.equals(x4PolicyInputId, other.x4PolicyInputId);
	}
}

