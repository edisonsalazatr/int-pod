package it.terna.nas.service.material.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import it.terna.kernel.basement.json.gson.strategy.exclusion.JsonExcludeField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Task 
{
	private int taskid;
	private String assetnum;
    private String description;
    private String siteid;
    private String x4microcontrollo;
    
    @JsonIgnore
    @JsonExcludeField
    private String status;
    private String classStructureId;
    private String classificationId;
    private List<WoServiceAddress> woserviceaddress = new ArrayList<>();
    
    public Task(String assetnum,
    			String taskDescription,
    			String siteId,
    			String idMicroControllo,
    			String status,
    			String classStructureId,
    			String classificationId) {
    	this.assetnum = assetnum;
    	this.description = taskDescription;
    	this.siteid = siteId;
    	this.x4microcontrollo = idMicroControllo;
    	this.status = status;
    	this.classStructureId = classStructureId;
    	this.classificationId = classificationId;
    }


    @Override
    public int hashCode()
    {
        return Objects.hash(assetnum, siteid, x4microcontrollo);
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        return Objects.equals(assetnum, other.assetnum) && Objects.equals(siteid, other.siteid) && Objects.equals(x4microcontrollo, other.x4microcontrollo);
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Task [taskid=");
        builder.append(taskid);
        builder.append(", assetnum=");
        builder.append(assetnum);
        builder.append(", description=");
        builder.append(description);
        builder.append(", siteid=");
        builder.append(siteid);
        builder.append(", x4microcontrollo=");
        builder.append(x4microcontrollo);
        builder.append(", status=");
        builder.append(status);
        builder.append(", classStructureId=");
        builder.append(classStructureId);
        builder.append(", classificationId=");
        builder.append(classificationId);
        builder.append("]");
        return builder.toString();
    }
}
