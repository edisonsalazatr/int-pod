package it.terna.nas.service.exposure.ws.maximo;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor
public class AssignmentType implements Serializable
{
	private static final long serialVersionUID = -6659449187702619314L;
    private Boolean appointment;
    private Boolean apptrequired;
    private Long assignmentid;
    private String craft;
    private String finishdate;
    private String href;
    private String laborcode;
    private Double laborhrs;
    private String localref;
    private String orgid;
    private Long parentassignid;
    private Boolean resourcelock;
    private String scheduledate;
    private String status;
    private Boolean worklog;
    private String wplaborid;
    private Boolean x4preposto;
    
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("AssignmentType [assignmentid=");
		builder.append(assignmentid);
		builder.append(", laborcode=");
		builder.append(laborcode);
		builder.append(", laborhrs=");
		builder.append(laborhrs);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	} 
}
