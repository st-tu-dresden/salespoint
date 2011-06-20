package org.salespointframework.util;


// TODO in ein anderes Package?
// inspired by http://msdn.microsoft.com/en-us/library/system.argumentnullexception.aspx
// denn NullRefEx und IllegalArgumentEx sind beide unpassend
public class ArgumentNullException extends IllegalArgumentException {

	private static final long serialVersionUID = -7522700184079490206L;
	
	private String paramName;
	
	protected ArgumentNullException() {

	}
	
	public ArgumentNullException(String paramName) {
		super(paramName + "must be not null");
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
