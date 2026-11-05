package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BearerToken implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "X4BEARERTOKENID")
	private Long				x4BearerTokenId;

	@Column(name = "URI")
	private String				uri;
	
	@Column(name = "CLIENT_NAME")
	private String				clientName;

	@Override
	public int hashCode() {
		return Objects.hash(x4BearerTokenId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BearerToken other = (BearerToken) obj;
		return Objects.equals(x4BearerTokenId, other.x4BearerTokenId);
	}

	
}
