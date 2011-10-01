package org.salespointframework.util;

// TODO in ein anderes Package?
// inspired by http://msdn.microsoft.com/en-us/library/system.argumentnullexception.aspx
// denn NullRefEx und IllegalArgumentEx sind beide unpassend

/**
 * TODO
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
public class ArgumentNullException extends IllegalArgumentException // TODO NPE (Effective Java)?
{
	private final String paramName;

	protected ArgumentNullException()
	{
		paramName = "";
	}

	/**
	 * Creates a new ArgumentNullException.
	 * @param paramName the name of the null parameter
	 */
	public ArgumentNullException(String paramName)
	{
		super(paramName + " must be not null");
		this.paramName = paramName;
	}

	/**
	 * Creates a new ArgumentNullException.
	 * @param paramName the name of the null parameter
	 * @param message an optional message
	 */
	public ArgumentNullException(String paramName, String message)
	{
		super(message);
		this.paramName = paramName;
	}

	/**
	 * 
	 * @return the name of the null parameter
	 */
	public final String getParamName()
	{
		return paramName;
	}
}
