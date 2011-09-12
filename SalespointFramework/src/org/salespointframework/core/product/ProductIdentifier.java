package org.salespointframework.core.product;

import javax.persistence.Embeddable;
import org.salespointframework.util.SalespointIdentifier;

@SuppressWarnings("serial")
@Embeddable
public final class ProductIdentifier extends SalespointIdentifier {
	public ProductIdentifier() {
		super();
	}

	public ProductIdentifier(String productIdentifier) {
		super(productIdentifier);
	}
}
