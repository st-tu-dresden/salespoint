package org.salespointframework.core.accountancy;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

@SuppressWarnings("serial")
@Embeddable
public class AccountancyEntryIdentifier extends SalespointIdentifier {
	public AccountancyEntryIdentifier() {
		super();
	}

	public AccountancyEntryIdentifier(String accountancyEntryIdentifier) {
		super(accountancyEntryIdentifier);
	}
}
