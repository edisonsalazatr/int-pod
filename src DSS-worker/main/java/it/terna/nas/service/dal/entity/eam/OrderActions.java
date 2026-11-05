package it.terna.nas.service.dal.entity.eam;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderActions 
{
	private List<Order> orders;
	
	public OrderActions(List<Order> orderInput)
	{
	    this.orders = orderInput;
	}
}
