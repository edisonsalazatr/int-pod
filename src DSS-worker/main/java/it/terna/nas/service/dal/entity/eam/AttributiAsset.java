package it.terna.nas.service.dal.entity.eam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL) 
public class AttributiAsset {
	
	@Id
    @Column(name = "assetspecid")
    private Long   assetSpecId;
	
    @Column(name = "assetnum")
    private String assetNum;

    @Column(name = "siteid")
    private String siteId;

    @Column(name = "classstructureid")
    private String classStructureId;
    
    @Column(name = "datatype")
    private String dataType;
    
    @Column(name = "measureunitid")
    private String measureUnitId;
    
    @Column(name = "numvalue")
    private BigDecimal numValue;

    @Column(name = "alnvalue")
    private String alnValue;
    
    @Column(name = "tablevalue")
    private String tableValue;

    @Column(name = "assetattrid")
    private String assetAttrId;

    @Column(name = "description")
    private String description;

    @Column(name = "assetattributeid")
    private Long   assetAttributeId;
	
    @Column(name = "DATEVALUE")
    private Date dateValue;
}
