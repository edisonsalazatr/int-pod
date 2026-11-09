package it.terna.nas.service.exposure.ws.maximo;

import java.util.List;

public class MaximoResponseArray<T> extends MaximoResponse {

	private static final long serialVersionUID = 3292341963718598363L;
	
	private List<T> responses;

    public List<T> getResponses() {
        return responses;
    }

    public void setResponses(List<T> responses) {
        this.responses = responses;
    }

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}

