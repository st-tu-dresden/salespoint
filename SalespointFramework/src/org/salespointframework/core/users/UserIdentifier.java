package org.salespointframework.core.users;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * Entity implementation class for Entity: UserIdentifier
 * 
 * @author hannesweisbach
 */
@SuppressWarnings("serial")
@Embeddable
public final class UserIdentifier extends SalespointIdentifier  {
	public UserIdentifier() {
		super();
	}

	public UserIdentifier(String userIdentifier) {
		super(userIdentifier);
	}
}
