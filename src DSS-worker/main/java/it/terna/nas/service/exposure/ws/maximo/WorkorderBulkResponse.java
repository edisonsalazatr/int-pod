package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.terna.kernel.basement.utility.Assorted;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkorderBulkResponse extends AbstractMaximoResponse
{
    private static final long serialVersionUID = 437180123386165282L;

    @JsonProperty("_responsedata")
    private WorkorderResponse responsedata;

    @JsonProperty("_responsemeta")
    private ResponseMeta      responsemeta;

    // Getters and Setters

    public WorkorderResponse getResponsedata()
    {
        return responsedata;
    }

    public void setResponsedata(WorkorderResponse responsedata)
    {
        this.responsedata = responsedata;
    }

    public ResponseMeta getResponsemeta()
    {
        return responsemeta;
    }

    public void setResponsemeta(ResponseMeta responsemeta)
    {
        this.responsemeta = responsemeta;
    }

    @Override
    public String toJson()
    {
        try {
            return Assorted.toJson(this);
        } catch (Exception ex) {
            // log occurred error
            log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
            // return empty JSON representation
            return null;
        }
    }

    @Override
    public String toString()
    {
        return toJson();
    }
}
