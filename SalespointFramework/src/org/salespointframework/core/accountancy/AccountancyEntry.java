package org.salespointframework.core.accountancy;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.joda.time.DateTime;
import org.salespointframework.util.Objects;

/**
 * Entity implementation class for Entity: AccountancyEntry
 *
 */
@Entity

public class AccountancyEntry implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private long id;
	
	private static final long serialVersionUID = 1L;
	@Column(name="TimeStamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, nullable=false, updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeStamp;
	private String s;
	
	
	@Deprecated
	protected AccountancyEntry() {}
	
	public AccountancyEntry(String s) {
		this.s = Objects.requireNonNull(s, "s");
	}
	
	public String toString() {
		return s;
	}
   
}
