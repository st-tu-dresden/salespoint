package org.salespointframework.core.product;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

import org.salespointframework.util.SalespointIdentifier;

@Embeddable
public class SerialNumber extends SalespointIdentifier {

		public SerialNumber() {
			super();
		}
}
