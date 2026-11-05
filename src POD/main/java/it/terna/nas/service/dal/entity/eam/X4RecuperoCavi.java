package it.terna.nas.service.dal.entity.eam;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * CREATE TABLE X4_RECUPERO_CAVI (
 		WONUM VARCHAR2(25 CHAR) NOT NULL,
 		IDINTERFACCIA VARCHAR2(50 CHAR),
 		MESSAGE CLOB,
 		DA_PROCESSARE NUMBER(1) DEFAULT 1,
 		ESITO VARCHAR2(128 CHAR)
	)
 * 
 */

@Entity
@Table(name = "X4_RECUPERO_CAVI")
@Getter
@Setter
@NoArgsConstructor
public class X4RecuperoCavi implements Serializable
{
	private static final long	serialVersionUID	= 8335743487814374263L;

	@Id
	@Column(name = "WONUM")
	private String				wonum;

	@Column(name = "IDINTERFACCIA")
	private String				idInterface;


	@Column(name = "MESSAGE")
	private String				message;

	@Column(name = "ESITO")
	private String				result;

	@Column(name = "DA_PROCESSARE")
	private int daProcessare;

	@Override
	public int hashCode() {
		return Objects.hash(daProcessare, idInterface, message, result);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		X4RecuperoCavi other = (X4RecuperoCavi) obj;
		return daProcessare == other.daProcessare && Objects.equals(idInterface, other.idInterface)
				&& Objects.equals(message, other.message) && Objects.equals(result, other.result);
	}

	@Override
	public String toString() {
		return "X4RecuperoCavi [message=" + message + ", result=" + result + ", idInterface=" + idInterface
				+ ", daProcessare=" + daProcessare + "]";
	}

	

	
}
