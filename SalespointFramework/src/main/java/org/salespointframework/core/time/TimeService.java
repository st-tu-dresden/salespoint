package org.salespointframework.core.time;

/**
 * 
 * 
 * @author Paul Henke
 *
 */
public interface TimeService {

/**
 * 
 * @return a {@link Time} implementation, it's {@link DefaultTime} by default.
 */
	Time getTime();
	
	/**
	 * 
	 * @param time {@link Time} implementation
	 */
	void setTime(Time time);
	
}
