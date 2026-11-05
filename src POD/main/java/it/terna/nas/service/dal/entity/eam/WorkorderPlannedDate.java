package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class WorkorderPlannedDate implements Serializable{
	
	 public static final long serialVersionUID = -4557431681963240963L;

	    @Id
	    @Column(name = "WORKORDERID")
	    private Long workorderid;

	    @Column(name = "WONUM")
	    private String wonum;
	    
	    @Column(name = "TARGSTARTDATE")
	    private Date targstartdate;
	    
	    @Column(name = "TARGCOMPDATE")
	    private Date targcompdate;

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			WorkorderPlannedDate other = (WorkorderPlannedDate) obj;
			return Objects.equals(workorderid, other.workorderid);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(workorderid);
		}
}
