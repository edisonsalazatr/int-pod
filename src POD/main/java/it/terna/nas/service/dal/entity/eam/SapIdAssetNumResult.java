package it.terna.nas.service.dal.entity.eam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(
        name = "SapIdAssetMapping",
        entities = @EntityResult(entityClass = SapIdAssetNumResult.class,
        						 fields      = {
        								 		@FieldResult(name = "x3SapId",						column ="X3SAPID"),
        								 		@FieldResult(name = "targetassetnum",				column ="TARGETASSETNUM")
        						 			   })
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SapIdAssetNumResult {
	
	@Id
	@Column(name = "X3SAPID")
	private String x3SapId;
    private String targetassetnum;

}
