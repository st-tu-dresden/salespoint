package org.salespointframework.core.accountancy;

import javax.persistence.Embeddable;

import org.salespointframework.util.SalespointIdentifier;

/**
 * <code>AccountancyEntryIdentifier</code> serves as an identifier type for
 * <code>AccountancyEntry</code> objects. The main reason for its existence is
 * typesafety for identifier across the Salespoint Framework. <br>
 * <code>AccoutancyEntryIdentifier</code> instances serve as primary key
 * attribute in <code>PersistentAccountancyEntry</code>, but can also be used as
 * a key for non-persistent, <code>Map</code>-based implementations.
 * 
 * @author hannesweisbach
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class AccountancyEntryIdentifier extends SalespointIdentifier {
}
