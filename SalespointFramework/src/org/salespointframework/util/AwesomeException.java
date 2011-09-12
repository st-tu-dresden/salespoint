package org.salespointframework.util;

/**
 * 
 * @author Paul Henke
 * 
 */
@SuppressWarnings("serial")
public final class AwesomeException extends RuntimeException {
	public AwesomeException() {
		throw new UnsupportedOperationException("AwesomeException is too awesome to be thrown");
	}
	
}
