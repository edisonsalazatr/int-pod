package it.terna.nas.service.exceptions;

public class EAMRetryException extends Exception
{
	private static final long	serialVersionUID	= 190763423161994306L;
	private String				errorCode;

	public EAMRetryException(String errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
	}

	public EAMRetryException(String errorCode, String message, Throwable cause)
	{
		super(message, cause);
		this.errorCode = errorCode;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("EAMRetryException [errorCode=");
		builder.append(errorCode);
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append("]");
		return builder.toString();
	}
}