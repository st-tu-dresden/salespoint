package org.salespointframework.core.inventory;

import org.salespointframework.core.product.AbstractProductType;
import org.salespointframework.core.product.AbstractProductInstance;

public abstract class AbstractInventoryEntry<T1 extends AbstractProductInstance<T2>,T2 extends AbstractProductType> implements InventoryEntry<T1, T2> {

}
