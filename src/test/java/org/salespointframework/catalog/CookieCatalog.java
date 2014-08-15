package org.salespointframework.catalog;

import org.salespointframework.catalog.Catalog;

/**
 * A dedicated repository for Cookies to make sure we don't run into autowiring ambiguities if multiple beans that match
 * type {@code Catalog<Cookie>}.
 *
 * @author Oliver Gierke
 */
public interface CookieCatalog extends Catalog<Cookie> {}
