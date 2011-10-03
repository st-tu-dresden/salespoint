package org.salespointframework.util;

// inspired by http://msdn.microsoft.com/en-us/library/system.argumentnullexception.aspx
// denn NullRefEx und IllegalArgumentEx sind beide unpassend
// erbt von NullRefEx wegen Effective Java 2 Item 60

/**
 * The exception is thrown when a null value is passed to a method that does not accept it as a valid argument.
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
public class ArgumentNullException extends NullPointerException 
{
	private final String parameterName;

	protected ArgumentNullException()
	{
		parameterName = "";
	}

	/**
	 * Creates a new ArgumentNullException.
	 * @param parameterName the name of the null parameter
	 */
	public ArgumentNullException(String parameterName)
	{
		super(parameterName + " must not be null");
		this.parameterName = parameterName;
	}

	/**
	 * Creates a new ArgumentNullException.
	 * @param parameterName the name of the null parameter
	 * @param message an optional message
	 */
	public ArgumentNullException(String parameterName, String message)
	{
		super(message);
		this.parameterName = parameterName;
	}

	/**
	 * 
	 * @return the name of the null parameter
	 */
	public final String getParameterName()
	{
		return parameterName;
	}
}
