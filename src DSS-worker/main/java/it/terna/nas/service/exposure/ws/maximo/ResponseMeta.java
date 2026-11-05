package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonProperty;


public  class ResponseMeta {
	
    private String status;
    
    @JsonProperty("Location")
    private String location;
    
    @JsonProperty("Content-Type")
    private String contentType;
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
