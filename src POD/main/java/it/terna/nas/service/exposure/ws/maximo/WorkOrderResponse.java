package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
public class WorkOrderResponse extends AbstractMaximoResponse 
{
	private static final long serialVersionUID = 6967251629688775952L;
	private String wonum;
	private String jpnum;
	private String description;
	private String fnlconstraint;
	private String sneconstraint;
	private Integer wopriority;
	private String assetnum;
	private String x3coin;
	private String x3tipospesa;
	private String x3odm;
	private String x3ntw;
	private String x3tipoattivita;
	private String x3tipoordine;
	private String x3nordineinterno;
	private String x3wbe;
	private String x3wbepian;
		
	@Override
	public String toJson()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
}
