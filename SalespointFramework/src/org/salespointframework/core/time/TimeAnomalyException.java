package org.salespointframework.core.time;

/**
 * This exception can be used to indicate anomalies in time based
 * implementations such as <code>Calendar</code> or <code>Time</code>.
 * 
 * @author Stanley Förster
 * 
 */
public class TimeAnomalyException extends IllegalArgumentException {
    private static final long serialVersionUID = -3136118025080389402L;

    /**
     * Creates a new <code>TimeAnomalyException</code> with a additional message.
     * 
     * @param message
     *            The message that gives a more detailed description about this
     *            exception.
     */
    public TimeAnomalyException(String message) {
        super("Warning: There occured an anomaly in time. Maybe the universe is corrupted!\n\n" + message);
    }
    
    /**
     * Creates a new <code>TimeAnomalyException</code> with an object that caused this exception.
     * 
     * @param cause An {@link Throwable} that indicates the original cause of this exception.
     */
    public TimeAnomalyException(Throwable cause) {
        super("Warning: There occured an anomaly in time. Maybe the universe is corrupted!", cause);
    }
}
