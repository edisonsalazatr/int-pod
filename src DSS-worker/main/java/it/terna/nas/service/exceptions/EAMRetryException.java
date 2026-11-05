package it.terna.nas.service.exceptions;

import java.util.Arrays;

public class EAMRetryException extends EAMException
{
	private static final long	serialVersionUID	= 190763423161994306L;

	public EAMRetryException(String errorCode, String message)
	{
		super(errorCode, message);
	}

	public EAMRetryException(String errorCode, String message, Throwable cause)
	{
		super(errorCode, message, cause);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EAMRetryException [getErrorCode()=");
		builder.append(getErrorCode());
		builder.append(", getMessage()=");
		builder.append(getMessage());
		builder.append(", getStackTrace()=");
		builder.append(Arrays.toString(getStackTrace()));
		builder.append("]");
		return builder.toString();
	}
}
