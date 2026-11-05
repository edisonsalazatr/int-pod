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
        name = "assetspecmapping",
        entities = @EntityResult(entityClass = AlnvalueResult.class,
        						 fields      = {
        								 		@FieldResult(name = "alnvalue",						column ="ALNVALUE")
        						 			   })
)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlnvalueResult {
	
	@Id
	@Column(name = "ALNVALUE")
	private String alnvalue;

}
