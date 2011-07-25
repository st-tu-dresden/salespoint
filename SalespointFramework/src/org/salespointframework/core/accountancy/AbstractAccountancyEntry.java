package org.salespointframework.core.accountancy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.joda.time.DateTime;

/**
 * This class represents an accountancy entries. The
 * <code>AbstractAccountancyEntry</code> is not intended to be directly used. Instead,
 * it should be sub-classed to define specific entry types for an accountancy,
 * for example a <code>ProductPaymentEntry</code>.
 * 
 * @author hannesweisbach
 * 
 */
@Entity
public abstract class AbstractAccountancyEntry implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@SuppressWarnings("unused")
	private long id;

	private static final long serialVersionUID = 1L;
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	
	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	public AbstractAccountancyEntry() {
		timeStamp = new Date();
	};
	
	public DateTime getTimeStamp() {
		return new DateTime(timeStamp);
	}
}
