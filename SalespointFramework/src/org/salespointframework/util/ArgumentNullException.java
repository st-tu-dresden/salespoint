package org.salespointframework.util;


// TODO in ein anderes Package?
// inspired by http://msdn.microsoft.com/en-us/library/system.argumentnullexception.aspx
// denn NullRefEx und IllegalArgumentEx sind beide unpassend

/**
 * 
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
public class ArgumentNullException extends IllegalArgumentException {

	
	private final String paramName;
	
	protected ArgumentNullException() {
		paramName = "";
	}
	
	public ArgumentNullException(String paramName) {
		super(paramName + " must be not null");
		this.paramName = paramName;
	}
	
	public ArgumentNullException(String paramName, String message) {
		super(message);
		this.paramName = paramName;
	}
	
	public String getParamName() {
		return paramName;
	}
}
