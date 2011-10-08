package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>ProductTypeIdentifier</code> serves as an identifier type for
 * {@link ProductType} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>ProductTypeIdentifier</code> instances serve as primary key
 * attribute in {@link PersistentProductType}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class ProductTypeIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link ProductType}s.
	 */
	public ProductTypeIdentifier()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param productTypeIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public ProductTypeIdentifier(String productTypeIdentifier)
	{
		super(productTypeIdentifier);
	}
}
