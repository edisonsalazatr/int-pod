package it.terna.nas.service.dal.entity.eam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
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
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "ASSET")
@JsonInclude(value = Include.NON_NULL)
public class Asset implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "ANCESTOR")
    private String            ancestor;

    @Id
    @Column(name = "ASSETNUM")
    private String            assetNum;

    @Column(name = "CLASSSTRUCTUREID")
    private String            classStructureId;

    @Column(name = "DESCRIPTION")
    private String            sedeTecnicaDescrizione;

    @Column(name = "PARENT")
    private String            parent;

    @Column(name = "STATUS")
    private String            status;

    @Column(name = "X3GEWRK")
    private String            centroLavoroResponsabile;

    @Column(name = "X3INGRP")
    private String            gruppoResponsabilePianifManuten;

    @Column(name = "X3ROOTASSETNUM")
    private String            x3RootAssetNum;

    @Column(name = "X3SAPID")
    private String            x3SapId;

    @Column(name = "X3COSTRUTTORECOMPONENTE")
    private String            codiceCostruttore;

    @Column(name = "X3TIPOCOMPONENTE")
    private String            tipoComponente;

    @Column(name = "CLASSIFICATIONID")
    private String            classificationId;

    @Column(name = "SITEID")
    private String            siteId;

    @Column(name = "X3BUKRS")
    private String            societa;

    @Column(name = "X3EQFNR")
    private String            sort;

    @Column(name = "INSTALLDATE")
    private Date              installDate;

    @Column(name = "X3KOKRS")
    private String            controllingArea;

    @Column(name = "X3KOSTL")
    private String            centroDiCosto;

    @Column(name = "X3SWERK")
    private String            divisioneUbicazione;

    @Column(name = "X3ZZSOCP")
    private String            societaProprietaria;

    @Column(name = "X3FUNZIONEELEMENTO")
    private String            modello;

    @Column(name = "X3RIFERIMENTOFASE")
    private String            riferimentoFase;
    
    @Column(name = "X4ELEMENTO")
	private String				macroComponente;

    @Column(name = "X3MSGRP")
    private String            impianto;
    

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Asset other = (Asset) obj;
        return Objects.equals(assetNum, other.assetNum);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(assetNum);
    }
}
