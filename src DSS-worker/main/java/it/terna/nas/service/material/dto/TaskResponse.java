package it.terna.nas.service.material.dto;

import java.util.List;
import it.terna.kernel.service.exposure.VoidResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskResponse extends VoidResponse
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private List<Task> tasks;
}
