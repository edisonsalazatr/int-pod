package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;

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
public class WorkorderHierarchyRestricted implements Serializable {

    public static final long serialVersionUID = -4557431681963240963L;
    
    @Id
    @Column(name = "WONUM")
	private String				wonum;
    
    @Column(name = "PARENT")
    private String 				parent;
    
    @Column(name = "STATUS")
    private String 				status;
    
    @Column(name = "livello")
    private String 				livello;
}
