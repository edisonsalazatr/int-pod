package it.terna.nas.service.exposure.ws.elive;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class InvioProvvedimentiRequest extends ExposureRequest{

	private static final long serialVersionUID = -8578489107254037762L;

	@NotNull
	@SerializedName("nomefile")
	@JsonProperty("nomefile")
	private final String nomeFile;

	private final List<RecordFileElive> record;

	@JsonCreator
	public InvioProvvedimentiRequest(
			@JsonProperty("nomefile") String nomeFile,
			@JsonProperty("record") List<RecordFileElive> record)
	{
		this.nomeFile = nomeFile;
		this.record   = record;
	}

	@Override
	public String toJson() {
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

}
