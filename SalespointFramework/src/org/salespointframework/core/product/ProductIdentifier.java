package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * TODO
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class ProductIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link ProductType}s.
	 */
	public ProductIdentifier()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param productIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public ProductIdentifier(String productIdentifier)
	{
		super(productIdentifier);
	}
}
