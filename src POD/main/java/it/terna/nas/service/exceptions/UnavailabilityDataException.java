package it.terna.nas.service.exceptions;

public class UnavailabilityDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String errorCode;
    private final String errorDescription;

    public UnavailabilityDataException(String errorCode, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
