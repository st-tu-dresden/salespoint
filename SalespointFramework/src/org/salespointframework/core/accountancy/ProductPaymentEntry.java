package org.salespointframework.core.accountancy;

import java.io.Serializable;
import javax.persistence.*;
import org.salespointframework.core.accountancy.AccountancyEntry;

/**
 * Entity implementation class for Entity: ProductPaymentEntry
 *
 */
@Entity

public class ProductPaymentEntry extends AccountancyEntry implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public ProductPaymentEntry() {}
   
}
