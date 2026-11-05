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
public class LabTransId implements Serializable {

    public static final long serialVersionUID = -4557431681963240963L;

    @Id
    @Column(name = "LABTRANSID")
	private Long labtransId;

}
