package org.salespointframework.core.catalog;

/**
 * A dedicated repository for Cookies to make sure we don't run into autowiring ambiguities if multiple beans that match
 * type {@code Products<Cookie>}.
 *
 * @author Oliver Gierke
 */
public interface CookieRepository extends Products<Cookie> {}
