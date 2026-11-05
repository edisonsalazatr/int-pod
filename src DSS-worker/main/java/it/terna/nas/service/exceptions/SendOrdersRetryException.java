package it.terna.nas.service.exceptions;

import java.util.Arrays;

public class SendOrdersRetryException extends EAMRetryException {

	private static final long serialVersionUID = 7866919716473634290L;

	public SendOrdersRetryException(String code, String description) {
        super(code, description);
    }
	
	public SendOrdersRetryException(String errorCode, String message, Throwable cause)
	{
		super(errorCode, message, cause);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("SendOrdersRetryException [getErrorCode()=");
		builder.append(getErrorCode());
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getStackTrace()=");
		builder.append(Arrays.toString(getStackTrace()));
		builder.append("]");
		return builder.toString();
	}
	
}

