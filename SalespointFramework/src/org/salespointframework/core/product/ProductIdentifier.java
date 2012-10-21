package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>ProductIdentifier</code> serves as an identifier type for
 * {@link Product} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>ProductIdentifier</code> instances serve as primary key
 * attribute in {@link PersistentProduct}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class ProductIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link Product}s.
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
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) { 
		return super.equals(other);
	}
}
