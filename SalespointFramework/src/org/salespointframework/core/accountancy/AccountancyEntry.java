package org.salespointframework.core.accountancy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.salespointframework.util.Objects;

/**
 * This class represents an accountancy entries. The
 * <code>AccountancyEntry</code> is not intended to be directly used. Instead,
 * it should be sub-classed to define specific entry types for an accountancy,
 * for example a <code>ProductPaymentEntry</code>.
 * 
 * @author hannesweisbach
 * 
 */
@Entity
public class AccountancyEntry implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private static final long serialVersionUID = 1L;
	@Column(name = "TimeStamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;

	/**
	 * Protected, parameterless Constructor required by the persistence layer.
	 * Do not use it.
	 */
	@Deprecated
	protected AccountancyEntry() {
	};
}
