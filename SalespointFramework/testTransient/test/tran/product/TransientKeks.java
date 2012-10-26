package test.tran.product;

import org.salespointframework.core.money.Money;
import org.salespointframework.core.product.TransientProduct;
import org.salespointframework.core.quantity.Metric;

@SuppressWarnings("javadoc")
public class TransientKeks extends TransientProduct
{

	public TransientKeks(String name, Money price, Metric metric)
	{
		super(name, price, metric);

	}

}
