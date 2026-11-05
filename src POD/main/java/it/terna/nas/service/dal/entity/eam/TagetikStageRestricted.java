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
public class TagetikStageRestricted implements Serializable {

    public static final long serialVersionUID = -4557431681963240963L;

    @Id
    @Column(name = "PERIODO")
    private String periodo;

	@Override
	public int hashCode() {
		return Objects.hash(periodo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TagetikStageRestricted other = (TagetikStageRestricted) obj;
		return Objects.equals(periodo, other.periodo);
	}
}
