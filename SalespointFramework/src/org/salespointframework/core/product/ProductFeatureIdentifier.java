package org.salespointframework.core.product;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>ProductFeatureIdentifier</code> serves as an identifier type for
 * {@link ProductFeature} objects. The main reason for its existence is
 * type safety for identifier across the Salespoint Framework. <br>
 * <code>ProductFeatureIdentifier</code> instances serve as primary key
 * attribute in {@link ProductFeature}, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
@Embeddable
public final class ProductFeatureIdentifier extends SalespointIdentifier
{
	/**
	 * Creates a new unique identifier for {@link ProductFeature}s.
	 */
	public ProductFeatureIdentifier()
	{
		super();
	}

	/**
	 * Only needed for property editor, shouldn't be used otherwise.
	 * 
	 * @param productFeatureIdentifier
	 *            The string representation of the identifier.
	 */
	@Deprecated
	public ProductFeatureIdentifier(String productFeatureIdentifier)
	{
		super(productFeatureIdentifier);
	}
}
